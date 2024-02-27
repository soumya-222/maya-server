package com.opentext.mayaserver.byod.aws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentext.mayaserver.config.ApplicationPropertiesConfig;
import com.opentext.mayaserver.exceptions.MayaServerException;
import com.opentext.mayaserver.models.AwsAccountDetails;
import com.opentext.mayaserver.models.DescribeOrganization;
import com.opentext.mayaserver.models.MemberAccounts;
import com.opentext.mayaserver.models.vo.AccountMetadataVO;
import com.opentext.mayaserver.utility.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

import static com.opentext.mayaserver.utility.Constants.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountsUtility {

    private final ApplicationPropertiesConfig applicationPropertiesConfig;

    public AwsAccountDetails getAwsAccountDetails(String useCaseName) throws ParseException, IOException {

        String targetPath = CommonUtils.createDirectoryBasedOnUseCase(useCaseName + FILE_PATH_SEPARATOR + ACCOUNTS_PATH, applicationPropertiesConfig.getMockoon().getNfsDataPath());
        String byodPath = applicationPropertiesConfig.getByodDataPath() + FILE_PATH_SEPARATOR + AWS + FILE_PATH_SEPARATOR + useCaseName + FILE_PATH_SEPARATOR + ACCOUNTS_PATH;
        File directoryPath = new File(byodPath);
        if (!directoryPath.exists() || directoryPath.listFiles().length == 0) {
            log.info("No account data files found under usecase directory " + byodPath);
            return null;
        }

        File[] filesInByodDirectory = directoryPath.listFiles();

        // Filter the JSON files from all the files
        List<File> jsonFilesInDirectory = filterJsonFiles(filesInByodDirectory);
        for (File file : jsonFilesInDirectory) {
            Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(targetPath + FILE_PATH_SEPARATOR + file.getName()), StandardCopyOption.REPLACE_EXISTING);
        }

        File byodFilesPath = new File(targetPath);
        File[] byodFiles = byodFilesPath.listFiles();

        String rootAccountId = null;
        List<AccountMetadataVO> accountMetadataVOList = new ArrayList<>();
        AccountMetadataVO accountMetadataVO = null;
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        boolean isDescribeAccountFilePresent = false;
        boolean isDescribeOrganizationFilePresent = false;
        boolean isListAccountsFilePresent = false;

        for (File file : byodFiles) {
            // Verify DescribeOrganizationFile
            if (!isDescribeOrganizationFilePresent && verifyAccountFile(file, ORGANIZATION)) {
                DescribeOrganization describeOrganization = objectMapper.readValue(file, DescribeOrganization.class);
                rootAccountId = describeOrganization.getOrganization().getMasterAccountId();
                accountMetadataVO = new AccountMetadataVO();
                accountMetadataVO.setEndpointType(AWS_DESCRIBE_ORGANIZATION);
                accountMetadataVO.setDataFilePath(List.of(file.getAbsolutePath()));
                accountMetadataVOList.add(accountMetadataVO);
                isDescribeOrganizationFilePresent = true;
            }
            // Verify DescribeAccountFile
            else if (!isDescribeAccountFilePresent && verifyAccountFile(file, ACCOUNT)) {
                accountMetadataVO = new AccountMetadataVO();
                accountMetadataVO.setEndpointType(AWS_DESCRIBE_ACCOUNT);
                accountMetadataVO.setDataFilePath(List.of(file.getAbsolutePath()));
                accountMetadataVOList.add(accountMetadataVO);
                isDescribeAccountFilePresent = true;
            }
            // Verify ListAccountsFile
            else if (verifyAccountFile(file, ACCOUNTS)) {
                isListAccountsFilePresent = true;
                MemberAccounts memberAccounts = objectMapper.readValue(file, MemberAccounts.class);
                map.put(file.getAbsolutePath(), memberAccounts.getNextToken());
            } else {
                log.info("JSON files doesn't match with AWS account data format");
            }
        }
        if (!isDescribeAccountFilePresent || !isListAccountsFilePresent || !isDescribeOrganizationFilePresent) {
            log.error("Unable to find the valid aws account files");
            throw new FileNotFoundException("Unable to find the valid AWS account files");
        }
        List<String> sortedListAccountsFiles = getSortedListAccountsFiles(map);
        Collections.reverse(sortedListAccountsFiles);
        accountMetadataVO = new AccountMetadataVO();
        accountMetadataVO.setEndpointType(AWS_LIST_ACCOUNTS);
        accountMetadataVO.setDataFilePath(sortedListAccountsFiles);
        accountMetadataVOList.add(accountMetadataVO);
        Collections.reverse(accountMetadataVOList);
        AwsAccountDetails awsAccountDetails = new AwsAccountDetails();
        awsAccountDetails.setAccountMetadataVOList(accountMetadataVOList);
        awsAccountDetails.setRootAccountID(rootAccountId);
        return awsAccountDetails;

    }

    private List<File> filterJsonFiles(File[] filesInDirectory) {
        return Arrays.stream(filesInDirectory).filter(file -> file.getName().endsWith(JSON)).collect(Collectors.toList());
    }

    private List<String> getSortedListAccountsFiles(Map<String, String> map) {
        List<String> sortedListAccounts = new ArrayList<>();
        int initialMapSize = map.size();
        String lastFileName = "";
        boolean lastFileFound = false;
        while (true) {
            int count = 0;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getValue() != null && entry.getValue().equals(entry.getKey().substring(entry.getKey().lastIndexOf(FILE_PATH_SEPARATOR) + 1))) {
                    log.error("Cyclic dependency found");
                    throw new MayaServerException("Cyclic dependency found");
                }
                count += 1;
                if (entry.getValue() != null && (entry.getValue()).equals(lastFileName)) {
                    lastFileName = entry.getKey().substring(entry.getKey().lastIndexOf(FILE_PATH_SEPARATOR) + 1);
                    sortedListAccounts.add(entry.getKey());
                    // Removing the key from the map eliminates the useless checks
                    map.remove(entry.getKey());
                    break;
                }
                // It will check the Next Page Token is NULL and assign that file name to lastFileName
                else if (entry.getValue() == null && !lastFileFound) {
                    lastFileName = entry.getKey().substring(entry.getKey().lastIndexOf(FILE_PATH_SEPARATOR) + 1);
                    sortedListAccounts.add(entry.getKey());
                    lastFileFound = true;
                    map.remove(entry.getKey());
                    break;
                }
                // This is to break the loop if we found files having more than one Next Page token as NULL
                else if (entry.getValue() == null && lastFileFound) {
                    log.error("Multiple files has Next Page Token as NULL");
                    throw new MayaServerException("Multiple files has Next Page Token as NULL");
                }
                // No List Accounts files are having Next Page Token as NULL
                else if (count + 1 > map.size() && map.size() == initialMapSize) {
                    log.error("No List Accounts files are having Next Page Token as NULL");
                    throw new MayaServerException("No List Accounts files are having Next Page Token as NULL");
                }
                // Next Page Token having invalid value or file which is not present
                else if (count + 1 > map.size()) {
                    log.error("Next Page Token is having invalid value ");
                    throw new MayaServerException("Next Page Token is having invalid value");
                }
            }
            if (map.size() == 0 && lastFileFound) {
                break;
            }
        }
        return sortedListAccounts;
    }

    private boolean verifyAccountFile(File file, String key) throws ParseException, IOException {
        JSONParser jsonParser = new JSONParser();
        JSONObject json = (JSONObject) jsonParser.parse(Files.readString(Path.of(file.getPath())));
        return json.containsKey(key);
    }

}
