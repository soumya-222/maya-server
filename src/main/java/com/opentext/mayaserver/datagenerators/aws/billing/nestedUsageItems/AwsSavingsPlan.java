package com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class AwsSavingsPlan {
    private Double amortizedUpfrontCommitmentForBillingPeriod;
    private Date endTime;
    private String instanceTypeFamily;
    private Double netAmortizedUpfrontCommitmentForBillingPeriod;
    private Double netRecurringCommitmentForBillingPeriod;
    private Double netSavingsPlanEffectiveCost;
    private String offeringType;
    private String paymentOption;
    private String purchaseTerm;
    private Double recurringCommitmentForBillingPeriod;
    private String region;
    private String savingsPlanArn;
    private Double savingsPlanEffectiveCost;
    private Double savingsPlanRate;
    private Date startTime;
    private Double totalCommitmentToDate;
    private Double usedCommitment;

    public AwsSavingsPlan() {}

    public AwsSavingsPlan(Double amortizedUpfrontCommitmentForBillingPeriod, Date endTime, String instanceTypeFamily,
                          Double netAmortizedUpfrontCommitmentForBillingPeriod, Double netRecurringCommitmentForBillingPeriod,
                          Double netSavingsPlanEffectiveCost, String offeringType, String paymentOption, String purchaseTerm,
                          Double recurringCommitmentForBillingPeriod, String region, String savingsPlanArn, Double savingsPlanEffectiveCost,
                          Double savingsPlanRate, Date startTime, Double totalCommitmentToDate, Double usedCommitment
    ) {
        this.amortizedUpfrontCommitmentForBillingPeriod = amortizedUpfrontCommitmentForBillingPeriod;
        this.endTime = endTime;
        this.instanceTypeFamily = instanceTypeFamily;
        this.netAmortizedUpfrontCommitmentForBillingPeriod = netAmortizedUpfrontCommitmentForBillingPeriod;
        this.netRecurringCommitmentForBillingPeriod = netRecurringCommitmentForBillingPeriod;
        this.netSavingsPlanEffectiveCost = netSavingsPlanEffectiveCost;
        this.offeringType = offeringType;
        this.paymentOption = paymentOption;
        this.purchaseTerm = purchaseTerm;
        this.recurringCommitmentForBillingPeriod = recurringCommitmentForBillingPeriod;
        this.region = region;
        this.savingsPlanArn = savingsPlanArn;
        this.savingsPlanEffectiveCost = savingsPlanEffectiveCost;
        this.savingsPlanRate = savingsPlanRate;
        this.startTime = startTime;
        this.totalCommitmentToDate = totalCommitmentToDate;
        this.usedCommitment = usedCommitment;
    }

    public Double getAmortizedUpfrontCommitmentForBillingPeriod() {
        return amortizedUpfrontCommitmentForBillingPeriod;
    }

    public void setAmortizedUpfrontCommitmentForBillingPeriod(Double amortizedUpfrontCommitmentForBillingPeriod) {
        this.amortizedUpfrontCommitmentForBillingPeriod = amortizedUpfrontCommitmentForBillingPeriod;
    }

    @JsonProperty("savingsPlan_endTime")
    public Date getEndTime() { return endTime; }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @JsonProperty("savingsPlan_instanceTypeFamily")
    public String getInstanceTypeFamily() {
        return instanceTypeFamily;
    }

    public void setInstanceTypeFamily(String instanceTypeFamily) {
        this.instanceTypeFamily = instanceTypeFamily;
    }

    public Double getNetAmortizedUpfrontCommitmentForBillingPeriod() {
        return netAmortizedUpfrontCommitmentForBillingPeriod;
    }

    public void setNetAmortizedUpfrontCommitmentForBillingPeriod(Double netAmortizedUpfrontCommitmentForBillingPeriod) {
        this.netAmortizedUpfrontCommitmentForBillingPeriod = netAmortizedUpfrontCommitmentForBillingPeriod;
    }

    public Double getNetRecurringCommitmentForBillingPeriod() {
        return netRecurringCommitmentForBillingPeriod;
    }

    public void setNetRecurringCommitmentForBillingPeriod(Double netRecurringCommitmentForBillingPeriod) {
        this.netRecurringCommitmentForBillingPeriod = netRecurringCommitmentForBillingPeriod;
    }

    public Double getNetSavingsPlanEffectiveCost() {
        return netSavingsPlanEffectiveCost;
    }

    public void setNetSavingsPlanEffectiveCost(Double netSavingsPlanEffectiveCost) {
        this.netSavingsPlanEffectiveCost = netSavingsPlanEffectiveCost;
    }

    public String getOfferingType() {
        return offeringType;
    }

    public void setOfferingType(String offeringType) {
        this.offeringType = offeringType;
    }

    public String getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(String paymentOption) {
        this.paymentOption = paymentOption;
    }


    public String getPurchaseTerm() {
        return purchaseTerm;
    }

    public void setPurchaseTerm(String purchaseTerm) {
        this.purchaseTerm = purchaseTerm;
    }

    public Double getRecurringCommitmentForBillingPeriod() {
        return recurringCommitmentForBillingPeriod;
    }

    public void setRecurringCommitmentForBillingPeriod(Double recurringCommitmentForBillingPeriod) {
        this.recurringCommitmentForBillingPeriod = recurringCommitmentForBillingPeriod;
    }

    @JsonProperty("savingsPlan_region")
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSavingsPlanArn() {
        return savingsPlanArn;
    }

    public void setSavingsPlanArn(String savingsPlanArn) {
        this.savingsPlanArn = savingsPlanArn;
    }

    public Double getSavingsPlanEffectiveCost() {
        return savingsPlanEffectiveCost;
    }

    public void setSavingsPlanEffectiveCost(Double savingsPlanEffectiveCost) {
        this.savingsPlanEffectiveCost = savingsPlanEffectiveCost;
    }

    public Double getSavingsPlanRate() {
        return savingsPlanRate;
    }

    public void setSavingsPlanRate(Double savingsPlanRate) {
        this.savingsPlanRate = savingsPlanRate;
    }

    @JsonProperty("savingsPlan_startTime")
    public Date getStartTime() { return startTime; }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Double getTotalCommitmentToDate() { return totalCommitmentToDate; }

    public void setTotalCommitmentToDate(Double totalCommitmentToDate) {
        this.totalCommitmentToDate = totalCommitmentToDate;
    }

    public Double getUsedCommitment() {
        return usedCommitment;
    }

    public void setUsedCommitment(Double usedCommitment) {
        this.usedCommitment = usedCommitment;
    }

    @Override
    public String toString() {
        return "AwsSavingsPlans{" +
                "amortizedUpfrontCommitmentForBillingPeriod='" + amortizedUpfrontCommitmentForBillingPeriod + '\'' +
                ", endTime='" + endTime + '\'' +
                ", netAmortizedUpfrontCommitmentForBillingPeriod='" + netAmortizedUpfrontCommitmentForBillingPeriod + '\'' +
                ", netRecurringCommitmentForBillingPeriod='" + netRecurringCommitmentForBillingPeriod + '\'' +
                ", netSavingsPlanEffectiveCost='" + netSavingsPlanEffectiveCost + '\'' +
                ", offeringType='" + offeringType + '\'' +
                ", paymentOption='" + paymentOption + '\'' +
                ", purchaseTerm='" + purchaseTerm + '\'' +
                ", recurringCommitmentForBillingPeriod='" + recurringCommitmentForBillingPeriod + '\'' +
                ", region='" + region + '\'' +
                ", savingsPlanArn='" + savingsPlanArn + '\'' +
                ", savingsPlanEffectiveCost='" + savingsPlanEffectiveCost + '\'' +
                ", savingsPlanRate='" + savingsPlanRate + '\'' +
                ", startTime='" + startTime + '\'' +
                ", totalCommitmentToDate='" + totalCommitmentToDate + '\'' +
                ", usedCommitment='" + usedCommitment + '\'' +
                '}';
    }

}
