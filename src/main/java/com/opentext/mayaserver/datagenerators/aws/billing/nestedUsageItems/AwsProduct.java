package com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AwsProduct {
    // new in 2018
    private String availabilityZone;
    private String eventType;
    private String freeTier;  // from freeQueryTypes
    private String inputMode; // from instanceCapacity.*
    private String mailboxStorage;
    private Double memoryGib;
    private String outputMode; // from physicalCores
    private String supportModes;
    // end new in 2018

    private String productName;
    private String accountAssistance;
    private String alarmType;
    private String architecturalReview;
    private String architectureSupport;
    private String availability; // This is a percentage, ie 99.9%
    private String bestPractices;
    private String bundle;
    private String cacheEngine;
    private String capacitystatus; // Lowercase s is seen in the data - not a typo
    // There is not an actual instance of this data in the spreadsheet - DateTime might be better suited
    private String caseSeverityresponseTimes; // Lowercase r is seen in the data - not a typo
    private String clockSpeed;
    private String contentType;
    private String currentGeneration;
    private String customerServiceAndCommunities;
    private String dataTransfer;
    // Does not appear in the data - might be more reasonable as a double / int
    private String dataTransferQuota;
    private String databaseEdition;
    private String databaseEngine;
    private String dedicatedEbsThroughput;
    private String deploymentOption;
    private String description;
    private String deviceOs;
    private String directconnectlocation; // Lowercase c & l is seen in the data - not a typo
    private String directorySize;
    private String directoryType;
    private String directoryTypeDescription;
    private String disableactivationconfirmationemail; // Lowercase a,c, & e is seen in the data - not a typo
    private String durability;
    private String ebsOptimized;
    private String ecu;
    private String endpointType;
    private String engineCode;
    private String enhancedNetworkingSupported;
    private String executionMode;
    private String feeCode;
    private String feeDescription;
    private String filesystemtype; // Lowercase s & t is seen in the data - not a typo
    private String freeTrial;
    private String freeQueryTypes;
    private Double freeUsageIncluded;
    private String fromLocation;
    private String fromLocationType;
    private String fromRegionCode;
    private String gpu;
    private Double gpuMemory;
    private String group;
    private String groupDescription;
    private String includedServices;
    private String instance;
    private String instanceCapacity10xLarge;
    private String instanceCapacity2xLarge;
    private String instanceCapacity4xLarge;
    private String instanceCapacity8xLarge;
    private String instanceCapacityLarge;
    private String instanceCapacityXlarge;
    private String instanceFamily;
    private String instanceType;
    private String instanceTypeFamily;
    private String integratingApi;
    private String integratingService;
    private String intelAvxAvailable;
    private String intelAvx2Available;
    private String intelTurboAvailable;
    private String launchSupport;
    private String license;
    private String licenseModel;
    private String location;
    private String locationType;
    private String logsDestination;
    private String machineLearningProcess;
    private String maxIopsBurstPerformance;
    private String maxIopsvolume; // Lowercase v is seen in the data - not a typo
    private String maxThroughputvolume; // same as above
    private String maxVolumeSize;
    private String maximumCapacity;
    private String maximumExtendedStorage;
    private String maximumStorageVolume;
    private String memory;
    private String messageDeliveryFrequency;
    private String messageDeliveryOrder;
    private String meterMode;
    private String minVolumeSize;
    private String minimumStorageVolume;
    private String networkPerformance;
    private Double normalizationSizeFactor;
    private String offeringClass;
    private String operatingSystem;
    private String operationsSupport;
    private String origin;
    private String parameterType;
    private String productOperation;
    private String physicalCores;
    private String physicalProcessor;
    private String preInstalledSw;
    private String pricingUnit;
    private String primaryplaceofuse; // Lowercase p,o & u is seen in the data - not a typo
    private String proactiveGuidance;
    private String processorArchitecture;
    private String processorFeatures;
    private String productFamily;
    private String productSchemaDescription;
    private String programmaticCaseManagement;
    private String provisioned;
    private String purchaseOption;
    private String purchaseterm; // Lowercase t is seen in the data - not a typo
    private String queueType;
    private String recipient;
    private String region;
    private String regioncode; // Lowercase c is seen in the data - not a typo
    private String replicationType;
    private String requestDescription;
    private String requestType;
    private String requesttype;
    private String resourceAssessment;
    private String resourceEndpoint;
    private String resourceType;
    private String routingTarget;
    private String routingType;
    private String runningMode;
    private String servicecode; // Lowercase c is seen in the data - not a typo
    private String servicename; // Lowercase n is seen in the data - not a typo
    private String singleOrDualPass;
    private String sku;
    private String softwareIncluded;
    private String softwareType;
    private String standardStorageRetentionIncluded;
    private String storage;
    private String storageClass;
    private String storageDescription;
    private String storageMedia;
    private String storageType;
    private String technicalSupport;
    private String tenancy;
    private String thirdpartySoftwareSupport; // Lowercase p seen in the data - not a typo
    private String tier;
    private String toLocation;
    private String toLocationType;
    private String toRegionCode;
    private String training;
    private String transferType;
    private String transcodingResult;
    private String trialProduct;
    private String upfrontCommitment;
    private String productUsageType;
    private String vcpu;
    private String version;
    private String videoCodec;
    private Double videoFrameRate;
    private String volumeType;
    private String whoCanOpenCases;
    private String withActiveUsers;
    private String workforceType;

    public AwsProduct() {
    }

    public AwsProduct(String availabilityZone, String eventType, String freeTier, String inputMode, String mailboxStorage, Double memoryGib, String outputMode, String supportModes, String productName, String accountAssistance, String alarmType, String architecturalReview, String architectureSupport, String availability, String bestPractices, String bundle, String cacheEngine, String capacitystatus, String caseSeverityresponseTimes, String clockSpeed, String contentType, String currentGeneration, String customerServiceAndCommunities, String dataTransfer, String dataTransferQuota, String databaseEdition, String databaseEngine, String dedicatedEbsThroughput, String deploymentOption, String description, String deviceOs, String directconnectlocation, String directorySize, String directoryType, String directoryTypeDescription, String disableactivationconfirmationemail, String durability, String ebsOptimized, String ecu, String endpointType, String engineCode, String enhancedNetworkingSupported, String executionMode, String feeCode, String feeDescription, String filesystemtype, String freeTrial, String freeQueryTypes, Double freeUsageIncluded, String fromLocation, String fromLocationType, String fromRegionCode, String gpu, Double gpuMemory, String group, String groupDescription, String includedServices, String instance, String instanceCapacity10xLarge, String instanceCapacity2xLarge, String instanceCapacity4xLarge, String instanceCapacity8xLarge, String instanceCapacityLarge, String instanceCapacityXlarge, String instanceFamily, String instanceType, String instanceTypeFamily, String integratingApi, String integratingService, String intelAvxAvailable, String intelAvx2Available, String intelTurboAvailable, String launchSupport, String license, String licenseModel, String location, String locationType, String logsDestination, String machineLearningProcess, String maxIopsBurstPerformance, String maxIopsvolume, String maxThroughputvolume, String maxVolumeSize, String maximumCapacity, String maximumExtendedStorage, String maximumStorageVolume, String memory, String messageDeliveryFrequency, String messageDeliveryOrder, String meterMode, String minVolumeSize, String minimumStorageVolume, String networkPerformance, Double normalizationSizeFactor, String offeringClass, String operatingSystem, String operationsSupport, String origin, String parameterType, String productOperation, String physicalCores, String physicalProcessor, String preInstalledSw, String pricingUnit, String primaryplaceofuse, String proactiveGuidance, String processorArchitecture, String processorFeatures, String productFamily, String productSchemaDescription, String programmaticCaseManagement, String provisioned, String purchaseOption, String purchaseterm, String queueType, String recipient, String region, String regioncode, String replicationType, String requestDescription, String requestType, String requesttype, String resourceAssessment, String resourceEndpoint, String resourceType, String routingTarget, String routingType, String runningMode, String servicecode, String servicename, String singleOrDualPass, String sku, String softwareIncluded, String softwareType, String standardStorageRetentionIncluded, String storage, String storageClass, String storageDescription, String storageMedia, String storageType, String technicalSupport, String tenancy, String thirdpartySoftwareSupport, String tier, String toLocation, String toLocationType, String toRegionCode, String training, String transferType, String transcodingResult, String trialProduct, String upfrontCommitment, String productUsageType, String vcpu, String version, String videoCodec, Double videoFrameRate, String volumeType, String whoCanOpenCases, String withActiveUsers, String workforceType) {
        this.availabilityZone = availabilityZone;
        this.eventType = eventType;
        this.freeTier = freeTier;
        this.inputMode = inputMode;
        this.mailboxStorage = mailboxStorage;
        this.memoryGib = memoryGib;
        this.outputMode = outputMode;
        this.supportModes = supportModes;
        this.productName = productName;
        this.accountAssistance = accountAssistance;
        this.alarmType = alarmType;
        this.architecturalReview = architecturalReview;
        this.architectureSupport = architectureSupport;
        this.availability = availability;
        this.bestPractices = bestPractices;
        this.bundle = bundle;
        this.cacheEngine = cacheEngine;
        this.capacitystatus = capacitystatus;
        this.caseSeverityresponseTimes = caseSeverityresponseTimes;
        this.clockSpeed = clockSpeed;
        this.contentType = contentType;
        this.currentGeneration = currentGeneration;
        this.customerServiceAndCommunities = customerServiceAndCommunities;
        this.dataTransfer = dataTransfer;
        this.dataTransferQuota = dataTransferQuota;
        this.databaseEdition = databaseEdition;
        this.databaseEngine = databaseEngine;
        this.dedicatedEbsThroughput = dedicatedEbsThroughput;
        this.deploymentOption = deploymentOption;
        this.description = description;
        this.deviceOs = deviceOs;
        this.directconnectlocation = directconnectlocation;
        this.directorySize = directorySize;
        this.directoryType = directoryType;
        this.directoryTypeDescription = directoryTypeDescription;
        this.disableactivationconfirmationemail = disableactivationconfirmationemail;
        this.durability = durability;
        this.ebsOptimized = ebsOptimized;
        this.ecu = ecu;
        this.endpointType = endpointType;
        this.engineCode = engineCode;
        this.enhancedNetworkingSupported = enhancedNetworkingSupported;
        this.executionMode = executionMode;
        this.feeCode = feeCode;
        this.feeDescription = feeDescription;
        this.filesystemtype = filesystemtype;
        this.freeTrial = freeTrial;
        this.freeQueryTypes = freeQueryTypes;
        this.freeUsageIncluded = freeUsageIncluded;
        this.fromLocation = fromLocation;
        this.fromLocationType = fromLocationType;
        this.fromRegionCode = fromRegionCode;
        this.gpu = gpu;
        this.gpuMemory = gpuMemory;
        this.group = group;
        this.groupDescription = groupDescription;
        this.includedServices = includedServices;
        this.instance = instance;
        this.instanceCapacity10xLarge = instanceCapacity10xLarge;
        this.instanceCapacity2xLarge = instanceCapacity2xLarge;
        this.instanceCapacity4xLarge = instanceCapacity4xLarge;
        this.instanceCapacity8xLarge = instanceCapacity8xLarge;
        this.instanceCapacityLarge = instanceCapacityLarge;
        this.instanceCapacityXlarge = instanceCapacityXlarge;
        this.instanceFamily = instanceFamily;
        this.instanceType = instanceType;
        this.instanceTypeFamily = instanceTypeFamily;
        this.integratingApi = integratingApi;
        this.integratingService = integratingService;
        this.intelAvxAvailable = intelAvxAvailable;
        this.intelAvx2Available = intelAvx2Available;
        this.intelTurboAvailable = intelTurboAvailable;
        this.launchSupport = launchSupport;
        this.license = license;
        this.licenseModel = licenseModel;
        this.location = location;
        this.locationType = locationType;
        this.logsDestination = logsDestination;
        this.machineLearningProcess = machineLearningProcess;
        this.maxIopsBurstPerformance = maxIopsBurstPerformance;
        this.maxIopsvolume = maxIopsvolume;
        this.maxThroughputvolume = maxThroughputvolume;
        this.maxVolumeSize = maxVolumeSize;
        this.maximumCapacity = maximumCapacity;
        this.maximumExtendedStorage = maximumExtendedStorage;
        this.maximumStorageVolume = maximumStorageVolume;
        this.memory = memory;
        this.messageDeliveryFrequency = messageDeliveryFrequency;
        this.messageDeliveryOrder = messageDeliveryOrder;
        this.meterMode = meterMode;
        this.minVolumeSize = minVolumeSize;
        this.minimumStorageVolume = minimumStorageVolume;
        this.networkPerformance = networkPerformance;
        this.normalizationSizeFactor = normalizationSizeFactor;
        this.offeringClass = offeringClass;
        this.operatingSystem = operatingSystem;
        this.operationsSupport = operationsSupport;
        this.origin = origin;
        this.parameterType = parameterType;
        this.productOperation = productOperation;
        this.physicalCores = physicalCores;
        this.physicalProcessor = physicalProcessor;
        this.preInstalledSw = preInstalledSw;
        this.pricingUnit = pricingUnit;
        this.primaryplaceofuse = primaryplaceofuse;
        this.proactiveGuidance = proactiveGuidance;
        this.processorArchitecture = processorArchitecture;
        this.processorFeatures = processorFeatures;
        this.productFamily = productFamily;
        this.productSchemaDescription = productSchemaDescription;
        this.programmaticCaseManagement = programmaticCaseManagement;
        this.provisioned = provisioned;
        this.purchaseOption = purchaseOption;
        this.purchaseterm = purchaseterm;
        this.queueType = queueType;
        this.recipient = recipient;
        this.region = region;
        this.regioncode = regioncode;
        this.replicationType = replicationType;
        this.requestDescription = requestDescription;
        this.requestType = requestType;
        this.requesttype = requesttype;
        this.resourceAssessment = resourceAssessment;
        this.resourceEndpoint = resourceEndpoint;
        this.resourceType = resourceType;
        this.routingTarget = routingTarget;
        this.routingType = routingType;
        this.runningMode = runningMode;
        this.servicecode = servicecode;
        this.servicename = servicename;
        this.singleOrDualPass = singleOrDualPass;
        this.sku = sku;
        this.softwareIncluded = softwareIncluded;
        this.softwareType = softwareType;
        this.standardStorageRetentionIncluded = standardStorageRetentionIncluded;
        this.storage = storage;
        this.storageClass = storageClass;
        this.storageDescription = storageDescription;
        this.storageMedia = storageMedia;
        this.storageType = storageType;
        this.technicalSupport = technicalSupport;
        this.tenancy = tenancy;
        this.thirdpartySoftwareSupport = thirdpartySoftwareSupport;
        this.tier = tier;
        this.toLocation = toLocation;
        this.toLocationType = toLocationType;
        this.toRegionCode = toRegionCode;
        this.training = training;
        this.transferType = transferType;
        this.transcodingResult = transcodingResult;
        this.trialProduct = trialProduct;
        this.upfrontCommitment = upfrontCommitment;
        this.productUsageType = productUsageType;
        this.vcpu = vcpu;
        this.version = version;
        this.videoCodec = videoCodec;
        this.videoFrameRate = videoFrameRate;
        this.volumeType = volumeType;
        this.whoCanOpenCases = whoCanOpenCases;
        this.withActiveUsers = withActiveUsers;
        this.workforceType = workforceType;
    }

    @JsonProperty("product_availabilityZone")
    public String getAvailabilityZone() {
        return availabilityZone;
    }

    public String getEventType() {
        return eventType;
    }

    public String getFreeTier() {
        return freeTier;
    }

    public String getInputMode() {
        return inputMode;
    }

    public String getMailboxStorage() {
        return mailboxStorage;
    }

    public Double getMemoryGib() {
        return memoryGib;
    }

    public String getOutputMode() {
        return outputMode;
    }

    public String getSupportModes() {
        return supportModes;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAccountAssistance() {
        return accountAssistance;
    }

    public void setAccountAssistance(String accountAssistance) {
        this.accountAssistance = accountAssistance;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getArchitecturalReview() {
        return architecturalReview;
    }

    public void setArchitecturalReview(String architecturalReview) {
        this.architecturalReview = architecturalReview;
    }

    public String getArchitectureSupport() {
        return architectureSupport;
    }

    public void setArchitectureSupport(String architectureSupport) {
        this.architectureSupport = architectureSupport;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getBestPractices() {
        return bestPractices;
    }

    public void setBestPractices(String bestPractices) {
        this.bestPractices = bestPractices;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getCacheEngine() {
        return cacheEngine;
    }

    public void setCacheEngine(String cacheEngine) {
        this.cacheEngine = cacheEngine;
    }

    public String getCapacitystatus() {
        return capacitystatus;
    }

    public void setCapacitystatus(String capacitystatus) {
        this.capacitystatus = capacitystatus;
    }

    public String getCaseSeverityresponseTimes() {
        return caseSeverityresponseTimes;
    }

    public void setCaseSeverityresponseTimes(String caseSeverityresponseTimes) {
        this.caseSeverityresponseTimes = caseSeverityresponseTimes;
    }

    public String getClockSpeed() {
        return clockSpeed;
    }

    public void setClockSpeed(String clockSpeed) {
        this.clockSpeed = clockSpeed;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCurrentGeneration() {
        return currentGeneration;
    }

    public void setCurrentGeneration(String currentGeneration) {
        this.currentGeneration = currentGeneration;
    }

    public String getCustomerServiceAndCommunities() {
        return customerServiceAndCommunities;
    }

    public void setCustomerServiceAndCommunities(String customerServiceAndCommunities) {
        this.customerServiceAndCommunities = customerServiceAndCommunities;
    }

    public String getDataTransfer() {
        return dataTransfer;
    }

    public void setDataTransfer(String dataTransfer) {
        this.dataTransfer = dataTransfer;
    }

    public String getDataTransferQuota() {
        return dataTransferQuota;
    }

    public void setDataTransferQuota(String dataTransferQuota) {
        this.dataTransferQuota = dataTransferQuota;
    }

    public String getDatabaseEdition() {
        return databaseEdition;
    }

    public void setDatabaseEdition(String databaseEdition) {
        this.databaseEdition = databaseEdition;
    }

    public String getDatabaseEngine() {
        return databaseEngine;
    }

    public void setDatabaseEngine(String databaseEngine) {
        this.databaseEngine = databaseEngine;
    }

    public String getDedicatedEbsThroughput() {
        return dedicatedEbsThroughput;
    }

    public void setDedicatedEbsThroughput(String dedicatedEbsThroughput) {
        this.dedicatedEbsThroughput = dedicatedEbsThroughput;
    }

    public String getDeploymentOption() {
        return deploymentOption;
    }

    public void setDeploymentOption(String deploymentOption) {
        this.deploymentOption = deploymentOption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeviceOs() {
        return deviceOs;
    }

    public void setDeviceOs(String deviceOs) {
        this.deviceOs = deviceOs;
    }

    public String getDirectconnectlocation() {
        return directconnectlocation;
    }

    public void setDirectconnectlocation(String directconnectlocation) {
        this.directconnectlocation = directconnectlocation;
    }

    public String getDirectorySize() {
        return directorySize;
    }

    public void setDirectorySize(String directorySize) {
        this.directorySize = directorySize;
    }

    public String getDirectoryType() {
        return directoryType;
    }

    public void setDirectoryType(String directoryType) {
        this.directoryType = directoryType;
    }

    public String getDirectoryTypeDescription() {
        return directoryTypeDescription;
    }

    public void setDirectoryTypeDescription(String directoryTypeDescription) {
        this.directoryTypeDescription = directoryTypeDescription;
    }

    public String getDisableactivationconfirmationemail() {
        return disableactivationconfirmationemail;
    }

    public void setDisableactivationconfirmationemail(String disableactivationconfirmationemail) {
        this.disableactivationconfirmationemail = disableactivationconfirmationemail;
    }

    public String getDurability() {
        return durability;
    }

    public void setDurability(String durability) {
        this.durability = durability;
    }

    public String getEbsOptimized() {
        return ebsOptimized;
    }

    public void setEbsOptimized(String ebsOptimized) {
        this.ebsOptimized = ebsOptimized;
    }

    public String getEcu() {
        return ecu;
    }

    public void setEcu(String ecu) {
        this.ecu = ecu;
    }

    public String getEndpointType() {
        return endpointType;
    }

    public void setEndpointType(String endpointType) {
        this.endpointType = endpointType;
    }

    public String getEngineCode() {
        return engineCode;
    }

    public void setEngineCode(String engineCode) {
        this.engineCode = engineCode;
    }

    public String getEnhancedNetworkingSupported() {
        return enhancedNetworkingSupported;
    }

    public void setEnhancedNetworkingSupported(String enhancedNetworkingSupported) {
        this.enhancedNetworkingSupported = enhancedNetworkingSupported;
    }

    public String getExecutionMode() {
        return executionMode;
    }

    public void setExecutionMode(String executionMode) {
        this.executionMode = executionMode;
    }

    public String getFeeCode() {
        return feeCode;
    }

    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode;
    }

    public String getFeeDescription() {
        return feeDescription;
    }

    public void setFeeDescription(String feeDescription) {
        this.feeDescription = feeDescription;
    }

    public String getFilesystemtype() {
        return filesystemtype;
    }

    public void setFilesystemtype(String filesystemtype) {
        this.filesystemtype = filesystemtype;
    }

    public String getFreeTrial() {
        return freeTrial;
    }

    public void setFreeTrial(String freeTrial) {
        this.freeTrial = freeTrial;
    }

    public String getFreeQueryTypes() {
        return freeQueryTypes;
    }

    public void setFreeQueryTypes(String freeQueryTypes) {
        this.freeQueryTypes = freeQueryTypes;
    }

    public Double getFreeUsageIncluded() {
        return freeUsageIncluded;
    }

    public void setFreeUsageIncluded(Double freeUsageIncluded) {
        this.freeUsageIncluded = freeUsageIncluded;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getFromLocationType() {
        return fromLocationType;
    }

    public void setFromLocationType(String fromLocationType) {
        this.fromLocationType = fromLocationType;
    }

    public String getFromRegionCode() {
        return fromRegionCode;
    }

    public void setFromRegionCode(String fromRegionCode) {
        this.fromRegionCode = fromRegionCode;
    }

    public String getGpu() {
        return gpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public Double getGpuMemory() {
        return gpuMemory;
    }

    public void setGpuMemory(Double gpuMemory) {
        this.gpuMemory = gpuMemory;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getIncludedServices() {
        return includedServices;
    }

    public void setIncludedServices(String includedServices) {
        this.includedServices = includedServices;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getInstanceCapacity10xLarge() {
        return instanceCapacity10xLarge;
    }

    public void setInstanceCapacity10xLarge(String instanceCapacity10xLarge) {
        this.instanceCapacity10xLarge = instanceCapacity10xLarge;
    }

    public String getInstanceCapacity2xLarge() {
        return instanceCapacity2xLarge;
    }

    public void setInstanceCapacity2xLarge(String instanceCapacity2xLarge) {
        this.instanceCapacity2xLarge = instanceCapacity2xLarge;
    }

    public String getInstanceCapacity4xLarge() {
        return instanceCapacity4xLarge;
    }

    public void setInstanceCapacity4xLarge(String instanceCapacity4xLarge) {
        this.instanceCapacity4xLarge = instanceCapacity4xLarge;
    }

    public String getInstanceCapacity8xLarge() {
        return instanceCapacity8xLarge;
    }

    public void setInstanceCapacity8xLarge(String instanceCapacity8xLarge) {
        this.instanceCapacity8xLarge = instanceCapacity8xLarge;
    }

    public String getInstanceCapacityLarge() {
        return instanceCapacityLarge;
    }

    public void setInstanceCapacityLarge(String instanceCapacityLarge) {
        this.instanceCapacityLarge = instanceCapacityLarge;
    }

    public String getInstanceCapacityXlarge() {
        return instanceCapacityXlarge;
    }

    public void setInstanceCapacityXlarge(String instanceCapacityXlarge) {
        this.instanceCapacityXlarge = instanceCapacityXlarge;
    }

    public String getInstanceFamily() {
        return instanceFamily;
    }

    public void setInstanceFamily(String instanceFamily) {
        this.instanceFamily = instanceFamily;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public String getInstanceTypeFamily() {
        return instanceTypeFamily;
    }

    public void setInstanceTypeFamily(String instanceTypeFamily) {
        this.instanceTypeFamily = instanceTypeFamily;
    }

    public String getIntegratingApi() {
        return integratingApi;
    }

    public void setIntegratingApi(String integratingApi) {
        this.integratingApi = integratingApi;
    }

    public String getIntegratingService() {
        return integratingService;
    }

    public void setIntegratingService(String integratingService) {
        this.integratingService = integratingService;
    }

    public String getIntelAvxAvailable() {
        return intelAvxAvailable;
    }

    public void setIntelAvxAvailable(String intelAvxAvailable) {
        this.intelAvxAvailable = intelAvxAvailable;
    }

    public String getIntelAvx2Available() {
        return intelAvx2Available;
    }

    public void setIntelAvx2Available(String intelAvx2Available) {
        this.intelAvx2Available = intelAvx2Available;
    }

    public String getIntelTurboAvailable() {
        return intelTurboAvailable;
    }

    public void setIntelTurboAvailable(String intelTurboAvailable) {
        this.intelTurboAvailable = intelTurboAvailable;
    }

    public String getLaunchSupport() {
        return launchSupport;
    }

    public void setLaunchSupport(String launchSupport) {
        this.launchSupport = launchSupport;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicenseModel() {
        return licenseModel;
    }

    public void setLicenseModel(String licenseModel) {
        this.licenseModel = licenseModel;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getLogsDestination() {
        return logsDestination;
    }

    public void setLogsDestination(String logsDestination) {
        this.logsDestination = logsDestination;
    }

    public String getMachineLearningProcess() {
        return machineLearningProcess;
    }

    public void setMachineLearningProcess(String machineLearningProcess) {
        this.machineLearningProcess = machineLearningProcess;
    }

    public String getMaxIopsBurstPerformance() {
        return maxIopsBurstPerformance;
    }

    public void setMaxIopsBurstPerformance(String maxIopsBurstPerformance) {
        this.maxIopsBurstPerformance = maxIopsBurstPerformance;
    }

    public String getMaxIopsvolume() {
        return maxIopsvolume;
    }

    public void setMaxIopsvolume(String maxIopsvolume) {
        this.maxIopsvolume = maxIopsvolume;
    }

    public String getMaxThroughputvolume() {
        return maxThroughputvolume;
    }

    public void setMaxThroughputvolume(String maxThroughputvolume) {
        this.maxThroughputvolume = maxThroughputvolume;
    }

    public String getMaxVolumeSize() {
        return maxVolumeSize;
    }

    public void setMaxVolumeSize(String maxVolumeSize) {
        this.maxVolumeSize = maxVolumeSize;
    }

    public String getMaximumCapacity() {
        return maximumCapacity;
    }

    public void setMaximumCapacity(String maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }

    public String getMaximumExtendedStorage() {
        return maximumExtendedStorage;
    }

    public void setMaximumExtendedStorage(String maximumExtendedStorage) {
        this.maximumExtendedStorage = maximumExtendedStorage;
    }

    public String getMaximumStorageVolume() {
        return maximumStorageVolume;
    }

    public void setMaximumStorageVolume(String maximumStorageVolume) {
        this.maximumStorageVolume = maximumStorageVolume;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getMessageDeliveryFrequency() {
        return messageDeliveryFrequency;
    }

    public void setMessageDeliveryFrequency(String messageDeliveryFrequency) {
        this.messageDeliveryFrequency = messageDeliveryFrequency;
    }

    public String getMessageDeliveryOrder() {
        return messageDeliveryOrder;
    }

    public void setMessageDeliveryOrder(String messageDeliveryOrder) {
        this.messageDeliveryOrder = messageDeliveryOrder;
    }

    public String getMeterMode() {
        return meterMode;
    }

    public void setMeterMode(String meterMode) {
        this.meterMode = meterMode;
    }

    public String getMinVolumeSize() {
        return minVolumeSize;
    }

    public void setMinVolumeSize(String minVolumeSize) {
        this.minVolumeSize = minVolumeSize;
    }

    public String getMinimumStorageVolume() {
        return minimumStorageVolume;
    }

    public void setMinimumStorageVolume(String minimumStorageVolume) {
        this.minimumStorageVolume = minimumStorageVolume;
    }

    public String getNetworkPerformance() {
        return networkPerformance;
    }

    public void setNetworkPerformance(String networkPerformance) {
        this.networkPerformance = networkPerformance;
    }

    public Double getNormalizationSizeFactor() {
        return normalizationSizeFactor;
    }

    public void setNormalizationSizeFactor(Double normalizationSizeFactor) {
        this.normalizationSizeFactor = normalizationSizeFactor;
    }

    @JsonProperty("product_offeringClass")
    public String getOfferingClass() {
        return offeringClass;
    }

    public void setOfferingClass(String offeringClass) {
        this.offeringClass = offeringClass;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getOperationsSupport() {
        return operationsSupport;
    }

    public void setOperationsSupport(String operationsSupport) {
        this.operationsSupport = operationsSupport;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getProductOperation() {
        return productOperation;
    }

    public void setProductOperation(String productOperation) {
        this.productOperation = productOperation;
    }

    public String getPhysicalCores() {
        return physicalCores;
    }

    public void setPhysicalCores(String physicalCores) {
        this.physicalCores = physicalCores;
    }

    public String getPhysicalProcessor() {
        return physicalProcessor;
    }

    public void setPhysicalProcessor(String physicalProcessor) {
        this.physicalProcessor = physicalProcessor;
    }

    public String getPreInstalledSw() {
        return preInstalledSw;
    }

    public void setPreInstalledSw(String preInstalledSw) {
        this.preInstalledSw = preInstalledSw;
    }

    public String getPricingUnit() {
        return pricingUnit;
    }

    public void setPricingUnit(String pricingUnit) {
        this.pricingUnit = pricingUnit;
    }

    public String getPrimaryplaceofuse() {
        return primaryplaceofuse;
    }

    public void setPrimaryplaceofuse(String primaryplaceofuse) {
        this.primaryplaceofuse = primaryplaceofuse;
    }

    public String getProactiveGuidance() {
        return proactiveGuidance;
    }

    public void setProactiveGuidance(String proactiveGuidance) {
        this.proactiveGuidance = proactiveGuidance;
    }

    public String getProcessorArchitecture() {
        return processorArchitecture;
    }

    public void setProcessorArchitecture(String processorArchitecture) {
        this.processorArchitecture = processorArchitecture;
    }

    public String getProcessorFeatures() {
        return processorFeatures;
    }

    public void setProcessorFeatures(String processorFeatures) {
        this.processorFeatures = processorFeatures;
    }

    public String getProductFamily() {
        return productFamily;
    }

    public void setProductFamily(String productFamily) {
        this.productFamily = productFamily;
    }

    public String getProductSchemaDescription() {
        return productSchemaDescription;
    }

    public void setProductSchemaDescription(String productSchemaDescription) {
        this.productSchemaDescription = productSchemaDescription;
    }

    public String getProgrammaticCaseManagement() {
        return programmaticCaseManagement;
    }

    public void setProgrammaticCaseManagement(String programmaticCaseManagement) {
        this.programmaticCaseManagement = programmaticCaseManagement;
    }

    public String getProvisioned() {
        return provisioned;
    }

    public void setProvisioned(String provisioned) {
        this.provisioned = provisioned;
    }

    @JsonProperty("product_purchaseOption")
    public String getPurchaseOption() {
        return purchaseOption;
    }

    public void setPurchaseOption(String purchaseOption) {
        this.purchaseOption = purchaseOption;
    }

    public String getPurchaseterm() {
        return purchaseterm;
    }

    public void setPurchaseterm(String purchaseterm) {
        this.purchaseterm = purchaseterm;
    }

    public String getQueueType() {
        return queueType;
    }

    public void setQueueType(String queueType) {
        this.queueType = queueType;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegioncode() {
        return regioncode;
    }

    public void setRegioncode(String regioncode) {
        this.regioncode = regioncode;
    }

    public String getReplicationType() {
        return replicationType;
    }

    public void setReplicationType(String replicationType) {
        this.replicationType = replicationType;
    }

    public String getRequestDescription() {
        return requestDescription;
    }

    public void setRequestDescription(String requestDescription) {
        this.requestDescription = requestDescription;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getResourceAssessment() {
        return resourceAssessment;
    }

    public void setResourceAssessment(String resourceAssessment) {
        this.resourceAssessment = resourceAssessment;
    }

    @JsonProperty("requesttype2")
    public String getRequesttype() {
        return requesttype;
    }

    public void setRequesttype(String requesttype) {
        this.requesttype = requesttype;
    }

    public String getResourceEndpoint() {
        return resourceEndpoint;
    }

    public void setResourceEndpoint(String resourceEndpoint) {
        this.resourceEndpoint = resourceEndpoint;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getRoutingTarget() {
        return routingTarget;
    }

    public void setRoutingTarget(String routingTarget) {
        this.routingTarget = routingTarget;
    }

    public String getRoutingType() {
        return routingType;
    }

    public void setRoutingType(String routingType) {
        this.routingType = routingType;
    }

    public String getRunningMode() {
        return runningMode;
    }

    public void setRunningMode(String runningMode) {
        this.runningMode = runningMode;
    }

    public String getServicecode() {
        return servicecode;
    }

    public void setServicecode(String servicecode) {
        this.servicecode = servicecode;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getSingleOrDualPass() {
        return singleOrDualPass;
    }

    public void setSingleOrDualPass(String singleOrDualPass) {
        this.singleOrDualPass = singleOrDualPass;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSoftwareIncluded() {
        return softwareIncluded;
    }

    public void setSoftwareIncluded(String softwareIncluded) {
        this.softwareIncluded = softwareIncluded;
    }

    public String getSoftwareType() {
        return softwareType;
    }

    public void setSoftwareType(String softwareType) {
        this.softwareType = softwareType;
    }

    public String getStandardStorageRetentionIncluded() {
        return standardStorageRetentionIncluded;
    }

    public void setStandardStorageRetentionIncluded(String standardStorageRetentionIncluded) {
        this.standardStorageRetentionIncluded = standardStorageRetentionIncluded;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getStorageClass() {
        return storageClass;
    }

    public void setStorageClass(String storageClass) {
        this.storageClass = storageClass;
    }

    public String getStorageDescription() {
        return storageDescription;
    }

    public void setStorageDescription(String storageDescription) {
        this.storageDescription = storageDescription;
    }

    public String getStorageMedia() {
        return storageMedia;
    }

    public void setStorageMedia(String storageMedia) {
        this.storageMedia = storageMedia;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getTechnicalSupport() {
        return technicalSupport;
    }

    public void setTechnicalSupport(String technicalSupport) {
        this.technicalSupport = technicalSupport;
    }

    public String getTenancy() {
        return tenancy;
    }

    public void setTenancy(String tenancy) {
        this.tenancy = tenancy;
    }

    public String getThirdpartySoftwareSupport() {
        return thirdpartySoftwareSupport;
    }

    public void setThirdpartySoftwareSupport(String thirdpartySoftwareSupport) {
        this.thirdpartySoftwareSupport = thirdpartySoftwareSupport;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getToLocationType() {
        return toLocationType;
    }

    public void setToLocationType(String toLocationType) {
        this.toLocationType = toLocationType;
    }

    public String getToRegionCode() {
        return toRegionCode;
    }

    public void setToRegionCode(String toRegionCode) {
        this.toRegionCode = toRegionCode;
    }

    public String getTraining() {
        return training;
    }

    public void setTraining(String training) {
        this.training = training;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTranscodingResult() {
        return transcodingResult;
    }

    public void setTranscodingResult(String transcodingResult) {
        this.transcodingResult = transcodingResult;
    }

    public String getTrialProduct() {
        return trialProduct;
    }

    public void setTrialProduct(String trialProduct) {
        this.trialProduct = trialProduct;
    }

    public String getUpfrontCommitment() {
        return upfrontCommitment;
    }

    public void setUpfrontCommitment(String upfrontCommitment) {
        this.upfrontCommitment = upfrontCommitment;
    }

    public String getProductUsageType() {
        return productUsageType;
    }

    public void setProductUsageType(String productUsageType) {
        this.productUsageType = productUsageType;
    }

    public String getVcpu() {
        return vcpu;
    }

    public void setVcpu(String vcpu) {
        this.vcpu = vcpu;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVideoCodec() {
        return videoCodec;
    }

    public void setVideoCodec(String videoCodec) {
        this.videoCodec = videoCodec;
    }

    public Double getVideoFrameRate() {
        return videoFrameRate;
    }

    public void setVideoFrameRate(Double videoFrameRate) {
        this.videoFrameRate = videoFrameRate;
    }

    public String getVolumeType() {
        return volumeType;
    }

    public void setVolumeType(String volumeType) {
        this.volumeType = volumeType;
    }

    public String getWhoCanOpenCases() {
        return whoCanOpenCases;
    }

    public void setWhoCanOpenCases(String whoCanOpenCases) {
        this.whoCanOpenCases = whoCanOpenCases;
    }

    public String getWithActiveUsers() {
        return withActiveUsers;
    }

    public void setWithActiveUsers(String withActiveUsers) {
        this.withActiveUsers = withActiveUsers;
    }

    public String getWorkforceType() {
        return workforceType;
    }

    public void setWorkforceType(String workforceType) {
        this.workforceType = workforceType;
    }

    @Override
    public String toString() {
        return "AwsProduct{" +
                "productName='" + productName + '\'' +
                ", accountAssistance='" + accountAssistance + '\'' +
                ", alarmType='" + alarmType + '\'' +
                ", architecturalReview='" + architecturalReview + '\'' +
                ", architectureSupport='" + architectureSupport + '\'' +
                ", availability='" + availability + '\'' +
                ", bestPractices='" + bestPractices + '\'' +
                ", bundle='" + bundle + '\'' +
                ", cacheEngine='" + cacheEngine + '\'' +
                ", caseSeverityresponseTimes='" + caseSeverityresponseTimes + '\'' +
                ", clockSpeed='" + clockSpeed + '\'' +
                ", contentType='" + contentType + '\'' +
                ", currentGeneration='" + currentGeneration + '\'' +
                ", customerServiceAndCommunities='" + customerServiceAndCommunities + '\'' +
                ", dataTransferQuota='" + dataTransferQuota + '\'' +
                ", databaseEdition='" + databaseEdition + '\'' +
                ", databaseEngine='" + databaseEngine + '\'' +
                ", dedicatedEbsThroughput='" + dedicatedEbsThroughput + '\'' +
                ", deploymentOption='" + deploymentOption + '\'' +
                ", description='" + description + '\'' +
                ", deviceOs='" + deviceOs + '\'' +
                ", directorySize='" + directorySize + '\'' +
                ", directoryType='" + directoryType + '\'' +
                ", directoryTypeDescription='" + directoryTypeDescription + '\'' +
                ", durability='" + durability + '\'' +
                ", ebsOptimized='" + ebsOptimized + '\'' +
                ", ecu='" + ecu + '\'' +
                ", endpointType='" + endpointType + '\'' +
                ", engineCode='" + engineCode + '\'' +
                ", enhancedNetworkingSupported='" + enhancedNetworkingSupported + '\'' +
                ", executionMode='" + executionMode + '\'' +
                ", feeCode='" + feeCode + '\'' +
                ", feeDescription='" + feeDescription + '\'' +
                ", freeTrial='" + freeTrial + '\'' +
                ", freeQueryTypes='" + freeQueryTypes + '\'' +
                ", fromLocation='" + fromLocation + '\'' +
                ", fromLocationType='" + fromLocationType + '\'' +
                ", gpu='" + gpu + '\'' +
                ", group='" + group + '\'' +
                ", groupDescription='" + groupDescription + '\'' +
                ", includedServices='" + includedServices + '\'' +
                ", instance='" + instance + '\'' +
                ", instanceCapacity10xLarge='" + instanceCapacity10xLarge + '\'' +
                ", instanceCapacity2xLarge='" + instanceCapacity2xLarge + '\'' +
                ", instanceCapacity4xLarge='" + instanceCapacity4xLarge + '\'' +
                ", instanceCapacity8xLarge='" + instanceCapacity8xLarge + '\'' +
                ", instanceCapacityLarge='" + instanceCapacityLarge + '\'' +
                ", instanceCapacityXlarge='" + instanceCapacityXlarge + '\'' +
                ", instanceFamily='" + instanceFamily + '\'' +
                ", instanceType='" + instanceType + '\'' +
                ", instanceTypeFamily='" + instanceTypeFamily + '\'' +
                ", launchSupport='" + launchSupport + '\'' +
                ", license='" + license + '\'' +
                ", licenseModel='" + licenseModel + '\'' +
                ", location='" + location + '\'' +
                ", locationType='" + locationType + '\'' +
                ", machineLearningProcess='" + machineLearningProcess + '\'' +
                ", maxIopsBurstPerformance='" + maxIopsBurstPerformance + '\'' +
                ", maxIopsvolume='" + maxIopsvolume + '\'' +
                ", maxThroughputvolume='" + maxThroughputvolume + '\'' +
                ", maxVolumeSize='" + maxVolumeSize + '\'' +
                ", maximumCapacity='" + maximumCapacity + '\'' +
                ", maximumExtendedStorage='" + maximumExtendedStorage + '\'' +
                ", maximumStorageVolume='" + maximumStorageVolume + '\'' +
                ", memory='" + memory + '\'' +
                ", messageDeliveryFrequency='" + messageDeliveryFrequency + '\'' +
                ", messageDeliveryOrder='" + messageDeliveryOrder + '\'' +
                ", meterMode='" + meterMode + '\'' +
                ", minVolumeSize='" + minVolumeSize + '\'' +
                ", minimumStorageVolume='" + minimumStorageVolume + '\'' +
                ", networkPerformance='" + networkPerformance + '\'' +
                ", normalizationSizeFactor=" + normalizationSizeFactor +
                ", operatingSystem='" + operatingSystem + '\'' +
                ", operationsSupport='" + operationsSupport + '\'' +
                ", origin='" + origin + '\'' +
                ", productOperation='" + productOperation + '\'' +
                ", physicalCores='" + physicalCores + '\'' +
                ", physicalProcessor='" + physicalProcessor + '\'' +
                ", preInstalledSw='" + preInstalledSw + '\'' +
                ", proactiveGuidance='" + proactiveGuidance + '\'' +
                ", processorArchitecture='" + processorArchitecture + '\'' +
                ", processorFeatures='" + processorFeatures + '\'' +
                ", productFamily='" + productFamily + '\'' +
                ", programmaticCaseManagement='" + programmaticCaseManagement + '\'' +
                ", provisioned='" + provisioned + '\'' +
                ", queueType='" + queueType + '\'' +
                ", recipient='" + recipient + '\'' +
                ", region='" + region + '\'' +
                ", requestDescription='" + requestDescription + '\'' +
                ", requestType='" + requestType + '\'' +
                ", requesttype='" + requesttype + '\'' +
                ", resourceEndpoint='" + resourceEndpoint + '\'' +
                ", resourceType='" + resourceType + '\'' +
                ", routingTarget='" + routingTarget + '\'' +
                ", routingType='" + routingType + '\'' +
                ", runningMode='" + runningMode + '\'' +
                ", servicecode='" + servicecode + '\'' +
                ", servicename='" + servicename + '\'' +
                ", sku='" + sku + '\'' +
                ", softwareIncluded='" + softwareIncluded + '\'' +
                ", softwareType='" + softwareType + '\'' +
                ", standardStorageRetentionIncluded='" + standardStorageRetentionIncluded + '\'' +
                ", storage='" + storage + '\'' +
                ", storageClass='" + storageClass + '\'' +
                ", storageDescription='" + storageDescription + '\'' +
                ", storageMedia='" + storageMedia + '\'' +
                ", storageType='" + storageType + '\'' +
                ", technicalSupport='" + technicalSupport + '\'' +
                ", tenancy='" + tenancy + '\'' +
                ", thirdpartySoftwareSupport='" + thirdpartySoftwareSupport + '\'' +
                ", toLocation='" + toLocation + '\'' +
                ", toLocationType='" + toLocationType + '\'' +
                ", training='" + training + '\'' +
                ", transferType='" + transferType + '\'' +
                ", productUsageType='" + productUsageType + '\'' +
                ", vcpu='" + vcpu + '\'' +
                ", version='" + version + '\'' +
                ", volumeType='" + volumeType + '\'' +
                ", whoCanOpenCases='" + whoCanOpenCases + '\'' +
                ", withActiveUsers='" + withActiveUsers + '\'' +
                '}';
    }
}
