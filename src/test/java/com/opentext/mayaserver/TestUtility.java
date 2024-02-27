package com.opentext.mayaserver;

import com.opentext.mayaserver.models.StateEnum;
import com.opentext.mayaserver.models.UseCase;
import com.opentext.mayaserver.models.vo.AccountVO;
import com.opentext.mayaserver.models.vo.BillingVO;
import com.opentext.mayaserver.models.vo.ModeEnumVO;
import com.opentext.mayaserver.models.vo.UseCaseVO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rajiv
 */

public class TestUtility {

    public List<UseCase> getUseCaseList(int count) {

        List<UseCase> useCaseList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            useCaseList.add(getUseCase(i));
        }
        return useCaseList;
    }

    public UseCase getUseCase(int index) {
        UseCase useCase = new UseCase();
        useCase.setId("9f8eb400-0030-45ea-9765-2b2d3aa84c7f");
        useCase.setUseCaseName("nova-" + index);
        useCase.setCloudProvider("aws");
        useCase.setState(StateEnum.SUBMITTED);
        useCase.setMockoonPort(3000 + index);
        return useCase;
    }

    public UseCaseVO getUseCaseVO() {
        UseCaseVO useCaseVO = new UseCaseVO();
        AccountVO accountVO = getAccountVO();
        BillingVO billingVO = getBillingVO();
        useCaseVO.setUseCaseName("nova-1");
        useCaseVO.setCloudProvider("aws");
        useCaseVO.setMode(ModeEnumVO.GENERATE);
        useCaseVO.setAccount(accountVO);
        useCaseVO.setBilling(billingVO);
        useCaseVO.setIsDemoModeEnabled(null);
        useCaseVO.setDemoRootAccountId(null);
        return useCaseVO;
    }

    public UseCaseVO getDemoUseCaseVO(String useCaseName, Boolean isDemoModeEnabled, String demoRootAccountId) {
        UseCaseVO useCaseVO = new UseCaseVO();
        AccountVO accountVO = getAccountVO();
        BillingVO billingVO = getBillingVO();
        useCaseVO.setUseCaseName(useCaseName);
        useCaseVO.setCloudProvider("aws");
        useCaseVO.setMode(ModeEnumVO.GENERATE);
        useCaseVO.setAccount(accountVO);
        useCaseVO.setBilling(billingVO);
        useCaseVO.setIsDemoModeEnabled(isDemoModeEnabled);
        useCaseVO.setDemoRootAccountId(demoRootAccountId);
        return useCaseVO;
    }

    private BillingVO getBillingVO() {
        BillingVO billingVO = new BillingVO();
        billingVO.setStartDate(LocalDate.parse("2023-06-14"));
        billingVO.setEndDate(LocalDate.parse("2023-06-14"));
        billingVO.setCurrentDate(LocalDate.parse("2023-06-14"));
        billingVO.setDayOfInvoice(LocalDate.parse("2023-06-14"));
        billingVO.setRecordsPerDay(100);
        return billingVO;
    }

    private AccountVO getAccountVO() {
        AccountVO accountVO = new AccountVO();
        accountVO.setPageSize(5);
        accountVO.setRootAccountSize(1);
        accountVO.setNumberOfAccounts(9);
        return accountVO;
    }
}
