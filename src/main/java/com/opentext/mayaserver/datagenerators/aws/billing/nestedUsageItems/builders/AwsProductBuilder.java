package com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.builders;

import com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.AwsProduct;

public class AwsProductBuilder<T> {
    private AwsSink<T> sink;

    private String availabilityZone;
    private String eventType;
    private String freeTier;
    private String inputMode;
    private String mailboxStorage;
    private Double memoryGib;
    private String outputMode;
    private String supportModes;

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

    public AwsProductBuilder(AwsSink<T> sink) {
        this.sink = sink;
    }

    public AwsProductBuilder<T> availabilityZone(String availabilityZone) {
        this.availabilityZone = availabilityZone;
        return this;
    }

    public AwsProductBuilder<T> eventType(String eventType) {
        this.eventType = eventType;
        return this;
    }

    public AwsProductBuilder<T> freeTier(String freeTier) {
        this.freeTier = freeTier;
        return this;
    }

    public AwsProductBuilder<T> inputMode(String inputMode) {
        this.inputMode = inputMode;
        return this;
    }

    public AwsProductBuilder<T> mailboxStorage(String mailboxStorage) {
        this.mailboxStorage = mailboxStorage;
        return this;
    }

    public AwsProductBuilder<T> memoryGib(Double memoryGib) {
        this.memoryGib = memoryGib;
        return this;
    }

    public AwsProductBuilder<T> outputMode(String outputMode) {
        this.outputMode = outputMode;
        return this;
    }

    public AwsProductBuilder<T> supportModes(String supportModes) {
        this.supportModes = supportModes;
        return this;
    }

    public AwsProductBuilder<T> productName(String productName) {
        this.productName = productName;
        return this;
    }

    public AwsProductBuilder<T> accountAssistance(String accountAssistance) {
        this.accountAssistance = accountAssistance;
        return this;
    }

    public AwsProductBuilder<T> alarmType(String alarmType) {
        this.alarmType = alarmType;
        return this;
    }

    public AwsProductBuilder<T> architecturalReview(String architecturalReview) {
        this.architecturalReview = architecturalReview;
        return this;
    }

    public AwsProductBuilder<T> architecturalSupport(String architectureSupport) {
        this.architectureSupport = architectureSupport;
        return this;
    }

    public AwsProductBuilder<T> availability(String availability) {
        this.availability = availability;
        return this;
    }

    public AwsProductBuilder<T> bestPractices(String bestPractices) {
        this.bestPractices = bestPractices;
        return this;
    }

    public AwsProductBuilder<T> bundle(String bundle) {
        this.bundle = bundle;
        return this;
    }

    public AwsProductBuilder<T> cacheEngine(String cacheEngine) {
        this.cacheEngine = cacheEngine;
        return this;
    }

    public AwsProductBuilder<T> capacitystatus(String capacitystatus) {
        this.capacitystatus = capacitystatus;
        return this;
    }

    public AwsProductBuilder<T> caseSeverityresponseTimes(String caseSeverityresponseTimes) {
        this.caseSeverityresponseTimes = caseSeverityresponseTimes;
        return this;
    }

    public AwsProductBuilder<T> clockSpeed(String clockSpeed) {
        this.clockSpeed = clockSpeed;
        return this;
    }

    public AwsProductBuilder<T> contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public AwsProductBuilder<T> currentGeneration(String currentGeneration) {
        this.currentGeneration = currentGeneration;
        return this;
    }

    public AwsProductBuilder<T> customerServiceAndCommunities(String customerServiceAndCommunities) {
        this.customerServiceAndCommunities = customerServiceAndCommunities;
        return this;
    }

    public AwsProductBuilder<T> dataTransfer(String dataTransfer) {
        this.dataTransfer = dataTransfer;
        return this;
    }

    public AwsProductBuilder<T> dataTransferQuota(String dataTransferQuota) {
        this.dataTransferQuota = dataTransferQuota;
        return this;
    }

    public AwsProductBuilder<T> databaseEdition(String databaseEdition) {
        this.databaseEdition = databaseEdition;
        return this;
    }

    public AwsProductBuilder<T> databaseEngine(String databaseEngine) {
        this.databaseEngine = databaseEngine;
        return this;
    }

