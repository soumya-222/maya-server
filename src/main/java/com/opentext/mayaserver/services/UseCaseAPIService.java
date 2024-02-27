package com.opentext.mayaserver.services;

import com.opentext.mayaserver.exceptions.MayaServerException;
import com.opentext.mayaserver.models.StateEnum;
import com.opentext.mayaserver.models.vo.*;

import java.io.IOException;
import java.util.List;

/**
 * @author Rajiv
 */
public interface UseCaseAPIService {

    UseCaseResponseVO createUseCase(UseCaseVO useCaseVO) throws MayaServerException;

    List<UseCaseResponseVO> getAllUseCase();

    UseCaseResponseVO getUseCaseByNameOrId(String useCaseName) throws MayaServerException;

    void deleteUseCase(String useCaseName) throws IOException;

    int getAvailableMockoonPort();

    void updateStatusOfUseCase(String  useCaseName, StateEnum state);

    ManagementAccountResponseVO getAllRootAccountBillCostData(String useCaseName);

    ManagementAccountResponseVO getRootAccountBillCostData(String useCaseName, String rootAccount, String billPeriod);

    UsageAccountResponseVO getAllMemberAccountBillCostData(String useCaseName);

    UsageAccountResponseVO getMemberAccountBillCostData(String useCaseName, String billPeriod, List<String> memberAccountId);

    void validateUserInput(String useCaseName, List<String> memberAccounts, String rootAccount, String billPeriod);
}
