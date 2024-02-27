package com.opentext.mayaserver.services.impl;

import com.opentext.mayaserver.exceptions.MayaServerException;
import com.opentext.mayaserver.models.StateEnum;
import com.opentext.mayaserver.models.UseCase;
import com.opentext.mayaserver.repository.UseCaseRepository;
import com.opentext.mayaserver.services.UseCaseDBOperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author Rajiv
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class UseCaseDBOperationServiceImpl implements UseCaseDBOperationService {

    private final UseCaseRepository useCaseRepository;

    @Transactional
    public Optional<UseCase> fetchUseCase(String useCaseName) {
        Optional<UseCase> useCaseOptional = null;
        useCaseOptional = useCaseRepository.findByUseCaseName(useCaseName);
        if (!useCaseOptional.isPresent()) {
            useCaseOptional = useCaseRepository.findById(useCaseName);
        }
        return useCaseOptional;
    }

    @Transactional
    public void updateStatusOfUseCase(String useCaseName, StateEnum state) {
        try {
            Optional<UseCase> optionalUseCase = fetchUseCase(useCaseName);
            if (optionalUseCase.isPresent()) {
                UseCase useCase = optionalUseCase.get();
                useCase.setState(state);
                useCaseRepository.save(useCase);
            }
        } catch (Exception e) {
            throw new MayaServerException("Unable to find the useCase with name {} " + useCaseName);
        }
    }
}
