package com.opentext.mayaserver.datagenerators.aws.billing;

import com.opencsv.CSVReader;
import com.opentext.mayaserver.datagenerators.aws.billing.model.AWSDictionary;
import com.opentext.mayaserver.datagenerators.aws.billing.model.AWSDictionaryData;
import com.opentext.mayaserver.datagenerators.util.DataGeneratorUtils;
import com.opentext.mayaserver.datagenerators.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.opentext.mayaserver.utility.Constants.LOCAL_REPORT_FILE_PATH;

public class AWSReportParser {
    private static final Logger logger = LoggerFactory.getLogger(AWSReportParser.class);
    private static final int BUFFER_SIZE = 5120;
    public InputStream getReportTemplateAsStream() {
        return this.getClass().getResourceAsStream(LOCAL_REPORT_FILE_PATH);
    }
    AwsUsageItem parseUsageItem(AWSDictionaryData[] dictionaryDataArray, String[] csvLine) {
        if (csvLine.length != dictionaryDataArray.length) {
            throw new IllegalArgumentException("csv line does not match schema");
        }

        AwsUsageItemBuilder builder = AwsUsageItem.builder();
        for (int i = 0; i < dictionaryDataArray.length; i++) {
            String val = csvLine[i];
            String key = dictionaryDataArray[i].getSchema();
            AWSDictionary column = dictionaryDataArray[i].getAwsBillDictionary();
            if (column != null) {
                try {
                    setColumn(column, val, builder);
                } catch (IllegalArgumentException e) {
                    logger.trace("ignoring unknown value in aws report csv '{}'/'{}'", dictionaryDataArray[i].getSchema(), val);
                } catch (Exception e) {
                    logger.warn("failed to set value {} for column {}", val, key, e);
                }
            }
        }
        return builder.buildAll();
    }

    public List<AwsUsageItem> loadReport() throws Exception {
        InputStream inputStream = null;
        try {
            inputStream = getReportTemplateAsStream();
            return loadCsv(inputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public CSVReader getCSVReader() throws Exception {
        InputStream inputStream = null;
        try {
            inputStream = getReportTemplateAsStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), BUFFER_SIZE);
            CSVReader csvReader = new CSVReader(reader);
            return csvReader;
        } finally {
            /*if (inputStream != null) {
                inputStream.close();
            }*/
        }
    }

    private List<AwsUsageItem> loadCsv(InputStream inputStream) throws Exception {
        List<String[]> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), BUFFER_SIZE);
        CSVReader csvReader = new CSVReader(reader);

        String[] headerLine = csvReader.readNext();
        AWSDictionaryData[] dataColumns = AWSDictionaryData.schemaToDataDictionaryArray(headerLine);

