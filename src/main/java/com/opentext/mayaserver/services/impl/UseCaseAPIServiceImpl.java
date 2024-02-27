package com.opentext.mayaserver.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.opentext.mayaserver.config.ApplicationPropertiesConfig;
import com.opentext.mayaserver.datagenerators.RuleEngine;
import com.opentext.mayaserver.datagenerators.aws.billing.model.BillCostData;
import com.opentext.mayaserver.datagenerators.util.DateUtils;
import com.opentext.mayaserver.datauploaders.s3mock.S3UploadService;
import com.opentext.mayaserver.environments.K8sUtils;
import com.opentext.mayaserver.environments.K8sObjectsManager;
import com.opentext.mayaserver.exceptions.MayaInvalidUserInputException;
import com.opentext.mayaserver.exceptions.MayaServerException;
import com.opentext.mayaserver.exceptions.MayaUseCaseConflictException;
import com.opentext.mayaserver.exceptions.MayaUseCaseNotFoundException;
import com.opentext.mayaserver.models.*;
import com.opentext.mayaserver.models.vo.*;
import com.opentext.mayaserver.repository.CloudCostDataRepository;
import com.opentext.mayaserver.repository.UseCaseRepository;
import com.opentext.mayaserver.services.AwsRecommendationSeedDataService;
import com.opentext.mayaserver.services.DataGeneratorService;
import com.opentext.mayaserver.services.UseCaseAPIService;
import com.opentext.mayaserver.utility.ProfileHandler;
import com.opentext.mayaserver.utility.UseCaseServiceHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.opentext.mayaserver.utility.Constants.*;
import static com.opentext.mayaserver.utility.DefaultData.*;
import static com.opentext.mayaserver.utility.UseCaseServiceHelper.*;

