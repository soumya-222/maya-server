package com.opentext.mayaserver.services;

import com.opentext.mayaserver.datagenerators.aws.account.AwsDemoCloudAccount;

import java.util.List;

public interface AwsRecommendationSeedDataService {
    List<String> listValidRootAccountIds();

    List<String> getMemberAccountIds(String rootAccountId);

    List<String> listAllMemberAccountIds();

    AwsDemoCloudAccount getAccount(String accountId);
}
