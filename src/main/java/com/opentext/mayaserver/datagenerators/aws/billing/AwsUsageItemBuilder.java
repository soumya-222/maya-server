package com.opentext.mayaserver.datagenerators.aws.billing;

import com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.*;
import com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.builders.*;

import java.util.ArrayList;
import java.util.List;

public class AwsUsageItemBuilder {

    private AwsSink<AwsUsageItemBuilder> sink = new AwsSink<AwsUsageItemBuilder>() {
        @Override
        public AwsUsageItemBuilder setAwsIdentity(AwsIdentity identity) {
            AwsUsageItemBuilder.this.awsIdentity = identity;
            return AwsUsageItemBuilder.this;
        }

        @Override
        public AwsUsageItemBuilder setAwsBill(AwsBill bill) {
            AwsUsageItemBuilder.this.awsBill = bill;
            return AwsUsageItemBuilder.this;
        }

        @Override
        public AwsUsageItemBuilder setAwsLineItem(AwsLineItem lineItem) {
            AwsUsageItemBuilder.this.awsLineItem = lineItem;
            return AwsUsageItemBuilder.this;
        }

        @Override
        public AwsUsageItemBuilder setAwsProduct(AwsProduct product) {
            AwsUsageItemBuilder.this.awsProduct = product;
            return AwsUsageItemBuilder.this;
        }

        @Override
        public AwsUsageItemBuilder setAwsPricing(AwsPricing pricing) {
            AwsUsageItemBuilder.this.awsPricing = pricing;
            return AwsUsageItemBuilder.this;
        }

        @Override
        public AwsUsageItemBuilder setAwsSavingsPlan(AwsSavingsPlan savingsPlan) {
            AwsUsageItemBuilder.this.awsSavingsPlan = savingsPlan;
            return AwsUsageItemBuilder.this;
        }

        @Override
        public AwsUsageItemBuilder setAwsCostCategory(AwsCostCategory costCategory) {
            AwsUsageItemBuilder.this.awsCostCategory = costCategory;
            return AwsUsageItemBuilder.this;
        }

        @Override
        public AwsUsageItemBuilder setAwsDiscount(AwsDiscount discount) {
            AwsUsageItemBuilder.this.awsDiscount = discount;
            return AwsUsageItemBuilder.this;
        }

        @Override
        public AwsUsageItemBuilder setAwsReservation(AwsReservation reservation) {
            AwsUsageItemBuilder.this.awsReservation = reservation;
            return AwsUsageItemBuilder.this;
        }

        @Override
        public AwsUsageItemBuilder setAwsCustomTag(AwsCustomTag customTag) {
            AwsUsageItemBuilder.this.awsCustomTag = customTag;
            return AwsUsageItemBuilder.this;
        }

        @Override
        public AwsUsageItemBuilder setAwsTags(List<AwsTag> tags) {
            AwsUsageItemBuilder.this.tags = tags;
            return AwsUsageItemBuilder.this;
        }
    };

