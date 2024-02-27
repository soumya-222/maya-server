package com.opentext.mayaserver.services.impl;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.opentext.mayaserver.util.Constants.DEMO_MEMBER_ACCOUNT_ID_LIST;
import static com.opentext.mayaserver.util.Constants.DEMO_ROOT_ACCOUNT_ID_LIST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AwsRecommendationSeedDataServiceImplTest {
    private AwsRecommendationSeedDataServiceImpl awsRecommendationSeedDataService;

    public AwsRecommendationSeedDataServiceImplTest() {
        this.awsRecommendationSeedDataService = new AwsRecommendationSeedDataServiceImpl();
    }

    @Test
    public void verifyAllRecommendationsCanBeRead() {
        List<String> validRootAccountIds = awsRecommendationSeedDataService.listValidRootAccountIds();
        assertEquals(2, validRootAccountIds.size());
        assertTrue(validRootAccountIds.containsAll(DEMO_ROOT_ACCOUNT_ID_LIST));

        List<String> allMemberAccountIds = awsRecommendationSeedDataService.listAllMemberAccountIds();
        assertEquals(5, allMemberAccountIds.size());
        assertTrue(allMemberAccountIds.containsAll(DEMO_MEMBER_ACCOUNT_ID_LIST));
    }
}
