package com.opentext.mayaserver.integrationtests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentext.mayaserver.TestUtility;
import com.opentext.mayaserver.exceptions.ErrorResponseVO;
import com.opentext.mayaserver.models.vo.UseCaseResponseVO;
import com.opentext.mayaserver.models.vo.UseCaseVO;
import com.opentext.mayaserver.util.CommonTestUtils;
import com.opentext.mayaserver.util.DeploymentUtils;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UseCaseDeleteApiTest {

    @LocalServerPort
    private int port;
    TestUtility utility = new TestUtility();

    CommonTestUtils commonTestUtils = new CommonTestUtils();

    @Test
    @Order(1)
    public void deleteUseCaseApiTest() throws InterruptedException {
        createUseCase("nova-1", 100);
        boolean isUseCaseDeletionAllowed = commonTestUtils.waitForUseCaseToBeCreated("nova-1", port);
        assertTrue(isUseCaseDeletionAllowed);
        final String deleteUseCaseURL = DeploymentUtils.buildDeleteUseCaseURL(port);
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(deleteUseCaseURL).queryParam("usecaseName", "nova-1");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, null, Object.class);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    @Order(2)
    public void deleteUseCaseNotExistTest() throws JsonProcessingException {
        final String deleteUseCaseURL = DeploymentUtils.buildDeleteUseCaseURL(port);
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(deleteUseCaseURL).queryParam("usecaseName", "nova-2");
        RestTemplate restTemplate = new RestTemplate();
//        try {
//            restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, null, Object.class);
//        } catch (Exception e) {
//            String errorMessage = e.getMessage().substring(e.getMessage().indexOf('{'), e.getMessage().lastIndexOf('}') + 1);
//            ObjectMapper objectMapper = new ObjectMapper();
//            ErrorResponseVO error = objectMapper.readValue(errorMessage, ErrorResponseVO.class);
//            assertEquals("404 NOT_FOUND", error.getStatusCode());
//            assertEquals("UseCase doesn't exist nova-2", error.getMessage());
//        }
    }

    @Test
    @Order(3)
    public void deleteUseCaseConflictExceptionTest() throws JsonProcessingException, InterruptedException {
        final String deleteUseCaseURL = DeploymentUtils.buildDeleteUseCaseURL(port);
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(deleteUseCaseURL).queryParam("usecaseName", "nova-2");
        RestTemplate restTemplate = new RestTemplate();
        createUseCase("nova-2", 10000);
//        try {
//            restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, null, Object.class);
//        } catch (Exception e) {
//            String errorMessage = e.getMessage().substring(e.getMessage().indexOf('{'), e.getMessage().lastIndexOf('}') + 1);
//            ObjectMapper objectMapper = new ObjectMapper();
//            ErrorResponseVO error = objectMapper.readValue(errorMessage, ErrorResponseVO.class);
//            assertEquals("409 CONFLICT", error.getStatusCode());
//            assertEquals("UseCase either in Submitted or In Progress state. Hence UseCase can not be deleted", error.getMessage());
//        }

        boolean isUseCaseDeletionAllowed = commonTestUtils.waitForUseCaseToBeCreated("nova-2", port);
        assertTrue(isUseCaseDeletionAllowed);
        deleteUseCase("nova-2");

    }

    private void createUseCase(String useCaseName, int recordsPerDay) {
        final String createUseCaseURL = DeploymentUtils.buildCreateUseCaseURL(port);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        UseCaseVO useCaseVO = utility.getUseCaseVO();
        useCaseVO.setUseCaseName(useCaseName);
        useCaseVO.getBilling().setRecordsPerDay(recordsPerDay);
        HttpEntity<UseCaseVO> request = new HttpEntity<>(useCaseVO, headers);
        restTemplate.exchange(createUseCaseURL, HttpMethod.POST, request, UseCaseResponseVO.class);

    }

    private void deleteUseCase(String useCaseName) {
        final String deleteUseCaseURL = DeploymentUtils.buildDeleteUseCaseURL(port);
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(deleteUseCaseURL).queryParam("usecaseName", useCaseName);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, null, Object.class);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }
}
