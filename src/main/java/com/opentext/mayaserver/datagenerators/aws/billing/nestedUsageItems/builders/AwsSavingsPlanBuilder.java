package com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.builders;

import com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.AwsSavingsPlan;

import java.util.Date;

public class AwsSavingsPlanBuilder<T> {
    private AwsSink<T> sink;

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

    public AwsSavingsPlanBuilder(AwsSink<T> sink) {
        this.sink = sink;
    }

    public AwsSavingsPlanBuilder<T> amortizedUpfrontCommitmentForBillingPeriod(Double amortizedUpfrontCommitmentForBillingPeriod) {
        this.amortizedUpfrontCommitmentForBillingPeriod = amortizedUpfrontCommitmentForBillingPeriod;
        return this;
    }

    public AwsSavingsPlanBuilder<T> endTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    public AwsSavingsPlanBuilder<T> instanceTypeFamily(String instanceTypeFamily) {
        this.instanceTypeFamily = instanceTypeFamily;
        return this;
    }

    public AwsSavingsPlanBuilder<T> netAmortizedUpfrontCommitmentForBillingPeriod(Double netAmortizedUpfrontCommitmentForBillingPeriod) {
        this.netAmortizedUpfrontCommitmentForBillingPeriod = netAmortizedUpfrontCommitmentForBillingPeriod;
        return this;
    }

    public AwsSavingsPlanBuilder<T> netRecurringCommitmentForBillingPeriod(Double netRecurringCommitmentForBillingPeriod) {
        this.netRecurringCommitmentForBillingPeriod = netRecurringCommitmentForBillingPeriod;
        return this;
    }

    public AwsSavingsPlanBuilder<T> netSavingsPlanEffectiveCost(Double netSavingsPlanEffectiveCost) {
        this.netSavingsPlanEffectiveCost = netSavingsPlanEffectiveCost;
        return this;
    }

    public AwsSavingsPlanBuilder<T> offeringType(String offeringType) {
        this.offeringType = offeringType;
        return this;
    }

    public AwsSavingsPlanBuilder<T> paymentOption(String paymentOption) {
        this.paymentOption = paymentOption;
        return this;
    }

    public AwsSavingsPlanBuilder<T> purchaseTerm(String purchaseTerm) {
        this.purchaseTerm = purchaseTerm;
        return this;
    }

    public AwsSavingsPlanBuilder<T> recurringCommitmentForBillingPeriod(Double recurringCommitmentForBillingPeriod) {
        this.recurringCommitmentForBillingPeriod = recurringCommitmentForBillingPeriod;
        return this;
    }

    public AwsSavingsPlanBuilder<T> region(String region) {
        this.region = region;
        return this;
    }

    public AwsSavingsPlanBuilder<T> savingsPlanArn(String savingsPlanArn) {
        this.savingsPlanArn = savingsPlanArn;
        return this;
    }

    public AwsSavingsPlanBuilder<T> savingsPlanEffectiveCost(Double savingsPlanEffectiveCost) {
        this.savingsPlanEffectiveCost = savingsPlanEffectiveCost;
        return this;
    }

    public AwsSavingsPlanBuilder<T> savingsPlanRate(Double savingsPlanRate) {
        this.savingsPlanRate = savingsPlanRate;
        return this;
    }

    public AwsSavingsPlanBuilder<T> startTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    public AwsSavingsPlanBuilder<T> totalCommitmentToDate(Double totalCommitmentToDate) {
        this.totalCommitmentToDate = totalCommitmentToDate;
        return this;
    }

    public AwsSavingsPlanBuilder<T> usedCommitment(Double usedCommitment) {
        this.usedCommitment = usedCommitment;
        return this;
    }

    public T build() {
        return sink.setAwsSavingsPlan(new AwsSavingsPlan(amortizedUpfrontCommitmentForBillingPeriod, endTime, instanceTypeFamily,
                netAmortizedUpfrontCommitmentForBillingPeriod, netRecurringCommitmentForBillingPeriod, netSavingsPlanEffectiveCost,
                offeringType, paymentOption, purchaseTerm, recurringCommitmentForBillingPeriod, region, savingsPlanArn,
                savingsPlanEffectiveCost, savingsPlanRate, startTime, totalCommitmentToDate, usedCommitment));
    }
}