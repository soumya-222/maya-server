package com.opentext.mayaserver.repository;

import com.opentext.mayaserver.models.UseCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Rajiv
 */
@Repository
@Transactional
public interface UseCaseRepository extends JpaRepository<UseCase, String> {

    Optional<UseCase> findByUseCaseName(String useCaseName);

    UseCase findByCloudProvider(String cloudProvider);

    List<UseCase> findAllByState(String state);

    Optional<UseCase> findByUseCaseNameOrId(String useCaseName, String id);
}
