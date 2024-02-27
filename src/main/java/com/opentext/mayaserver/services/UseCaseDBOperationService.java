package com.opentext.mayaserver.services;

import com.opentext.mayaserver.exceptions.MayaServerException;
import com.opentext.mayaserver.models.StateEnum;
import com.opentext.mayaserver.models.vo.ManagementAccountResponseVO;
import com.opentext.mayaserver.models.vo.UsageAccountResponseVO;
import com.opentext.mayaserver.models.vo.UseCaseResponseVO;
import com.opentext.mayaserver.models.vo.UseCaseVO;

import java.io.IOException;
import java.util.List;

/**
 * @author Rajiv
 */
public interface UseCaseDBOperationService {

    void updateStatusOfUseCase(String  useCaseName, StateEnum state);

}
