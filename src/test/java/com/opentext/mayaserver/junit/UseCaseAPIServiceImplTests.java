package com.opentext.mayaserver.junit;


import com.opentext.mayaserver.TestUtility;
import com.opentext.mayaserver.datagenerators.RuleEngine;
import com.opentext.mayaserver.exceptions.MayaServerException;
import com.opentext.mayaserver.models.StateEnum;
import com.opentext.mayaserver.models.UseCase;
import com.opentext.mayaserver.models.vo.StateEnumVO;
import com.opentext.mayaserver.models.vo.UseCaseResponseVO;
import com.opentext.mayaserver.models.vo.UseCaseVO;
import com.opentext.mayaserver.repository.UseCaseRepository;
import com.opentext.mayaserver.services.UseCaseAPIService;
import com.opentext.mayaserver.services.impl.UseCaseAPIServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * @author Rajiv
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UseCaseAPIServiceImplTests {

    @Autowired
    UseCaseAPIServiceImpl useCaseAPIServiceImpl;

    @Autowired
    UseCaseAPIService useCaseAPIService;

    @MockBean
    UseCaseRepository useCaseRepository;

    @MockBean
    RuleEngine ruleEngine;

    TestUtility utility = new TestUtility();

    @Test
    public void assertContextTest() {
        assertNotNull(useCaseAPIServiceImpl);
        assertNotNull(useCaseAPIService);
        assertNotNull(useCaseRepository);
        assertNotNull(ruleEngine);
    }

    @Test
    public void createUseCaseSuccessTests() throws IOException {
        UseCaseVO useCaseVO = utility.getUseCaseVO();
        when(useCaseRepository.findAll()).then(new Answer<List<UseCase>>() {
            @Override
            public List<UseCase> answer(InvocationOnMock invocationOnMock) throws Throwable {
                return utility.getUseCaseList(1);
            }
        });
        when(useCaseRepository.save(any(UseCase.class))).then(new Answer<UseCase>() {
            @Override
            public UseCase answer(InvocationOnMock invocationOnMock) throws Throwable {
                List<UseCase> useCases = utility.getUseCaseList(1);
                return useCases.get(0);
            }
        });
        doNothing().when(ruleEngine).cloudProviderDelegator(any(UseCaseVO.class));
        UseCaseResponseVO useCaseResponseVOByName = useCaseAPIService.createUseCase(useCaseVO);
        assertNotNull(useCaseResponseVOByName);
        assertEquals("nova-1", useCaseResponseVOByName.getUseCaseName());
        assertEquals(StateEnumVO.SUBMITTED, useCaseResponseVOByName.getState());
    }

    @Test
    public void getUseCaseByNameOrIdSuccessTests() {
        List<UseCase> useCases = utility.getUseCaseList(1);
        Optional<UseCase> optionalUseCase = Optional.of(useCases.get(0));
        when(useCaseRepository.findByUseCaseName(any(String.class))).then(new Answer<Optional<UseCase>>() {
            @Override
            public Optional<UseCase> answer(InvocationOnMock invocationOnMock) throws Throwable {
                return optionalUseCase;
            }
        });
        when(useCaseRepository.findById(any(String.class))).then(new Answer<Optional<UseCase>>() {
            @Override
            public Optional<UseCase> answer(InvocationOnMock invocationOnMock) throws Throwable {
                return optionalUseCase;
            }
        });

        UseCaseResponseVO useCaseResponseVOByName = useCaseAPIService.getUseCaseByNameOrId("nova-1");
        assertNotNull(useCaseResponseVOByName);
        assertEquals("nova-1", useCaseResponseVOByName.getUseCaseName());

        UseCaseResponseVO useCaseResponseVOById = useCaseAPIService.getUseCaseByNameOrId("9f8eb400-0030-45ea-9765-2b2d3aa84c7f");
        assertNotNull(useCaseResponseVOById);
        assertEquals("9f8eb400-0030-45ea-9765-2b2d3aa84c7f", useCaseResponseVOById.getUseCaseId());
    }


    @Test
    public void getUseCaseByNameOrIdFailureTests() {
        Optional<UseCase> emptyOptionalUseCase = Optional.empty();
        when(useCaseRepository.findByUseCaseName(any(String.class))).then(new Answer<Optional<UseCase>>() {
            @Override
            public Optional<UseCase> answer(InvocationOnMock invocationOnMock) throws Throwable {
                return emptyOptionalUseCase;
            }
        });
        when(useCaseRepository.findById(any(String.class))).then(new Answer<Optional<UseCase>>() {
            @Override
            public Optional<UseCase> answer(InvocationOnMock invocationOnMock) throws Throwable {
                return emptyOptionalUseCase;
            }
        });

        UseCaseResponseVO useCaseResponseVOByName = useCaseAPIService.getUseCaseByNameOrId("nova-1");
        assertNull(useCaseResponseVOByName);
        UseCaseResponseVO useCaseResponseVOById = useCaseAPIService.getUseCaseByNameOrId("9f8eb400-0030-45ea-9765-2b2d3aa84c7f");
        assertNull(useCaseResponseVOById);
    }


    @Test
    public void deleteUseCaseTests() throws IOException {
        List<UseCase> useCases = utility.getUseCaseList(1);
        Optional<UseCase> optionalUseCase = Optional.of(useCases.get(0));
        when(useCaseRepository.findByUseCaseName(any(String.class))).then(new Answer<Optional<UseCase>>() {
            @Override
            public Optional<UseCase> answer(InvocationOnMock invocationOnMock) throws Throwable {
                optionalUseCase.get().setState(StateEnum.CREATED);
                return optionalUseCase;
            }
        });
        when(useCaseRepository.findById(any(String.class))).then(new Answer<Optional<UseCase>>() {
            @Override
            public Optional<UseCase> answer(InvocationOnMock invocationOnMock) throws Throwable {

                return optionalUseCase;
            }
        });

        // Mocking JPA delete call
        doNothing().when(useCaseRepository).delete(any(UseCase.class));

        useCaseAPIService.deleteUseCase("nova-1");
    }

    @Test
    public void deleteNonExistingUseCaseTests() {
        List<UseCase> useCases = utility.getUseCaseList(1);
        Optional<UseCase> emptyOptionalUseCase = Optional.empty();
        when(useCaseRepository.findByUseCaseName(any(String.class))).then(new Answer<Optional<UseCase>>() {
            @Override
            public Optional<UseCase> answer(InvocationOnMock invocationOnMock) throws Throwable {
                return emptyOptionalUseCase;
            }
        });
        when(useCaseRepository.findById(any(String.class))).then(new Answer<Optional<UseCase>>() {
            @Override
            public Optional<UseCase> answer(InvocationOnMock invocationOnMock) throws Throwable {
                return emptyOptionalUseCase;
            }
        });

        // Mocking JPA delete call
        doNothing().when(useCaseRepository).delete(any(UseCase.class));
        assertThrows(MayaServerException.class, () -> {
            useCaseAPIService.deleteUseCase("nova-1");
        });

    }

    @Test
    public void getAllUseCaseTests() {
        when(useCaseRepository.findAll()).then(new Answer<List<UseCase>>() {
            @Override
            public List<UseCase> answer(InvocationOnMock invocationOnMock) throws Throwable {
                return utility.getUseCaseList(5);
            }
        });
        List<UseCaseResponseVO> useCaseResponseVOList = useCaseAPIService.getAllUseCase();
        assertNotNull(useCaseResponseVOList);
        assertEquals(5, useCaseResponseVOList.size());
        assertEquals("nova-4", useCaseResponseVOList.get(3).getUseCaseName());
    }

    @Test
    public void identifyAccountAvailablePortTests() {
        when(useCaseRepository.findAll()).then(new Answer<List<UseCase>>() {
            @Override
            public List<UseCase> answer(InvocationOnMock invocationOnMock) throws Throwable {
                return utility.getUseCaseList(5);
            }
        });
        int port = useCaseAPIService.getAvailableMockoonPort();
        assertEquals(3006, port);
    }

    @Test
    public void identifyAccountAvailablePortForFirstUseCaseTests() {
        when(useCaseRepository.findAll()).then(new Answer<List<UseCase>>() {
            @Override
            public List<UseCase> answer(InvocationOnMock invocationOnMock) throws Throwable {
                return utility.getUseCaseList(0);
            }
        });
        int port = useCaseAPIService.getAvailableMockoonPort();
        assertEquals(3001, port);
    }

    // Test if already 49 ports are used
    @Test
    public void identifyAccountAvailablePortExactTests() {
        when(useCaseRepository.findAll()).then(new Answer<List<UseCase>>() {
            @Override
            public List<UseCase> answer(InvocationOnMock invocationOnMock) throws Throwable {
                return utility.getUseCaseList(49);
            }
        });
        int port = useCaseAPIService.getAvailableMockoonPort();
        assertEquals(3050, port);
    }

    // Test if request comes for 51 usecase
    @Test
    public void identifyAccountAvailablePortsIfMaximumLimitReachedTests() {
        when(useCaseRepository.findAll()).then(new Answer<List<UseCase>>() {
            @Override
            public List<UseCase> answer(InvocationOnMock invocationOnMock) throws Throwable {
                return utility.getUseCaseList(51);
            }
        });
        assertThrows(MayaServerException.class, () -> {
            useCaseAPIService.getAvailableMockoonPort();
        });
    }
}
