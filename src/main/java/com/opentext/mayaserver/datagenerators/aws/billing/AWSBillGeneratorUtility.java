package com.opentext.mayaserver.datagenerators.aws.billing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentext.mayaserver.datagenerators.aws.billing.model.*;
import com.opentext.mayaserver.datagenerators.util.DataGeneratorUtils;
import com.opentext.mayaserver.datagenerators.util.DateUtils;
import com.opentext.mayaserver.exceptions.MayaServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.GZIPOutputStream;

import static com.opentext.mayaserver.datagenerators.util.DataGeneratorUtils.getRowCopyLimit;
import static com.opentext.mayaserver.datagenerators.util.DateUtils.generateRandomDateInBetween;
import static com.opentext.mayaserver.utility.Constants.*;
import static com.opentext.mayaserver.utility.DefaultData.DEFAULT_RECORDS_PER_CSV;
import static com.opentext.mayaserver.utility.UseCaseServiceHelper.isEmptyString;

/**
 * @author Rajiv
 */

@Slf4j
@Component
public class AWSBillGeneratorUtility {

    private final String TAX_LINE_ITEM_DESCRIPTION_PATTERN = "Tax for product code %s usage type %s"; //example: Tax for product code AmazonEC2 usage type USW2-EBS:VolumeUsage.gp2
    private final String ENTERPRISE_DISCOUNT_LINE_ITEM_DESCRIPTION_PATTERN = "Enterprise Discount Program Discount for %s"; //example: Enterprise Discount Program Discount for AmazonEC2
    private final String EC2_DEFAULT_LINE_ITEM_DESCRIPTION_PATTERN = "$%f per On Demand %s %s Instance Hour"; //example: $1.248 per On Demand Linux i3.4xlarge Instance Hour
    protected final String EC2_PRODUCT_CODE = "AmazonEC2";
    protected final String RDS_PRODUCT_CODE = "AmazonRDS";
    protected final String S3_PRODUCT_CODE = "AmazonS3";

    private ObjectMapper objectMapper;


    public String getLineItemDescription(
            String productCode,
            double cost,
            String operatingSystem,
            String instanceType,
            LineItemType lineItemType
    ) {
        switch (lineItemType) {
            case TAX:
                return String.format(TAX_LINE_ITEM_DESCRIPTION_PATTERN, productCode, instanceType);
            case ENTERPRISE_DISCOUNT:
                return String.format(ENTERPRISE_DISCOUNT_LINE_ITEM_DESCRIPTION_PATTERN, instanceType);
            default:
                if (productCode.equals(EC2_PRODUCT_CODE)) {
                    return String.format(
                            EC2_DEFAULT_LINE_ITEM_DESCRIPTION_PATTERN,
                            cost,
                            operatingSystem,
                            instanceType
                    );
                }
        }

        return null;
    }

    public String generateLineItemIdFromSeed(int seed) {
        return null; //generateAlphaNumericContent(seed, LINE_ITEM_ID_LENGTH);
    }

    public Map<String, Integer> prepareHeaderIndex(String[] lineHeader) {
        Map<String, Integer> billHeaderIndexMap = new HashMap<>();
        for (int i = 0; i < lineHeader.length; i++) {
            billHeaderIndexMap.put(lineHeader[i], i);
        }
        return billHeaderIndexMap;
    }

