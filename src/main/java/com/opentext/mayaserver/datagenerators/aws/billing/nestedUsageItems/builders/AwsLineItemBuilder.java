package com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.builders;

import com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.AwsLineItem;

import java.util.Date;

public class AwsLineItemBuilder<T> {
    private AwsSink<T> sink;

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

    public AwsLineItemBuilder(AwsSink<T> sink) {
        this.sink = sink;
    }

    public AwsLineItemBuilder<T> usageAccountId(String usageAccountId) {
        this.usageAccountId = usageAccountId;
        return this;
    }

    public AwsLineItemBuilder<T> lineItemType(String lineItemType) {
        this.lineItemType = lineItemType;
        return this;
    }

    public AwsLineItemBuilder<T> usageStartDate(Date usageStartDate) {
        this.usageStartDate = usageStartDate;
        return this;
    }

    public AwsLineItemBuilder<T> usageStartDateTrunc(Date usageStartDateTrunc) {
        this.usageStartDateTrunc = usageStartDateTrunc;
        return this;
    }

    public AwsLineItemBuilder<T> usageEndDate(Date usageEndDate) {
        this.usageEndDate = usageEndDate;
        return this;
    }

    public AwsLineItemBuilder<T> productCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public AwsLineItemBuilder<T> usageType(String usageType) {
        this.usageType = usageType;
        return this;
    }

    public AwsLineItemBuilder<T> operation(String operation) {
        this.operation = operation;
        return this;
    }

    public AwsLineItemBuilder<T> availabilityZone(String availabilityZone) {
        this.availabilityZone = availabilityZone;
        return this;
    }

    public AwsLineItemBuilder<T> resourceId(String resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public AwsLineItemBuilder<T> usageAmount(Double usageAmount) {
        this.usageAmount = usageAmount;
        return this;
    }

    public AwsLineItemBuilder<T> normalizationFactor(Double normalizationFactor) {
        this.normalizationFactor = normalizationFactor;
        return this;
    }

    public AwsLineItemBuilder<T> normalizedUsageAmount(Double normalizedUsageAmount) {
        this.normalizedUsageAmount = normalizedUsageAmount;
        return this;
    }

    public AwsLineItemBuilder<T> currencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public AwsLineItemBuilder<T> netUnblendedRate(Double netUnblendedRate) {
        this.netUnblendedRate = netUnblendedRate;
        return this;
    }

    public AwsLineItemBuilder<T> netUnblendedCost(Double netUnblendedCost) {
        this.netUnblendedCost = netUnblendedCost;
        return this;
    }

    public AwsLineItemBuilder<T> unblendedRate(Double unblendedRate) {
        this.unblendedRate = unblendedRate;
        return this;
    }

    public AwsLineItemBuilder<T> unblendedCost(Double unblendedCost) {
        this.unblendedCost = unblendedCost;
        return this;
    }

    public AwsLineItemBuilder<T> blendedRate(Double blendedRate) {
        this.blendedRate = blendedRate;
        return this;
    }

    public AwsLineItemBuilder<T> blendedCost(Double blendedCost) {
        this.blendedCost = blendedCost;
        return this;
    }

    public AwsLineItemBuilder<T> lineItemDescription(String lineItemDescription) {
        this.lineItemDescription = lineItemDescription;
        return this;
    }

    public AwsLineItemBuilder<T> taxType(String taxType) {
        this.taxType = taxType;
        return this;
    }

    public AwsLineItemBuilder<T> legalEntity(String legalEntity) {
        this.legalEntity = legalEntity;
        return this;
    }

    public T build() {
        return sink.setAwsLineItem(new AwsLineItem(usageAccountId, lineItemType, usageStartDate, usageStartDateTrunc,
                usageEndDate, productCode, usageType, operation, availabilityZone, resourceId, usageAmount,
                normalizationFactor, normalizedUsageAmount, currencyCode, netUnblendedRate, netUnblendedCost,
                unblendedRate, unblendedCost, blendedRate, blendedCost, lineItemDescription, taxType, legalEntity));
    }
}
