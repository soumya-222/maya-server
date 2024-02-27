package com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems;

import java.util.Date;
public class AwsLineItem {
    private String usageAccountId;
    private String lineItemType;
    private Date usageStartDate;
    private Date usageStartDateTrunc;
    private Date usageEndDate;
    private String productCode;
    private String usageType;
    private String operation;
    private String availabilityZone;
    private String resourceId;
    private Double usageAmount;
    private Double normalizationFactor;
    private Double normalizedUsageAmount;
    private String currencyCode;
    private Double netUnblendedRate;
    private Double netUnblendedCost;
    private Double unblendedRate;
    private Double unblendedCost;
    private Double blendedRate;
    private Double blendedCost;
    private String lineItemDescription;
    private String taxType;
    private String legalEntity;

    public AwsLineItem() {
    }

    public AwsLineItem(String usageAccountId, String lineItemType, Date usageStartDate, Date usageStartDateTrunc, Date usageEndDate, String productCode, String usageType, String operation, String availabilityZone, String resourceId, Double usageAmount, Double normalizationFactor, Double normalizedUsageAmount, String currencyCode, Double netUnblendedRate, Double netUnblendedCost,  Double unblendedRate, Double unblendedCost, Double blendedRate, Double blendedCost, String lineItemDescription, String taxType, String legalEntity) {
        this.usageAccountId = usageAccountId;
        this.lineItemType = lineItemType;
        this.usageStartDate = usageStartDate;
        this.usageStartDateTrunc = usageStartDateTrunc;
        this.usageEndDate = usageEndDate;
        this.productCode = productCode;
        this.usageType = usageType;
        this.operation = operation;
        this.availabilityZone = availabilityZone;
        this.resourceId = resourceId;
        this.usageAmount = usageAmount;
        this.normalizationFactor = normalizationFactor;
        this.normalizedUsageAmount = normalizedUsageAmount;
        this.currencyCode = currencyCode;
        this.netUnblendedRate = netUnblendedRate;
        this.netUnblendedCost = netUnblendedCost;
        this.unblendedRate = unblendedRate;
        this.unblendedCost = unblendedCost;
        this.blendedRate = blendedRate;
        this.blendedCost = blendedCost;
        this.lineItemDescription = lineItemDescription;
        this.taxType = taxType;
        this.legalEntity = legalEntity;
    }

    public String getUsageAccountId() {
        return usageAccountId;
    }

    public void setUsageAccountId(String usageAccountId) {
        this.usageAccountId = usageAccountId;
    }

    public String getLineItemType() {
        return lineItemType;
    }

    public void setLineItemType(String lineItemType) {
        this.lineItemType = lineItemType;
    }

    public Date getUsageStartDate() { return usageStartDate; }

    public void setUsageStartDate(Date usageStartDate) {
        this.usageStartDate = usageStartDate;
    }

    public Date getUsageStartDateTrunc() {
        return usageStartDateTrunc;
    }

    public void setUsageStartDateTrunc(Date usageStartDateTrunc) {
        this.usageStartDateTrunc = usageStartDateTrunc;
    }

    public Date getUsageEndDate() { return usageEndDate;}

    public void setUsageEndDate(Date usageEndDate) {
        this.usageEndDate = usageEndDate;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getAvailabilityZone() {
        return availabilityZone;
    }

    public void setAvailabilityZone(String availabilityZone) {
        this.availabilityZone = availabilityZone;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Double getUsageAmount() {
        return usageAmount;
    }

    public void setUsageAmount(Double usageAmount) {
        this.usageAmount = usageAmount;
    }

    public Double getNormalizationFactor() {
        return normalizationFactor;
    }

    public void setNormalizationFactor(Double normalizationFactor) {
        this.normalizationFactor = normalizationFactor;
    }

    public Double getNormalizedUsageAmount() {
        return normalizedUsageAmount;
    }

    public void setNormalizedUsageAmount(Double normalizedUsageAmount) {
        this.normalizedUsageAmount = normalizedUsageAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Double getNetUnblendedRate() {
        return netUnblendedRate;
    }

    public void setNetUnblendedRate(Double netUnblendedRate) {
        this.netUnblendedRate = netUnblendedRate;
    }

    public Double getNetUnblendedCost() {
        return netUnblendedCost;
    }

    public void setNetUnblendedCost(Double netUnblendedCost) {
        this.netUnblendedCost = netUnblendedCost;
    }

    public Double getUnblendedRate() {
        return unblendedRate;
    }

    public void setUnblendedRate(Double unblendedRate) {
        this.unblendedRate = unblendedRate;
    }

    public Double getUnblendedCost() {
        return unblendedCost;
    }

    public void setUnblendedCost(Double unblendedCost) {
        this.unblendedCost = unblendedCost;
    }

    public Double getBlendedRate() {
        return blendedRate;
    }

    public void setBlendedRate(Double blendedRate) {
        this.blendedRate = blendedRate;
    }

    public Double getBlendedCost() {
        return blendedCost;
    }

    public void setBlendedCost(Double blendedCost) {
        this.blendedCost = blendedCost;
    }

    public String getLineItemDescription() {
        return lineItemDescription;
    }

    public void setLineItemDescription(String lineItemDescription) {
        this.lineItemDescription = lineItemDescription;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public String getLegalEntity() {
        return legalEntity;
    }

    public void setLegalEntity(String legalEntity) {
        this.legalEntity = legalEntity;
    }

    @Override
    public String toString() {
        return "AwsLineItem{" +
                "usageAccountId='" + usageAccountId + '\'' +
                ", lineItemType='" + lineItemType + '\'' +
                ", usageStartDate='" + usageStartDate + '\'' +
                ", usageEndDate='" + usageEndDate + '\'' +
                ", productCode='" + productCode + '\'' +
                ", usageType='" + usageType + '\'' +
                ", operation='" + operation + '\'' +
                ", availabilityZone='" + availabilityZone + '\'' +
                ", resourceId='" + resourceId + '\'' +
                ", usageAmount=" + usageAmount +
                ", normalizationFactor=" + normalizationFactor +
                ", normalizedUsageAmount=" + normalizedUsageAmount +
                ", currencyCode='" + currencyCode + '\'' +
                ", netUnblendedRate=" + netUnblendedRate +
                ", netUnblendedCost=" + netUnblendedCost +
                ", unblendedRate=" + unblendedRate +
                ", unblendedCost=" + unblendedCost +
                ", blendedRate=" + blendedRate +
                ", blendedCost=" + blendedCost +
                ", lineItemDescription='" + lineItemDescription + '\'' +
                ", taxType='" + taxType + '\'' +
                ", legalEntity='" + legalEntity + '\'' +
                '}';
    }
}
