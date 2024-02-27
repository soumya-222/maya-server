package com.opentext.mayaserver.datagenerators.aws.account;

import com.opentext.mayaserver.config.ApplicationPropertiesConfig;
import com.opentext.mayaserver.datagenerators.util.DataGeneratorUtils;
import com.opentext.mayaserver.models.*;
import com.opentext.mayaserver.models.vo.AccountMetadataVO;
import com.opentext.mayaserver.models.vo.UseCaseVO;
import com.opentext.mayaserver.services.AwsRecommendationSeedDataService;
import com.opentext.mayaserver.services.DataGeneratorService;
import com.opentext.mayaserver.utility.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.opentext.mayaserver.utility.Constants.*;

@RequiredArgsConstructor
@Slf4j
@Component
public class AWSAccountDataGenerator {

    private static final Random random = new Random();
    private final ApplicationPropertiesConfig applicationPropertiesConfig;
    private final DataGeneratorService dataGeneratorService;
    private final AwsRecommendationSeedDataService awsRecommendationSeedDataService;

    public void generateAccounts(UseCaseVO useCaseVO) throws IOException {
        log.info("Generating AWS Account");
        LocalDate startDate = useCaseVO.getBilling().getStartDate();
        String useCaseName = useCaseVO.getUseCaseName();
        String describeOrganizationFilePath = null;
        String describeAccountFilePath = null;
        List<String> listAccountsFilePaths = null;
        List<AccountMetadataVO> accountMetadataVOList = dataGeneratorService.getAccountMetaData(useCaseName);
        for (AccountMetadataVO accountMetadataVO : accountMetadataVOList) {
            switch (accountMetadataVO.getEndpointType()) {
                case AWS_DESCRIBE_ORGANIZATION ->
                        describeOrganizationFilePath = accountMetadataVO.getDataFilePath().stream().findFirst().get();
                case AWS_DESCRIBE_ACCOUNT ->
                        describeAccountFilePath = accountMetadataVO.getDataFilePath().stream().findFirst().get();
                case AWS_LIST_ACCOUNTS -> listAccountsFilePaths = accountMetadataVO.getDataFilePath();
            }
        }

        List<String> memberAccountIdList = dataGeneratorService.getMemberAccounts(useCaseName);
        Objects.requireNonNull(listAccountsFilePaths).sort(Comparator.comparingInt(this::getNumberFromString));

        CommonUtils.createDirectoryBasedOnUseCase(useCaseName, applicationPropertiesConfig.getMockoon().getNfsDataPath());
        String organizationId = DataGeneratorUtils.generateRandomStringUsingRegex(ORGANIZATION_ID_REGEX);
        BigDecimal timeStamp = generateTimeStamp(startDate, MASTER_ACCOUNT);
        LocalDate masterAccountCreatedDate = Instant.ofEpochMilli(timeStamp.longValue() * 1000).atZone(ZoneId.systemDefault()).toLocalDate();

        AccountHolder accountHolder = createAccountHolder(useCaseName, useCaseVO.isDemoModeEnabled());

        generateOrganisationApiResponse(accountHolder, organizationId, describeOrganizationFilePath);
        generateDescribeAccountApiResponse(accountHolder, timeStamp, describeAccountFilePath);
        generateListAccountsApiResponse(useCaseVO, accountHolder, organizationId, memberAccountIdList, masterAccountCreatedDate, listAccountsFilePaths);
        log.info("AWS Account Data generated successfully");
    }

    private AccountHolder createAccountHolder(String useCaseName, boolean isDemoModeEnabled) {

        StringBuilder accountBuilder = new StringBuilder();
        String masterAccountId = dataGeneratorService.getRootAccount(useCaseName);
        String masterAccountName;
        String masterAccountStatus;
        if (isDemoModeEnabled) {
            AwsDemoCloudAccount masterAccount = awsRecommendationSeedDataService.getAccount(masterAccountId);
            masterAccountName = masterAccount.getName();
            masterAccountStatus = mapToStatus(masterAccount.getPhaseId());
        } else {
            masterAccountName = generateMasterAccountName();
            masterAccountStatus = AccountState.ACTIVE.name();
        }
        String masterAccountDomainName = generateMasterAccountDomainName(masterAccountName);
        String masterAccountEmail = generateEmailForAccounts(masterAccountName, masterAccountDomainName);
        String organizationId = DataGeneratorUtils.generateRandomStringUsingRegex(ORGANIZATION_ID_REGEX);
        accountBuilder.append(ARN_PREFIX)
                .append(masterAccountId)
                .append(ACCOUNT_PREFIX)
                .append(organizationId)
                .append("/")
                .append(masterAccountId);
        String masterAccountArn = accountBuilder.toString();

        AccountHolder accountHolder = AccountHolder.builder()
                .masterAccountId(masterAccountId)
                .masterAccountArn(masterAccountArn)
                .masterAccountDomainName(masterAccountDomainName)
                .masterAccountEmail(masterAccountEmail)
                .masterAccountStatus(masterAccountStatus)
                .masterAccountName(masterAccountName).build();
        return accountHolder;
    }

