package com.opentext.mayaserver.datagenerators.aws.billing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opentext.mayaserver.config.ApplicationPropertiesConfig;
import com.opentext.mayaserver.datagenerators.aws.billing.model.AWSDictionary;
import com.opentext.mayaserver.datagenerators.aws.billing.model.BillCostData;
import com.opentext.mayaserver.datagenerators.util.DataGeneratorUtils;
import com.opentext.mayaserver.datauploaders.s3mock.S3UploadService;
import com.opentext.mayaserver.exceptions.MayaServerException;
import com.opentext.mayaserver.models.CloudCostData;
import com.opentext.mayaserver.models.StateEnum;
import com.opentext.mayaserver.models.UseCase;
import com.opentext.mayaserver.models.vo.UseCaseVO;
import com.opentext.mayaserver.repository.CloudCostDataRepository;
import com.opentext.mayaserver.repository.UseCaseRepository;
import com.opentext.mayaserver.services.UseCaseAPIService;
import com.opentext.mayaserver.utility.CommonUtils;
import com.opentext.mayaserver.utility.ProfileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.opentext.mayaserver.utility.Constants.*;
import static com.opentext.mayaserver.utility.DefaultData.DEFAULT_RECORDS_PER_CSV;

/**
 * @author Rajiv
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class AWSBillingDataGenerator {

    private UseCaseAPIService useCaseAPIService;
    private final S3UploadService s3UploadService;
    private final ApplicationPropertiesConfig applicationPropertiesConfig;
    private final AWSBillGeneratorUtility awsBillGeneratorUtility;
    private final ProfileHandler profileHandler;
    private final UseCaseRepository useCaseRepository;
    private final CloudCostDataRepository cloudCostDataRepository;

    @Autowired
    public void setUseCaseAPIService(@Lazy UseCaseAPIService useCaseAPIService) {
        this.useCaseAPIService = useCaseAPIService;
    }

    @Async
    public void generateBills(UseCaseVO useCaseVO, String rootAccountID, List<String> memberAccountIDs) {
        log.info("Generating aws bills");
        String useCaseName = null;
        try {
            Map<String, List<BillCostData>> accountBillDetailsMap = new HashMap<>();
            List<BillCostData> billData = new ArrayList<>();
            accountBillDetailsMap.put(ROOT_ACCOUNT, billData);
            useCaseName = useCaseVO.getUseCaseName();
            int fileCount = numberOfCSVFileTobeCreated(useCaseVO.getBilling().getRecordsPerDay());
            String absolutePath = CommonUtils.createDirectoryBasedOnUseCase(useCaseName + FILE_PATH_SEPARATOR + AWS_COST_DIR, applicationPropertiesConfig.getS3Mock().getNfs());
            Map<String, String> billDirMap = DataGeneratorUtils.createAwsBillDirectories(absolutePath, useCaseVO.getBilling().getStartDate(), useCaseVO.getBilling().getEndDate());
            for (Map.Entry<String, String> entry : billDirMap.entrySet()) {
                String monthFileAbsolutePath = entry.getValue();
                for (int i = 1; i <= fileCount; i++) {
                    String billFilePath = monthFileAbsolutePath + FILE_PATH_SEPARATOR + BILL_FILE_NAME + i + CSV_EXTENSION;
                    prepareBillsCSV(useCaseVO, billFilePath, rootAccountID, memberAccountIDs, accountBillDetailsMap);
                    Thread.sleep(1000);
                }
            }
            log.info("Generating aws bills {}", Thread.currentThread());
            awsBillGeneratorUtility.generateReportManifest(billDirMap, useCaseName, rootAccountID);
            log.info("AWS Billing Data generated successfully");
            if (profileHandler.isProductionProfileActive()) {
                s3UploadService.uploadAWSBillToS3(BUCKET_NAME, applicationPropertiesConfig.getS3Mock().getNfs() + FILE_PATH_SEPARATOR + useCaseName, useCaseName, useCaseVO.getMode());
            }
            saveCostData(useCaseName, accountBillDetailsMap);
            useCaseAPIService.updateStatusOfUseCase(useCaseName, StateEnum.CREATED);

        } catch (Exception e) {
            log.error("Error reported while bill generation, updating usecase status to FAILED", e);
            useCaseAPIService.updateStatusOfUseCase(useCaseName, StateEnum.FAILED);
            throw new MayaServerException("Error reported while bill generation", e);
        }

    }


    public void prepareBillsCSV(UseCaseVO useCaseVO, String filePath, String rootAccountID, List<String> memberAccountIDs, Map<String, List<BillCostData>> accountBillDetailsMap) throws IOException {
        InputStream inputStream = null;
        try {
            List<Integer> maxMinRange = awsBillGeneratorUtility.calculateMaxAndMinimumCopy(useCaseVO.getBilling().getRecordsPerDay());
            int totalMemberAccounts = memberAccountIDs.size();
            File file = new File(filePath);
            inputStream = new AWSReportParser().getReportTemplateAsStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), BUFFER_SIZE);
            CSVReader csvReader = new CSVReader(reader);
            String[] csvLine;
            String[] headerLine = csvReader.readNext();
            Map<String, Integer>  billHeaderIndexMap = awsBillGeneratorUtility.prepareHeaderIndex(headerLine);
            FileWriter outputFile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputFile);
            List<String[]> data = new ArrayList<>();
            data.add(headerLine);
            //TODO: Update LineItemID , Time , PayerAccount ID
            Map<String, Double> rootAccountCostMap = new HashMap<>();
            rootAccountCostMap.put(AWSDictionary.LINE_ITEM_BLENDED_COST.val, 0.00);
            rootAccountCostMap.put(AWSDictionary.LINE_ITEM_UNBLENDED_COST.val, 0.00);
            rootAccountCostMap.put(AMORTIZED_COST, 0.00);
            while ((csvLine = csvReader.readNext()) != null) {
                // Update Columns as per mock
                int limit = DataGeneratorUtils.getRowCopyLimit(maxMinRange.get(1), maxMinRange.get(0));
                for (int i = 0; i < limit; i++) {
                    List<String[]> sameLineIDEntry = awsBillGeneratorUtility.updateRequiredColumns(csvLine, rootAccountID, memberAccountIDs, totalMemberAccounts, file, rootAccountCostMap, accountBillDetailsMap, billHeaderIndexMap);
                    data.addAll(sameLineIDEntry);
                }
            }
            //TODO:  Sort here
            writer.writeAll(data);
            writer.close();
            log.debug("FinOps CSV bills generated successfully '{}'", filePath);
            String gunZipFilePath = awsBillGeneratorUtility.buildGunZipFile(filePath);
            FileUtils.forceDelete(new File(filePath));
            mergeMultipleCSVFileBillCost(rootAccountID, rootAccountCostMap, file, accountBillDetailsMap);
            log.info("FinOps CSV files compressed to GZ file successfully '{}'", gunZipFilePath);
        } catch (Exception e) {
            log.error("Error reported while generating bill '{}'", e.getMessage());
            throw new MayaServerException("Bill generation failed ", e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private void mergeMultipleCSVFileBillCost(String rootAccountId, Map<String, Double> rootAccountCostMap, File billPath, Map<String, List<BillCostData>> accountBillDetailsMap) {
        String billPeriodDir = billPath.getParentFile().getParentFile().getName();
        AtomicBoolean isBillEntryFound = new AtomicBoolean(false);
        double blendedCost = rootAccountCostMap.get(AWSDictionary.LINE_ITEM_BLENDED_COST.val);
        double unblendedCost = rootAccountCostMap.get(AWSDictionary.LINE_ITEM_UNBLENDED_COST.val);
        double amortizedCost = rootAccountCostMap.get(AMORTIZED_COST);

        List<BillCostData> billCostDataList = accountBillDetailsMap.get(ROOT_ACCOUNT);
        // Now update the existing cost per bill period
        billCostDataList.forEach(item -> {
            if (item.getBillPeriod().equals(billPeriodDir)) {
                isBillEntryFound.set(true);
                item.setAmortizedCost(item.getAmortizedCost() + amortizedCost);
                item.setBlendedCost(item.getBlendedCost() + blendedCost);
                item.setUnblendedCost(item.getUnblendedCost() + unblendedCost);
            }
        });
        if (!isBillEntryFound.get()) {
            billCostDataList.add(awsBillGeneratorUtility.createBillCostData(rootAccountId, ROOT_ACCOUNT, amortizedCost, blendedCost, unblendedCost, billPeriodDir));
        }
    }

    /**
     * Saving Root Account details and member account details in table.
     * These data are persisted as list values
     *
     * @param useCaseName
     */
    @Transactional
    public void saveCostData(String useCaseName, Map<String, List<BillCostData>> accountBillDetailsMap) {
        Optional<UseCase> useCaseOptional = useCaseRepository.findByUseCaseName(useCaseName);
        if (useCaseOptional.isPresent()) {
            CloudCostData cloudCostData = new CloudCostData();
            cloudCostData.setUseCaseId(useCaseOptional.get());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String rootAccountCostData = null;
            String memberAccountCostData = new String();
            try {
                rootAccountCostData = objectMapper.writeValueAsString(accountBillDetailsMap.get(ROOT_ACCOUNT));
                accountBillDetailsMap.remove(ROOT_ACCOUNT);
                memberAccountCostData = objectMapper.writeValueAsString(accountBillDetailsMap);
            } catch (JsonProcessingException e) {
                log.error("Unable to parse root account cost data to string");
            }
            cloudCostData.setRootAccountCost(rootAccountCostData);
            cloudCostData.setMemberAccountCost(memberAccountCostData);
            // Handling async delete flow if someone removed usecase data before saving cost data.
            // Need latest state of usecase entry before saving cost data
            useCaseRepository.findByUseCaseName(useCaseName).ifPresentOrElse(usecase ->
                    {
                        cloudCostDataRepository.save(cloudCostData);
                        log.info("Cost data saved successfully into DB");
                    },
                    () -> log.error("Cost data couldn't be saved to DB, it could be because associated UseCase '{}' entry was not found.", useCaseName)
            );
        } else {
            log.error("Unexpected behaviour traced UseCase '{}' entry not found in DB. Hence aborted Cost data save operation", useCaseName);
        }
        accountBillDetailsMap.clear();   // clearing off the memory
    }

    /**
     * Calculating count of csv bill that need to be created for given request.
     *
     * @param recordsPerDay
     * @return count of csv files that need to be created
     */
    private int numberOfCSVFileTobeCreated(int recordsPerDay) {
        if (recordsPerDay <= 0) {
            return 0;
        } else if (recordsPerDay <= DEFAULT_RECORDS_PER_CSV) {
            return 1;
        } else {
            int number = recordsPerDay / DEFAULT_RECORDS_PER_CSV;
            return recordsPerDay % DEFAULT_RECORDS_PER_CSV == 0 ? number : number + 1;
        }
    }

}
