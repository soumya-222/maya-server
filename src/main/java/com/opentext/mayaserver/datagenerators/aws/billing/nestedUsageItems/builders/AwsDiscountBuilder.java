package com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.builders;

import com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.AwsDiscount;

public class AwsDiscountBuilder <T> {
    private AwsSink<T> sink;

    private Double bundledDiscount;
    private Double totalDiscount;

    public AwsDiscountBuilder(AwsSink<T> sink) {
        this.sink = sink;
    }

    public AwsDiscountBuilder<T> bundledDiscount(Double bundledDiscount) {
        this.bundledDiscount = bundledDiscount;
        return this;
    }

    public AwsDiscountBuilder<T> totalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
        return this;
    }

    public T build() {
        return sink.setAwsDiscount(new AwsDiscount(bundledDiscount, totalDiscount));
    }

}
