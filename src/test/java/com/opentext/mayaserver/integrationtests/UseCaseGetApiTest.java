package com.opentext.mayaserver.integrationtests;

import com.opentext.mayaserver.TestUtility;
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
public class UseCaseGetApiTest {

    @LocalServerPort
    private int port;
    TestUtility utility = new TestUtility();

    @Test
    @Order(1)
    public void createUseCaseApiTest() {
        // Creating the use case
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
    }

    @Test
    @Order(2)
    public void getUseCaseApiTest() {
        final String getUseCaseURL = DeploymentUtils.buildGetUseCaseURL(port);
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getUseCaseURL).queryParam("usecaseName", "nova-1");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UseCaseResponseVO> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, UseCaseResponseVO.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        UseCaseResponseVO useCaseResponseVO = responseEntity.getBody();
        assert useCaseResponseVO != null;
        assertEquals("nova-1", useCaseResponseVO.getUseCaseName());
    }

    @Test
    @Order(3)
    public void deleteUseCaseApiTest() throws InterruptedException {
        CommonTestUtils commonTestUtils = new CommonTestUtils();
        boolean isUseCaseDeletionAllowed = commonTestUtils.waitForUseCaseToBeCreated("nova-1", port);
        assertTrue(isUseCaseDeletionAllowed);
        final String deleteUseCaseURL = DeploymentUtils.buildDeleteUseCaseURL(port);
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(deleteUseCaseURL).queryParam("usecaseName", "nova-1");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, null, Object.class);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }
}
