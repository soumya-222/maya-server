package com.opentext.mayaserver.repository;

import com.opentext.mayaserver.models.CloudCostData;
import com.opentext.mayaserver.models.UseCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Rajiv
 */
@Repository
public interface CloudCostDataRepository extends JpaRepository<CloudCostData, String> {

    Optional<CloudCostData> findByUseCaseId(UseCase useCase);

}
