package com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.builders;

import com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.AwsIdentity;

public class AwsIdentityBuilder<T> {
    private AwsSink<T> sink;

    private String lineItemId;
    private String timeInterval;

    public AwsIdentityBuilder(AwsSink<T> sink) {
        this.sink = sink;
    }

    public AwsIdentityBuilder<T> lineItemId(String lineItemId) {
        this.lineItemId = lineItemId;
        return this;
    }

    public AwsIdentityBuilder<T> timeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
        return this;
    }

    public T build() {
        return sink.setAwsIdentity(new AwsIdentity(lineItemId, timeInterval));
    }
}