    public List<String[]> updateRequiredColumns(String[] csvLine, String rootAccountID, List<String> memberAccountIDs, int totalMemberAccounts, File billPath, Map<String, Double> rootAccountCostMap, Map<String, List<BillCostData>> accountBillDetailsMap, Map<String, Integer>  billHeaderIndexMap) {
        double amortizedCost = 0.00;
        double blendedCost = 0.00;
        double unblendedCost = 0.00;

        String billPeriodDir = billPath.getParentFile().getParentFile().getName();
        String[] billPeriod = billPeriodDir.split("-");

        LineItem lineItem = LineItem.builder().id(DataGeneratorUtils.generateAlphaNumericContent(AWS_BILL_LINE_ITEM_ID_LENGTH)).lineItemType(LineItemType.TAX).memberAccountID(memberAccountIDs.get(getRandomMemberAccountIdIndex(totalMemberAccounts))).billingPeriodStartDate(DateUtils.prepareBillDate(billPeriod[0], 0, 0, 0)).billingPeriodEndDate(DateUtils.prepareBillDate(billPeriod[1], 0, 0, 0)).build();
        List<String[]> listOfSameLineItemIdLine = new ArrayList<>();
        for (int i = 0; i < getRowCopyLimit(MINIMUM_SAME_LINE_ITEM_ID, MAXIMUM_SAME_LINE_ITEM_ID); i++) {
            LocalDate randomDate = generateRandomDateInBetween(billPeriod[0], billPeriod[1]);
            List<String> dateList = DateUtils.prepareLineItemUsageDate(randomDate, 0, 0, 0);
            String usageStartDate = dateList.get(0);
            String usageEndDate = dateList.get(1);
            String[] tuple = prepareTuple(csvLine, rootAccountID, billHeaderIndexMap, lineItem, usageStartDate, usageEndDate);
            updateLineItemDescription(tuple, lineItem.getMemberAccountID(), billHeaderIndexMap);
            listOfSameLineItemIdLine.add(tuple);
            if (tuple[billHeaderIndexMap.get(AWSDictionary.LINE_ITEM_LINE_ITEM_TYPE.val)].trim().equals(LineItemType.TAX.getValue())) {
                break;
            }
            if (HOT_PRODUCT_CODE_MAP.containsKey(csvLine[billHeaderIndexMap.get(AWSDictionary.LINE_ITEM_PRODUCT_CODE.val)])) {
                for (int j = 0; j <= HOT_PRODUCT_CODE_MAP.get(csvLine[billHeaderIndexMap.get(AWSDictionary.LINE_ITEM_PRODUCT_CODE.val)]); j++) {
                    String[] onDemandTuple = prepareTuple(csvLine, rootAccountID, billHeaderIndexMap, lineItem, usageStartDate, usageEndDate);
                    listOfSameLineItemIdLine.add(onDemandTuple);
                    amortizedCost += calculateAmortizedCost(onDemandTuple, billHeaderIndexMap);
                    unblendedCost += Double.parseDouble(onDemandTuple[billHeaderIndexMap.get(AWSDictionary.LINE_ITEM_UNBLENDED_COST.val)]);
                    blendedCost += Double.parseDouble(onDemandTuple[billHeaderIndexMap.get(AWSDictionary.LINE_ITEM_BLENDED_COST.val)]);
                }
            }
            amortizedCost += calculateAmortizedCost(tuple, billHeaderIndexMap);
            unblendedCost += Double.parseDouble(tuple[billHeaderIndexMap.get(AWSDictionary.LINE_ITEM_UNBLENDED_COST.val)]);
            blendedCost += Double.parseDouble(tuple[billHeaderIndexMap.get(AWSDictionary.LINE_ITEM_BLENDED_COST.val)]);
        }
        calculateRootAccountCost(blendedCost, unblendedCost, amortizedCost, rootAccountCostMap);
        calculateMemberAccountCost(blendedCost, unblendedCost, amortizedCost, accountBillDetailsMap, billPeriodDir, lineItem.getMemberAccountID());
        return listOfSameLineItemIdLine;
    }

