package com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.builders;

import com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.*;

import java.util.List;

// https://dzone.com/articles/nested-fluent-builders
// Understanding the Sink nested builder pattern
public interface AwsSink<T> {
    T setAwsIdentity(AwsIdentity identity);

    T setAwsBill(AwsBill bill);

    T setAwsLineItem(AwsLineItem lineItem);

    T setAwsProduct(AwsProduct product);

    T setAwsPricing(AwsPricing pricing);

    T setAwsSavingsPlan(AwsSavingsPlan savingsPlans);
  
    T setAwsCostCategory(AwsCostCategory costCategory);
  
    T setAwsDiscount(AwsDiscount discount);

    T setAwsReservation(AwsReservation reservation);

    T setAwsCustomTag(AwsCustomTag customTag);

    T setAwsTags(List<AwsTag> tags);
}
