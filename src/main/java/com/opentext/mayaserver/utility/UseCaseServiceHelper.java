package com.opentext.mayaserver.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.opentext.mayaserver.models.StateEnum;
import com.opentext.mayaserver.models.vo.ModeEnumVO;
import com.opentext.mayaserver.models.vo.StateEnumVO;
import com.opentext.mayaserver.models.vo.UseCaseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.opentext.mayaserver.utility.Constants.*;

/**
 * @author Rajiv
 */
@Component
@Slf4j
public class UseCaseServiceHelper {
    public StateEnumVO useCaseStateToVO(StateEnum state) {
        return StateEnumVO.values()[state.ordinal()];
    }

    public static boolean isEmptyString(String param) {
        return (param == null || param.trim().length() == 0);
    }

    private final static String[] ENDPOINTS_TYPE = {AWS_LIST_ACCOUNTS, AWS_DESCRIBE_ACCOUNT, AWS_DESCRIBE_ORGANIZATION};

    public static String getEndpoints(int position) {
        return ENDPOINTS_TYPE[position];
    }

    public static int getEndpointsLength() {
        return ENDPOINTS_TYPE.length;
    }

    public String createAccountDataFilePath(String nfsPath, UseCaseVO useCaseVO, String endpointType) {

        if (isEmptyString(nfsPath) || isEmptyString(endpointType)) {
            log.error("File name can't be generated either dataPath or FileName is invalid");
            return "INVALID ENDPOINT TYPE ";
        } else {
            String useCaseName = useCaseVO.getUseCaseName();
            String cloudProvider = useCaseVO.getCloudProvider().toUpperCase();
            String filePath = nfsPath + FILE_PATH_SEPARATOR + useCaseName + FILE_PATH_SEPARATOR;

            String accountAbsoluteFileNames = null;
            switch (endpointType) {
                case AWS_LIST_ACCOUNTS:
                    accountAbsoluteFileNames = createListAccountFileName(filePath, useCaseName, cloudProvider, useCaseVO);
                    break;

                case AWS_DESCRIBE_ACCOUNT:
                    String describeAccountFileName = filePath + useCaseName + "_" + cloudProvider + "_DescribeAccount.json";
                    log.info("describeAccountFileName {} ", describeAccountFileName);
                    List<String> describeAccount = new ArrayList<>();
                    describeAccount.add(describeAccountFileName);
                    accountAbsoluteFileNames = mapListToString(describeAccount);
                    break;

                case AWS_DESCRIBE_ORGANIZATION:
                    String describeOrganizationFileName = filePath + useCaseName + "_" + cloudProvider + "_DescribeOrganization.json";
                    log.info("describeOrganizationFileName {} ", describeOrganizationFileName);
                    List<String> describeOrganisation = new ArrayList<>();
                    describeOrganisation.add(describeOrganizationFileName);
                    accountAbsoluteFileNames = mapListToString(describeOrganisation);
                    break;

                default:
                    accountAbsoluteFileNames = "INVALID FILE ";
            }
            return accountAbsoluteFileNames;
        }
    }

    /**
     * Sample:
     * <useCaseName>_<CloudProvider>_DescribeOrganization.json
     * <p>
     * maple-usecase1_AWS_DescribeOrganization.json
     * <p>
     * maple-usecase1_AWS_DescribeAccount_555555555555.json
     * <p>
     * maple-usecase1_AWS_ListAccounts_1-20.json
     * <p>
     * Full file path:var/opt/maple-usecase1/maple-usecase1_AWS_ListAccounts_101-120.json
     */
    private String createListAccountFileName(String filePath, String useCaseName, String cloudProvider, UseCaseVO useCaseVO) {
        List<String> filenameList = new ArrayList<>();
        String accountFileNameList = null;
        int startRange = FILE_START_RANGE;
        int endRange = FILE_END_RANGE;
        int memberAccountSize = useCaseVO.getAccount().getNumberOfAccounts();
        int pageSize = useCaseVO.getAccount().getPageSize();
        if (pageSize == 0) {
            accountFileNameList = filePath + useCaseName + "_" + cloudProvider + "_ListAccounts_" + startRange + "-" + endRange + ".json";
            return accountFileNameList;
        }
        int loopCountSize = memberAccountSize / pageSize;
        loopCountSize = memberAccountSize % pageSize == 0 ? loopCountSize : loopCountSize + 1;
        for (int i = 1; i <= loopCountSize; i++) {
            String listAccountFileName = filePath + useCaseName + "_" + cloudProvider + "_ListAccounts_" + startRange + "-" + endRange + ".json";
            filenameList.add(listAccountFileName);
            startRange = endRange + 1;
            endRange += pageSize;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            accountFileNameList = objectMapper.writeValueAsString(filenameList);
            log.debug(" mapper {}", accountFileNameList);
        } catch (Exception e) {
            log.error(" Unable to generate file names");
        }
        return accountFileNameList;
    }


    public String generateRandomAccountId() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public String generateRandomMemberAccountId(int count) {
        List<String> memberAccountList = new ArrayList<>();
        String memberAccountStringList = null;
        for (int i = 0; i < count; i++) {
            memberAccountList.add(generateRandomAccountId());
        }
        return mapListToString(memberAccountList);
    }

    public String mapListToString(List<String> memberAccountList) {
        String memberAccountStringList = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            memberAccountStringList = objectMapper.writeValueAsString(memberAccountList);
        } catch (Exception e) {
            log.error(" Unable to map data.");
        }
        return memberAccountStringList;
    }

    public boolean isBYODMode(UseCaseVO useCaseVO) {
        return useCaseVO.getMode() == ModeEnumVO.BYOD;
    }

    public boolean isGenerateMode(UseCaseVO useCaseVO) {
        return useCaseVO.getMode() == ModeEnumVO.GENERATE;
    }
}