    private String[] prepareTuple(String[] csvLine, String rootAccountID, Map<String, Integer> billHeaderIndexMap, LineItem lineItem, String usageStartDate, String usageEndDate) {
        String[] tuple = csvLine.clone();
        tuple[billHeaderIndexMap.get(AWSDictionary.IDENTITY_LINE_ITEM_ID.val)] = lineItem.getId();
        tuple[billHeaderIndexMap.get(AWSDictionary.IDENTITY_TIME_INTERVAL.val)] = usageStartDate + "/" + usageEndDate;
        tuple[billHeaderIndexMap.get(AWSDictionary.BILL_PAYER_ACCOUNT_ID.val)] = rootAccountID;
        tuple[billHeaderIndexMap.get(AWSDictionary.LINE_ITEM_USAGE_ACCOUNT_ID.val)] = lineItem.getMemberAccountID();
        tuple[billHeaderIndexMap.get(AWSDictionary.BILL_BILLING_PERIOD_START_DATE.val)] = lineItem.getBillingPeriodStartDate();
        tuple[billHeaderIndexMap.get(AWSDictionary.BILL_BILLING_PERIOD_END_DATE.val)] = lineItem.getBillingPeriodEndDate();
        tuple[billHeaderIndexMap.get(AWSDictionary.LINE_ITEM_USAGE_START_DATE.val)] = usageStartDate;
        tuple[billHeaderIndexMap.get(AWSDictionary.LINE_ITEM_USAGE_END_DATE.val)] = usageEndDate;
        updateLineItemDescription(tuple, lineItem.getMemberAccountID(), billHeaderIndexMap);
        return tuple;
    }

    private double calculateAmortizedCost(String[] tuple, Map<String, Integer> billHeaderIndexMap) {
        String lineItemType = tuple[billHeaderIndexMap.get(AWSDictionary.LINE_ITEM_LINE_ITEM_TYPE.val)];
        double amortizedCost = 0.00;
        switch (lineItemType) {
            case SAVINGS_PLAN_COVERED_USAGE, SAVINGS_PLAN_NEGATION -> {
                if (!isEmptyString(tuple[billHeaderIndexMap.get(AWSDictionary.SAVINGS_PLAN_SAVINGS_PLAN_EFFECTIVE_COST.val)])) {
                    amortizedCost = Double.parseDouble(tuple[billHeaderIndexMap.get(AWSDictionary.SAVINGS_PLAN_SAVINGS_PLAN_EFFECTIVE_COST.val)]);
                }
            }
            case DISCOUNTED_USAGE -> {
                if (!isEmptyString(tuple[billHeaderIndexMap.get(AWSDictionary.RESERVATION_EFFECTIVE_COST.val)])) {
                    amortizedCost = Double.parseDouble(tuple[billHeaderIndexMap.get(AWSDictionary.RESERVATION_EFFECTIVE_COST.val)]);
                }
            }
            case FEE -> {
                if (isEmptyString(tuple[billHeaderIndexMap.get(AWSDictionary.RESERVATION_RESERVATION_ARN.val)])) {
                    amortizedCost = Double.parseDouble(tuple[billHeaderIndexMap.get(AWSDictionary.LINE_ITEM_UNBLENDED_COST.val)]);
                }
            }
            case RI_FEE -> {
                if (!isEmptyString(tuple[billHeaderIndexMap.get(AWSDictionary.RESERVATION_UNUSED_AMORTIZED_UPFRONT_FEE_FOR_BILLING_PERIOD.val)])) {
                    amortizedCost = Double.parseDouble(tuple[billHeaderIndexMap.get(AWSDictionary.RESERVATION_UNUSED_AMORTIZED_UPFRONT_FEE_FOR_BILLING_PERIOD.val)]);
                }
            }
            default -> {
                if (!isEmptyString(tuple[billHeaderIndexMap.get(AWSDictionary.LINE_ITEM_UNBLENDED_COST.val)])) {
                    amortizedCost = Double.parseDouble(tuple[billHeaderIndexMap.get(AWSDictionary.LINE_ITEM_UNBLENDED_COST.val)]);
                }
            }
        }
        return amortizedCost;
    }

    private void calculateRootAccountCost(double blendedCost, double unblendedCost, double amortizedCost, Map<String, Double> rootAccountCostMap) {
        unblendedCost += rootAccountCostMap.get(AWSDictionary.LINE_ITEM_UNBLENDED_COST.val);
        blendedCost += rootAccountCostMap.get(AWSDictionary.LINE_ITEM_BLENDED_COST.val);
        amortizedCost += rootAccountCostMap.get(AMORTIZED_COST);
        rootAccountCostMap.put(AWSDictionary.LINE_ITEM_UNBLENDED_COST.val, unblendedCost);
        rootAccountCostMap.put(AWSDictionary.LINE_ITEM_BLENDED_COST.val, blendedCost);
        rootAccountCostMap.put(AMORTIZED_COST, amortizedCost);
    }

