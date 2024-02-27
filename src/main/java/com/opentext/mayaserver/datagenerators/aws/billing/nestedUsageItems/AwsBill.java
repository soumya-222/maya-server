package com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems;


import java.util.Date;

public class AwsBill {
    private String invoiceId;
    private String invoicingEntity;
    private String billingEntity;
    private String billType;
    private String payerAccountId;
    private Date billingPeriodStartDate;
    private Date billingPeriodStartDateTrunc;
    private Date billingPeriodEndDate;

    public AwsBill() {}

    public AwsBill(String invoiceId, String invoicingEntity, String billingEntity, String billType, String payerAccountId, Date billingPeriodStartDate, Date billingPeriodStartDateTrunc, Date billingPeriodEndDate) {
        this.invoiceId = invoiceId;
        this.invoicingEntity = invoicingEntity;
        this.billingEntity = billingEntity;
        this.billType = billType;
        this.payerAccountId = payerAccountId;
        this.billingPeriodStartDate = billingPeriodStartDate;
        this.billingPeriodStartDateTrunc = billingPeriodStartDateTrunc;
        this.billingPeriodEndDate = billingPeriodEndDate;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoicingEntity() {
        return invoicingEntity;
    }

    public void setInvoicingEntity(String invoicingEntity) {
        this.invoicingEntity = invoicingEntity;
    }

    public String getBillingEntity() {
        return billingEntity;
    }

    public void setBillingEntity(String billingEntity) {
        this.billingEntity = billingEntity;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getPayerAccountId() {
        return payerAccountId;
    }

    public void setPayerAccountId(String payerAccountId) {
        this.payerAccountId = payerAccountId;
    }

    public Date getBillingPeriodStartDate() { return billingPeriodStartDate; }

    public void setBillingPeriodStartDate(Date billingPeriodStartDate) { this.billingPeriodStartDate = billingPeriodStartDate; }

    public Date getBillingPeriodStartDateTrunc() {
        return billingPeriodStartDateTrunc;
    }

    public void setBillingPeriodStartDateTrunc(Date billingPeriodStartDateTrunc) {
        this.billingPeriodStartDateTrunc = billingPeriodStartDateTrunc;
    }

    public Date getBillingPeriodEndDate() {
        return billingPeriodEndDate;
    }

    public void setBillingPeriodEndDate(Date billingPeriodEndDate) { this.billingPeriodEndDate = billingPeriodEndDate; }

    @Override
    public String toString() {
        return "AwsBill{" +
                "invoiceId='" + invoiceId + '\'' +
                ", billingEntity='" + billingEntity + '\'' +
                ", billType='" + billType + '\'' +
                ", payerAccountId='" + payerAccountId + '\'' +
                ", billingPeriodStartDate='" + billingPeriodStartDate + '\'' +
                ", billingPeriodEndDate='" + billingPeriodEndDate + '\'' +
                '}';
    }}
