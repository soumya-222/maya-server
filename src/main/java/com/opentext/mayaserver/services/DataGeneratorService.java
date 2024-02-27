package com.opentext.mayaserver.services;

import com.opentext.mayaserver.models.vo.AccountDataVO;
import com.opentext.mayaserver.models.vo.AccountMetadataVO;
import com.opentext.mayaserver.models.vo.AccountResponseVO;

import java.util.List;

/**
 * @author Rajiv
 */
public interface DataGeneratorService {

    String getRootAccount(String useCaseName);

    List<String> getMemberAccounts(String useCaseName);

    AccountDataVO getAccountData(String useCaseName);
    AccountResponseVO getAccountDataList(String useCaseName);

    List<AccountMetadataVO> getAccountMetaData(String useCaseName);
}
