package com.opentext.mayaserver.repository;

import com.opentext.mayaserver.models.AccountData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rajiv
 */
@Repository
public interface AccountDataRepository extends JpaRepository<AccountData, String> {


}
