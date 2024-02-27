package com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AwsReservation {

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
    // All three below this would likely make sense as doubles - but they do not appear as doubles so I'm going w/ String for safety
    private String totalReservedNormalizedUnits;
    private String totalReservedUnits;
    private String unitsPerReservation;

    public AwsReservation() {}

    public AwsReservation(Double amortizedUpfrontCostForUsage, Double amortizedUpfrontFeeForBillingPeriod, String availabilityZone, Double effectiveCost, String endTime, String modificationStatus, String netAmortizedUpfrontCostForUsage, String netAmortizedUpfrontFeeForBillingPeriod, String netEffectiveCost, String netRecurringFeeForUsage, String netUnusedAmortizedUpfrontFeeForBillingPeriod, String netUnusedRecurringFee, String netUpfrontValue, Double recurringFeeForUsage, String startTime, Double unusedAmortizedUpfrontFeeForBillingPeriod, Double unusedNormalizedUnitQuantity, Double unusedQuantity, Double unusedRecurringFee, Double upfrontValue, String normalizedUnitsPerReservation, String numberOfReservations, String reservationARN, String subscriptionId, String totalReservedNormalizedUnits, String totalReservedUnits, String unitsPerReservation) {
        this.amortizedUpfrontCostForUsage = amortizedUpfrontCostForUsage;
        this.amortizedUpfrontFeeForBillingPeriod = amortizedUpfrontFeeForBillingPeriod;
        this.availabilityZone = availabilityZone;
        this.effectiveCost = effectiveCost;
        this.endTime = endTime;
        this.modificationStatus = modificationStatus;
        this.netAmortizedUpfrontCostForUsage = netAmortizedUpfrontCostForUsage;
        this.netAmortizedUpfrontFeeForBillingPeriod = netAmortizedUpfrontFeeForBillingPeriod;
        this.netEffectiveCost = netEffectiveCost;
        this.netRecurringFeeForUsage = netRecurringFeeForUsage;
        this.netUnusedAmortizedUpfrontFeeForBillingPeriod = netUnusedAmortizedUpfrontFeeForBillingPeriod;
        this.netUnusedRecurringFee = netUnusedRecurringFee;
        this.netUpfrontValue = netUpfrontValue;
        this.recurringFeeForUsage = recurringFeeForUsage;
        this.startTime = startTime;
        this.subscriptionId = subscriptionId;
        this.unusedAmortizedUpfrontFeeForBillingPeriod = unusedAmortizedUpfrontFeeForBillingPeriod;
        this.unusedNormalizedUnitQuantity = unusedNormalizedUnitQuantity;
        this.unusedQuantity = unusedQuantity;
        this.unusedRecurringFee = unusedRecurringFee;
        this.upfrontValue = upfrontValue;
        this.normalizedUnitsPerReservation = normalizedUnitsPerReservation;
        this.numberOfReservations = numberOfReservations;
        this.reservationARN = reservationARN;
        this.totalReservedNormalizedUnits = totalReservedNormalizedUnits;
        this.totalReservedUnits = totalReservedUnits;
        this.unitsPerReservation = unitsPerReservation;
    }

    public Double getAmortizedUpfrontCostForUsage() {
        return amortizedUpfrontCostForUsage;
    }

    public Double getAmortizedUpfrontFeeForBillingPeriod() {
        return amortizedUpfrontFeeForBillingPeriod;
    }

    @JsonProperty("reservation_availabilityZone")
    public String getAvailabilityZone() {
        return availabilityZone;
    }

    public void setAvailabilityZone(String availabilityZone) {
        this.availabilityZone = availabilityZone;
    }

    public Double getEffectiveCost() {
        return effectiveCost;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getModificationStatus() {
        return modificationStatus;
    }

    public String getNetAmortizedUpfrontCostForUsage() {
        return netAmortizedUpfrontCostForUsage;
    }

    public void setNetAmortizedUpfrontCostForUsage(String netAmortizedUpfrontCostForUsage) {
        this.netAmortizedUpfrontCostForUsage = netAmortizedUpfrontCostForUsage;
    }

    public String getNetAmortizedUpfrontFeeForBillingPeriod() {
        return netAmortizedUpfrontFeeForBillingPeriod;
    }

    public void setNetAmortizedUpfrontFeeForBillingPeriod(String netAmortizedUpfrontFeeForBillingPeriod) {
        this.netAmortizedUpfrontFeeForBillingPeriod = netAmortizedUpfrontFeeForBillingPeriod;
    }

    public String getNetEffectiveCost() {
        return netEffectiveCost;
    }

    public void setNetEffectiveCost(String netEffectiveCost) {
        this.netEffectiveCost = netEffectiveCost;
    }

    public String getNetRecurringFeeForUsage() {
        return netRecurringFeeForUsage;
    }

    public void setNetRecurringFeeForUsage(String netRecurringFeeForUsage) {
        this.netRecurringFeeForUsage = netRecurringFeeForUsage;
    }

    public String getNetUnusedAmortizedUpfrontFeeForBillingPeriod() {
        return netUnusedAmortizedUpfrontFeeForBillingPeriod;
    }

    public void setNetUnusedAmortizedUpfrontFeeForBillingPeriod(String netUnusedAmortizedUpfrontFeeForBillingPeriod) {
        this.netUnusedAmortizedUpfrontFeeForBillingPeriod = netUnusedAmortizedUpfrontFeeForBillingPeriod;
    }

    public String getNetUnusedRecurringFee() {
        return netUnusedRecurringFee;
    }

    public void setNetUnusedRecurringFee(String netUnusedRecurringFee) {
        this.netUnusedRecurringFee = netUnusedRecurringFee;
    }

    public String getNetUpfrontValue() {
        return netUpfrontValue;
    }

    public void setNetUpfrontValue(String netUpfrontValue) {
        this.netUpfrontValue = netUpfrontValue;
    }

    public Double getRecurringFeeForUsage() {
        return recurringFeeForUsage;
    }

    public String getStartTime() {
        return startTime;
    }

    public Double getUnusedAmortizedUpfrontFeeForBillingPeriod() {
        return unusedAmortizedUpfrontFeeForBillingPeriod;
    }

    public Double getUnusedNormalizedUnitQuantity() {
        return unusedNormalizedUnitQuantity;
    }

    public Double getUnusedQuantity() {
        return unusedQuantity;
    }

    public Double getUnusedRecurringFee() {
        return unusedRecurringFee;
    }

    public Double getUpfrontValue() {
        return upfrontValue;
    }

    public String getNormalizedUnitsPerReservation() {
        return normalizedUnitsPerReservation;
    }

    public void setNormalizedUnitsPerReservation(String normalizedUnitsPerReservation) {
        this.normalizedUnitsPerReservation = normalizedUnitsPerReservation;
    }

    public String getNumberOfReservations() {
        return numberOfReservations;
    }

    public void setNumberOfReservations(String numberOfReservations) {
        this.numberOfReservations = numberOfReservations;
    }

    public String getReservationARN() {
        return reservationARN;
    }

    public void setReservationARN(String reservationARN) {
        this.reservationARN = reservationARN;
    }

    @JsonProperty("reservation_subscriptionId")
    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getTotalReservedNormalizedUnits() {
        return totalReservedNormalizedUnits;
    }

    public void setTotalReservedNormalizedUnits(String totalReservedNormalizedUnits) {
        this.totalReservedNormalizedUnits = totalReservedNormalizedUnits;
    }

    public String getTotalReservedUnits() {
        return totalReservedUnits;
    }

    public void setTotalReservedUnits(String totalReservedUnits) {
        this.totalReservedUnits = totalReservedUnits;
    }

    public String getUnitsPerReservation() {
        return unitsPerReservation;
    }

    public void setUnitsPerReservation(String unitsPerReservation) {
        this.unitsPerReservation = unitsPerReservation;
    }

    @Override
    public String toString() {
        return "AwsReservation{" +
                "normalizedUnitsPerReservation='" + normalizedUnitsPerReservation + '\'' +
                ", numberOfReservations='" + numberOfReservations + '\'' +
                ", reservationARN='" + reservationARN + '\'' +
                ", totalReservedNormalizedUnits='" + totalReservedNormalizedUnits + '\'' +
                ", totalReservedUnits='" + totalReservedUnits + '\'' +
                ", unitsPerReservation='" + unitsPerReservation + '\'' +
                '}';
    }
}
