package com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems;

public class AwsIdentity {
    private String lineItemId;
    private String timeInterval; // this one is in the format: <zulu time start>/<zulu time end>

    public AwsIdentity() {}

    public AwsIdentity(String lineItemId, String timeInterval) {
        this.lineItemId = lineItemId;
        this.timeInterval = timeInterval;
    }

    public String getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(String lineItemId) {
        this.lineItemId = lineItemId;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    @Override
    public String toString() {
        return "AwsIdentity{" +
                "lineItemId='" + lineItemId + '\'' +
                ", timeInterval='" + timeInterval + '\'' +
                '}';
    }
}
