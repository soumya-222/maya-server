package com.opentext.mayaserver.controllers;

import com.opentext.mayaserver.datagenerators.aws.billing.model.BillCostData;
import com.opentext.mayaserver.exceptions.ApiException;
import com.opentext.mayaserver.exceptions.ErrorMessage;
import com.opentext.mayaserver.exceptions.ErrorResponseVO;
import com.opentext.mayaserver.exceptions.MayaInvalidUserInputException;
import com.opentext.mayaserver.exceptions.MayaServerException;
import com.opentext.mayaserver.models.vo.*;
import com.opentext.mayaserver.services.DataGeneratorService;
import com.opentext.mayaserver.services.UseCaseAPIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.opentext.mayaserver.utility.CommonUtils.getSortedMemberAccountCostData;
import static com.opentext.mayaserver.utility.CommonUtils.getSortedRootAccountCostData;
import static com.opentext.mayaserver.utility.Constants.ALL;
import static com.opentext.mayaserver.utility.UseCaseServiceHelper.isEmptyString;


/**
 * @author Rajiv
 */

@RequiredArgsConstructor
@RestController
@Slf4j
public class UseCaseAPIController implements UseCaseAPI {

    private final UseCaseAPIService useCaseAPIService;
    private final DataGeneratorService dataGeneratorService;

    @Override
    @Operation(summary = "Create Usecase", tags = {"MAYA Controllers"})
    public ResponseEntity<Object> createUseCase(UseCaseVO useCaseVO) {
        try {
            log.info("useCaseVO {}", useCaseVO);
            UseCaseResponseVO useCaseResponseVO = useCaseAPIService.createUseCase(useCaseVO);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(useCaseResponseVO);
        } catch (MayaInvalidUserInputException inputException){
            log.error("Invalid user input: " + inputException.getMessage());
            ErrorResponseVO errorResponseVO = new ErrorResponseVO();
            errorResponseVO.setMessage(inputException.getMessage());
            errorResponseVO.setStatusCode(HttpStatus.BAD_REQUEST.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseVO);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new MayaServerException(e.getMessage(), e);
        }
    }

    @Operation(summary = "Get Usecase details", tags = {"MAYA Controllers"})
    @Override
    public ResponseEntity<Object> getUseCase(@Parameter(description = "Filter by usecase name.", example = "nova-10") String useCaseName) {
        try {
            log.info("Fetching usecase details: {}", useCaseName);
            if (!isEmptyString(useCaseName)) {
                UseCaseResponseVO responseVO = useCaseAPIService.getUseCaseByNameOrId(useCaseName);
                if (responseVO == null) {
                    ErrorResponseVO errorResponseVO = ApiException.recordNotFound("Record Not found with usecaseName: " + useCaseName, HttpStatus.BAD_REQUEST.toString(), "Please check usecase name or Id");
                    return ResponseEntity.status(HttpStatus.OK).body(errorResponseVO);
                } else {
                    return ResponseEntity.status(HttpStatus.OK).body(responseVO);
                }

            } else {
                List<UseCaseResponseVO> useCaseResponseVOList = useCaseAPIService.getAllUseCase();
                if (useCaseResponseVOList == null) {
                    ErrorResponseVO errorResponseVO = ApiException.recordNotFound("No records available", HttpStatus.OK.toString(), "Please create some Usecase then retry.");
                    return ResponseEntity.status(HttpStatus.OK).body(errorResponseVO);
                } else {
                    return ResponseEntity.status(HttpStatus.OK).body(useCaseResponseVOList);
                }
            }
        } catch (Exception e) {
            throw new MayaServerException(e.getMessage(), e);
        }
    }