    public AwsProductBuilder<T> dedicatedEbsThroughput(String dedicatedEbsThroughput) {
        this.dedicatedEbsThroughput = dedicatedEbsThroughput;
        return this;
    }

    public AwsProductBuilder<T> deploymentOption(String deploymentOption) {
        this.deploymentOption = deploymentOption;
        return this;
    }

    public AwsProductBuilder<T> description(String description) {
        this.description = description;
        return this;
    }

    public AwsProductBuilder<T> directorySize(String directorySize) {
        this.directorySize = directorySize;
        return this;
    }

    public AwsProductBuilder<T> directoryType(String directoryType) {
        this.directoryType = directoryType;
        return this;
    }

    public AwsProductBuilder<T> directoryTypeDescription(String directoryTypeDescription) {
        this.directoryTypeDescription = directoryTypeDescription;
        return this;
    }

    public AwsProductBuilder<T> disableactivationconfirmationemail(String disableactivationconfirmationemail) {
        this.disableactivationconfirmationemail = disableactivationconfirmationemail;
        return this;
    }

    public AwsProductBuilder<T> durability(String durability) {
        this.durability = durability;
        return this;
    }

    public AwsProductBuilder<T> ebsOptimized(String ebsOptimized) {
        this.ebsOptimized = ebsOptimized;
        return this;
    }

    public AwsProductBuilder<T> ecu(String ecu) {
        this.ecu = ecu;
        return this;
    }

    public AwsProductBuilder<T> endpointType(String endpointType) {
        this.endpointType = endpointType;
        return this;
    }

    public AwsProductBuilder<T> engineCode(String engineCode) {
        this.engineCode = engineCode;
        return this;
    }

    public AwsProductBuilder<T> enhancedNetworkingSupported(String enhancedNetworkingSupported) {
        this.enhancedNetworkingSupported = enhancedNetworkingSupported;
        return this;
    }

    public AwsProductBuilder<T> feeCode(String feeCode) {
        this.feeCode = feeCode;
        return this;
    }

    public AwsProductBuilder<T> feeDescription(String feeDescription) {
        this.feeDescription = feeDescription;
        return this;
    }

    public AwsProductBuilder<T> filesystemtype(String filesystemtype) {
        this.filesystemtype = filesystemtype;
        return this;
    }

    public AwsProductBuilder<T> freeTrial(String freeTrial) {
        this.freeTrial = freeTrial;
        return this;
    }