    private void generateListAccountsApiResponse(UseCaseVO useCaseVO, AccountHolder accountHolder, String organizationId, List<String> memberAccountIdList, LocalDate startDate, List<String> listAccountsFilePaths) throws IOException {
        int memberAccountNameSuffix = 0;
        int memberAccountNameIndex = 0;
        int accountIdIndex = 0;

        int memberAccountSize = useCaseVO.getAccount().getNumberOfAccounts();
        StringBuilder accountBuilder = new StringBuilder();

        int pageLimit = useCaseVO.getAccount().getPageSize();
        int pageCount = (int) Math.ceil((double) memberAccountSize / pageLimit);
        String accountStatus;

        accountBuilder.append(ARN_PREFIX)
                .append(accountHolder.getMasterAccountId())
                .append(ACCOUNT_PREFIX)
                .append(organizationId)
                .append("/");

        String listAccountArn = accountBuilder.toString();
        accountBuilder.setLength(0);

        for (int page = 1; page <= pageCount; page++) {
            int startIndex = (page - 1) * pageLimit + 1;
            int endIndex = Math.min(startIndex + pageLimit - 1, memberAccountSize);

            List<Account> memberAccountList = new ArrayList<>();
            String listAccountsFileName = listAccountsFilePaths.get(page - 1);

            for (int j = startIndex; j <= endIndex; j++) {
                double activeProbability = 0.7;
                if (random.nextDouble() < activeProbability) {
                    accountStatus = AccountState.ACTIVE.name();
                } else {
                    accountStatus = random.nextBoolean() ? AccountState.SUSPENDED.name() : AccountState.PENDING_CLOSURE.name();
                }

                String listAccountName;
                String memberAccountId = memberAccountIdList.get(accountIdIndex++);
                if (!useCaseVO.isDemoModeEnabled()) {
                    memberAccountNameIndex = memberAccountNameIndex % MEMBER_ACCOUNT_NAMES.length;
                    listAccountName = memberAccountNameSuffix == 0 ? MEMBER_ACCOUNT_NAMES[memberAccountNameIndex] : MEMBER_ACCOUNT_NAMES[memberAccountNameIndex] + memberAccountNameSuffix;
                    if (memberAccountNameIndex == MEMBER_ACCOUNT_NAMES.length - 1) {
                        memberAccountNameSuffix++;
                    }
                    memberAccountNameIndex++;
                } else {
                    AwsDemoCloudAccount demoAccount = awsRecommendationSeedDataService.getAccount(memberAccountId);
                    listAccountName = demoAccount.getName();
                    accountStatus = mapToStatus(demoAccount.getPhaseId());
                }
                BigDecimal timeStamp = generateTimeStamp(startDate, MEMBER_ACCOUNT);
                String listAccountEmail = generateEmailForAccounts(listAccountName, accountHolder.getMasterAccountDomainName());
                String joinedMethod = getJoinedMethod();
                Account account = Account.builder()
                        .arn(listAccountArn + memberAccountId)
                        .email(listAccountEmail)
                        .id(memberAccountId)
                        .joinedMethod(joinedMethod)
                        .joinedTimestamp(timeStamp)
                        .name(listAccountName)
                        .status(accountStatus)
                        .build();
                memberAccountList.add(account);
            }
            String nextToken = null;
            if (memberAccountSize != endIndex) {
                String filename = listAccountsFilePaths.get(page);
                if (filename.indexOf(".") > 0) {
                    nextToken = filename.substring(filename.lastIndexOf(FILE_PATH_SEPARATOR) + 1);
                }
            }

            accountBuilder.setLength(0);
            MemberAccounts memberAccounts = MemberAccounts.builder()
                    .account(memberAccountList)
                    .nextToken(nextToken).build();

            DataGeneratorUtils.writeContentToFile(memberAccounts, listAccountsFileName);
        }
    }