    private String id;
    private String manifestKey;
    private AwsIdentity awsIdentity;
    private AwsBill awsBill;
    private AwsLineItem awsLineItem;
    private AwsProduct awsProduct;
    private AwsPricing awsPricing;
    private AwsSavingsPlan awsSavingsPlan;
    private AwsCostCategory awsCostCategory;
    private AwsDiscount awsDiscount;
    private AwsReservation awsReservation;
    private AwsCustomTag awsCustomTag;
    private List<AwsTag> tags;
    private AwsIdentityBuilder<AwsUsageItemBuilder> awsIdentityBuilder;
    private AwsBillBuilder<AwsUsageItemBuilder> awsBillBuilder;
    private AwsLineItemBuilder<AwsUsageItemBuilder> awsLineItemBuilder;
    private AwsProductBuilder<AwsUsageItemBuilder> awsProductBuilder;
    private AwsPricingBuilder<AwsUsageItemBuilder> awsPricingBuilder;
    private AwsSavingsPlanBuilder<AwsUsageItemBuilder> awsSavingsPlanBuilder;
    private AwsCostCategoryBuilder<AwsUsageItemBuilder> awsCostCategoryBuilder;
    private AwsDiscountBuilder<AwsUsageItemBuilder> awsDiscountBuilder;
    private AwsReservationBuilder<AwsUsageItemBuilder> awsReservationBuilder;
    private AwsCustomTagBuilder<AwsUsageItemBuilder> awsCustomTagBuilder;
    public AwsUsageItemBuilder() {
        this.awsIdentityBuilder = new AwsIdentityBuilder<>(sink);
        this.awsBillBuilder = new AwsBillBuilder<>(sink);
        this.awsLineItemBuilder = new AwsLineItemBuilder<>(sink);
        this.awsProductBuilder = new AwsProductBuilder<>(sink);
        this.awsPricingBuilder = new AwsPricingBuilder<>(sink);
        this.awsSavingsPlanBuilder = new AwsSavingsPlanBuilder<>(sink);
        this.awsCostCategoryBuilder = new AwsCostCategoryBuilder<>(sink);
        this.awsDiscountBuilder = new AwsDiscountBuilder<>(sink);
        this.awsReservationBuilder = new AwsReservationBuilder<>(sink);
        this.awsCustomTagBuilder = new AwsCustomTagBuilder<>(sink);
    }

    public AwsUsageItemBuilder id(String id) {
        this.id = id;
        return this;
    }

    public AwsUsageItemBuilder manifestKey(String manifestKey) {
        this.manifestKey = manifestKey;
        return this;
    }

    public AwsIdentityBuilder<AwsUsageItemBuilder> awsIdentity() {
        return this.awsIdentityBuilder;
    }

    public AwsBillBuilder<AwsUsageItemBuilder> awsBill() {
        return this.awsBillBuilder;
    }

    public AwsLineItemBuilder<AwsUsageItemBuilder> awsLineItem() {
        return this.awsLineItemBuilder;
    }

    public AwsProductBuilder<AwsUsageItemBuilder> awsProduct() {
        return this.awsProductBuilder;
    }

    public AwsPricingBuilder<AwsUsageItemBuilder> awsPricing() {
        return this.awsPricingBuilder;
    }

    public AwsSavingsPlanBuilder<AwsUsageItemBuilder> awsSavingsPlan() {
        return this.awsSavingsPlanBuilder;
    }

    public AwsCostCategoryBuilder<AwsUsageItemBuilder> awsCostCategory() {
        return this.awsCostCategoryBuilder;
    }

    public AwsDiscountBuilder<AwsUsageItemBuilder> awsDiscount() {
        return this.awsDiscountBuilder;
    }

    public AwsReservationBuilder<AwsUsageItemBuilder> awsReservation() {
        return this.awsReservationBuilder;
    }

     public AwsCustomTagBuilder<AwsUsageItemBuilder> awsCustomTag() {
        return this.awsCustomTagBuilder;
    }

    public AwsUsageItemBuilder addTag(String bucket, String key, String columnKey, String val) {
        AwsTag tag = new AwsTag();
        tag.setBucket(bucket);
        tag.setKey(key);
        tag.setColumnKey(columnKey);
        tag.setVal(val);
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(tag);
        return this;
    }

    public AwsUsageItem build() {
        return new AwsUsageItem(id, manifestKey, awsIdentity, awsBill, awsLineItem, awsProduct, awsPricing, awsSavingsPlan, awsCostCategory, awsDiscount, awsReservation, awsCustomTag, tags);
    }

    public AwsUsageItem buildAll() {
        this.awsIdentityBuilder.build();
        this.awsBillBuilder.build();
        this.awsLineItemBuilder.build();
        this.awsProductBuilder.build();
        this.awsPricingBuilder.build();
        this.awsSavingsPlanBuilder.build();
        this.awsCostCategoryBuilder.build();
        this.awsDiscountBuilder.build();
        this.awsReservationBuilder.build();
        this.awsCustomTagBuilder.build();
        return this.build();
    }
}