    public AwsProductBuilder<T> fromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
        return this;
    }

    public AwsProductBuilder<T> fromLocationType(String fromLocationType) {
        this.fromLocationType = fromLocationType;
        return this;
    }

    public AwsProductBuilder<T> fromRegionCode(String fromRegionCode) {
        this.fromRegionCode = fromRegionCode;
        return this;
    }

    public AwsProductBuilder<T> gpu(String gpu) {
        this.gpu = gpu;
        return this;
    }

    public AwsProductBuilder<T> gpuMemory(Double gpuMemory) {
        this.gpuMemory = gpuMemory;
        return this;
    }

    public AwsProductBuilder<T> group(String group) {
        this.group = group;
        return this;
    }

    public AwsProductBuilder<T> groupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
        return this;
    }

    public AwsProductBuilder<T> includedServices(String includedServices) {
        this.includedServices = includedServices;
        return this;
    }

    public AwsProductBuilder<T> instanceFamily(String instanceFamily) {
        this.instanceFamily = instanceFamily;
        return this;
    }

    public AwsProductBuilder<T> instanceType(String instanceType) {
        this.instanceType = instanceType;
        return this;
    }

    public AwsProductBuilder<T> instanceTypeFamily(String instanceTypeFamily) {
        this.instanceTypeFamily = instanceTypeFamily;
        return this;
    }

    public AwsProductBuilder<T> integratingApi(String integratingApi) {
        this.integratingApi = integratingApi;
        return this;
    }

    public AwsProductBuilder<T> integratingService(String integratingService) {
        this.integratingService = integratingService;
        return this;
    }

    public AwsProductBuilder<T> intelAvxAvailable(String intelAvxAvailable) {
        this.intelAvxAvailable = intelAvxAvailable;
        return this;
    }

    public AwsProductBuilder<T> intelAvx2Available(String intelAvx2Available) {
        this.intelAvx2Available = intelAvx2Available;
        return this;
    }

    public AwsProductBuilder<T> intelTurboAvailable(String intelTurboAvailable) {
        this.intelTurboAvailable = intelTurboAvailable;
        return this;
    }

    public AwsProductBuilder<T> launchSupport(String launchSupport) {
        this.launchSupport = launchSupport;
        return this;
    }

    public AwsProductBuilder<T> license(String license) {
        this.license = license;
        return this;
    }

    public AwsProductBuilder<T> licenseModel(String licenseModel) {
        this.licenseModel = licenseModel;
        return this;
    }

    public AwsProductBuilder<T> location(String location) {
        this.location = location;
        return this;
    }

    public AwsProductBuilder<T> locationType(String locationType) {
        this.locationType = locationType;
        return this;
    }

    public AwsProductBuilder<T> logsDestination(String logsDestination) {
        this.logsDestination = logsDestination;
        return this;
    }

    public AwsProductBuilder<T> machineLearningProcess(String machineLearningProcess) {
        this.machineLearningProcess = machineLearningProcess;
        return this;
    }

    public AwsProductBuilder<T> maxIopsBurstPerformance(String maxIopsBurstPerformance) {
        this.maxIopsBurstPerformance = maxIopsBurstPerformance;
        return this;
    }

    public AwsProductBuilder<T> maxIopsvolume(String maxIopsvolume) {
        this.maxIopsvolume = maxIopsvolume;
        return this;
    }

    public AwsProductBuilder<T> maxThroughputvolume(String maxThroughputvolume) {
        this.maxThroughputvolume = maxThroughputvolume;
        return this;
    }

    public AwsProductBuilder<T> maxVolumeSize(String maxVolumeSize) {
        this.maxVolumeSize = maxVolumeSize;
        return this;
    }

    public AwsProductBuilder<T> maximumExtendedStorage(String maximumExtendedStorage) {
        this.maximumExtendedStorage = maximumExtendedStorage;
        return this;
    }

    public AwsProductBuilder<T> maximumStorageVolume(String maximumStorageVolume) {
        this.maximumStorageVolume = maximumStorageVolume;
        return this;
    }

    public AwsProductBuilder<T> memory(String memory) {
        this.memory = memory;
        return this;
    }

    public AwsProductBuilder<T> messageDeliveryFrequency(String messageDeliveryFrequency) {
        this.messageDeliveryFrequency = messageDeliveryFrequency;
        return this;
    }

    public AwsProductBuilder<T> messageDeliveryOrder(String messageDeliveryOrder) {
        this.messageDeliveryOrder = messageDeliveryOrder;
        return this;
    }

    public AwsProductBuilder<T> minVolumeSize(String minVolumeSize) {
        this.minVolumeSize = minVolumeSize;
        return this;
    }

    public AwsProductBuilder<T> minimumStorageVolume(String minimumStorageVolume) {
        this.minimumStorageVolume = minimumStorageVolume;
        return this;
    }

    public AwsProductBuilder<T> networkPerformance(String networkPerformance) {
        this.networkPerformance = networkPerformance;
        return this;
    }

    public AwsProductBuilder<T> normalizationSizeFactor(Double normalizationSizeFactor) {
        this.normalizationSizeFactor = normalizationSizeFactor;
        return this;
    }

    public AwsProductBuilder<T> offeringClass(String offeringClass) {
        this.offeringClass = offeringClass;
        return this;
    }

    public AwsProductBuilder<T> operatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
        return this;
    }

    public AwsProductBuilder<T> productOperation(String operation) {
        this.productOperation = operation;
        return this;
    }

    public AwsProductBuilder<T> operationsSupport(String operationsSupport) {
        this.operationsSupport = operationsSupport;
        return this;
    }

    public AwsProductBuilder<T> physicalProcessor(String physicalProcessor) {
        this.physicalProcessor = physicalProcessor;
        return this;
    }

    public AwsProductBuilder<T> preInstalledSw(String preInstalledSw) {
        this.preInstalledSw = preInstalledSw;
        return this;
    }

    public AwsProductBuilder<T> pricingUnit(String pricingUnit) {
        this.pricingUnit = pricingUnit;
        return this;
    }

    public AwsProductBuilder<T> primaryplaceofuse(String primaryplaceofuse) {
        this.primaryplaceofuse = primaryplaceofuse;
        return this;
    }

    public AwsProductBuilder<T> proactiveGuidance(String proactiveGuidance) {
        this.proactiveGuidance = proactiveGuidance;
        return this;
    }

    public AwsProductBuilder<T> processorArchitecture(String processorArchitecture) {
        this.processorArchitecture = processorArchitecture;
        return this;
    }

    public AwsProductBuilder<T> processorFeatures(String processorFeatures) {
        this.processorFeatures = processorFeatures;
        return this;
    }

    public AwsProductBuilder<T> productFamily(String productFamily) {
        this.productFamily = productFamily;
        return this;
    }

    public AwsProductBuilder<T> productSchemaDescription(String productSchemaDescription) {
        this.productSchemaDescription = productSchemaDescription;
        return this;
    }

    public AwsProductBuilder<T> programmaticCaseManagement(String programmaticCaseManagement) {
        this.programmaticCaseManagement = programmaticCaseManagement;
        return this;
    }

    public AwsProductBuilder<T> provisioned(String provisioned) {
        this.provisioned = provisioned;
        return this;
    }

    public AwsProductBuilder<T> purchaseOption(String purchaseOption) {
        this.purchaseOption = purchaseOption;
        return this;
    }

    public AwsProductBuilder<T> purchaseterm(String purchaseterm) {
        this.purchaseterm = purchaseterm;
        return this;
    }

    public AwsProductBuilder<T> queueType(String queueType) {
        this.queueType = queueType;
        return this;
    }

    public AwsProductBuilder<T> region(String region) {
        this.region = region;
        return this;
    }

    public AwsProductBuilder<T> regioncode(String regioncode) {
        this.regioncode = regioncode;
        return this;
    }

    public AwsProductBuilder<T> replicationType(String replicationType) {
        this.replicationType = replicationType;
        return this;
    }

    public AwsProductBuilder<T> requestDescription(String requestDescription) {
        this.requestDescription = requestDescription;
        return this;
    }

    public AwsProductBuilder<T> requestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    public AwsProductBuilder<T> resourceEndpoint(String resourceEndpoint) {
        this.resourceEndpoint = resourceEndpoint;
        return this;
    }

    public AwsProductBuilder<T> resourceType(String resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    public AwsProductBuilder<T> routingTarget(String routingTarget) {
        this.routingTarget = routingTarget;
        return this;
    }

    public AwsProductBuilder<T> routingType(String routingType) {
        this.routingType = routingType;
        return this;
    }

    public AwsProductBuilder<T> runningMode(String runningMode) {
        this.runningMode = runningMode;
        return this;
    }

    public AwsProductBuilder<T> servicecode(String servicecode) {
        this.servicecode = servicecode;
        return this;
    }

    public AwsProductBuilder<T> servicename(String servicename) {
        this.servicename = servicename;
        return this;
    }

    public AwsProductBuilder<T> singleOrDualPass(String singleOrDualPass) {
        this.singleOrDualPass = singleOrDualPass;
        return this;
    }

    public AwsProductBuilder<T> sku(String sku) {
        this.sku = sku;
        return this;
    }

    public AwsProductBuilder<T> softwareIncluded(String softwareIncluded) {
        this.softwareIncluded = softwareIncluded;
        return this;
    }

    public AwsProductBuilder<T> softwareType(String softwareType) {
        this.softwareType = softwareType;
        return this;
    }

    public AwsProductBuilder<T> standardStorageRetentionIncluded(String standardStorageRetentionIncluded) {
        this.standardStorageRetentionIncluded = standardStorageRetentionIncluded;
        return this;
    }

    public AwsProductBuilder<T> storage(String storage) {
        this.storage = storage;
        return this;
    }

    public AwsProductBuilder<T> storageClass(String storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    public AwsProductBuilder<T> storageMedia(String storageMedia) {
        this.storageMedia = storageMedia;
        return this;
    }

    public AwsProductBuilder<T> storageType(String storageType) {
        this.storageType = storageType;
        return this;
    }

    public AwsProductBuilder<T> technicalSupport(String technicalSupport) {
        this.technicalSupport = technicalSupport;
        return this;
    }

    public AwsProductBuilder<T> tenancy(String tenancy) {
        this.tenancy = tenancy;
        return this;
    }

    public AwsProductBuilder<T> thirdpartySoftwareSupport(String thirdpartySoftwareSupport) {
        this.thirdpartySoftwareSupport = thirdpartySoftwareSupport;
        return this;
    }

    public AwsProductBuilder<T> tier(String tier) {
        this.tier = tier;
        return this;
    }

    public AwsProductBuilder<T> toLocation(String toLocation) {
        this.toLocation = toLocation;
        return this;
    }

    public AwsProductBuilder<T> toLocationType(String toLocationType) {
        this.toLocationType = toLocationType;
        return this;
    }

    public AwsProductBuilder<T> toRegionCode(String toRegionCode) {
        this.toRegionCode = toRegionCode;
        return this;
    }

    public AwsProductBuilder<T> training(String training) {
        this.training = training;
        return this;
    }

    public AwsProductBuilder<T> transferType(String transferType) {
        this.transferType = transferType;
        return this;
    }

    public AwsProductBuilder<T> transcodingResult(String transcodingResult) {
        this.transcodingResult = transcodingResult;
        return this;
    }

    public AwsProductBuilder<T> trialProduct(String trialProduct) {
        this.trialProduct = trialProduct;
        return this;
    }

    public AwsProductBuilder<T> upfrontCommitment(String upfrontCommitment) {
        this.upfrontCommitment = upfrontCommitment;
        return this;
    }

    public AwsProductBuilder<T> productUsageType(String usageType)  {
        this.productUsageType = usageType;
        return this;
    }

    public AwsProductBuilder<T> vcpu(String vcpu) {
        this.vcpu = vcpu;
        return this;
    }

    public AwsProductBuilder<T> version(String version) {
        this.version = version;
        return this;
    }

    public AwsProductBuilder<T> videoCodec(String videoCodec) {
        this.videoCodec = videoCodec;
        return this;
    }

    public AwsProductBuilder<T> videoFrameRate(Double videoFrameRate) {
        this.videoFrameRate = videoFrameRate;
        return this;
    }

    public AwsProductBuilder<T> volumeType(String volumeType) {
        this.volumeType = volumeType;
        return this;
    }

    public AwsProductBuilder<T> whoCanOpenCases(String whoCanOpenCases) {
        this.whoCanOpenCases = whoCanOpenCases;
        return this;
    }

    public AwsProductBuilder<T> deviceOs(String deviceOs) {
        this.deviceOs = deviceOs;
        return this;
    }

    public AwsProductBuilder<T> directconnectlocation(String directconnectlocation) {
        this.directconnectlocation = directconnectlocation;
        return this;
    }

    public AwsProductBuilder<T> executionMode(String executionMode) {
        this.executionMode = executionMode;
        return this;
    }

    public AwsProductBuilder<T> freeQueryTypes(String freeQueryTypes) {
        this.freeQueryTypes = freeQueryTypes;
        return this;
    }

    public AwsProductBuilder<T> freeUsageIncluded(Double freeUsageIncluded) {
        this.freeUsageIncluded = freeUsageIncluded;
        return this;
    }

    public AwsProductBuilder<T> instance(String instance) {
        this.instance = instance;
        return this;
    }

    public AwsProductBuilder<T> instanceCapacity10xLarge(String instanceCapacity10xLarge) {
        this. instanceCapacity10xLarge = instanceCapacity10xLarge;
        return this;
    }

    public AwsProductBuilder<T> instanceCapacity2xLarge(String instanceCapacity2xLarge) {
        this.instanceCapacity2xLarge = instanceCapacity2xLarge;
        return this;
    }

    public AwsProductBuilder<T> instanceCapacity4xLarge(String instanceCapacity4xLarge) {
        this.instanceCapacity4xLarge = instanceCapacity4xLarge;
        return this;
    }

    public AwsProductBuilder<T> instanceCapacity8xLarge(String instanceCapacity8xLarge) {
        this.instanceCapacity8xLarge = instanceCapacity8xLarge;
        return this;
    }

    public AwsProductBuilder<T> instanceCapacityLarge(String instanceCapacityLarge) {
        this.instanceCapacityLarge = instanceCapacityLarge;
        return this;
    }

    public AwsProductBuilder<T> instanceCapacityXlarge(String instanceCapacityXlarge) {
        this.instanceCapacityXlarge = instanceCapacityXlarge;
        return this;
    }

    public AwsProductBuilder<T> maximumCapacity(String maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
        return this;
    }

    public AwsProductBuilder<T> meterMode(String meterMode) {
        this.meterMode = meterMode;
        return this;
    }

    public AwsProductBuilder<T> origin(String origin) {
        this.origin = origin;
        return this;
    }

    public AwsProductBuilder<T> parameterType(String parameterType) {
        this.parameterType = parameterType;
        return this;
    }

    public AwsProductBuilder<T> physicalCores(String physicalCores) {
        this.physicalCores = physicalCores;
        return this;
    }

    public AwsProductBuilder<T> recipient(String recipient) {
        this.recipient = recipient;
        return this;
    }

    public AwsProductBuilder<T> requesttype(String requesttype) {
        this.requesttype = requesttype;
        return this;
    }

    public AwsProductBuilder<T> resourceAssessment(String resourceAssessment) {
        this.resourceAssessment = resourceAssessment;
        return this;
    }

    public AwsProductBuilder<T> storageDescription(String storageDescription) {
        this.storageDescription = storageDescription;
        return this;
    }

    public AwsProductBuilder<T> withActiveUsers(String withActiveUsers) {
        this.withActiveUsers = withActiveUsers;
        return this;
    }

    public AwsProductBuilder<T> workforceType(String workforceType) {
        this.workforceType = workforceType;
        return this;
    }

    public T build() {
        return sink.setAwsProduct(
            new AwsProduct(availabilityZone, eventType, freeTier, inputMode, mailboxStorage, memoryGib, outputMode, supportModes, productName, accountAssistance, alarmType, architecturalReview, architectureSupport, availability, bestPractices, bundle, cacheEngine, capacitystatus, caseSeverityresponseTimes, clockSpeed, contentType, currentGeneration, customerServiceAndCommunities, dataTransfer, dataTransferQuota, databaseEdition, databaseEngine, dedicatedEbsThroughput, deploymentOption, description, deviceOs, directconnectlocation, directorySize, directoryType, directoryTypeDescription, disableactivationconfirmationemail, durability, ebsOptimized, ecu, endpointType, engineCode, enhancedNetworkingSupported, executionMode, feeCode, feeDescription, filesystemtype, freeTrial, freeQueryTypes, freeUsageIncluded, fromLocation, fromLocationType, fromRegionCode, gpu, gpuMemory, group, groupDescription, includedServices, instance, instanceCapacity10xLarge, instanceCapacity2xLarge, instanceCapacity4xLarge, instanceCapacity8xLarge, instanceCapacityLarge, instanceCapacityXlarge, instanceFamily, instanceType, instanceTypeFamily, integratingApi, integratingService, intelAvxAvailable, intelAvx2Available, intelTurboAvailable, launchSupport, license, licenseModel, location, locationType, logsDestination, machineLearningProcess, maxIopsBurstPerformance, maxIopsvolume, maxThroughputvolume, maxVolumeSize, maximumCapacity, maximumExtendedStorage, maximumStorageVolume, memory, messageDeliveryFrequency, messageDeliveryOrder, meterMode, minVolumeSize, minimumStorageVolume, networkPerformance, normalizationSizeFactor, offeringClass, operatingSystem, operationsSupport, origin, parameterType, productOperation, physicalCores, physicalProcessor, preInstalledSw, pricingUnit, primaryplaceofuse, proactiveGuidance, processorArchitecture, processorFeatures, productFamily, productSchemaDescription, programmaticCaseManagement, provisioned, purchaseOption, purchaseterm, queueType, recipient, region, regioncode, replicationType, requestDescription, requestType, requesttype, resourceAssessment, resourceEndpoint, resourceType, routingTarget, routingType, runningMode, servicecode, servicename, singleOrDualPass, sku, softwareIncluded, softwareType, standardStorageRetentionIncluded, storage, storageClass, storageDescription, storageMedia, storageType, technicalSupport, tenancy, thirdpartySoftwareSupport, tier, toLocation, toLocationType, toRegionCode, training, transferType, transcodingResult, trialProduct, upfrontCommitment, productUsageType, vcpu, version, videoCodec, videoFrameRate, volumeType, whoCanOpenCases, withActiveUsers, workforceType)
        );
    }
}