    private void calculateMemberAccountCost(double blendedCost, double unblendedCost, double amortizedCost, Map<String, List<BillCostData>> accountBillDetailsMap, String billPeriodDir, String memberAccountID) {
        AtomicBoolean isBillEntryFound = new AtomicBoolean(false);
        String billKey = MEMBER_ACCOUNTS + "_" + billPeriodDir;
        List<BillCostData> memberAccountBillDataList = accountBillDetailsMap.get(billKey);
        BillCostData memberAccountBillCostData = createBillCostData(memberAccountID, MEMBER_ACCOUNTS, amortizedCost, blendedCost, unblendedCost, billPeriodDir);
        // No bill added then create
        if (memberAccountBillDataList == null) {
            memberAccountBillDataList = new ArrayList<>();
            memberAccountBillDataList.add(memberAccountBillCostData);
            accountBillDetailsMap.put(billKey, memberAccountBillDataList);
        } else {
            // Update list here
            memberAccountBillDataList.forEach(item -> {
                if (item.getBillPeriod().equals(billPeriodDir) && item.getAccountId().equals(memberAccountID)) {
                    isBillEntryFound.set(true);
                    item.setAmortizedCost(item.getAmortizedCost() + amortizedCost);
                    item.setBlendedCost(item.getBlendedCost() + blendedCost);
                    item.setUnblendedCost(item.getUnblendedCost() + unblendedCost);
                }
            });
            if (!isBillEntryFound.get()) {
                memberAccountBillDataList.add(memberAccountBillCostData);
            }
        }
    }

    protected BillCostData createBillCostData(String accountId, String accountType, double amortizedCost, double blendedCost, double unblendedCost, String billPeriodDir) {
        return BillCostData.builder().accountId(accountId)
                .accountType(accountType)
                .amortizedCost(amortizedCost)
                .blendedCost(blendedCost)
                .unblendedCost(unblendedCost)
                .billPeriod(billPeriodDir)
                .build();
    }

    private void updateLineItemDescription(String[] tuple, String memberAccountId, Map<String, Integer>  billHeaderIndexMap) {
        String description = "SavingsPlanNegation used by AccountId : %s and UsageSku : %s";
        if (tuple[billHeaderIndexMap.get(AWSDictionary.LINE_ITEM_LINE_ITEM_TYPE.val)].trim().equals(SAVINGS_PLAN_NEGATION)) {
            String currentDescription = tuple[billHeaderIndexMap.get(AWSDictionary.LINE_ITEM_LINE_ITEM_DESCRIPTION.val)].trim();
            String usageSku = currentDescription.substring(currentDescription.lastIndexOf(":") + 1).trim();
            tuple[billHeaderIndexMap.get(AWSDictionary.LINE_ITEM_LINE_ITEM_DESCRIPTION.val)] = String.format(description, memberAccountId, usageSku);
        }
    }

    private int getRandomMemberAccountIdIndex(int length) {
        Random random = new Random();
        return random.nextInt(length);
    }

    public List<Integer> calculateMaxAndMinimumCopy(int requestedRecordsPerDay) {
        // Setting minimum copy range 35% less
        List<Integer> maxAndMinRange = new ArrayList<>();
        if (requestedRecordsPerDay <= DEFAULT_RECORDS_PER_CSV) {
            double d = (double) requestedRecordsPerDay / (double) (LOCAL_REPORT_TOTAL_LINE * MAXIMUM_SAME_LINE_ITEM_ID);
            int max = d < 2 ? 2 : (int) (d);
            maxAndMinRange.add(max);
            maxAndMinRange.add((int) (max * 0.65));
        } else {
            maxAndMinRange.add(MAXIMUM_TEMPLATE_COPY);
            maxAndMinRange.add(MINIMUM_TEMPLATE_COPY);
        }
        return maxAndMinRange;
    }

