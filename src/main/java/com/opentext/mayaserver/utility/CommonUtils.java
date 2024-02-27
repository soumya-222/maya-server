package com.opentext.mayaserver.utility;

import com.opentext.mayaserver.datagenerators.aws.billing.model.BillCostData;
import com.opentext.mayaserver.datagenerators.util.DateUtils;
import com.opentext.mayaserver.exceptions.MayaServerException;
import com.opentext.mayaserver.models.vo.ManagementAccountResponseVO;
import com.opentext.mayaserver.models.vo.UsageAccountResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.opentext.mayaserver.utility.Constants.FILE_PATH_SEPARATOR;

@Slf4j
public class CommonUtils {
    public static String createDirectoryBasedOnUseCase(String useCaseName, String path) {
        File theDirectory = new File(path + FILE_PATH_SEPARATOR + useCaseName);
        if (theDirectory.exists()) {
            try {
                FileUtils.deleteDirectory(theDirectory);
            } catch (Exception e) {
                log.error("Unable to delete the directory with Usecase name {}", useCaseName);
                throw new MayaServerException(e.getMessage());
            }
        }
        theDirectory.mkdirs();
        return theDirectory.getAbsolutePath();
    }

    public static LocalDate toLocalDateFormat(String date) {
        return LocalDate.parse(date);
    }

    public static ManagementAccountResponseVO getSortedRootAccountCostData(ManagementAccountResponseVO rootAccountCostResponseVO) {
        List<BillCostData> sortedRootAccountList = rootAccountCostResponseVO.getData().stream()
                .sorted(Comparator.comparing(BillCostData::getBillPeriod, (billPeriod1, billPeriod2) -> {
                    String[] parts1 = billPeriod1.split("-");
                    String[] parts2 = billPeriod2.split("-");
                    LocalDate date1 = DateUtils.toLocalDateFormat(parts1[0]);
                    LocalDate date2 = DateUtils.toLocalDateFormat(parts2[0]);
                    return date2.compareTo(date1);
                }))
                .collect(Collectors.toList());
        rootAccountCostResponseVO.setData(sortedRootAccountList);
        return rootAccountCostResponseVO;
    }

    public static UsageAccountResponseVO getSortedMemberAccountCostData(UsageAccountResponseVO usageAccountResponseVO) {
        Map<String, List<BillCostData>> memberAccountCostResponseVOData = usageAccountResponseVO.getData();
        List<Map.Entry<String, List<BillCostData>>> entryList = new ArrayList<>(memberAccountCostResponseVOData.entrySet());
        Comparator<Map.Entry<String, List<BillCostData>>> dateComparator = (entry1, entry2) -> {
            String key1 = entry1.getKey();
            String key2 = entry2.getKey();
            LocalDate date1 = DateUtils.toLocalDateFormat(key1.substring(key1.lastIndexOf('_') + 1, key1.indexOf('-')));
            LocalDate date2 = DateUtils.toLocalDateFormat(key2.substring(key2.lastIndexOf('_') + 1, key2.indexOf('-')));
            return date2.compareTo(date1);
        };
        entryList.sort(dateComparator);
        Map<String, List<BillCostData>> firstMemberAccountCostDetails = new LinkedHashMap<>();
        for (Map.Entry<String, List<BillCostData>> entry : entryList) {
            firstMemberAccountCostDetails.put(entry.getKey(), entry.getValue());
        }
        usageAccountResponseVO.setData(firstMemberAccountCostDetails);
        return usageAccountResponseVO;
    }

    public static String readFileFromResource(String resourcePath, String fileName) throws IOException {
        InputStreamReader reader = null;
        String filePath = (resourcePath == null) ? fileName : (resourcePath.endsWith(FILE_PATH_SEPARATOR))? resourcePath + fileName : resourcePath + FILE_PATH_SEPARATOR + fileName;
        try {
            Resource resource = new ClassPathResource(filePath);
            reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            log.error("Unable to load resource file {}", filePath);
            throw new MayaServerException(e.getMessage());
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
