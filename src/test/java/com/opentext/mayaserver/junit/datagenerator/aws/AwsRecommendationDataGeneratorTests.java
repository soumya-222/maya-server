package com.opentext.mayaserver.junit.datagenerator.aws;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.opentext.mayaserver.config.ApplicationPropertiesConfig;
import com.opentext.mayaserver.datagenerators.aws.recommendation.AWSRecommendationGenerator;
import com.opentext.mayaserver.datagenerators.aws.recommendation.RecommendationHelper;
import com.opentext.mayaserver.datagenerators.aws.recommendation.model.AwsLookbackPeriod;
import com.opentext.mayaserver.datagenerators.aws.recommendation.model.AwsSavingsPlanRequestAccountScope;
import com.opentext.mayaserver.datagenerators.aws.recommendation.model.AwsSavingsPlanRequestPaymentOption;
import com.opentext.mayaserver.datagenerators.aws.recommendation.model.AwsSavingsPlanRequestPlanType;
import com.opentext.mayaserver.datagenerators.aws.recommendation.model.AwsSavingsPlanRequestTerm;
import com.opentext.mayaserver.datagenerators.aws.recommendation.model.RecommendationHolder;
import com.opentext.mayaserver.junit.datagenerator.util.DataGeneratorTestUtil;
import com.opentext.mayaserver.models.vo.AccountDataVO;
import com.opentext.mayaserver.utility.CommonUtils;
import com.opentext.mayaserver.utility.UseCaseServiceHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.opentext.mayaserver.util.Constants.DEMO_MEMBER_ACCOUNT_ID_LIST;
import static com.opentext.mayaserver.util.Constants.DEMO_ROOT_ACCOUNT_ID_LIST;
import static com.opentext.mayaserver.utility.Constants.AWS_SP_RECOMMENDATION;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AwsRecommendationDataGeneratorTests {

    private static final String USE_CASE_NAME = "test-001";

    @Autowired
    AWSRecommendationGenerator awsRecommendationGenerator;
    @Autowired
    ApplicationPropertiesConfig applicationPropertiesConfig;
    @Autowired
    RecommendationHelper recommendationHelper;

    private final DataGeneratorTestUtil dataGeneratorTestUtil = new DataGeneratorTestUtil();
    private final AccountDataVO accountDataVO = createAccountDataVO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void contextLoad() {
        assertNotNull(awsRecommendationGenerator);
        assertNotNull(applicationPropertiesConfig);
    }

    @Test
    public void generateAwsSpRecommendationApiResponsesNoDemoModeEnabledTest() throws NoSuchMethodException, IOException {
        String path = ReflectionTestUtils.invokeMethod(CommonUtils.class, "createDirectoryBasedOnUseCase", USE_CASE_NAME, applicationPropertiesConfig.getRecommendation().getNfsDataPath());
        assert path != null;

        Method generateAwsSpRecommendationApiResponses = AWSRecommendationGenerator.class.getDeclaredMethod("generateAwsSpRecommendationApiResponses", AccountDataVO.class, List.class, boolean.class);
        generateAwsSpRecommendationApiResponses.setAccessible(true);
        List<RecommendationHolder> spRecommendationHolders = getAwsSpRecommendationHolders(USE_CASE_NAME);
        assertEquals(114, spRecommendationHolders.size());
        ReflectionUtils.invokeMethod(generateAwsSpRecommendationApiResponses, awsRecommendationGenerator, accountDataVO, spRecommendationHolders, false);

        Set<String> totalIdSet = new HashSet<>();
        for (RecommendationHolder r: spRecommendationHolders) {
            String dataFile = r.getDataFilePath();
            validateJsonContentMatch(r.getName(), dataFile, false, totalIdSet);
        }

        assertTrue(totalIdSet.contains(accountDataVO.getRootAccount()));
        assertTrue(totalIdSet.containsAll(accountDataVO.getMemberAccounts()));

        File useCaseDirectory = new File(path);
        if (useCaseDirectory.exists()) {
            FileUtils.deleteDirectory(useCaseDirectory);
        }
    }

    @Test
    public void generateAwsSpRecommendationApiResponsesDemoModeEnabledTest1() throws NoSuchMethodException, IOException {
        demoModeEnabledTest("usageAccount1", Arrays.asList("usageAccount2", "wpfo80dosug", "1lv3a6vkc36"));
    }

    @Test
    public void generateAwsSpRecommendationApiResponsesDemoModeEnabledTest2() throws NoSuchMethodException, IOException {
        demoModeEnabledTest("usageAccount3", Arrays.asList("usageAccount4", "wiqb9u2ol7f"));
    }

    public void demoModeEnabledTest(String rootAccountId, List<String> memberAccountIds) throws NoSuchMethodException, IOException {
        String path = ReflectionTestUtils.invokeMethod(CommonUtils.class, "createDirectoryBasedOnUseCase", USE_CASE_NAME, applicationPropertiesConfig.getRecommendation().getNfsDataPath());
        assert path != null;

        Method generateAwsSpRecommendationApiResponses = AWSRecommendationGenerator.class.getDeclaredMethod("generateAwsSpRecommendationApiResponses", AccountDataVO.class, List.class, boolean.class);
        generateAwsSpRecommendationApiResponses.setAccessible(true);
        List<RecommendationHolder> spRecommendationHolders = getAwsSpRecommendationHolders(USE_CASE_NAME);
        assertEquals(114, spRecommendationHolders.size());
        AccountDataVO demoAccountDataVO = createDemoAccountDataVO(rootAccountId, memberAccountIds);
        ReflectionUtils.invokeMethod(generateAwsSpRecommendationApiResponses, awsRecommendationGenerator, demoAccountDataVO, spRecommendationHolders, true);

        Set<String> totalIdSet = new HashSet<>();
        for (RecommendationHolder r: spRecommendationHolders) {
            String dataFile = r.getDataFilePath();
            validateJsonContentMatch(r.getName(), dataFile, true, totalIdSet);
        }

        assertEquals(memberAccountIds.size() + 1, totalIdSet.size());
        assertTrue(totalIdSet.toString(), totalIdSet.contains(rootAccountId));
        assertTrue(totalIdSet.toString(), totalIdSet.containsAll(memberAccountIds));

        File useCaseDirectory = new File(path);
        if (useCaseDirectory.exists()) {
            FileUtils.deleteDirectory(useCaseDirectory);
        }
    }

    private void validateJsonContentMatch(String nameString, String filePath, boolean isDemoModeEnabled, Set<String> totalIdSet) {
        String[] nameArray = nameString.split("-");
        assertTrue(nameString, nameArray.length == 5 || nameArray.length == 6);
        if (nameArray.length == 6) {
            assertEquals(nameString, "p2", nameArray[5]);
        }
        try {
            String fileContent = readFile(filePath);

            ObjectNode rootNode = (ObjectNode) objectMapper.readTree(fileContent);
            if (nameArray.length == 5 && nameArray[0].equals("LI") && nameArray[1].equals("7d") && nameArray[3].equals("EC")) {
                assertNotNull(nameString, rootNode.get("NextPageToken"));
                assertTrue(nameString, !StringUtils.isEmpty(rootNode.get("NextPageToken").textValue()));
            } else {
                assertNull(nameString, rootNode.get("NextPageToken"));
            }

            ObjectNode metadata = (ObjectNode) rootNode.get("Metadata");
            assertNotNull(nameString, metadata.get("GenerationTimestamp"));
            assertNotNull(nameString, metadata.get("RecommendationId"));
            ObjectNode SpRecommendation = (ObjectNode) rootNode.get("SavingsPlansPurchaseRecommendation");
            validateAccountScope(nameString, nameArray[0], SpRecommendation.get("AccountScope").textValue());
            validateLookbackPeriodInDays(nameString, nameArray[1], SpRecommendation.get("LookbackPeriodInDays").textValue());
            validatePaymentOption(nameString, nameArray[2], SpRecommendation.get("PaymentOption").textValue());
            validateSavingsPlansType(nameString, nameArray[3], SpRecommendation.get("SavingsPlansType").textValue());
            validateTermInYears(nameString, nameArray[4], SpRecommendation.get("TermInYears").textValue());
            validateAccountIds(nameString, (ArrayNode) SpRecommendation.get("SavingsPlansPurchaseRecommendationDetails"), nameArray[0], isDemoModeEnabled, totalIdSet);
        } catch (IOException e) {
            fail("Failed to get content from file " + filePath + ": " + e);
        }
    }

    private void validateAccountScope(String nameString, String shortName, String accountScope) {
        try {
            assertEquals(nameString,
                    AwsSavingsPlanRequestAccountScope.enabled().stream().filter(a -> a.getShortName().equals(shortName)).findFirst().get().name(),
                    accountScope.toUpperCase());
        } catch (Exception e) {
            fail(nameString + ": " + e);
        }
    }

    private void validateLookbackPeriodInDays(String nameString, String shortName, String lookbackPeriodInDays) {
        try {
            assertEquals(nameString,
                    AwsLookbackPeriod.enabled().stream().filter(a -> a.getShortName().equals(shortName)).findFirst().get().name(),
                    lookbackPeriodInDays);
        } catch (Exception e) {
            fail(nameString + ": " + e);
        }
    }

    private void validatePaymentOption(String nameString, String shortName, String paymentOption) {
        try {
            assertEquals(nameString,
                    AwsSavingsPlanRequestPaymentOption.enabled().stream().filter(a -> a.getShortName().equals(shortName)).findFirst().get().name(),
                    paymentOption);
        } catch (Exception e) {
            fail(nameString + ": " + e);
        }
    }

    private void validateSavingsPlansType(String nameString, String shortName, String savingsPlansType) {
        try {
            assertEquals(nameString,
                    AwsSavingsPlanRequestPlanType.enabled().stream().filter(a -> a.getShortName().equals(shortName)).findFirst().get().name(),
                    savingsPlansType);
        } catch (Exception e) {
            fail(nameString + ": " + e);
        }
    }

    private void validateTermInYears(String nameString, String shortName, String termInYears) {
        try {
            assertEquals(nameString,
                    AwsSavingsPlanRequestTerm.enabled().stream().filter(a -> a.getShortName().equals(shortName)).findFirst().get().name(),
                    termInYears);
        } catch (Exception e) {
            fail(nameString + ": " + e);
        }
    }

    private void validateAccountIds(String nameString, ArrayNode SpRecommendationDetailsArray, String AccountScopeSHortName, boolean isDemoModeEnabled, Set<String> totalIdSet) {
        assertNotNull(nameString, SpRecommendationDetailsArray);
        for (JsonNode SpRecommendationDetails : SpRecommendationDetailsArray) {
            ObjectNode detailsNode = (ObjectNode) SpRecommendationDetails;
            String accountId = detailsNode.get("AccountId").textValue();
            totalIdSet.add(accountId);
            if (!isDemoModeEnabled) {
                if (AwsSavingsPlanRequestAccountScope.PAYER.getShortName().equals(AccountScopeSHortName)) {
                    assertEquals(nameString, accountDataVO.getRootAccount(), accountId);
                } else {
                    assertTrue(nameString + " " + accountId, accountDataVO.getMemberAccounts().contains(accountId));
                }
            } else {
                if (AwsSavingsPlanRequestAccountScope.PAYER.getShortName().equals(AccountScopeSHortName)) {
                    assertTrue(nameString + " " + accountId, DEMO_ROOT_ACCOUNT_ID_LIST.contains(accountId));
                } else {
                    assertTrue(nameString + " " + accountId, DEMO_MEMBER_ACCOUNT_ID_LIST.contains(accountId));
                }

            }
        }
    }
    private AccountDataVO createAccountDataVO() {
        AccountDataVO accountDataVO = new AccountDataVO();
        UseCaseServiceHelper useCaseServiceHelper = new UseCaseServiceHelper();
        accountDataVO.setRootAccount(useCaseServiceHelper.generateRandomAccountId());
        accountDataVO.setMemberAccounts(dataGeneratorTestUtil.getMemberAccountIdList());
        accountDataVO.setId("testId");
        return accountDataVO;
    }
    private AccountDataVO createDemoAccountDataVO(String rootAccountId, List<String> memberAccountIds) {
        AccountDataVO accountDataVO = new AccountDataVO();
        accountDataVO.setRootAccount(rootAccountId);
        accountDataVO.setMemberAccounts(memberAccountIds);
        return accountDataVO;
    }

    private List<RecommendationHolder> getAwsSpRecommendationHolders(String useCaseName) {
        return recommendationHelper.getRecommendationHolders(useCaseName).stream().filter(r -> r.getEndpointType() == AWS_SP_RECOMMENDATION).toList();
    }

    private String readFile(String filePath) throws IOException {
        File file = new File(filePath);
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }
}
