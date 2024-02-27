package com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.builders;

import com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.AwsReservation;

public class AwsReservationBuilder<T> {
    private AwsSink<T> sink;

    // New in 2018
    private Double amortizedUpfrontCostForUsage;
    private Double amortizedUpfrontFeeForBillingPeriod;
    private Double effectiveCost;
    private String endTime;
    private String modificationStatus;
    private Double recurringFeeForUsage;
    private String startTime;
    private Double unusedAmortizedUpfrontFeeForBillingPeriod;
    private Double unusedNormalizedUnitQuantity;
    private Double unusedQuantity;
    private Double unusedRecurringFee;
    private Double upfrontValue;
    // End New in 2018

    private String availabilityZone;
    private String netAmortizedUpfrontCostForUsage;
    private String netAmortizedUpfrontFeeForBillingPeriod;
    private String netEffectiveCost;
    private String netRecurringFeeForUsage;
    private String netUnusedAmortizedUpfrontFeeForBillingPeriod;
    private String netUnusedRecurringFee;
    private String netUpfrontValue;
    private String normalizedUnitsPerReservation;
    private String numberOfReservations;
    private String reservationARN;
    private String subscriptionId;
    private String totalReservedNormalizedUnits;
    private String totalReservedUnits;
    private String unitsPerReservation;

    public AwsReservationBuilder(AwsSink<T> sink) {
        this.sink = sink;
    }

    public AwsReservationBuilder<T> amortizedUpfrontCostForUsage(Double amortizedUpfrontCostForUsage) {
        this.amortizedUpfrontCostForUsage = amortizedUpfrontCostForUsage;
        return this;
    }

    public AwsReservationBuilder<T> amortizedUpfrontFeeForBillingPeriod(Double amortizedUpfrontFeeForBillingPeriod) {
        this.amortizedUpfrontFeeForBillingPeriod = amortizedUpfrontFeeForBillingPeriod;
        return this;
    }

    public AwsReservationBuilder<T> availabilityZone(String availabilityZone) {
        this.availabilityZone = availabilityZone;
        return this;
    }

    public AwsReservationBuilder<T> effectiveCost(Double effectiveCost) {
        this.effectiveCost = effectiveCost;
        return this;
    }

    public AwsReservationBuilder<T> endTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    public AwsReservationBuilder<T> modificationStatus(String modificationStatus) {
        this.modificationStatus = modificationStatus;
        return this;
    }

    public AwsReservationBuilder<T> netAmortizedUpfrontCostForUsage(String netAmortizedUpfrontCostForUsage) {
        this.netAmortizedUpfrontCostForUsage = netAmortizedUpfrontCostForUsage;
        return this;
    }

    public AwsReservationBuilder<T> netAmortizedUpfrontFeeForBillingPeriod(String netAmortizedUpfrontFeeForBillingPeriod) {
        this.netAmortizedUpfrontFeeForBillingPeriod = netAmortizedUpfrontFeeForBillingPeriod;
        return this;
    }

    public AwsReservationBuilder<T> netEffectiveCost(String netEffectiveCost) {
        this.netEffectiveCost = netEffectiveCost;
        return this;
    }

    public AwsReservationBuilder<T> netRecurringFeeForUsage(String netRecurringFeeForUsage) {
        this.netRecurringFeeForUsage = netRecurringFeeForUsage;
        return this;
    }

    public AwsReservationBuilder<T> netUnusedAmortizedUpfrontFeeForBillingPeriod(String netUnusedAmortizedUpfrontFeeForBillingPeriod) {
        this.netUnusedAmortizedUpfrontFeeForBillingPeriod = netUnusedAmortizedUpfrontFeeForBillingPeriod;
        return this;
    }

    public AwsReservationBuilder<T> netUnusedRecurringFee(String netUnusedRecurringFee) {
        this.netUnusedRecurringFee = netUnusedRecurringFee;
        return this;
    }

    public AwsReservationBuilder<T> netUpfrontValue(String netUpfrontValue) {
        this.netUpfrontValue = netUpfrontValue;
        return this;
    }

    public AwsReservationBuilder<T> recurringFeeForUsage(Double recurringFeeForUsage) {
        this.recurringFeeForUsage = recurringFeeForUsage;
        return this;
    }

    public AwsReservationBuilder<T> startTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public AwsReservationBuilder<T> subscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
        return this;
    }

    public AwsReservationBuilder<T> unusedAmortizedUpfrontFeeForBillingPeriod(Double unusedAmortizedUpfrontFeeForBillingPeriod) {
        this.unusedAmortizedUpfrontFeeForBillingPeriod = unusedAmortizedUpfrontFeeForBillingPeriod;
        return this;
    }

    public AwsReservationBuilder<T> unusedNormalizedUnitQuantity(Double unusedNormalizedUnitQuantity) {
        this.unusedNormalizedUnitQuantity = unusedNormalizedUnitQuantity;
        return this;
    }

    public AwsReservationBuilder<T> unusedQuantity(Double unusedQuantity) {
        this.unusedQuantity = unusedQuantity;
        return this;
    }

    public AwsReservationBuilder<T> unusedRecurringFee(Double unusedRecurringFee) {
        this.unusedRecurringFee = unusedRecurringFee;
        return this;
    }

    public AwsReservationBuilder<T> upfrontValue(Double upfrontValue) {
        this.upfrontValue = upfrontValue;
        return this;
    }

    public AwsReservationBuilder<T> normalizedUnitsPerReservation(String normalizedUnitsPerReservation) {
        this.normalizedUnitsPerReservation = normalizedUnitsPerReservation;
        return this;
    }

    public AwsReservationBuilder<T> numberOfReservations(String numberOfReservations) {
        this.numberOfReservations = numberOfReservations;
        return this;
    }

    public AwsReservationBuilder<T> reservationARN(String reservationARN) {
        this.reservationARN = reservationARN;
        return this;
    }

    public AwsReservationBuilder<T> totalReservedNormalizedUnits(String totalReservedNormalizedUnits) {
        this.totalReservedNormalizedUnits = totalReservedNormalizedUnits;
        return this;
    }

    public AwsReservationBuilder<T> totalReservedUnits(String totalReservedUnits) {
        this.totalReservedUnits = totalReservedUnits;
        return this;
    }

    public AwsReservationBuilder<T> unitsPerReservation(String unitsPerReservation) {
        this.unitsPerReservation = unitsPerReservation;
        return this;
    }

    public T build() {
        return sink.setAwsReservation(new AwsReservation(amortizedUpfrontCostForUsage, amortizedUpfrontFeeForBillingPeriod, availabilityZone, effectiveCost, endTime, modificationStatus, netAmortizedUpfrontCostForUsage, netAmortizedUpfrontFeeForBillingPeriod, netEffectiveCost, netRecurringFeeForUsage, netUnusedAmortizedUpfrontFeeForBillingPeriod, netUnusedRecurringFee, netUpfrontValue, recurringFeeForUsage, startTime, unusedAmortizedUpfrontFeeForBillingPeriod, unusedNormalizedUnitQuantity, unusedQuantity, unusedRecurringFee, upfrontValue, normalizedUnitsPerReservation, numberOfReservations, reservationARN, subscriptionId, totalReservedNormalizedUnits, totalReservedUnits, unitsPerReservation));
    }
}
