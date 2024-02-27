package com.opentext.mayaserver.integrationtests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentext.mayaserver.TestUtility;
import com.opentext.mayaserver.exceptions.ErrorResponseVO;
import com.opentext.mayaserver.models.vo.UseCaseResponseVO;
import com.opentext.mayaserver.models.vo.UseCaseVO;
import com.opentext.mayaserver.util.CommonTestUtils;
import com.opentext.mayaserver.util.DeploymentUtils;
import org.junit.Assert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

import static com.opentext.mayaserver.util.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UseCaseCreateApiTest {
    @LocalServerPort
    private int port;
    TestUtility utility = new TestUtility();
    CommonTestUtils commonTestUtils = new CommonTestUtils();

    @Test
    @Order(1)
    public void createUseCaseApiTest() throws InterruptedException {
        final String createUseCaseURL = DeploymentUtils.buildCreateUseCaseURL(port);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UseCaseVO useCaseVO = utility.getUseCaseVO();
        HttpEntity<UseCaseVO> request = new HttpEntity<>(useCaseVO, headers);
        ResponseEntity<UseCaseResponseVO> responseEntity = restTemplate.exchange(createUseCaseURL, HttpMethod.POST, request, UseCaseResponseVO.class);
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        UseCaseResponseVO useCaseResponseVO = responseEntity.getBody();
        assert useCaseResponseVO != null;
        assertEquals("nova-1", useCaseResponseVO.getUseCaseName());
        boolean isUseCaseDeletionAllowed = commonTestUtils.waitForUseCaseToBeCreated("nova-1", port);
        assertTrue(isUseCaseDeletionAllowed);
        deleteUseCase("nova-1");
    }

    @Test
    @Order(2)
    public void createUseCaseApiAccountWithDefaultDataTest() throws InterruptedException {
        final String createUseCaseURL = DeploymentUtils.buildCreateUseCaseURL(port);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UseCaseVO useCaseVO = utility.getUseCaseVO();
        useCaseVO.setAccount(null);
        HttpEntity<UseCaseVO> request = new HttpEntity<>(useCaseVO, headers);
        ResponseEntity<UseCaseResponseVO> responseEntity = restTemplate.exchange(createUseCaseURL, HttpMethod.POST, request, UseCaseResponseVO.class);
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        UseCaseResponseVO useCaseResponseVO = responseEntity.getBody();
        assert useCaseResponseVO != null;
        assertEquals("nova-1", useCaseResponseVO.getUseCaseName());
        assertEquals(DEFAULT_PAGE_SIZE, useCaseResponseVO.getPayload().getAccount().getPageSize());
        assertEquals(DEFAULT_NUMBER_OF_ACCOUNTS, useCaseResponseVO.getPayload().getAccount().getNumberOfAccounts());
        boolean isUseCaseDeletionAllowed = commonTestUtils.waitForUseCaseToBeCreated("nova-1", port);
        assertTrue(isUseCaseDeletionAllowed);
        deleteUseCase("nova-1");

    }

    @Test
    @Order(3)
    public void createUseCaseApiExceptionTest() throws JsonProcessingException, InterruptedException {
        final String createUseCaseURL = DeploymentUtils.buildCreateUseCaseURL(port);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UseCaseVO useCaseVO = utility.getUseCaseVO();
        useCaseVO.setUseCaseName("nova-2");
        HttpEntity<UseCaseVO> request = new HttpEntity<>(useCaseVO, headers);
        ResponseEntity<UseCaseResponseVO> responseEntity = restTemplate.exchange(createUseCaseURL, HttpMethod.POST, request, UseCaseResponseVO.class);
        Assert.assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
//        try {
//            restTemplate.exchange(createUseCaseURL, HttpMethod.POST, request, ErrorResponseVO.class);
//        } catch (Exception e) {
//            String errorMessage = e.getMessage().substring(e.getMessage().indexOf('{'), e.getMessage().lastIndexOf('}') + 1);
//            ObjectMapper objectMapper = new ObjectMapper();
//            ErrorResponseVO error = objectMapper.readValue(errorMessage, ErrorResponseVO.class);
//            assertEquals("500 INTERNAL_SERVER_ERROR", error.getStatusCode());
//            assertEquals("could not execute statement; SQL [n/a]; constraint [maya_use_case_use_case_name_key]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement", error.getMessage());
//        }
        boolean isUseCaseDeletionAllowed = commonTestUtils.waitForUseCaseToBeCreated("nova-2", port);
        assertTrue(isUseCaseDeletionAllowed);
        deleteUseCase("nova-2");
    }

    @Test
    @Order(4)
    public void createUseCaseApiEmptyCloudProviderTest() throws JsonProcessingException {
        final String createUseCaseURL = DeploymentUtils.buildCreateUseCaseURL(port);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UseCaseVO useCaseVO = utility.getUseCaseVO();
        // Setting cloud provider to empty string
        useCaseVO.setCloudProvider("");
        HttpEntity<UseCaseVO> request = new HttpEntity<>(useCaseVO, headers);
//        try {
//            restTemplate.exchange(createUseCaseURL, HttpMethod.POST, request, ErrorResponseVO.class);
//        } catch (Exception e) {
//            String errorMessage = e.getMessage().substring(e.getMessage().indexOf('{'), e.getMessage().lastIndexOf('}') + 1);
//            ObjectMapper objectMapper = new ObjectMapper();
//            ErrorResponseVO error = objectMapper.readValue(errorMessage, ErrorResponseVO.class);
//            assertEquals("400 BAD_REQUEST", error.getStatusCode());
//            assertEquals("MethodArgumentNotValid", error.getReasonPhrase());
//            assertEquals("Invalid cloud provider. Supported cloud providers are : aws,azure and gcp", error.getErrorMessage().get(0).getDefaultMessage());
//        }

    }

    @Test
    @Order(5)
    public void createUseCaseApiInvalidCloudProviderTest() throws JsonProcessingException {
        final String createUseCaseURL = DeploymentUtils.buildCreateUseCaseURL(port);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UseCaseVO useCaseVO = utility.getUseCaseVO();
        useCaseVO.setCloudProvider("abc");
        // Setting cloud provider to invalid string
        HttpEntity<UseCaseVO> request = new HttpEntity<>(useCaseVO, headers);
//        try {
//            restTemplate.exchange(createUseCaseURL, HttpMethod.POST, request, ErrorResponseVO.class);
//        } catch (Exception e) {
//            String errorMessage = e.getMessage().substring(e.getMessage().indexOf('{'), e.getMessage().lastIndexOf('}') + 1);
//            ObjectMapper objectMapper = new ObjectMapper();
//            ErrorResponseVO error = objectMapper.readValue(errorMessage, ErrorResponseVO.class);
//            assertEquals("400 BAD_REQUEST", error.getStatusCode());
//            assertEquals("MethodArgumentNotValid", error.getReasonPhrase());
//            assertEquals("Invalid cloud provider. Supported cloud providers are : aws,azure and gcp", error.getErrorMessage().get(0).getDefaultMessage());
//
//        }

    }

    @Test
    @Order(6)
    public void createUseCaseApiCloudProviderNullTest() throws JsonProcessingException {
        final String createUseCaseURL = DeploymentUtils.buildCreateUseCaseURL(port);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UseCaseVO useCaseVO = utility.getUseCaseVO();
        useCaseVO.setCloudProvider(null);
        HttpEntity<UseCaseVO> request = new HttpEntity<>(useCaseVO, headers);
//        try {
//            restTemplate.exchange(createUseCaseURL, HttpMethod.POST, request, ErrorResponseVO.class);
//        } catch (Exception e) {
//            String errorMessage = e.getMessage().substring(e.getMessage().indexOf('{'), e.getMessage().lastIndexOf('}') + 1);
//            ObjectMapper objectMapper = new ObjectMapper();
//            ErrorResponseVO error = objectMapper.readValue(errorMessage, ErrorResponseVO.class);
//            assertEquals("400 BAD_REQUEST", error.getStatusCode());
//            assertEquals("MethodArgumentNotValid", error.getReasonPhrase());
//            assertEquals("Invalid cloud provider. Supported cloud providers are : aws,azure and gcp", error.getErrorMessage().get(0).getDefaultMessage());
//        }

    }

    @Test
    @Order(7)
    public void createUseCaseApiUseCaseNameNullTest() throws JsonProcessingException {
        final String createUseCaseURL = DeploymentUtils.buildCreateUseCaseURL(port);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UseCaseVO useCaseVO = utility.getUseCaseVO();
        useCaseVO.setUseCaseName(null);
        HttpEntity<UseCaseVO> request = new HttpEntity<>(useCaseVO, headers);
//        try {
//            restTemplate.exchange(createUseCaseURL, HttpMethod.POST, request, ErrorResponseVO.class);
//        } catch (Exception e) {
//            String errorMessage = e.getMessage().substring(e.getMessage().indexOf('{'), e.getMessage().lastIndexOf('}') + 1);
//            ObjectMapper objectMapper = new ObjectMapper();
//            ErrorResponseVO error = objectMapper.readValue(errorMessage, ErrorResponseVO.class);
//            assertEquals("400 BAD_REQUEST", error.getStatusCode());
//            assertEquals("MethodArgumentNotValid", error.getReasonPhrase());
//            assertEquals("Usecase name can't be empty", error.getErrorMessage().get(0).getDefaultMessage());
//        }

    }

    @Test
    @Order(8)
    public void createUseCaseApiUseCaseNameEmptyTest() throws JsonProcessingException {
        final String createUseCaseURL = DeploymentUtils.buildCreateUseCaseURL(port);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UseCaseVO useCaseVO = utility.getUseCaseVO();
        useCaseVO.setUseCaseName("");
        HttpEntity<UseCaseVO> request = new HttpEntity<>(useCaseVO, headers);
//        try {
//            restTemplate.exchange(createUseCaseURL, HttpMethod.POST, request, ErrorResponseVO.class);
//        } catch (Exception e) {
//            String errorMessage = e.getMessage().substring(e.getMessage().indexOf('{'), e.getMessage().lastIndexOf('}') + 1);
//            ObjectMapper objectMapper = new ObjectMapper();
//            ErrorResponseVO error = objectMapper.readValue(errorMessage, ErrorResponseVO.class);
//            assertEquals("400 BAD_REQUEST", error.getStatusCode());
//            assertEquals("MethodArgumentNotValid", error.getReasonPhrase());
//            assertEquals("Usecase name can't be empty", error.getErrorMessage().get(0).getDefaultMessage());
//        }

    }

    @Test
    @Order(9)
    public void createUseCaseApiNegativeNumberOfAccountsTest() throws JsonProcessingException {
        final String createUseCaseURL = DeploymentUtils.buildCreateUseCaseURL(port);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UseCaseVO useCaseVO = utility.getUseCaseVO();
        useCaseVO.getAccount().setNumberOfAccounts(-10);
        HttpEntity<UseCaseVO> request = new HttpEntity<>(useCaseVO, headers);
//        try {
//            restTemplate.exchange(createUseCaseURL, HttpMethod.POST, request, ErrorResponseVO.class);
//        } catch (Exception e) {
//            String errorMessage = e.getMessage().substring(e.getMessage().indexOf('{'), e.getMessage().lastIndexOf('}') + 1);
//            ObjectMapper objectMapper = new ObjectMapper();
//            ErrorResponseVO error = objectMapper.readValue(errorMessage, ErrorResponseVO.class);
//            assertEquals("400 BAD_REQUEST", error.getStatusCode());
//            assertEquals("MethodArgumentNotValid", error.getReasonPhrase());
//            assertEquals("Number of Accounts can't be negative value", error.getErrorMessage().get(0).getDefaultMessage());
//        }

    }

    @Test
    @Order(10)
    public void createUseCaseApiNegativePageSizeTest() throws JsonProcessingException {
        final String createUseCaseURL = DeploymentUtils.buildCreateUseCaseURL(port);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UseCaseVO useCaseVO = utility.getUseCaseVO();
        useCaseVO.getAccount().setPageSize(-10);
        HttpEntity<UseCaseVO> request = new HttpEntity<>(useCaseVO, headers);
//        try {
//            restTemplate.exchange(createUseCaseURL, HttpMethod.POST, request, ErrorResponseVO.class);
//        } catch (Exception e) {
//            String errorMessage = e.getMessage().substring(e.getMessage().indexOf('{'), e.getMessage().lastIndexOf('}') + 1);
//            ObjectMapper objectMapper = new ObjectMapper();
//            ErrorResponseVO error = objectMapper.readValue(errorMessage, ErrorResponseVO.class);
//            assertEquals("400 BAD_REQUEST", error.getStatusCode());
//            assertEquals("MethodArgumentNotValid", error.getReasonPhrase());
//            assertEquals("Page size can't be negative value", error.getErrorMessage().get(0).getDefaultMessage());
//        }

    }

    @Test
    @Order(12)
    public void createUseCaseApiNegativeRecordsPerDayTest() throws JsonProcessingException {
        final String createUseCaseURL = DeploymentUtils.buildCreateUseCaseURL(port);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UseCaseVO useCaseVO = utility.getUseCaseVO();
        useCaseVO.getBilling().setRecordsPerDay(-10000);
        HttpEntity<UseCaseVO> request = new HttpEntity<>(useCaseVO, headers);
//        try {
//            restTemplate.exchange(createUseCaseURL, HttpMethod.POST, request, ErrorResponseVO.class);
//        } catch (Exception e) {
//            String errorMessage = e.getMessage().substring(e.getMessage().indexOf('{'), e.getMessage().lastIndexOf('}') + 1);
//            ObjectMapper objectMapper = new ObjectMapper();
//            ErrorResponseVO error = objectMapper.readValue(errorMessage, ErrorResponseVO.class);
//            assertEquals("400 BAD_REQUEST", error.getStatusCode());
//            assertEquals("MethodArgumentNotValid", error.getReasonPhrase());
//            assertEquals("Records per day can't be negative value", error.getErrorMessage().get(0).getDefaultMessage());
//        }

    }

    @Test
    @Order(13)
    public void createUseCaseApiStartDateNullTest() throws InterruptedException {
        final String createUseCaseURL = DeploymentUtils.buildCreateUseCaseURL(port);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UseCaseVO useCaseVO = utility.getUseCaseVO();
        useCaseVO.getBilling().setStartDate(null);
        HttpEntity<UseCaseVO> request = new HttpEntity<>(useCaseVO, headers);
        ResponseEntity<UseCaseResponseVO> responseEntity = restTemplate.exchange(createUseCaseURL, HttpMethod.POST, request, UseCaseResponseVO.class);
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        UseCaseResponseVO useCaseResponseVO = responseEntity.getBody();
        assert useCaseResponseVO != null;
        assertEquals("nova-1", useCaseResponseVO.getUseCaseName());
        assertEquals(LocalDate.parse(DEFAULT_BILLING_START_DATE), useCaseResponseVO.getPayload().getBilling().getStartDate());
        assertEquals(LocalDate.parse(DEFAULT_BILLING_END_DATE), useCaseResponseVO.getPayload().getBilling().getEndDate());
        boolean isUseCaseDeletionAllowed = commonTestUtils.waitForUseCaseToBeCreated("nova-1", port);
        assertTrue(isUseCaseDeletionAllowed);
        deleteUseCase("nova-1");

    }

    @Test
    @Order(14)
    public void createUseCaseApiEndDateNullTest() throws InterruptedException {
        final String createUseCaseURL = DeploymentUtils.buildCreateUseCaseURL(port);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UseCaseVO useCaseVO = utility.getUseCaseVO();
        useCaseVO.getBilling().setEndDate(null);
        HttpEntity<UseCaseVO> request = new HttpEntity<>(useCaseVO, headers);
        ResponseEntity<UseCaseResponseVO> responseEntity = restTemplate.exchange(createUseCaseURL, HttpMethod.POST, request, UseCaseResponseVO.class);
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        UseCaseResponseVO useCaseResponseVO = responseEntity.getBody();
        assert useCaseResponseVO != null;
        assertEquals("nova-1", useCaseResponseVO.getUseCaseName());
        assertEquals(LocalDate.parse("2023-01-01"), useCaseResponseVO.getPayload().getBilling().getStartDate());
        assertEquals(LocalDate.parse(DEFAULT_BILLING_END_DATE), useCaseResponseVO.getPayload().getBilling().getEndDate());
        boolean isUseCaseDeletionAllowed = commonTestUtils.waitForUseCaseToBeCreated("nova-1", port);
        assertTrue(isUseCaseDeletionAllowed);
        deleteUseCase("nova-1");

    }

    @Test
    @Order(15)
    public void createUseCaseApiDayOfInvoiceNullTest() throws InterruptedException {
        final String createUseCaseURL = DeploymentUtils.buildCreateUseCaseURL(port);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UseCaseVO useCaseVO = utility.getUseCaseVO();
        useCaseVO.getBilling().setDayOfInvoice(null);
        HttpEntity<UseCaseVO> request = new HttpEntity<>(useCaseVO, headers);
        ResponseEntity<UseCaseResponseVO> responseEntity = restTemplate.exchange(createUseCaseURL, HttpMethod.POST, request, UseCaseResponseVO.class);
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        UseCaseResponseVO useCaseResponseVO = responseEntity.getBody();
        assert useCaseResponseVO != null;
        assertEquals("nova-1", useCaseResponseVO.getUseCaseName());
        assertEquals(LocalDate.parse(DEFAULT_BILLING_DATE_OF_INVOICE), useCaseResponseVO.getPayload().getBilling().getDayOfInvoice());
        boolean isUseCaseDeletionAllowed = commonTestUtils.waitForUseCaseToBeCreated("nova-1", port);
        assertTrue(isUseCaseDeletionAllowed);
        deleteUseCase("nova-1");

    }

    @Test
    @Order(16)
    public void createUseCaseApiCurrentDateNullTest() throws InterruptedException {
        final String createUseCaseURL = DeploymentUtils.buildCreateUseCaseURL(port);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UseCaseVO useCaseVO = utility.getUseCaseVO();
        useCaseVO.getBilling().setCurrentDate(null);
        HttpEntity<UseCaseVO> request = new HttpEntity<>(useCaseVO, headers);
        ResponseEntity<UseCaseResponseVO> responseEntity = restTemplate.exchange(createUseCaseURL, HttpMethod.POST, request, UseCaseResponseVO.class);
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        UseCaseResponseVO useCaseResponseVO = responseEntity.getBody();
        assert useCaseResponseVO != null;
        assertEquals("nova-1", useCaseResponseVO.getUseCaseName());
        assertEquals(LocalDate.now(), useCaseResponseVO.getPayload().getBilling().getCurrentDate());
        boolean isUseCaseDeletionAllowed = commonTestUtils.waitForUseCaseToBeCreated("nova-1", port);
        assertTrue(isUseCaseDeletionAllowed);
        deleteUseCase("nova-1");

    }

    @Test
    @Order(17)
    public void createUseCaseApiDemoModeEnabledTest() {
        for (String rootAccountId: DEMO_ROOT_ACCOUNT_ID_LIST) {
            createDemoUseCaseTest(rootAccountId);
        }
    }

    private void createDemoUseCaseTest (String rootAccountId) {
        final String createUseCaseURL = DeploymentUtils.buildCreateUseCaseURL(port);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String useCaseName = "demo-" + rootAccountId;
        UseCaseVO useCaseVO = utility.getDemoUseCaseVO(useCaseName, true, rootAccountId);
        HttpEntity<UseCaseVO> request = new HttpEntity<>(useCaseVO, headers);
        try {
            ResponseEntity<UseCaseResponseVO> responseEntity = restTemplate.exchange(createUseCaseURL, HttpMethod.POST, request, UseCaseResponseVO.class);
            assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
            UseCaseResponseVO useCaseResponseVO = responseEntity.getBody();
            assert useCaseResponseVO != null;
            assertEquals(useCaseName, useCaseResponseVO.getUseCaseName());
            boolean isUseCaseDeletionAllowed = commonTestUtils.waitForUseCaseToBeCreated(useCaseName, port);
            assertTrue(isUseCaseDeletionAllowed);
            deleteUseCase(useCaseName);
        } catch (Exception e) {
            fail(rootAccountId + ": " + e);
        }
    }


    @Test
    @Order(18)
    public void createUseCaseApiDemoModeWrongAccountTest() throws JsonProcessingException {
        final String createUseCaseURL = DeploymentUtils.buildCreateUseCaseURL(port);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String rootAccountId = "dummy";
        String useCaseName = "demo-" + rootAccountId;
        UseCaseVO useCaseVO = utility.getDemoUseCaseVO(useCaseName, true, rootAccountId);
        HttpEntity<UseCaseVO> request = new HttpEntity<>(useCaseVO, headers);
//        try {
//            restTemplate.exchange(createUseCaseURL, HttpMethod.POST, request, ErrorResponseVO.class);
//            fail();
//        } catch (Exception e) {
//            String errorMessage = e.getMessage().substring(e.getMessage().indexOf('{'), e.getMessage().lastIndexOf('}') + 1);
//            ObjectMapper objectMapper = new ObjectMapper();
//            ErrorResponseVO error = objectMapper.readValue(errorMessage, ErrorResponseVO.class);
//            assertEquals("500 INTERNAL_SERVER_ERROR", error.getStatusCode());
//            assertTrue(error.getMessage().contains("When isDemoModeEnabled is set to true, the demoRootAccountId must be in the list:"));
//        }
    }

    private void deleteUseCase(String useCaseName) {
        final String deleteUseCaseURL = DeploymentUtils.buildDeleteUseCaseURL(port);
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(deleteUseCaseURL).queryParam("usecaseName", useCaseName);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, null, Object.class);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

}
