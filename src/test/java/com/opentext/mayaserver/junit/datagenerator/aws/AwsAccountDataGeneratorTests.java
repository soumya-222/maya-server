package com.opentext.mayaserver.junit.datagenerator.aws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentext.mayaserver.TestUtility;
import com.opentext.mayaserver.config.ApplicationPropertiesConfig;
import com.opentext.mayaserver.datagenerators.aws.account.AWSAccountDataGenerator;
import com.opentext.mayaserver.datagenerators.aws.account.AccountHolder;
import com.opentext.mayaserver.junit.datagenerator.util.DataGeneratorTestUtil;
import com.opentext.mayaserver.models.*;
import com.opentext.mayaserver.models.vo.UseCaseVO;
import com.opentext.mayaserver.utility.CommonUtils;
import com.opentext.mayaserver.utility.Constants;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.opentext.mayaserver.utility.Constants.FILE_PATH_SEPARATOR;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AwsAccountDataGeneratorTests {

    @Autowired
    AWSAccountDataGenerator awsAccountDataGenerator;
    @Autowired
    ApplicationPropertiesConfig applicationPropertiesConfig;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TestUtility testUtility = new TestUtility();
    private final DataGeneratorTestUtil dataGeneratorTestUtil = new DataGeneratorTestUtil();

    @Test
    public void contextLoad() {
        assertNotNull(awsAccountDataGenerator);
        assertNotNull(applicationPropertiesConfig);
    }

    @Test
    public void generateListAccountsApiResponseTest() throws NoSuchMethodException, IOException {
        String path = ReflectionTestUtils.invokeMethod(CommonUtils.class, "createDirectoryBasedOnUseCase", "nova-1", applicationPropertiesConfig.getMockoon().getNfsDataPath());
        assert path != null;
        String organizationId = dataGeneratorTestUtil.getOrganizationId();
        AccountHolder accountHolder = dataGeneratorTestUtil.getAccountHolder();
        List<String> memberAccountIdList = dataGeneratorTestUtil.getMemberAccountIdList();
        List<String> listAccountsFilePaths = dataGeneratorTestUtil.getListAccountFilePaths(path);
        UseCaseVO useCaseVO = testUtility.getUseCaseVO();
        LocalDate localDate = dataGeneratorTestUtil.getLocalDate();
        Method generateListAccountsApiResponse = AWSAccountDataGenerator.class.getDeclaredMethod("generateListAccountsApiResponse", UseCaseVO.class, AccountHolder.class, String.class, List.class, LocalDate.class, List.class);
        generateListAccountsApiResponse.setAccessible(true);
        ReflectionUtils.invokeMethod(generateListAccountsApiResponse, awsAccountDataGenerator, useCaseVO, accountHolder, organizationId, memberAccountIdList, localDate, listAccountsFilePaths);

        // Verifying if file is created with the proper json value
        File file = new File(path + FILE_PATH_SEPARATOR + "nova-1_AWS_ListAccounts_1-5.json");
        assertTrue(file.exists());
        MemberAccounts memberAccounts = objectMapper.readValue(new File(path + FILE_PATH_SEPARATOR + "nova-1_AWS_ListAccounts_1-5.json"), MemberAccounts.class);
        List<Account> accountList = memberAccounts.getAccount();
        assertEquals("nova-1_AWS_ListAccounts_6-10.json", memberAccounts.getNextToken());
        assertEquals(5, accountList.size());
        file = new File(path + FILE_PATH_SEPARATOR + "nova-1_AWS_ListAccounts_6-10.json");
        assertTrue(file.exists());
        memberAccounts = objectMapper.readValue(new File(path + FILE_PATH_SEPARATOR + "nova-1_AWS_ListAccounts_6-10.json"), MemberAccounts.class);
        accountList = memberAccounts.getAccount();
        assertNull(memberAccounts.getNextToken());
        assertEquals(4, accountList.size());
        File useCaseDirectory = new File(path);
        if (useCaseDirectory.exists()) {
            FileUtils.deleteDirectory(useCaseDirectory);
        }
    }

    @Test
    public void generateOrganisationApiResponseTest() throws NoSuchMethodException, IOException {
        String path = ReflectionTestUtils.invokeMethod(CommonUtils.class, "createDirectoryBasedOnUseCase", "nova-1", applicationPropertiesConfig.getMockoon().getNfsDataPath());
        assert path != null;
        String organizationId = dataGeneratorTestUtil.getOrganizationId();
        AccountHolder accountHolder = dataGeneratorTestUtil.getAccountHolder();
        Method generateOrganisationApiResponse = AWSAccountDataGenerator.class.getDeclaredMethod("generateOrganisationApiResponse", AccountHolder.class, String.class, String.class);
        generateOrganisationApiResponse.setAccessible(true);
        ReflectionUtils.invokeMethod(generateOrganisationApiResponse, awsAccountDataGenerator, accountHolder, organizationId, path + FILE_PATH_SEPARATOR + "nova-1_AWS_DescribeOrganisation.json");

        // Verifying if file is created with the proper json value
        File file = new File(path + FILE_PATH_SEPARATOR + "nova-1_AWS_DescribeOrganisation.json");
        assertTrue(file.exists());
        DescribeOrganization describeOrganization = objectMapper.readValue(new File(path + FILE_PATH_SEPARATOR + "nova-1_AWS_DescribeOrganisation.json"), DescribeOrganization.class);
        Organization organization = describeOrganization.getOrganization();
        assertEquals("745496560928", organization.getMasterAccountId());
        assertEquals("arn:aws:organizations::745496560928:account/o-r85162u2oc0/745496560928", organization.getMasterAccountArn());
        assertEquals("acme-inc@acmeinc.com", organization.getMasterAccountEmail());
        assertEquals(organizationId, organization.getId());
        File useCaseDirectory = new File(path);
        if (useCaseDirectory.exists()) {
            FileUtils.deleteDirectory(useCaseDirectory);
        }
    }

    @Test
    public void generateDescribeAccountApiResponseTest() throws NoSuchMethodException, IOException {
        String path = ReflectionTestUtils.invokeMethod(CommonUtils.class, "createDirectoryBasedOnUseCase", "nova-1", applicationPropertiesConfig.getMockoon().getNfsDataPath());
        assert path != null;
        AccountHolder accountHolder = dataGeneratorTestUtil.getAccountHolder();
        BigDecimal timeStamp = dataGeneratorTestUtil.getTimeStamp();
        Method generateDescribeAccountApiResponse = AWSAccountDataGenerator.class.getDeclaredMethod("generateDescribeAccountApiResponse", AccountHolder.class, BigDecimal.class, String.class);
        generateDescribeAccountApiResponse.setAccessible(true);
        ReflectionUtils.invokeMethod(generateDescribeAccountApiResponse, awsAccountDataGenerator, accountHolder, timeStamp, path + FILE_PATH_SEPARATOR + "nova-1_AWS_DescribeAccount.json");

        // Verifying if file is created with the proper json value
        File file = new File(path + FILE_PATH_SEPARATOR + "nova-1_AWS_DescribeAccount.json");
        assertTrue(file.exists());
        MasterAccount masterAccount = objectMapper.readValue(new File(path + FILE_PATH_SEPARATOR + "nova-1_AWS_DescribeAccount.json"), MasterAccount.class);
        Account account = masterAccount.getAccount();
        assertEquals("ACME INC", account.getName());
        assertEquals("arn:aws:organizations::745496560928:account/o-r85162u2oc0/745496560928", account.getArn());
        assertEquals("acme-inc@acmeinc.com", account.getEmail());
        assertEquals("745496560928", account.getId());
        File useCaseDirectory = new File(path);
        if (useCaseDirectory.exists()) {
            FileUtils.deleteDirectory(useCaseDirectory);
        }
    }

    @Test
    public void getNumberFromStringTest() {
        assertEquals(81, (Integer) ReflectionTestUtils.invokeMethod(awsAccountDataGenerator, "getNumberFromString", "nova-1_AWS_ListAccounts_81-100.json"));
    }

    @Test
    public void getLowerCaseTest() {
        assertEquals("test user", ReflectionTestUtils.invokeMethod(awsAccountDataGenerator, "getLowerCase", "TEST USER"));
        assertEquals("test user", ReflectionTestUtils.invokeMethod(awsAccountDataGenerator, "getLowerCase", "  Test User "));
        assertEquals("test user", ReflectionTestUtils.invokeMethod(awsAccountDataGenerator, "getLowerCase", "test user"));
    }

    @Test
    public void generateMasterAccountDomainNameTest() {
        assertEquals("dataanalysis", ReflectionTestUtils.invokeMethod(awsAccountDataGenerator, "generateMasterAccountDomainName", "DATA ANALYSIS"));
    }

    @Test
    public void generateEmailForAccountsTest() {
        assertEquals("test-user@helloworld.com", ReflectionTestUtils.invokeMethod(awsAccountDataGenerator, "generateEmailForAccounts", "TEST USER", "helloworld"));
    }

    @Test
    public void generateMasterAccountNameTest() {
        String masterAccountName = ReflectionTestUtils.invokeMethod(awsAccountDataGenerator, "generateMasterAccountName");
        assert masterAccountName != null;
        boolean masterAccountNamePresent = false;
        for (int i = 0; i < Constants.ROOT_ACCOUNT_NAMES.length; i++) {
            if (masterAccountName.equals(Constants.ROOT_ACCOUNT_NAMES[i])) {
                masterAccountNamePresent = true;
                break;
            }
        }
        assertTrue(masterAccountNamePresent);
    }

}
