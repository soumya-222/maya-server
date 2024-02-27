package com.opentext.mayaserver.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.opentext.mayaserver.exceptions.MayaServerException;
import com.opentext.mayaserver.models.AccountData;
import com.opentext.mayaserver.models.AccountMetadata;
import com.opentext.mayaserver.models.UseCase;
import com.opentext.mayaserver.models.vo.AccountDataVO;
import com.opentext.mayaserver.models.vo.AccountMetadataVO;
import com.opentext.mayaserver.models.vo.AccountResponseVO;
import com.opentext.mayaserver.repository.UseCaseRepository;
import com.opentext.mayaserver.services.DataGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Rajiv
 */

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DataGeneratorServiceImpl implements DataGeneratorService {

    private final UseCaseRepository useCaseRepository;


    @Override
    public String getRootAccount(String useCaseName) {
        Optional<UseCase> useCase = fetchUseCaseDetails(useCaseName);
        if (useCase.isPresent()) {
            return useCase.get().getAccountDataList().get(0).getRootAccount();
        } else {
            log.error("Unable to fetch associated Root Account for use case '{}'", useCaseName);
            return null;
        }
    }

    @Override
    public List<String> getMemberAccounts(String useCaseName) {
        Optional<UseCase> useCase = fetchUseCaseDetails(useCaseName);
        List<String> memberAccountList = null;
        if (useCase.isPresent()) {
            String memberAccountStringList = useCase.get().getAccountDataList().get(0).getMemberAccounts();
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.findAndRegisterModules();
                objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                memberAccountList = objectMapper.readValue(memberAccountStringList, List.class);
            } catch (Exception e) {
                log.error(" Unable to parse member account list associated with UseCase '{}'", useCaseName);
            }
            return memberAccountList;
        } else {
            log.error("Unable to fetch associated member account for use case '{}'", useCaseName);
            return null;
        }
    }

    @Override
    public AccountDataVO getAccountData(String useCaseName) {
        Optional<UseCase> useCaseOptional = fetchUseCaseDetails(useCaseName);
        AccountDataVO accountDataVO = null;
        if (useCaseOptional.isPresent()) {
            accountDataVO = new AccountDataVO();
            AccountData accountData = useCaseOptional.get().getAccountDataList().get(0);
            accountDataVO.setId(accountData.getId());
            accountDataVO.setRootAccount(accountData.getRootAccount());
            accountDataVO.setMemberAccounts(parseMemberAccount(accountData.getMemberAccounts()));
            return accountDataVO;
        } else {
            log.error("Unable to fetch associated account data for use case '{}'", useCaseName);
            return null;
        }
    }

    @Override
    public AccountResponseVO getAccountDataList(String useCaseName) {
        Optional<UseCase> useCaseOptional = fetchUseCaseDetails(useCaseName);
        AccountResponseVO accountResponseVO = null;
        if (useCaseOptional.isPresent()) {
            accountResponseVO = new AccountResponseVO();
            List<AccountData> accountData = useCaseOptional.get().getAccountDataList();
            List<AccountDataVO> accountDataVOList = accountData.stream()
                    .map(account -> {
                        AccountDataVO accountDataVO = new AccountDataVO();
                        accountDataVO.setId(account.getId());
                        accountDataVO.setRootAccount(account.getRootAccount());
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();
                            List<String> memberAccounts = objectMapper.readValue(account.getMemberAccounts(), new TypeReference<List<String>>() {
                            });
                            accountDataVO.setMemberAccounts(memberAccounts);
                        } catch (Exception e) {
                            throw new MayaServerException(e.getMessage(), e);
                        }

                        return accountDataVO;
                    })
                    .collect(Collectors.toList());
            accountResponseVO.setAccountData(accountDataVOList);

            return accountResponseVO;
        } else {
            log.error("Unable to fetch associated account data for use case '{}'", useCaseName);
            return null;
        }
    }

    @Override
    public List<AccountMetadataVO> getAccountMetaData(String useCaseName) {
        Optional<UseCase> useCaseOptional = fetchUseCaseDetails(useCaseName);
        List<AccountMetadataVO> accountMetadataVOList = null;
        AccountMetadataVO accountMetadataVO = null;
        if (useCaseOptional.isPresent()) {
            accountMetadataVOList = new ArrayList<>();
            List<AccountMetadata> accountMetadataList = useCaseOptional.get().getAccountMetadataList();
            if (accountMetadataList != null && accountMetadataList.size() > 0) {
                for (AccountMetadata accountMetadata : accountMetadataList) {
                    accountMetadataVO = new AccountMetadataVO();
                    accountMetadataVO.setId(accountMetadata.getId());
                    accountMetadataVO.setEndpointType(accountMetadata.getEndpointType());
                    accountMetadataVO.setDataFilePath(parseMemberAccount(accountMetadata.getDataFilePath()));
                    accountMetadataVOList.add(accountMetadataVO);
                }
            }
        } else {
            log.error(" Unable to parse account metadata for associated usecase {}", useCaseName);
        }
        return accountMetadataVOList;
    }

    private List<String> parseMemberAccount(String memberAccountStringList) {
        List<String> memberAccountList = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            memberAccountList = objectMapper.readValue(memberAccountStringList, List.class);
        } catch (Exception e) {
            log.error(" Unable to parse member account list");
        }
        return memberAccountList;
    }

    public Optional<UseCase> fetchUseCaseDetails(String useCaseName) {
        return useCaseRepository.findByUseCaseNameOrId(useCaseName, useCaseName);
    }
}
