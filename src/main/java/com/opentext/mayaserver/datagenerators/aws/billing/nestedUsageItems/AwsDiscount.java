package com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems;

public class AwsDiscount {

    private Double bundledDiscount;
    private Double totalDiscount;

    public AwsDiscount() {}

    public AwsDiscount(Double bundledDiscount, Double totalDiscount) {
        this.bundledDiscount = bundledDiscount;
        this.totalDiscount = totalDiscount;
    }

    public Double getBundledDiscount() {
        return bundledDiscount;
    }

    public void setBundledDiscount(Double bundledDiscount) {
        this.bundledDiscount = bundledDiscount;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    @Override
    public String toString() {
        return "AwsDiscount{" +
                "bundledDiscount='" + bundledDiscount + '\'' +
                ", totalDiscount='" + totalDiscount + '\'' +
                '}';
    }
}
