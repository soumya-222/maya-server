package com.opentext.mayaserver.util;

import com.opentext.mayaserver.models.StateEnum;
import com.opentext.mayaserver.models.vo.StateEnumVO;
import com.opentext.mayaserver.models.vo.UseCaseResponseVO;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class CommonTestUtils {

    public boolean waitForUseCaseToBeCreated(String useCaseName, int port) throws InterruptedException {
        for (int i = 0; i < 12; i++) {
            StateEnumVO stateEnumVO = getUseCaseStatus(useCaseName, port);
            if (StateEnum.isUseCaseDeletionAllowed(StateEnum.valueOf(stateEnumVO.name()))) {
                Thread.sleep(500);
                return true;
            } else {
                Thread.sleep(5000);
            }
        }
        return false;
    }

    private StateEnumVO getUseCaseStatus(String useCaseName, int port) {
        final String getUseCaseURL = DeploymentUtils.buildGetUseCaseURL(port);
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getUseCaseURL).queryParam("usecaseName", useCaseName);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UseCaseResponseVO> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, UseCaseResponseVO.class);
        UseCaseResponseVO useCaseResponseVO = responseEntity.getBody();
        return useCaseResponseVO.getState();
    }
}