/**
 * @author Rajiv
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class UseCaseAPIServiceImpl implements UseCaseAPIService {

    private final RuleEngine ruleEngine;
    private final UseCaseRepository useCaseRepository;
    private final CloudCostDataRepository cloudCostDataRepository;
    private final UseCaseServiceHelper useCaseServiceHelper;
    private final ApplicationPropertiesConfig applicationPropertiesConfig;
    private final K8sObjectsManager k8sObjectsManager;
    private final ProfileHandler profileHandler;
    private final S3UploadService s3UploadService;
    private final DataGeneratorService dataGeneratorService;
    private final AwsRecommendationSeedDataService awsRecommendationSeedDataService;

    private ObjectMapper objectMapper;

    @Override
    public UseCaseResponseVO createUseCase(UseCaseVO useCaseVO) {
        UseCase savedUseCase = null;
        try {
            validateUseCaseVO(useCaseVO);
            String useCaseName = useCaseVO.getUseCaseName();
            useCaseVO.setCloudProvider(useCaseVO.getCloudProvider().toLowerCase());
            log.info("Use case initiated '{}'", useCaseName);
            setDefaultValues(useCaseVO);
            savedUseCase = saveUseCase(useCaseVO);
            ruleEngine.cloudProviderDelegator(useCaseVO);
            return prepareUseCaseResponseVO(savedUseCase);
        } catch (Exception e) {
            if (savedUseCase != null) {
                updateStatusOfUseCase(useCaseVO.getUseCaseName(), StateEnum.FAILED);
            }
            throw new MayaServerException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public List<UseCaseResponseVO> getAllUseCase() throws MayaServerException {
        List<UseCase> useCaseList = useCaseRepository.findAll();
        List<UseCaseResponseVO> useCaseResponseVOList = new ArrayList<>();
        useCaseList.stream().forEach(item -> {
            useCaseResponseVOList.add(prepareUseCaseResponseVO(item));
        });
        return useCaseResponseVOList;
    }


    @Override
    public UseCaseResponseVO getUseCaseByNameOrId(String useCaseName) throws MayaServerException {
        UseCaseResponseVO useCaseResponseVO = null;
        Optional<UseCase> useCaseOptional = null;
        try {
            useCaseOptional = fetchUseCase(useCaseName);
            if (useCaseOptional.isEmpty()) {
                log.error("UseCase  not found {}", useCaseName);
            } else {
                useCaseResponseVO = prepareUseCaseResponseVO(useCaseOptional.get());
            }
        } catch (Exception e) {
            throw new MayaServerException(e.getMessage(), e);
        }
        return useCaseResponseVO;
    }

    @Override
    public void deleteUseCase(String useCaseName) throws IOException {
        Optional<UseCase> useCaseOptional = fetchUseCase(useCaseName);
        if (useCaseOptional.isPresent()) {
            StateEnum stateEnum = useCaseOptional.get().getState();
            if (StateEnum.isUseCaseDeletionAllowed(stateEnum)) {
                Optional<CloudCostData> cloudCostDataOptional = cloudCostDataRepository.findByUseCaseId(useCaseOptional.get());
                cloudCostDataOptional.ifPresentOrElse(
                        cloudCostData
                                -> cloudCostDataRepository.delete(cloudCostData),
                        ()
                                -> log.warn("No cost related data found wrt {}", useCaseName));
                useCaseRepository.delete(useCaseOptional.get());
                if (profileHandler.isProductionProfileActive()) {
                    String namespace = K8sUtils.getNamespace();
                    k8sObjectsManager.deleteK8Objects(namespace, useCaseName);
                    s3UploadService.deleteAWSBillFromS3(BUCKET_NAME, useCaseName);
                }
                List<String> pathList = Arrays.asList(
                        applicationPropertiesConfig.getMockoon().getNfsDataPath(),
                        applicationPropertiesConfig.getMockoon().getNfsConfigPath(),
                        applicationPropertiesConfig.getRecommendation().getNfsDataPath(),
                        applicationPropertiesConfig.getRecommendation().getNfsConfigPath()
                );
                deleteDirectories(pathList, useCaseName);
                log.info("UseCase deleted '{}'", useCaseName);
            } else {
                throw new MayaUseCaseConflictException("UseCase either in Submitted or In Progress state. Hence UseCase can not be deleted");
            }
        } else {
            throw new MayaUseCaseNotFoundException("UseCase doesn't exist " + useCaseName);
        }
    }

    private void deleteDirectories(List<String> pathList, String useCaseName) throws IOException {
        for (String path : pathList) {
            deleteDirectory(path, useCaseName);
        }
    }

    private void deleteDirectory(String path, String useCaseName) throws IOException {
        File theDirectory = new File(path + FILE_PATH_SEPARATOR + useCaseName);
        if (theDirectory.exists()) {
            FileUtils.deleteDirectory(theDirectory);
            log.info("useCase directory deleted: {} ", theDirectory);
        }
    }

    @Override
    public int getAvailableMockoonPort() {
        List<Integer> usedPortList = new ArrayList<>();
        List<UseCase> useCaseList = useCaseRepository.findAll();
        if (useCaseList != null && useCaseList.size() < 1) {
            return DEFAULT_PORT;
        } else if (useCaseList != null && useCaseList.size() >= 50) {
            String message = "Maximum number of server limit reached. Please delete some Use Case and retry";
            log.error(message);
            throw new MayaServerException(message);
        } else {
            useCaseList.stream().forEach(useCase -> {
                int port = useCase.getMockoonPort();
                usedPortList.add(port);
            });
            AtomicInteger atomicInteger = new AtomicInteger();
            atomicInteger.set(PORT_START_RANGE);
            for (int i = 0; i <= (PORT_END_RANGE - PORT_START_RANGE); i++) {
                boolean portFound = usedPortList.stream().anyMatch(item -> item == atomicInteger.get());
                if (!portFound) {
                    return atomicInteger.get();
                }
                atomicInteger.set(atomicInteger.incrementAndGet());
            }
        }
        return DEFAULT_PORT;
    }

    @Transactional
    public Optional<UseCase> fetchUseCase(String useCaseName) {
        Optional<UseCase> useCaseOptional = null;
        useCaseOptional = useCaseRepository.findByUseCaseName(useCaseName);
        if (!useCaseOptional.isPresent()) {
            useCaseOptional = useCaseRepository.findById(useCaseName);
        }
        return useCaseOptional;
    }

    private UseCaseResponseVO prepareUseCaseResponseVO(UseCase useCase) {
        UseCaseResponseVO useCaseResponseVO = new UseCaseResponseVO();
        useCaseResponseVO.setUseCaseName(useCase.getUseCaseName());
        useCaseResponseVO.setUseCaseId(useCase.getId());
        useCaseResponseVO.setState(useCaseServiceHelper.useCaseStateToVO(useCase.getState()));
        useCaseResponseVO.setAccountURL(useCase.getAccountURL());
        useCaseResponseVO.setBillingURL(useCase.getBillingURL());
        useCaseResponseVO.setRecommendationURL(useCase.getRecommendationURL());
        try {
            objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            UseCaseVO useCasePayload = objectMapper.readValue(useCase.getPayload(), UseCaseVO.class);
            useCaseResponseVO.setPayload(useCasePayload);
        } catch (Exception e) {
            log.error("Error while fetching payload: {}", e.getMessage());
        }
        return useCaseResponseVO;
    }

    @Transactional
    public UseCase saveUseCase(UseCaseVO useCaseVO) throws JsonProcessingException {
        String useCaseName = useCaseVO.getUseCaseName();
        UseCase usecase = new UseCase();
        usecase.setUseCaseName(useCaseName);
        usecase.setCloudProvider(useCaseVO.getCloudProvider());
        usecase.setState(StateEnum.SUBMITTED);
        usecase.setAccountURL(HTTPS + applicationPropertiesConfig.getHost() + "/" + useCaseName + ACCOUNT_CONTEXT_PATH);
        usecase.setMockoonPort(getAvailableMockoonPort());
        usecase.setBillingURL(HTTPS + applicationPropertiesConfig.getHost() + "/" + useCaseName);
        usecase.setRecommendationURL(HTTPS + applicationPropertiesConfig.getHost() + "/" + useCaseName +  RECOMMENDATION_CONTEXT_PATH);
        usecase.setIsDemoModeEnabled(useCaseVO.isDemoModeEnabled());
        usecase.setDemoRootAccountId(useCaseVO.getDemoRootAccountId());
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String payloadJson = objectMapper.writeValueAsString(useCaseVO);
        usecase.setPayload(payloadJson);
        if (!useCaseServiceHelper.isBYODMode(useCaseVO)) {
            usecase.setAccountMetadataList(generateAccountMetaData(useCaseVO));
            usecase.setAccountDataList(generateAccountData(useCaseVO));
        }
        return useCaseRepository.save(usecase);
    }

    private List<AccountMetadata> generateAccountMetaData(UseCaseVO useCaseVO) {
        List<AccountMetadata> accountMetadataList = new ArrayList<>();
        String nfsPath = applicationPropertiesConfig.getMockoon().getNfsDataPath();
        for (int i = 0; i < getEndpointsLength(); i++) {
            AccountMetadata accountMetadata = new AccountMetadata();
            accountMetadata.setEndpointType(getEndpoints(i));
            accountMetadata.setDataFilePath(useCaseServiceHelper.createAccountDataFilePath(nfsPath, useCaseVO, getEndpoints(i)));
            accountMetadataList.add(accountMetadata);
        }
        return accountMetadataList;
    }

    private List<AccountData> generateAccountData(UseCaseVO useCaseVO) {
        List<AccountData> accountDataList = new ArrayList<>();
        for (int i = 0; i < ROOT_ACCOUNT_SIZE; i++) {
            AccountData accountData = new AccountData();
            if (!useCaseVO.isDemoModeEnabled()) {
                accountData.setRootAccount(useCaseServiceHelper.generateRandomAccountId());
                accountData.setMemberAccounts(useCaseServiceHelper.generateRandomMemberAccountId(useCaseVO.getAccount().getNumberOfAccounts()));
            } else {
                String rootAccountId = useCaseVO.getDemoRootAccountId();
                assert (awsRecommendationSeedDataService.listValidRootAccountIds().contains(rootAccountId));
                accountData.setRootAccount(rootAccountId);
                accountData.setMemberAccounts(useCaseServiceHelper.mapListToString(awsRecommendationSeedDataService.getMemberAccountIds(rootAccountId)));
            }
            accountDataList.add(accountData);
        }
        return accountDataList;
    }

    /**
     * Setting default values to account data
     *
     * @param useCaseVO
     */
    private void setDefaultValues(UseCaseVO useCaseVO) {
        if (useCaseServiceHelper.isBYODMode(useCaseVO)) {
            return;
        }
        AccountVO account = useCaseVO.getAccount();
        BillingVO billingVO = useCaseVO.getBilling();
        if (account == null) {
            useCaseVO.setAccount(getAccountData());
        } else {
            overrideAccountPropertyData(account);
        }
        if (useCaseVO.getIsDemoModeEnabled() == null) {
            useCaseVO.setIsDemoModeEnabled(false);
        }
        if (useCaseVO.isDemoModeEnabled()) {
            overrideDemoAccountPropertyData(useCaseVO.getAccount(), awsRecommendationSeedDataService.getMemberAccountIds(useCaseVO.getDemoRootAccountId()).size());
        }
        if (billingVO == null) {
            useCaseVO.setBilling(getBillingData());
        } else {
            overrideBillingPropertyData(billingVO);
        }
    }

    @Transactional
    public void updateStatusOfUseCase(String useCaseName, StateEnum state) {
        try {
            Optional<UseCase> optionalUseCase = fetchUseCase(useCaseName);
            if (optionalUseCase.isPresent()) {
                UseCase useCase = optionalUseCase.get();
                useCase.setState(state);
                useCaseRepository.save(useCase);
            }
        } catch (Exception e) {
            throw new MayaServerException("Unable to find the useCase with name {} " + useCaseName);
        }
    }

    @Override
    public ManagementAccountResponseVO getAllRootAccountBillCostData(String useCaseName) {
        ManagementAccountResponseVO billCostDataResponseVO = null;
        Optional<CloudCostData> cloudCostDataOptional = getCloudCostData(useCaseName);
        if (cloudCostDataOptional.isPresent()) {
            billCostDataResponseVO = new ManagementAccountResponseVO();
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.findAndRegisterModules();
                List<BillCostData> billCostDataList = mapper.readValue(cloudCostDataOptional.get().getRootAccountCost(), new TypeReference<List<BillCostData>>() {
                });
                billCostDataResponseVO.setData(billCostDataList);
            } catch (Exception e) {
                log.error("unable parse member account list data");
            }
        } else {
            log.error("Unable to fetch associated root account cost data for useCase '{}'", useCaseName);
            return null;
        }
        return billCostDataResponseVO;
    }

    @Override
    public ManagementAccountResponseVO getRootAccountBillCostData(String useCaseName, String rootAccount, String billPeriod) {
        ManagementAccountResponseVO billCostDataResponseVO = getAllRootAccountBillCostData(useCaseName);
        if (billCostDataResponseVO != null) {
            if (isEmptyString(rootAccount)) {
                List<BillCostData> filteredData = filterBillCostData(billCostDataResponseVO.getData(), billPeriod, billCostDataResponseVO.getData().get(0).getAccountId());
                billCostDataResponseVO.setData(filteredData);
            } else {
                List<BillCostData> filteredData = filterBillCostData(billCostDataResponseVO.getData(), billPeriod, rootAccount);
                billCostDataResponseVO.setData(filteredData);
            }
        }
        return billCostDataResponseVO;
    }

    @Override
    public UsageAccountResponseVO getAllMemberAccountBillCostData(String useCaseName) {
        UsageAccountResponseVO memberAccountCostResponseVO = null;
        Optional<CloudCostData> cloudCostDataOptional = getCloudCostData(useCaseName);
        if (cloudCostDataOptional.isPresent()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.findAndRegisterModules();
                Map<String, List<BillCostData>> memberListMap = mapper.readValue(cloudCostDataOptional.get().getMemberAccountCost(), new TypeReference<Map<String, List<BillCostData>>>() {
                });
                memberAccountCostResponseVO = new UsageAccountResponseVO();
                memberAccountCostResponseVO.setData(memberListMap);
                return memberAccountCostResponseVO;
            } catch (Exception e) {
                log.error("unable parse member account list data");
            }

        } else {
            log.error("Unable to fetch associated member account cost data for useCase '{}'", useCaseName);
        }
        return memberAccountCostResponseVO;
    }

    @Override
    public UsageAccountResponseVO getMemberAccountBillCostData(String useCaseName, String billPeriod, List<String> memberAccountId) {
        UsageAccountResponseVO memberAccountCostResponseVO = null;
        Optional<CloudCostData> cloudCostDataOptional = getCloudCostData(useCaseName);
        Map<String, List<BillCostData>> memberListMap = null;
        Map<String, List<BillCostData>> filteredDataMap = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            memberListMap = mapper.readValue(cloudCostDataOptional.get().getMemberAccountCost(), new TypeReference<Map<String, List<BillCostData>>>() {
            });
        } catch (Exception e) {
            log.error("Unable to read value {} ", useCaseName);
        }
        if (cloudCostDataOptional.isPresent()) {
            if (billPeriod != null) {
                if (billPeriod.equals(ALL)) {
                    for (Map.Entry<String, List<BillCostData>> dataEntry : memberListMap.entrySet()) {
                        List<BillCostData> dataList = dataEntry.getValue();
                        List<BillCostData> filteredList = dataList.stream()
                                .filter(data -> memberAccountId.contains(data.getAccountId()))
                                .collect(Collectors.toList());
                        if (!filteredList.isEmpty()) {
                            filteredDataMap.put(dataEntry.getKey(), filteredList);
                        }
                    }
                } else {
                    filteredDataMap = getFilteredMemberAccountCost(memberListMap, billPeriod, memberAccountId, filteredDataMap, DATE_EXACT);
                    if (filteredDataMap.isEmpty()) {
                        filteredDataMap = getFilteredMemberAccountCost(memberListMap, billPeriod, memberAccountId, filteredDataMap, DATE_RANGE);
                    }
                }
            } else {
                for (Map.Entry<String, List<BillCostData>> entry : memberListMap.entrySet()) {
                    String key = entry.getKey();
                    List<BillCostData> billCostDataList = entry.getValue();
                    List<BillCostData> filteredBillCostDataList = billCostDataList.stream().filter(data -> memberAccountId.contains(data.getAccountId())).collect(Collectors.toList());
                    if (!filteredBillCostDataList.isEmpty()) {
                        filteredDataMap.put(key, filteredBillCostDataList);
                    }
                }

            }
            memberAccountCostResponseVO = new UsageAccountResponseVO();
            memberAccountCostResponseVO.setData(filteredDataMap);

            return memberAccountCostResponseVO;
        } else {
            log.error("Unable to fetch associated member account cost data for useCase '{}'", useCaseName);
            return memberAccountCostResponseVO;
        }
    }

    public Map<String, List<BillCostData>> getFilteredMemberAccountCost(Map<String, List<BillCostData>> memberListMap, String billPeriod, List<String> memberAccountId, Map<String, List<BillCostData>> filteredDataMap, String typeCheck) {
        String[] dateParts = billPeriod.split("-");
        LocalDate startDate = DateUtils.toLocalDateFormat(dateParts[0]);
        LocalDate endDate = DateUtils.toLocalDateFormat(dateParts[1]);
        for (Map.Entry<String, List<BillCostData>> entry : memberListMap.entrySet()) {
            String key = entry.getKey();
            List<BillCostData> billCostDataList = entry.getValue();

            List<BillCostData> filteredBillCostDataList = new ArrayList<>();

            for (BillCostData data : billCostDataList) {
                LocalDate dataStartDate = DateUtils.toLocalDateFormat(data.getBillPeriod().split("-")[0]);
                LocalDate dataEndDate = DateUtils.toLocalDateFormat(data.getBillPeriod().split("-")[1]);

                if (typeCheck.equals(DATE_EXACT) && dataStartDate.isEqual(startDate) && dataEndDate.isEqual(endDate)) {
                    if (memberAccountId.contains(data.getAccountId())) {
                        filteredBillCostDataList.add(data);
                    }
                } else if (typeCheck.equals(DATE_RANGE)) {
                    if (isWithinDateRange(dataStartDate, dataEndDate, startDate.plusDays(1), endDate.minusDays(1))) {
                        if (memberAccountId.contains(data.getAccountId())) {
                            filteredBillCostDataList.add(data);
                        }
                    }
                }
            }

            if (!filteredBillCostDataList.isEmpty()) {
                filteredDataMap.put(key, filteredBillCostDataList);
            }
        }
        return filteredDataMap;
    }

    private boolean isWithinDateRange(
            LocalDate dataStartDate,
            LocalDate dataEndDate,
            LocalDate filterStartDate,
            LocalDate filterEndDate) {
        return !dataEndDate.isBefore(filterStartDate) && !dataStartDate.isAfter(filterEndDate);
    }

    public boolean isEqualBillPeriod(List<BillCostData> data, String queryBillPeriod) {
        for (BillCostData billCostData : data) {
            String[] billPeriodParts = billCostData.getBillPeriod().split("-");
            String[] queryPeriodParts = queryBillPeriod.split("-");

            if (billPeriodParts.length != 2 || queryPeriodParts.length != 2) {
                return false;
            }

            LocalDate billStartDate = DateUtils.toLocalDateFormat(billPeriodParts[0]);
            LocalDate billEndDate = DateUtils.toLocalDateFormat(billPeriodParts[1]);

            LocalDate queryStartDate = DateUtils.toLocalDateFormat(queryPeriodParts[0]);
            LocalDate queryEndDate = DateUtils.toLocalDateFormat(queryPeriodParts[1]);
            if (billStartDate.isEqual(queryStartDate) && billEndDate.isEqual(queryEndDate)) {
                return true;
            }
        }
        return false;
    }

    public void validateUserInput(String useCaseName, List<String> memberAccounts, String rootAccount, String billPeriod) {
        UseCaseResponseVO useCaseResponseVO = getUseCaseByNameOrId(useCaseName);
        if (useCaseResponseVO == null) {
            throw new MayaUseCaseNotFoundException("Record Not found with useCaseName: " + useCaseName + " Please check useCaseName or Id");
        } else if (!useCaseResponseVO.getState().name().equals(StateEnum.CREATED.name())) {
            throw new MayaServerException(useCaseName + " data creation is under process");
        }
        validateRootAccount(useCaseName, rootAccount);
        validateMemberAccount(useCaseName, memberAccounts);
        validateBillPeriod(billPeriod);
    }

    private static void validateBillPeriod(String billPeriod) {
        if (billPeriod != null && !billPeriod.equals(ALL)) {
            try {
                String[] dateParts = billPeriod.split("-");
                LocalDate startDate = DateUtils.toLocalDateFormat(dateParts[0]);
                LocalDate endDate = DateUtils.toLocalDateFormat(dateParts[1]);
                if (endDate.isBefore(startDate)) {
                    throw new MayaInvalidUserInputException("EndDate should not be less than startDate.");
                }
            } catch (DateTimeParseException e) {
                log.error("Incorrect billPeriod format received {} ", billPeriod);
                throw new MayaInvalidUserInputException("Incorrect billPeriod format received, please provide valid format(yyyymmdd-yyyymmdd) or all for entire billPeriod.", e);
            }
        }
    }

    private void validateRootAccount(String useCaseName, String rootAccount) {
        if (rootAccount != null && !isValidAccountId(rootAccount)) {
            throw new MayaInvalidUserInputException("Root account is either empty or invalid format, please provide valid root account.");
        }
        if (!isEmptyString(rootAccount) && !dataGeneratorService.getRootAccount(useCaseName).equals(rootAccount)) {
            throw new MayaInvalidUserInputException("Root account " + rootAccount + " not available, please provide valid root account.");
        }
    }

    private void validateMemberAccount(String useCaseName, List<String> memberAccounts) {
        if (memberAccounts != null && memberAccounts.size() == 0) {
            throw new MayaInvalidUserInputException("memberAccounts must not be empty");
        }
        if (memberAccounts != null && !isListUnique(memberAccounts)) {
            throw new MayaInvalidUserInputException("memberAccounts must be unique entries.");
        }
        if (memberAccounts != null && memberAccounts.size() > 10) {
            throw new MayaInvalidUserInputException("memberAccounts must not be more than 10 unique entries.");
        }
        if (memberAccounts != null) {
            List<String> memberAccountId = new ArrayList<>();
            for (String memberId : memberAccounts) {
                if (memberId.trim().length() > 0 && isValidAccountId(memberId)) {
                    if (!dataGeneratorService.getMemberAccounts(useCaseName).contains(memberId)) {
                        memberAccountId.add(memberId);
                    }
                } else {
                    throw new MayaInvalidUserInputException("Invalid memberAccount " + memberId + ", please provide valid member account.");
                }
            }
            if (!memberAccountId.isEmpty()) {
                throw new MayaInvalidUserInputException("Member Account with value " + memberAccountId + " not available, please provide valid member account.");
            }
        }
    }

    private void validateUseCaseVO(UseCaseVO useCaseVO) {
        if (useCaseVO.isDemoModeEnabled()) {
            List<String> validAccountIds = awsRecommendationSeedDataService.listValidRootAccountIds();
            String rootAccountId = useCaseVO.getDemoRootAccountId();
            log.info("validateUseCaseVO demoModeEnabled, checking root account " + rootAccountId);
            if (!validAccountIds.contains(rootAccountId)) {
                log.error("validateUseCaseVO root account (" + rootAccountId + ") not in the valid list " + validAccountIds);
                throw new MayaInvalidUserInputException("When isDemoModeEnabled is set to true, the demoRootAccountId must be in the list: " + validAccountIds);
            }
        }
    }

    private boolean isListUnique(List<String> list) {
        Set<String> set = new HashSet<>(list);
        return set.size() == list.size();
    }

    private boolean isValidAccountId(String accountId) {
        boolean isNumber = false;
        try {
            Long.parseLong(accountId);
            isNumber = true;
            return isNumber;
        } catch (NumberFormatException e) {
            log.error("Unable to parse accountId {}", accountId);
        }
        return isNumber;
    }

    private List<BillCostData> filterBillCostData(List<BillCostData> data, String queryBillPeriod, String queryAccountId) {
        if (queryBillPeriod != null && queryAccountId != null) {
            if (isEqualBillPeriod(data, queryBillPeriod)) {
                return data.stream()
                        .filter(item -> item.getBillPeriod().equals(queryBillPeriod) && item.getAccountId().equals(queryAccountId))
                        .collect(Collectors.toList());
            } else {
                return data.stream()
                        .filter(item -> isAnyPartOfPeriodInRange(item.getBillPeriod(), queryBillPeriod) && item.getAccountId().equals(queryAccountId))
                        .collect(Collectors.toList());
            }
        } else {
            return data.stream()
                    .filter(item -> item.getAccountId().equals(queryAccountId))
                    .collect(Collectors.toList());
        }
    }

    private boolean isAnyPartOfPeriodInRange(String billPeriod, String queryBillPeriod) {
        String[] billPeriodParts = billPeriod.split("-");
        String[] queryPeriodParts = queryBillPeriod.split("-");

        if (billPeriodParts.length != 2 || queryPeriodParts.length != 2) {
            return false;
        }

        LocalDate billStartDate = DateUtils.toLocalDateFormat(billPeriodParts[0]);
        LocalDate billEndDate = DateUtils.toLocalDateFormat(billPeriodParts[1]);

        LocalDate queryStartDate = DateUtils.toLocalDateFormat(queryPeriodParts[0]);
        LocalDate queryEndDate = DateUtils.toLocalDateFormat(queryPeriodParts[1]);
        if (isWithinDateRange(billStartDate, billEndDate, queryStartDate.plusDays(1), queryEndDate.minusDays(1))) {
            return true;
        }
        return false;
    }

    public Optional<CloudCostData> getCloudCostData(String useCaseName) {
        Optional<UseCase> useCaseOptional = fetchUseCase(useCaseName);
        if (useCaseOptional.isPresent()) {
            return cloudCostDataRepository.findByUseCaseId(useCaseOptional.get());
        } else {
            log.error("Unable to fetch associated cloud cost data for use case '{}'", useCaseName);
            return Optional.empty();
        }
    }
}