    @Operation(summary = "Get usecase cost data  details like Amortized, Blended and Unblended Cost, By default only root account latest bill/cost data will be shown.", tags = {"MAYA Controllers"})
    @Override
    public ResponseEntity<Object> getUseCaseCostData(@Parameter(description = "Filter by usecase name", example = "nova-10") String useCaseName, @Parameter(description = "Filter cost by root account. It accepts only one Root account id at a time.", example = "9090909090") String rootAccount, @Parameter(description = "Filter cost by usage account id. It accepts maximum 10 usage account id and each value must be unique and comma seperated.", example = "2222222222,4444444444,5555555555 ") List<String> memberAccounts, @Parameter(description = "Filter cost by bill period it accepts in (startdate-enddate) 'yyyymmdd-yyyymmdd' format. If not provided then latest month cost data will be shown. \n Send 'all' for all bills  associated with Root and Usage account", example = "20230601-20230701") String billPeriod) {
        useCaseAPIService.validateUserInput(useCaseName, memberAccounts, rootAccount, billPeriod);
        CloudCostDataResponseVO cloudCostDataResponseVO = null;
        if (!isEmptyString(rootAccount) && !isEmptyString(billPeriod) && memberAccounts == null) {
            ManagementAccountResponseVO rootAccountCostResponseVO = null;
            if (billPeriod.equals(ALL)) {
                rootAccountCostResponseVO = useCaseAPIService.getAllRootAccountBillCostData(useCaseName);
                getSortedRootAccountCostData(rootAccountCostResponseVO);
                cloudCostDataResponseVO = new CloudCostDataResponseVO();
                cloudCostDataResponseVO.setManagementAccountIdCostData(rootAccountCostResponseVO);
            } else {
                rootAccountCostResponseVO = useCaseAPIService.getRootAccountBillCostData(useCaseName, rootAccount, billPeriod);
                getSortedRootAccountCostData(rootAccountCostResponseVO);
                cloudCostDataResponseVO = new CloudCostDataResponseVO();
                cloudCostDataResponseVO.setManagementAccountIdCostData(rootAccountCostResponseVO);
            }
            return ResponseEntity.status(HttpStatus.OK).body(cloudCostDataResponseVO);

        } else if (!isEmptyString(rootAccount) && isEmptyString(billPeriod) && memberAccounts == null) {
            ManagementAccountResponseVO rootAccountCostResponseVO = useCaseAPIService.getRootAccountBillCostData(useCaseName, rootAccount, billPeriod);
            getSortedRootAccountCostData(rootAccountCostResponseVO);
            cloudCostDataResponseVO = new CloudCostDataResponseVO();
            cloudCostDataResponseVO.setManagementAccountIdCostData(rootAccountCostResponseVO);
            List<BillCostData> sortedRootAccountList = Arrays.asList(rootAccountCostResponseVO.getData().get(0));
            rootAccountCostResponseVO.setData(sortedRootAccountList);
            cloudCostDataResponseVO.setManagementAccountIdCostData(rootAccountCostResponseVO);
            return ResponseEntity.status(HttpStatus.OK).body(cloudCostDataResponseVO);

        } else if (memberAccounts != null && !isEmptyString(billPeriod) && isEmptyString(rootAccount)) {
            UsageAccountResponseVO memberAccountCostResponseVO = null;
            cloudCostDataResponseVO = new CloudCostDataResponseVO();
            cloudCostDataResponseVO.setUsageAccountIdCostData(memberAccountCostResponseVO);
            memberAccountCostResponseVO = useCaseAPIService.getMemberAccountBillCostData(useCaseName, billPeriod, memberAccounts);
            getSortedMemberAccountCostData(memberAccountCostResponseVO);
            cloudCostDataResponseVO = new CloudCostDataResponseVO();
            cloudCostDataResponseVO.setUsageAccountIdCostData(memberAccountCostResponseVO);
            return ResponseEntity.status(HttpStatus.OK).body(cloudCostDataResponseVO);

        } else if (memberAccounts != null && isEmptyString(billPeriod) && isEmptyString(rootAccount)) {
            UsageAccountResponseVO memberAccountCostResponseVO = useCaseAPIService.getMemberAccountBillCostData(useCaseName, billPeriod, memberAccounts);
            getSortedMemberAccountCostData(memberAccountCostResponseVO);
            cloudCostDataResponseVO = new CloudCostDataResponseVO();
            Map.Entry<String, List<BillCostData>> firstEntry = memberAccountCostResponseVO.getData().entrySet().iterator().next();
            Map<String, List<BillCostData>> firstMemberAccountCostDetails = new LinkedHashMap<>();
            firstMemberAccountCostDetails.put(firstEntry.getKey(), firstEntry.getValue());
            memberAccountCostResponseVO.setData(firstMemberAccountCostDetails);
            cloudCostDataResponseVO.setUsageAccountIdCostData(memberAccountCostResponseVO);
            return ResponseEntity.status(HttpStatus.OK).body(cloudCostDataResponseVO);
        } else if (memberAccounts != null && !isEmptyString(rootAccount) && !isEmptyString(billPeriod)) {
            if (billPeriod.equals(ALL)) {
                UsageAccountResponseVO usageAccountResponseVO = useCaseAPIService.getMemberAccountBillCostData(useCaseName, billPeriod, memberAccounts);
                getSortedMemberAccountCostData(usageAccountResponseVO);
                ManagementAccountResponseVO managementAccountResponseVO = useCaseAPIService.getAllRootAccountBillCostData(useCaseName);
                getSortedRootAccountCostData(managementAccountResponseVO);
                cloudCostDataResponseVO = new CloudCostDataResponseVO();
                cloudCostDataResponseVO.setUsageAccountIdCostData(usageAccountResponseVO);
                cloudCostDataResponseVO.setManagementAccountIdCostData(managementAccountResponseVO);
                return ResponseEntity.status(HttpStatus.OK).body(cloudCostDataResponseVO);
            } else {
                cloudCostDataResponseVO = new CloudCostDataResponseVO();
                UsageAccountResponseVO usageAccountResponseVO = useCaseAPIService.getMemberAccountBillCostData(useCaseName, billPeriod, memberAccounts);
                getSortedMemberAccountCostData(usageAccountResponseVO);
                cloudCostDataResponseVO.setUsageAccountIdCostData(usageAccountResponseVO);
                ManagementAccountResponseVO managementAccountResponseVO = useCaseAPIService.getRootAccountBillCostData(useCaseName, rootAccount, billPeriod);
                getSortedRootAccountCostData(managementAccountResponseVO);
                cloudCostDataResponseVO.setManagementAccountIdCostData(managementAccountResponseVO);
                return ResponseEntity.status(HttpStatus.OK).body(cloudCostDataResponseVO);
            }

        } else if (memberAccounts == null && isEmptyString(rootAccount) && isEmptyString(billPeriod)) {
            cloudCostDataResponseVO = new CloudCostDataResponseVO();
            ManagementAccountResponseVO rootAccountCostResponseVO = useCaseAPIService.getAllRootAccountBillCostData(useCaseName);
            getSortedRootAccountCostData(rootAccountCostResponseVO);
            List<BillCostData> sortedRootAccountList = Arrays.asList(rootAccountCostResponseVO.getData().get(0));
            rootAccountCostResponseVO.setData(sortedRootAccountList);
            cloudCostDataResponseVO.setManagementAccountIdCostData(rootAccountCostResponseVO);
            return ResponseEntity.status(HttpStatus.OK).body(cloudCostDataResponseVO);
        } else if (memberAccounts == null && isEmptyString(rootAccount) && !isEmptyString(billPeriod)) {
            cloudCostDataResponseVO = new CloudCostDataResponseVO();
            ManagementAccountResponseVO rootAccountCostResponseVO = null;
            if (billPeriod.equals(ALL)) {
                rootAccountCostResponseVO = useCaseAPIService.getAllRootAccountBillCostData(useCaseName);
                getSortedRootAccountCostData(rootAccountCostResponseVO);
                cloudCostDataResponseVO = new CloudCostDataResponseVO();
                cloudCostDataResponseVO.setManagementAccountIdCostData(rootAccountCostResponseVO);
            } else {
                rootAccountCostResponseVO = useCaseAPIService.getRootAccountBillCostData(useCaseName, rootAccount, billPeriod);
                getSortedRootAccountCostData(rootAccountCostResponseVO);
                cloudCostDataResponseVO.setManagementAccountIdCostData(rootAccountCostResponseVO);
            }
            cloudCostDataResponseVO.setManagementAccountIdCostData(rootAccountCostResponseVO);
            return ResponseEntity.status(HttpStatus.OK).body(cloudCostDataResponseVO);
        } else {
            ManagementAccountResponseVO rootAccountCostResponseVO = useCaseAPIService.getRootAccountBillCostData(useCaseName, rootAccount, billPeriod);
            UsageAccountResponseVO memberAccountCostResponseVO = useCaseAPIService.getMemberAccountBillCostData(useCaseName, billPeriod, memberAccounts);
            getSortedRootAccountCostData(rootAccountCostResponseVO);
            getSortedMemberAccountCostData(memberAccountCostResponseVO);
            List<BillCostData> sortedRootAccountList = Arrays.asList(rootAccountCostResponseVO.getData().get(0));
            rootAccountCostResponseVO.setData(sortedRootAccountList);
            cloudCostDataResponseVO = new CloudCostDataResponseVO();
            cloudCostDataResponseVO.setManagementAccountIdCostData(rootAccountCostResponseVO);
            Map.Entry<String, List<BillCostData>> firstEntry = memberAccountCostResponseVO.getData().entrySet().iterator().next();
            Map<String, List<BillCostData>> firstMemberAccountCostDetails = new LinkedHashMap<>();
            firstMemberAccountCostDetails.put(firstEntry.getKey(), firstEntry.getValue());
            memberAccountCostResponseVO.setData(firstMemberAccountCostDetails);
            cloudCostDataResponseVO.setUsageAccountIdCostData(memberAccountCostResponseVO);
            return ResponseEntity.status(HttpStatus.OK).body(cloudCostDataResponseVO);
        }
    }

    @Operation(summary = "Show Account Details (Root account/Usage account)", tags = {"MAYA Controllers"})
    @Override
    public ResponseEntity<Object> getAllAccounts(@Parameter(description = "Filter by usecase name.", example = "nova-10") String useCaseName) {
        AccountResponseVO accountResponseVO = dataGeneratorService.getAccountDataList(useCaseName);
        if (accountResponseVO != null) {
            return ResponseEntity.status(HttpStatus.OK).body(accountResponseVO);
        } else {
            ErrorResponseVO errorResponseVO = ApiException.recordNotFound("Record Not found with usecase name: " + useCaseName, HttpStatus.BAD_REQUEST.toString(), "Please check usecase name or Id");
            return ResponseEntity.status(HttpStatus.OK).body(errorResponseVO);
        }
    }

    @Operation(summary = "Delete Usecase", tags = {"MAYA Controllers"})
    @Override
    public ResponseEntity<Object> deleteUseCase(@Parameter(description = "Provide usecase name which you want to delete.", example = "nova-10") String useCaseName) throws IOException {
        if (!isEmptyString(useCaseName)) {
            useCaseAPIService.deleteUseCase(useCaseName);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
