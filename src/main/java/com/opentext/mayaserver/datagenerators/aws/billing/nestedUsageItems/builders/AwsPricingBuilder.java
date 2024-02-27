package com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.builders;

import com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.AwsPricing;

public class AwsPricingBuilder<T> {
    private AwsSink<T> sink;

    private String rateId;
    private String rateCode;
    private String leaseContractLength;
    private String offeringClass;
    private String purchaseOption;
    private Double publicOnDemandCost;
    private Double publicOnDemandRate;
    private String term;
    private String unit;

    public AwsPricingBuilder(AwsSink<T> sink) {
        this.sink = sink;
    }

    public AwsPricingBuilder<T> rateId(String rateId) {
        this.rateId = rateId;
        return this;
    }

    public AwsPricingBuilder<T> rateCode(String rateCode) {
        this.rateCode = rateCode;
        return this;
    }

    public AwsPricingBuilder<T> leaseContractLength(String leaseContractLength) {
        this.leaseContractLength = leaseContractLength;
        return this;
    }

    public AwsPricingBuilder<T> offeringClass(String offeringClass) {
        this.offeringClass = offeringClass;
        return this;
    }

    public AwsPricingBuilder<T> purchaseOption(String purchaseOption) {
        this.purchaseOption = purchaseOption;
        return this;
    }

    public AwsPricingBuilder<T> publicOnDemandCost(Double publicOnDemandCost) {
        this.publicOnDemandCost = publicOnDemandCost;
        return this;
    }

    public AwsPricingBuilder<T> publicOnDemandRate(Double publicOnDemandRate) {
        this.publicOnDemandRate = publicOnDemandRate;
        return this;
    }

    public AwsPricingBuilder<T> term(String term) {
        this.term = term;
        return this;
    }

    public AwsPricingBuilder<T> unit(String unit) {
        this.unit = unit;
        return this;
    }

    public T build() {
        return sink.setAwsPricing(new AwsPricing(rateId, rateCode, leaseContractLength, offeringClass, purchaseOption, publicOnDemandRate,
                publicOnDemandCost, term, unit));
    }
}
