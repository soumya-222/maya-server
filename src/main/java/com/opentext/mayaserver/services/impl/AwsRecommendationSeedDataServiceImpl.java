package com.opentext.mayaserver.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentext.mayaserver.datagenerators.aws.account.AwsDemoCloudAccount;
import com.opentext.mayaserver.exceptions.MayaServerException;
import com.opentext.mayaserver.services.AwsRecommendationSeedDataService;
import com.opentext.mayaserver.utility.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.opentext.mayaserver.utility.Constants.AWS_DEMO_CLOUD_ACCOUNTS_FILE;

@Service
@Slf4j
public class AwsRecommendationSeedDataServiceImpl implements AwsRecommendationSeedDataService {
    private Map<String, AwsDemoCloudAccount> demoAccountsMap;

    public AwsRecommendationSeedDataServiceImpl() {
        loadDemoAccounts();
    }

    @Override
    public List<String> listValidRootAccountIds() {
        return demoAccountsMap.values().stream().filter(account -> account.isRootAccount()).map(account -> account.getAccountId()).collect(Collectors.toList());
    }

    @Override
    public List<String> getMemberAccountIds(String rootAccountId) {
        List<String> accountIds = new ArrayList<>();
        demoAccountsMap.forEach((id, account) -> {
            if (!account.isRootAccount() && rootAccountId.equals(account.getRootAccountId())) {
                accountIds.add(id);
            }
        });
        return accountIds;
    }

    @Override
    public List<String> listAllMemberAccountIds() {
        return demoAccountsMap.values().stream().filter(account -> !account.isRootAccount()).map(account -> account.getAccountId()).collect(Collectors.toList());
    }

    @Override
    public AwsDemoCloudAccount getAccount(String accountId) {
        return demoAccountsMap.get(accountId);
    }

    private void loadDemoAccounts() {
        demoAccountsMap = new HashMap<>();
        try {
            String fileContent = CommonUtils.readFileFromResource("demo", AWS_DEMO_CLOUD_ACCOUNTS_FILE);
            List<AwsDemoCloudAccount> demoCloudAccounts = (new ObjectMapper()).readValue(fileContent, new TypeReference<>() {});
            demoAccountsMap = demoCloudAccounts.stream().collect(Collectors.toMap(AwsDemoCloudAccount::getAccountId, Function.identity()));
        }  catch (IOException e) {
            log.error("Failed to process AWS demo accounts file {}: {}", AWS_DEMO_CLOUD_ACCOUNTS_FILE, e.toString());
            throw new MayaServerException("Failed to process AWS demo accounts file " + AWS_DEMO_CLOUD_ACCOUNTS_FILE + ": " + e);
        }
    }
}
