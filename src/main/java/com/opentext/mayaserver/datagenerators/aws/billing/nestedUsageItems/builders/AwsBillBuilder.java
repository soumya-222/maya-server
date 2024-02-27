package com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.builders;

import com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.AwsBill;

import java.util.Date;

public class AwsBillBuilder<T> {
    private AwsSink<T> sink;

    private String invoiceId;
    private String invoicingEntity;
    private String billingEntity;
    private String billType;
    private String payerAccountId;
    private Date billingPeriodStartDate;
    private Date billingPeriodStartDateTrunc;
    private Date billingPeriodEndDate;

    public AwsBillBuilder(AwsSink<T> sink) {
        this.sink = sink;
    }

    public AwsBillBuilder<T> invoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
        return this;
    }

    public AwsBillBuilder<T> invoicingEntity(String invoicingEntity) {
        this.invoicingEntity = invoicingEntity;
        return this;
    }

    public AwsBillBuilder<T> billingEntity(String billingEntity) {
        this.billingEntity = billingEntity;
        return this;
    }

    public AwsBillBuilder<T> billType(String billType) {
        this.billType = billType;
        return this;
    }

    public AwsBillBuilder<T> payerAccountId(String payerAccountId) {
        this.payerAccountId = payerAccountId;
        return this;
    }

    public AwsBillBuilder<T> billingPeriodStartDate(Date billingPeriodStartDate) {
        this.billingPeriodStartDate = billingPeriodStartDate;
        return this;
    }

    public AwsBillBuilder<T> billingPeriodStartDateTrunc(Date billingPeriodStartDateTrunc) {
        this.billingPeriodStartDateTrunc = billingPeriodStartDateTrunc;
        return this;
    }

    public AwsBillBuilder<T> billingPeriodEndDate(Date billingPeriodEndDate) {
        this.billingPeriodEndDate = billingPeriodEndDate;
        return this;
    }

    public T build() {
        return sink.setAwsBill(new AwsBill(invoiceId, invoicingEntity, billingEntity, billType, payerAccountId, billingPeriodStartDate, billingPeriodStartDateTrunc, billingPeriodEndDate));
    }
}
