package com.opentext.mayaserver.repository;

import com.opentext.mayaserver.models.AccountMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rajiv
 */
@Repository
public interface AccountMetaDataRepository extends JpaRepository<AccountMetadata, String> {


}
