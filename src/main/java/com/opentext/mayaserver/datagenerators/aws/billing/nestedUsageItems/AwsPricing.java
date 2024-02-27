package com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems;

public class AwsPricing {
    private String rateId;
    private String rateCode;
    private String leaseContractLength;
    private String offeringClass;
    private String purchaseOption;
    private Double publicOnDemandCost;
    private Double publicOnDemandRate;
    private String term;
    private String unit;

    public AwsPricing() {
    }

    public AwsPricing(String rateId, String rateCode, String leaseContractLength, String offeringClass, String purchaseOption, Double publicOnDemandCost, Double publicOnDemandRate, String term, String unit) {
        this.rateId = rateId;
        this.rateCode = rateCode;
        this.leaseContractLength = leaseContractLength;
        this.offeringClass = offeringClass;
        this.purchaseOption = purchaseOption;
        this.publicOnDemandCost = publicOnDemandCost;
        this.publicOnDemandRate = publicOnDemandRate;
        this.term = term;
        this.unit = unit;
    }

    public String getRateId() {
        return rateId;
    }

    public void setRateId(String rateId) {
        this.rateId = rateId;
    }

    public String getRateCode() {
        return rateCode;
    }

    public void setRateCode(String rateCode) {
        this.rateCode = rateCode;
    }

    public String getLeaseContractLength() {
        return leaseContractLength;
    }

    public void setLeaseContractLength(String leaseContractLength) {
        this.leaseContractLength = leaseContractLength;
    }

    public String getOfferingClass() {
        return offeringClass;
    }

    public void setOfferingClass(String offeringClass) {
        this.offeringClass = offeringClass;
    }

    public String getPurchaseOption() {
        return purchaseOption;
    }

    public void setPurchaseOption(String purchaseOption) {
        this.purchaseOption = purchaseOption;
    }

    public Double getPublicOnDemandCost() {
        return publicOnDemandCost;
    }

    public void setPublicOnDemandCost(Double publicOnDemandCost) {
        this.publicOnDemandCost = publicOnDemandCost;
    }

    public Double getPublicOnDemandRate() {
        return publicOnDemandRate;
    }

    public void setPublicOnDemandRate(Double publicOnDemandRate) {
        this.publicOnDemandRate = publicOnDemandRate;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "AwsPricing{" +
                "rateId='" + rateId + '\'' +
                ", rateCode='" + rateCode + '\'' +
                ", leaseContractLength='" + leaseContractLength + '\'' +
                ", offeringClass='" + offeringClass + '\'' +
                ", purchaseOption='" + purchaseOption + '\'' +
                ", publicOnDemandCost=" + publicOnDemandCost +
                ", publicOnDemandRate=" + publicOnDemandRate +
                ", term='" + term + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }
}