        String[] csvLine;
        while ((csvLine = csvReader.readNext()) != null) {
            lines.add(csvLine);
        }
        return convertToUsageItems(dataColumns, lines);
    }

    private List<AwsUsageItem> convertToUsageItems(AWSDictionaryData[] dictionaryData, List<String[]> csvLines) {
        List<AwsUsageItem> items = csvLines.parallelStream().map((line -> {
            AwsUsageItem item = parseUsageItem(dictionaryData, line);
            item.getBill().setBillingPeriodStartDateTrunc(item.getBill().getBillingPeriodStartDate());
            item.getLineItem().setUsageStartDateTrunc(item.getLineItem().getUsageStartDate());
            item.setId(DataGeneratorUtils.newUuid());
            return item;
        })).collect(Collectors.toList());

        return items;
    }

    private void setColumn(AWSDictionary column, String val, AwsUsageItemBuilder builder) {
        switch (column) {
            case IDENTITY_LINE_ITEM_ID:
                builder.awsIdentity().lineItemId(val);
                break;
            case IDENTITY_TIME_INTERVAL:
                builder.awsIdentity().timeInterval(val);
                break;
            case BILL_INVOICE_ID:
                builder.awsBill().invoiceId(val);
                break;
            case BILL_INVOICING_ENTITY:
                builder.awsBill().invoicingEntity(val);
                break;
            case BILL_BILLING_ENTITY:
                builder.awsBill().billingEntity(val);
                break;
            case BILL_BILL_TYPE:
                builder.awsBill().billType(val);
                break;
            case BILL_PAYER_ACCOUNT_ID:
                builder.awsBill().payerAccountId(val);
                break;
            case BILL_BILLING_PERIOD_START_DATE:
                builder.awsBill().billingPeriodStartDate(DateUtils.toJavaUtilDateFromIso8601String(val));
                break;
            case BILL_BILLING_PERIOD_END_DATE:
                builder.awsBill().billingPeriodEndDate(DateUtils.toJavaUtilDateFromIso8601String(val));
                break;
            case LINE_ITEM_USAGE_ACCOUNT_ID:
                builder.awsLineItem().usageAccountId(val);
                break;
            case LINE_ITEM_LINE_ITEM_TYPE:
                builder.awsLineItem().lineItemType(val);
                break;
            case LINE_ITEM_USAGE_START_DATE:
                builder.awsLineItem().usageStartDate(DateUtils.toJavaUtilDateFromIso8601String(val));
                break;
            case LINE_ITEM_USAGE_END_DATE:
                builder.awsLineItem().usageEndDate(DateUtils.toJavaUtilDateFromIso8601String(val));
                break;
            case LINE_ITEM_PRODUCT_CODE:
                builder.awsLineItem().productCode(val);
                break;
            case LINE_ITEM_USAGE_TYPE:
                builder.awsLineItem().usageType(val);
                break;
            case LINE_ITEM_OPERATION:
                builder.awsLineItem().operation(val);
                break;
            case LINE_ITEM_AVAILABILITY_ZONE:
                builder.awsLineItem().availabilityZone(val);
                break;
            case LINE_ITEM_RESOURCE_ID:
                builder.awsLineItem().resourceId(val);
                break;
            case LINE_ITEM_USAGE_AMOUNT:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsLineItem().usageAmount(Double.parseDouble(val));
                }
                break;
            case LINE_ITEM_NORMALIZATION_FACTOR:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsLineItem().normalizationFactor(Double.parseDouble(val));
                }
                break;
            case LINE_ITEM_NORMALIZED_USAGE_AMOUNT:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsLineItem().normalizedUsageAmount(Double.parseDouble(val));
                }
                break;
            case LINE_ITEM_CURRENCY_CODE:
                builder.awsLineItem().currencyCode(val);
                break;
            case LINE_ITEM_NET_UNBLENDED_RATE:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsLineItem().netUnblendedRate(Double.parseDouble(val));
                }
                break;
            case LINE_ITEM_NET_UNBLENDED_COST:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsLineItem().netUnblendedCost(Double.parseDouble(val));
                }
                break;
            case LINE_ITEM_UNBLENDED_RATE:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsLineItem().unblendedRate(Double.parseDouble(val));
                }
                break;
            case LINE_ITEM_UNBLENDED_COST:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsLineItem().unblendedCost(Double.parseDouble(val));
                }
                break;
            case LINE_ITEM_BLENDED_RATE:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsLineItem().blendedRate(Double.parseDouble(val));
                }
                break;
            case LINE_ITEM_BLENDED_COST:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsLineItem().blendedCost(Double.parseDouble(val));
                }
                break;
            case LINE_ITEM_LINE_ITEM_DESCRIPTION:
                builder.awsLineItem().lineItemDescription(val);
                break;
            case LINE_ITEM_TAX_TYPE:
                builder.awsLineItem().taxType(val);
                break;
            case LINE_ITEM_LEGAL_ENTITY:
                builder.awsLineItem().legalEntity(val);
                break;

            case PRODUCT_AVAILABILITY_ZONE:
                builder.awsProduct().availabilityZone(val);
                break;
            case PRODUCT_EVENT_TYPE:
                builder.awsProduct().eventType(val);
                break;
            case PRODUCT_FREE_TIER:
                builder.awsProduct().freeTier(val);
                break;
            case PRODUCT_INPUT_MODE:
                builder.awsProduct().inputMode(val);
                break;
            case PRODUCT_MAILBOX_STORAGE:
                builder.awsProduct().mailboxStorage(val);
                break;
            case PRODUCT_MEMORY_GIB:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsProduct().memoryGib(Double.parseDouble(val));
                }
                break;
            case PRODUCT_OUTPUT_MODE:
                builder.awsProduct().outputMode(val);
                break;
            case PRODUCT_SUPPORT_MODES:
                builder.awsProduct().supportModes(val);
                break;
            case PRODUCT_PRODUCT_NAME:
                builder.awsProduct().productName(val);
                break;
            case PRODUCT_ACCOUNT_ASSISTANCE:
                builder.awsProduct().accountAssistance(val);
                break;
            case PRODUCT_ALARM_TYPE:
                builder.awsProduct().alarmType(val);
                break;
            case PRODUCT_ARCHITECTURAL_REVIEW:
                builder.awsProduct().architecturalReview(val);
                break;
            case PRODUCT_ARCHITECTURE_SUPPORT:
                builder.awsProduct().architecturalSupport(val);
                break;
            case PRODUCT_AVAILABILITY:
                builder.awsProduct().availability(val);
                break;
            case PRODUCT_BEST_PRACTICES:
                builder.awsProduct().bestPractices(val);
                break;
            case PRODUCT_BUNDLE:
                builder.awsProduct().bundle(val);
                break;
            case PRODUCT_CACHE_ENGINE:
                builder.awsProduct().cacheEngine(val);
                break;
            case PRODUCT_CAPACITY_STATUS:
                builder.awsProduct().capacitystatus(val);
                break;
            case PRODUCT_CASE_SEVERITY_RESPONSE_TIMES:
                builder.awsProduct().caseSeverityresponseTimes(val);
                break;
            case PRODUCT_CLOCK_SPEED:
                builder.awsProduct().clockSpeed(val);
                break;
            case PRODUCT_CONTENT_TYPE:
                builder.awsProduct().contentType(val);
                break;
            case PRODUCT_CURRENT_GENERATION:
                builder.awsProduct().currentGeneration(val);
                break;
            case PRODUCT_CUSTOMER_SERVICE_AND_COMMUNITIES:
                builder.awsProduct().customerServiceAndCommunities(val);
                break;
            case PRODUCT_DATA_TRANSFER:
                builder.awsProduct().dataTransfer(val);
                break;
            case PRODUCT_DATA_TRANSFER_QUOTA:
                builder.awsProduct().dataTransferQuota(val);
                break;
            case PRODUCT_DATABASE_EDITION:
                builder.awsProduct().databaseEdition(val);
                break;
            case PRODUCT_DATABASE_ENGINE:
                builder.awsProduct().databaseEngine(val);
                break;
            case PRODUCT_DEDICATED_EBS_THROUGHPUT:
                builder.awsProduct().dedicatedEbsThroughput(val);
                break;
            case PRODUCT_DEPLOYMENT_OPTION:
                builder.awsProduct().deploymentOption(val);
                break;
            case PRODUCT_DESCRIPTION:
                builder.awsProduct().description(val);
                break;
            case PRODUCT_DEVICE_OS:
                builder.awsProduct().deviceOs(val);
                break;
            case PRODUCT_DIRECT_CONNECT_LOCATION:
                builder.awsProduct().directconnectlocation(val);
                break;
            case PRODUCT_DIRECTORY_SIZE:
                builder.awsProduct().directorySize(val);
                break;
            case PRODUCT_DIRECTORY_TYPE:
                builder.awsProduct().directoryType(val);
                break;
            case PRODUCT_DIRECTORY_TYPE_DESCRIPTION:
                builder.awsProduct().directoryTypeDescription(val);
                break;
            case PRODUCT_DISABLE_ACTIVATION_CONFIRMATION_EMAIL:
                builder.awsProduct().disableactivationconfirmationemail(val);
                break;
            case PRODUCT_DURABILITY:
                builder.awsProduct().durability(val);
                break;
            case PRODUCT_EBS_OPTIMIZED:
                builder.awsProduct().ebsOptimized(val);
                break;
            case PRODUCT_ECU:
                builder.awsProduct().ecu(val);
                break;
            case PRODUCT_ENDPOINT_TYPE:
                builder.awsProduct().endpointType(val);
                break;
            case PRODUCT_ENGINE_CODE:
                builder.awsProduct().engineCode(val);
                break;
            case PRODUCT_ENHANCED_NETWORKING_SUPPORTED:
                builder.awsProduct().enhancedNetworkingSupported(val);
                break;
            case PRODUCT_EXECUTION_MODE:
                builder.awsProduct().executionMode(val);
                break;
            case PRODUCT_FEE_CODE:
                builder.awsProduct().feeCode(val);
                break;
            case PRODUCT_FEE_DESCRIPTION:
                builder.awsProduct().feeDescription(val);
                break;
            case PRODUCT_FILE_SYSTEM_TYPE:
                builder.awsProduct().filesystemtype(val);
                break;
            case PRODUCT_FREE_QUERY_TYPES:
                builder.awsProduct().freeQueryTypes(val);
                break;
            case PRODUCT_FREE_USAGE_INCLUDED:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsProduct().freeUsageIncluded(Double.parseDouble(val));
                }
                break;
            case PRODUCT_FREE_TRIAL:
                builder.awsProduct().freeTrial(val);
                break;
            case PRODUCT_FROM_LOCATION:
                builder.awsProduct().fromLocation(val);
                break;
            case PRODUCT_FROM_LOCATION_TYPE:
                builder.awsProduct().fromLocationType(val);
                break;
            case PRODUCT_FROM_REGION_CODE:
                builder.awsProduct().fromRegionCode(val);
                break;
            case PRODUCT_GPU:
                builder.awsProduct().gpu(val);
                break;
            case PRODUCT_GPU_MEMORY:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsProduct().gpuMemory(Double.parseDouble(val));
                }
                break;
            case PRODUCT_GROUP:
                builder.awsProduct().group(val);
                break;
            case PRODUCT_GROUP_DESCRIPTION:
                builder.awsProduct().groupDescription(val);
                break;
            case PRODUCT_INCLUDED_SERVICES:
                builder.awsProduct().includedServices(val);
                break;
            case PRODUCT_INSTANCE:
                builder.awsProduct().instance(val);
                break;
            case PRODUCT_INSTANCE_CAPACITY_10X_LARGE:
                builder.awsProduct().instanceCapacity10xLarge(val);
                break;
            case PRODUCT_INSTANCE_CAPACITY_2X_LARGE:
                builder.awsProduct().instanceCapacity2xLarge(val);
                break;
            case PRODUCT_INSTANCE_CAPACITY_4X_LARGE:
                builder.awsProduct().instanceCapacity4xLarge(val);
                break;
            case PRODUCT_INSTANCE_CAPACITY_8X_LARGE:
                builder.awsProduct().instanceCapacity8xLarge(val);
                break;
            case PRODUCT_INSTANCE_CAPACITY_LARGE:
                builder.awsProduct().instanceCapacityLarge(val);
                break;
            case PRODUCT_INSTANCE_CAPACITY_X_LARGE:
                builder.awsProduct().instanceCapacityXlarge(val);
                break;
            case PRODUCT_INSTANCE_FAMILY:
                builder.awsProduct().instanceFamily(val);
                break;
            case PRODUCT_INSTANCE_TYPE:
                builder.awsProduct().instanceType(val);
                break;
            case PRODUCT_INSTANCE_TYPE_FAMILY:
                builder.awsProduct().instanceTypeFamily(val);
                break;
            case PRODUCT_INTEGRATING_API:
                builder.awsProduct().integratingApi(val);
                break;
            case PRODUCT_INTEGRATING_SERVICE:
                builder.awsProduct().integratingService(val);
                break;
            case PRODUCT_INTEL_AVX_AVAILABLE:
                builder.awsProduct().intelAvxAvailable(val);
                break;
            case PRODUCT_INTEL_AVX_2_AVAILABLE:
                builder.awsProduct().intelAvx2Available(val);
                break;
            case PRODUCT_INTEL_TURBO_AVAILABLE:
                builder.awsProduct().intelTurboAvailable(val);
                break;
            case PRODUCT_LAUNCH_SUPPORT:
                builder.awsProduct().launchSupport(val);
                break;
            case PRODUCT_LICENSE:
                builder.awsProduct().license(val);
                break;
            case PRODUCT_LICENSE_MODEL:
                builder.awsProduct().licenseModel(val);
                break;
            case PRODUCT_LOCATION:
                builder.awsProduct().location(val);
                break;
            case PRODUCT_LOCATION_TYPE:
                builder.awsProduct().locationType(val);
                break;
            case PRODUCT_LOGS_DESTINATION:
                builder.awsProduct().logsDestination(val);
                break;
            case PRODUCT_MACHINE_LEARNING_PROCESS:
                builder.awsProduct().machineLearningProcess(val);
                break;
            case PRODUCT_MAX_IOPS_BURST_PERFORMANCE:
                builder.awsProduct().maxIopsBurstPerformance(val);
                break;
            case PRODUCT_MAX_IOPS_VOLUME:
                builder.awsProduct().maxIopsvolume(val);
                break;
            case PRODUCT_MAX_THROUGHPUT_VOLUME:
                builder.awsProduct().maxThroughputvolume(val);
                break;
            case PRODUCT_MAX_VOLUME_SIZE:
                builder.awsProduct().maxVolumeSize(val);
                break;
            case PRODUCT_MAXIMUM_CAPACITY:
                builder.awsProduct().maximumCapacity(val);
                break;
            case PRODUCT_MAXIMUM_EXTENDED_STORAGE:
                builder.awsProduct().maximumExtendedStorage(val);
                break;
            case PRODUCT_MAXIMUM_STORAGE_VOLUME:
                builder.awsProduct().maximumStorageVolume(val);
                break;
            case PRODUCT_MEMORY:
                builder.awsProduct().memory(val);
                break;
            case PRODUCT_MESSAGE_DELIVERY_FREQUENCY:
                builder.awsProduct().messageDeliveryFrequency(val);
                break;
            case PRODUCT_MESSAGE_DELIVER_ORDER:
                builder.awsProduct().messageDeliveryOrder(val);
                break;
            case PRODUCT_METER_MODE:
                builder.awsProduct().meterMode(val);
                break;
            case PRODUCT_MIN_VOLUME_SIZE:
                builder.awsProduct().minVolumeSize(val);
                break;
            case PRODUCT_MINIMUM_STORAGE_VOLUME:
                builder.awsProduct().minimumStorageVolume(val);
                break;
            case PRODUCT_NETWORK_PERFORMANCE:
                builder.awsProduct().networkPerformance(val);
                break;
            case PRODUCT_NORMALIZATION_SIZE_FACTOR:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsProduct().normalizationSizeFactor(Double.parseDouble(val));
                }
                break;
            case PRODUCT_OFFERING_CLASS:
                builder.awsProduct().offeringClass(val);
                break;
            case PRODUCT_OPERATING_SYSTEM:
                builder.awsProduct().operatingSystem(val);
                break;
            case PRODUCT_OPERATION:
                builder.awsProduct().productOperation(val);
                break;
            case PRODUCT_OPERATIONS_SUPPORT:
                builder.awsProduct().operationsSupport(val);
                break;
            case PRODUCT_ORIGIN:
                builder.awsProduct().origin(val);
                break;
            case PRODUCT_PARAMETER_TYPE:
                builder.awsProduct().parameterType(val);
                break;
            case PRODUCT_PHYSICAL_CORES:
                builder.awsProduct().physicalCores(val);
                break;
            case PRODUCT_PHYSICAL_PROCESSOR:
                builder.awsProduct().physicalProcessor(val);
                break;
            case PRODUCT_PRE_INSTALLED_SW:
                builder.awsProduct().preInstalledSw(val);
                break;
            case PRODUCT_PRICING_UNIT:
                builder.awsProduct().pricingUnit(val);
                break;
            case PRODUCT_PRIMARY_PLACE_OF_USE:
                builder.awsProduct().primaryplaceofuse(val);
                break;
            case PRODUCT_PROACTIVE_GUIDANCE:
                builder.awsProduct().proactiveGuidance(val);
                break;
            case PRODUCT_PROCESSOR_ARCHITECTURE:
                builder.awsProduct().processorArchitecture(val);
                break;
            case PRODUCT_PROCESSOR_FEATURES:
                builder.awsProduct().processorFeatures(val);
                break;
            case PRODUCT_PRODUCT_FAMILY:
                builder.awsProduct().productFamily(val);
                break;
            case PRODUCT_PRODUCT_SCHEMA_DESCRIPTION:
                builder.awsProduct().productSchemaDescription(val);
                break;
            case PRODUCT_PROGRAMMATIC_CASE_MANAGEMENT:
                builder.awsProduct().programmaticCaseManagement(val);
                break;
            case PRODUCT_PROVISIONED:
                builder.awsProduct().provisioned(val);
                break;
            case PRODUCT_PURCHASE_OPTION:
                builder.awsProduct().purchaseOption(val);
                break;
            case PRODUCT_PURCHASE_TERM:
                builder.awsProduct().purchaseterm(val);
                break;
            case PRODUCT_QUEUE_TYPE:
                builder.awsProduct().queueType(val);
                break;
            case PRODUCT_RECIPIENT:
                builder.awsProduct().recipient(val);
                break;
            case PRODUCT_REGION:
                builder.awsProduct().region(val);
                break;
            case PRODUCT_REGION_CODE:
                builder.awsProduct().regioncode(val);
                break;
            case PRODUCT_REPLICATION_TYPE:
                builder.awsProduct().replicationType(val);
                break;
            case PRODUCT_REQUEST_DESCRIPTION:
                builder.awsProduct().requestDescription(val);
                break;
            case PRODUCT_REQUEST_TYPE:
                builder.awsProduct().requestType(val);
                break;
            case PRODUCT_REQUESTTYPE:
                builder.awsProduct().requesttype(val);
                break;
            case PRODUCT_RESOURCE_ASSESSMENT:
                builder.awsProduct().resourceAssessment(val);
                break;
            case PRODUCT_RESOURCE_ENDPOINT:
                builder.awsProduct().resourceEndpoint(val);
                break;
            case PRODUCT_RESOURCE_TYPE:
                builder.awsProduct().resourceType(val);
                break;
            case PRODUCT_ROUTING_TARGET:
                builder.awsProduct().routingTarget(val);
                break;
            case PRODUCT_ROUTING_TYPE:
                builder.awsProduct().routingType(val);
                break;
            case PRODUCT_RUNNING_MODE:
                builder.awsProduct().runningMode(val);
                break;
            case PRODUCT_SERVICE_CODE:
                builder.awsProduct().servicecode(val);
                break;
            case PRODUCT_SERVICE_NAME:
                builder.awsProduct().servicename(val);
                break;
            case PRODUCT_SINGLE_OR_DUAL_PASS:
                builder.awsProduct().singleOrDualPass(val);
                break;
            case PRODUCT_SKU:
                builder.awsProduct().sku(val);
                break;
            case PRODUCT_SOFTWARE_INCLUDED:
                builder.awsProduct().softwareIncluded(val);
                break;
            case PRODUCT_SOFTWARE_TYPE:
                builder.awsProduct().softwareType(val);
                break;
            case PRODUCT_STANDARD_STORAGE_RETENTION_INCLUDED:
                builder.awsProduct().standardStorageRetentionIncluded(val);
                break;
            case PRODUCT_STORAGE:
                builder.awsProduct().storage(val);
                break;
            case PRODUCT_STORAGE_CLASS:
                builder.awsProduct().storageClass(val);
                break;
            case PRODUCT_STORAGE_DESCRIPTION:
                builder.awsProduct().storageDescription(val);
                break;
            case PRODUCT_STORAGE_MEDIA:
                builder.awsProduct().storageMedia(val);
                break;
            case PRODUCT_STORAGE_TYPE:
                builder.awsProduct().storageType(val);
                break;
            case PRODUCT_TECHNICAL_SUPPORT:
                builder.awsProduct().technicalSupport(val);
                break;
            case PRODUCT_TENANCY:
                builder.awsProduct().tenancy(val);
                break;
            case PRODUCT_THIRD_PARTY_SOFTWARE_SUPPORT:
                builder.awsProduct().thirdpartySoftwareSupport(val);
                break;
            case PRODUCT_TIER:
                builder.awsProduct().tier(val);
                break;
            case PRODUCT_TO_LOCATION:
                builder.awsProduct().toLocation(val);
                break;
            case PRODUCT_TO_LOCATION_TYPE:
                builder.awsProduct().toLocationType(val);
                break;
            case PRODUCT_TO_REGION_CODE:
                builder.awsProduct().toRegionCode(val);
                break;
            case PRODUCT_TRAINING:
                builder.awsProduct().training(val);
                break;
            case PRODUCT_TRANSFER_TYPE:
                builder.awsProduct().transferType(val);
                break;
            case PRODUCT_TRANSCODING_RESULT:
                builder.awsProduct().transcodingResult(val);
                break;
            case PRODUCT_TRIAL_PRODUCT:
                builder.awsProduct().trialProduct(val);
                break;
            case PRODUCT_UPFRONT_COMMITMENT:
                builder.awsProduct().upfrontCommitment(val);
                break;
            case PRODUCT_USAGE_TYPE:
                builder.awsProduct().productUsageType(val);
                break;
            case PRODUCT_VCPU:
                builder.awsProduct().vcpu(val);
                break;
            case PRODUCT_VERSION:
                builder.awsProduct().version(val);
                break;
            case PRODUCT_VIDEO_CODEC:
                builder.awsProduct().videoCodec(val);
                break;
            case PRODUCT_VIDEO_FRAME_RATE:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsProduct().videoFrameRate(Double.parseDouble(val));
                }
                break;
            case PRODUCT_VOLUME_TYPE:
                builder.awsProduct().volumeType(val);
                break;
            case PRODUCT_WHO_CAN_OPEN_CASES:
                builder.awsProduct().whoCanOpenCases(val);
                break;
            case PRODUCT_WITH_ACTIVE_USERS:
                builder.awsProduct().withActiveUsers(val);
                break;
            case PRODUCT_WORKFORCE_TYPE:
                builder.awsProduct().workforceType(val);
                break;
            case PRICING_RATE_ID:
                builder.awsPricing().rateId(val);
                break;
            case PRICING_RATE_CODE:
                builder.awsPricing().rateCode(val);
                break;
            case PRICING_LEASE_CONTRACT_LENGTH:
                builder.awsPricing().leaseContractLength(val);
                break;
            case PRICING_OFFERING_CLASS:
                builder.awsPricing().offeringClass(val);
                break;
            case PRICING_PURCHASE_OPTION:
                builder.awsPricing().purchaseOption(val);
                break;
            case PRICING_PUBLIC_ON_DEMAND_COST:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsPricing().publicOnDemandCost(Double.parseDouble(val));
                }
                break;
            case PRICING_PUBLIC_ON_DEMAND_RATE:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsPricing().publicOnDemandRate(Double.parseDouble(val));
                }
                break;
            case PRICING_TERM:
                builder.awsPricing().term(val);
                break;
            case PRICING_UNIT:
                builder.awsPricing().unit(val);
                break;
            case SAVINGS_PLAN_AMORTIZED_UPFRONT_COMMITMENT_FOR_BILLING_PERIOD:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsSavingsPlan().amortizedUpfrontCommitmentForBillingPeriod(Double.parseDouble(val));
                }
                break;
            case SAVINGS_PLAN_END_TIME:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsSavingsPlan().endTime(DateUtils.toJavaUtilDateFromIso8601String(val));
                }
                break;
            case SAVINGS_PLAN_INSTANCE_TYPE_FAMILY:
                builder.awsSavingsPlan().instanceTypeFamily(val);
                break;
            case SAVINGS_PLAN_NET_AMORTIZED_UPFRONT_COMMITMENT_FOR_BILLING_PERIOD:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsSavingsPlan().netAmortizedUpfrontCommitmentForBillingPeriod(Double.parseDouble(val));
                }
                break;
            case SAVINGS_PLAN_NET_RECURRING_COMMITMENT_FOR_BILLING_PERIOD:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsSavingsPlan().netRecurringCommitmentForBillingPeriod(Double.parseDouble(val));
                }
                break;
            case SAVINGS_PLAN_NET_SAVINGS_PLAN_EFFECTIVE_COST:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsSavingsPlan().netSavingsPlanEffectiveCost(Double.parseDouble(val));
                }
                break;
            case SAVINGS_PLAN_OFFERING_TYPE:
                builder.awsSavingsPlan().offeringType(val);
                break;
            case SAVINGS_PLAN_PAYMENT_OPTION:
                builder.awsSavingsPlan().paymentOption(val);
                break;
            case SAVINGS_PLAN_PURCHASE_TERM:
                builder.awsSavingsPlan().purchaseTerm(val);
                break;
            case SAVINGS_PLAN_RECURRING_COMMITMENT_FOR_BILLING_PERIOD:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsSavingsPlan().recurringCommitmentForBillingPeriod(Double.parseDouble(val));
                }
                break;
            case SAVINGS_PLAN_REGION:
                builder.awsSavingsPlan().region(val);
                break;
            case SAVINGS_PLAN_SAVINGS_PLAN_ARN:
                builder.awsSavingsPlan().savingsPlanArn(val);
                break;
            case SAVINGS_PLAN_SAVINGS_PLAN_EFFECTIVE_COST:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsSavingsPlan().savingsPlanEffectiveCost(Double.parseDouble(val));
                }
                break;
            case SAVINGS_PLAN_SAVINGS_PLAN_RATE:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsSavingsPlan().savingsPlanRate(Double.parseDouble(val));
                }
                break;
            case SAVINGS_PLAN_START_TIME:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsSavingsPlan().startTime(DateUtils.toJavaUtilDateFromIso8601String(val));
                }
                break;
            case SAVINGS_PLAN_TOTAL_COMMITMENT_TO_DATE:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsSavingsPlan().totalCommitmentToDate(Double.parseDouble(val));
                }
                break;
            case SAVINGS_PLAN_USED_COMMITMENT:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsSavingsPlan().usedCommitment(Double.parseDouble(val));
                }
                break;
            case COST_CATEGORY_PROJECT:
                builder.awsCostCategory().project(val);
                break;
            case COST_CATEGORY_TEAM:
                builder.awsCostCategory().team(val);
                break;
            case COST_CATEGORY_ENVIRONMENT:
                builder.awsCostCategory().environment(val);
                break;
            case DISCOUNT_BUNDLED_DISCOUNT:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsDiscount().bundledDiscount(Double.parseDouble(val));
                }
                break;
            case DISCOUNT_TOTAL_DISCOUNT:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsDiscount().totalDiscount(Double.parseDouble(val));
                }
                break;
            case RESERVATION_AMORTIZED_UPFRONT_COST_FOR_USAGE:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsReservation().amortizedUpfrontCostForUsage(Double.parseDouble(val));
                }
                break;
            case RESERVATION_AMORTIZED_UPFRONT_FEE_FOR_BILLING_PERIOD:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsReservation().amortizedUpfrontFeeForBillingPeriod(Double.parseDouble(val));
                }
                break;
            case RESERVATION_AVAILABILITY_ZONE:
                builder.awsReservation().availabilityZone(val);
                break;
            case RESERVATION_EFFECTIVE_COST:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsReservation().effectiveCost(Double.parseDouble(val));
                }
                break;
            case RESERVATION_END_TIME:
                builder.awsReservation().endTime(val);
                break;
            case RESERVATION_MODIFICATION_STATUS:
                builder.awsReservation().modificationStatus(val);
                break;
            case RESERVATION_NET_AMORTIZED_UPFRONT_COST_FOR_USAGE:
                builder.awsReservation().netAmortizedUpfrontCostForUsage(val);
                break;
            case RESERVATION_NET_AMORTIZED_UPFRONT_FEE_FOR_BILLING_PERIOD:
                builder.awsReservation().netAmortizedUpfrontFeeForBillingPeriod(val);
                break;
            case RESERVATION_NET_EFFECTIVE_COST:
                builder.awsReservation().netEffectiveCost(val);
                break;
            case RESERVATION_NET_RECURRING_FEE_FOR_USAGE:
                builder.awsReservation().netRecurringFeeForUsage(val);
                break;
            case RESERVATION_NET_UNUSED_AMORTIZED_UPFRONT_FEE_FOR_BILLING_PERIOD:
                builder.awsReservation().netUnusedAmortizedUpfrontFeeForBillingPeriod(val);
                break;
            case RESERVATION_NET_UNUSED_RECURRING_FEE:
                builder.awsReservation().netUnusedRecurringFee(val);
                break;
            case RESERVATION_NET_UPFRONT_VALUE:
                builder.awsReservation().netUpfrontValue(val);
                break;
            case RESERVATION_RECURRING_FEE_FOR_USAGE:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsReservation().recurringFeeForUsage(Double.parseDouble(val));
                }
                break;
            case RESERVATION_START_TIME:
                builder.awsReservation().startTime(val);
                break;
            case RESERVATION_SUBSCRIPTION_ID:
                builder.awsReservation().subscriptionId(val);
                break;
            case RESERVATION_UNUSED_AMORTIZED_UPFRONT_FEE_FOR_BILLING_PERIOD:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsReservation().unusedAmortizedUpfrontFeeForBillingPeriod(Double.parseDouble(val));
                }
                break;
            case RESERVATION_UNUSED_NORMALIZED_UNIT_QUANTITY:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsReservation().unusedNormalizedUnitQuantity(Double.parseDouble(val));
                }
                break;
            case RESERVATION_UNUSED_QUANTITY:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsReservation().unusedQuantity(Double.parseDouble(val));
                }
                break;
            case RESERVATION_UNUSED_RECURRING_FEE:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsReservation().unusedRecurringFee(Double.parseDouble(val));
                }
                break;
            case RESERVATION_UPFRONT_VALUE:
                if (!StringUtils.isEmpty(val)) {
                    builder.awsReservation().upfrontValue(Double.parseDouble(val));
                }
                break;
            case RESERVATION_NORMALIZED_UNITS_PER_RESERVATION:
                builder.awsReservation().normalizedUnitsPerReservation(val);
                break;
            case RESERVATION_NUMBER_OF_RESERVATIONS:
                builder.awsReservation().numberOfReservations(val);
                break;
            case RESERVATION_RESERVATION_ARN:
                builder.awsReservation().reservationARN(val);
                break;
            case RESERVATION_TOTAL_RESERVED_NORMALIZED_UNITS:
                builder.awsReservation().totalReservedNormalizedUnits(val);
                break;
            case RESERVATION_TOTAL_RESERVED_UNITS:
                builder.awsReservation().totalReservedUnits(val);
                break;
            case RESERVATION_UNITS_PER_RESERVATION:
                builder.awsReservation().unitsPerReservation(val);
                break;
        }
    }
}