    protected String buildGunZipFile(String csvSourceFilePath) throws Exception {
        byte[] buffer = new byte[BUFFER_SIZE];
        String gunZipFilePath = csvSourceFilePath.concat(GUN_ZIP_EXTENSION);
        FileOutputStream fileOutputStream = null;
        GZIPOutputStream gzipOuputStream = null;
        FileInputStream fileInput = null;
        try {
            fileOutputStream = new FileOutputStream(gunZipFilePath);
            gzipOuputStream = new GZIPOutputStream(fileOutputStream);
            fileInput = new FileInputStream(csvSourceFilePath);
            int bytes_read;
            while ((bytes_read = fileInput.read(buffer)) > 0) {
                gzipOuputStream.write(buffer, 0, bytes_read);
            }
            fileInput.close();
            gzipOuputStream.finish();
            gzipOuputStream.close();
            log.debug("FinOps CSV bill file compressed successfully {}", gunZipFilePath);
            return gunZipFilePath;
        } catch (Exception ex) {
            log.error(" Unable to zip the FinOps csv file {}", csvSourceFilePath);
            throw new MayaServerException("Unable to compress csv file " + csvSourceFilePath, ex);
        } finally {
            if (fileInput != null) {
                fileInput.close();
            }
            if (gzipOuputStream != null) {
                gzipOuputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }

    public void generateReportManifest(Map<String, String> billDirMap, String useCaseName, String rootAccountID) throws IOException {
        String reportID = DataGeneratorUtils.generateRandomStringUsingRegex(AWS_REPORT_ID_REGEX);
        for (Map.Entry<String, String> entry : billDirMap.entrySet()) {
            String assemblyDirAbsolutePath = entry.getValue();
            File dayDirectory = new File(assemblyDirAbsolutePath);
            File[] gzipBillFiles = dayDirectory.listFiles();
            List<String> reportKeyList = new ArrayList<>();
            Arrays.stream(gzipBillFiles).forEach(item -> {
                String path = item.getPath();
                path = path.substring(path.indexOf(useCaseName));
                reportKeyList.add(path);
            });
            ReportManifest reportManifest = ReportManifest.builder()
                    .assemblyId(dayDirectory.getName())
                    .account(rootAccountID)
                    .columns(generateReportColumn())
                    .charset(CHARSET)
                    .compression(COMPRESSION)
                    .contentType(CONTENT_TYPE)
                    .reportId(reportID)
                    .reportName(AWS_COST_DIR)
                    .billingPeriod(generateBillingPeriod(dayDirectory))
                    .bucket(useCaseName)
                    .reportKeys(reportKeyList)
                    .additionalArtifactKeys(new ArrayList<>())
                    .build();
            String manifestFilePath = assemblyDirAbsolutePath + FILE_PATH_SEPARATOR + AWS_COST_DIR + MANIFEST_FILE_NAME + JSON;
            DataGeneratorUtils.writeContentToFile(reportManifest, manifestFilePath);
            log.debug("Manifest file generated successfully '{}'", manifestFilePath);
            String copyLocation = dayDirectory.getParent() + FILE_PATH_SEPARATOR + AWS_COST_DIR + MANIFEST_FILE_NAME + JSON;
            Files.copy(Paths.get(manifestFilePath), Paths.get(copyLocation), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private ReportBillingPeriod generateBillingPeriod(File path) {
        String[] billPeriod = path.getParentFile().getName().split("-");
        String timeStamp = "T000000.000Z";
        return ReportBillingPeriod.builder()
                .start(billPeriod[0].concat(timeStamp))
                .end(billPeriod[1].concat(timeStamp)).build();
    }

    List<ReportColumn> generateReportColumn() {
        List<ReportColumn> reportColumnList = new ArrayList<>();
        try {
            String[] columns = COLUMNS_JSON_STRING.split(";");
            objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            for (String col : columns) {
                ReportColumn reportColumn = objectMapper.readValue(col.trim(), ReportColumn.class);
                reportColumnList.add(reportColumn);
            }
            return reportColumnList;
        } catch (JsonProcessingException e) {
            log.error("Error while preparing manifest  column fields {}", e.getMessage());
            throw new MayaServerException("Error while parsing column fields " + e.getMessage());
        }

    }
}