    private String mapToStatus(String phaseId) {
        return AccountState.valueOf(phaseId.toUpperCase()).name();
    }

    private void generateDescribeAccountApiResponse(AccountHolder accountHolder, BigDecimal timeStamp, String describeAccountFilePath) throws IOException {
        Account account = Account.builder()
                .arn(accountHolder.getMasterAccountArn())
                .name(accountHolder.getMasterAccountName())
                .id(accountHolder.getMasterAccountId())
                .email(accountHolder.getMasterAccountEmail())
                .joinedTimestamp(timeStamp)
                .status(accountHolder.getMasterAccountStatus())
                .joinedMethod(JOINED_METHODS[1])
                .build();

        MasterAccount masterAccount = MasterAccount.builder()
                .account(account)
                .build();

        DataGeneratorUtils.writeContentToFile(masterAccount, describeAccountFilePath);
    }

    private void generateOrganisationApiResponse(AccountHolder accountHolder, String organizationId, String describeOrganizationFilePath) throws IOException {

        StringBuilder accountBuilder = new StringBuilder();
        accountBuilder.append(ARN_PREFIX)
                .append(accountHolder.getMasterAccountId())
                .append(ORGANIZATION_PREFIX)
                .append(organizationId);
        String organizationArn = accountBuilder.toString();

        accountBuilder.setLength(0);

        Organization organization = Organization.builder()
                .masterAccountArn(accountHolder.getMasterAccountArn())
                .masterAccountEmail(accountHolder.getMasterAccountEmail())
                .masterAccountId(accountHolder.getMasterAccountId())
                .arn(organizationArn)
                .featureSet(FEATURE_SET[0])
                .id(organizationId)
                .build();
        DescribeOrganization describeOrganization = DescribeOrganization.builder().organization(organization).build();

        DataGeneratorUtils.writeContentToFile(describeOrganization, describeOrganizationFilePath);

    }

    private BigDecimal generateTimeStamp(LocalDate startDate, String accountType) {
        if (accountType.equals(MASTER_ACCOUNT)) {
            LocalDate localDate = startDate.minusYears(1);
            Timestamp timestamp = Timestamp.valueOf(localDate.atTime(LocalTime.ofNanoOfDay(random.nextInt(86400000))));
            return BigDecimal.valueOf(timestamp.getTime() / 1000.0).setScale(3, RoundingMode.DOWN);
        } else {
            long minDay = startDate.toEpochDay();
            long maxDay = startDate.plusYears(1).toEpochDay();
            long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
            LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
            Timestamp timestamp = Timestamp.valueOf(randomDate.atTime(LocalTime.ofNanoOfDay(random.nextInt(86400000))));
            return BigDecimal.valueOf(timestamp.getTime() / 1000.0).setScale(3, RoundingMode.DOWN);
        }
    }

    private String generateMasterAccountName() {
        int index = random.nextInt(ROOT_ACCOUNT_NAMES.length);
        return ROOT_ACCOUNT_NAMES[index];
    }

    private String generateEmailForAccounts(String accountName, String masterAccountDomainName) {
        String accountNameLower = getLowerCase(accountName);
        String name = accountNameLower.replace(" ", "-");
        return name + AT_SYMBOL + masterAccountDomainName + TOP_LEVEL_DOMAIN;
    }

    private String generateMasterAccountDomainName(String masterAccountName) {
        String memberAccountNameLower = getLowerCase(masterAccountName);
        return memberAccountNameLower.replace(" ", "");
    }

    private String getLowerCase(String text) {
        return text.toLowerCase().trim();
    }

    private String getJoinedMethod() {
        double invitedProbability = 0.8;
        if (random.nextDouble() < invitedProbability) {
            return JOINED_METHODS[0];
        } else {
            return JOINED_METHODS[1];
        }
    }

    private int getNumberFromString(String str) {
        String numberString = str.substring(str.lastIndexOf('_') + 1, str.lastIndexOf('-'));
        return Integer.parseInt(numberString);
    }
}

