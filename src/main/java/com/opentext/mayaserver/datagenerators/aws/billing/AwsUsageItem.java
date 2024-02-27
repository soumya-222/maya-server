package com.opentext.mayaserver.datagenerators.aws.billing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.*;
import com.opentext.mayaserver.exceptions.MayaServerException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AwsUsageItem {

    private String id;
    private String manifestKey;

    
    @JsonUnwrapped
    private AwsIdentity identity;
    
    @JsonUnwrapped
    private AwsBill bill;
    
    @JsonUnwrapped
    private AwsLineItem lineItem;
    
    @JsonUnwrapped
    private AwsProduct product;
    
    @JsonUnwrapped
    private AwsPricing pricing;
    
    @JsonUnwrapped
    private AwsSavingsPlan savingsPlan;
    
    @JsonUnwrapped
    private AwsCostCategory costCategory;
    
    @JsonUnwrapped
    private AwsDiscount discount;
    
    @JsonUnwrapped
    private AwsReservation reservation;

    @JsonUnwrapped
    private AwsCustomTag customTag;
    @JsonIgnore
    private List<AwsTag> tags;

    public AwsUsageItem() {
    }

    public AwsUsageItem(String id, String manifestKey, AwsIdentity identity, AwsBill bill, AwsLineItem lineItem, AwsProduct product, AwsPricing pricing, AwsSavingsPlan savingsPlan, AwsCostCategory costCategory, AwsDiscount discount, AwsReservation reservation, AwsCustomTag customTag, List<AwsTag> tags) {
        this.id = id;
        this.manifestKey = manifestKey;
        this.identity = identity;
        this.bill = bill;
        this.lineItem = lineItem;
        this.product = product;
        this.pricing = pricing;
        this.savingsPlan = savingsPlan;
        this.costCategory = costCategory;
        this.discount = discount;
        this.reservation = reservation;
        this.customTag = customTag;
        this.tags = tags;
    }

    public static AwsUsageItemBuilder builder() {
        return new AwsUsageItemBuilder();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManifestKey() {
        return manifestKey;
    }

    public void setManifestKey(String manifestKey) {
        this.manifestKey = manifestKey;
    }

    public AwsIdentity getIdentity() {
        return identity;
    }

    public void setIdentity(AwsIdentity identity) {
        this.identity = identity;
    }

    public AwsBill getBill() {
        return bill;
    }

    public void setBill(AwsBill bill) {
        this.bill = bill;
    }

    public AwsLineItem getLineItem() {
        return lineItem;
    }

    public void setLineItem(AwsLineItem lineItem) {
        this.lineItem = lineItem;
    }

    public AwsProduct getProduct() {
        return product;
    }

    public void setProduct(AwsProduct product) {
        this.product = product;
    }

    public AwsPricing getPricing() {
        return pricing;
    }

    public void setPricing(AwsPricing pricing) {
        this.pricing = pricing;
    }

    public AwsSavingsPlan getSavingsPlan() {
        return savingsPlan;
    }

    public void setSavingsPlan(AwsSavingsPlan savingsPlan) {
        this.savingsPlan = savingsPlan;
    }

    public AwsCostCategory getCostCategory() {
        return costCategory;
    }

    public void setCostCategory(AwsCostCategory costCategory) {
        this.costCategory = costCategory;
    }

    public AwsDiscount getDiscount() {
        return discount;
    }

    public void setDiscount(AwsDiscount discount) {
        this.discount = discount;
    }

    public AwsReservation getReservation() {
        return reservation;
    }

    public void setReservation(AwsReservation reservation) {
        this.reservation = reservation;
    }
    public AwsCustomTag getCustomTag() {
        return customTag;
    }

    public void setCustomTag(AwsCustomTag customTag) {
        this.customTag = customTag;
    }

     public List<AwsTag> getTags() {
        return tags;
    }

    public void setTags(List<AwsTag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "AwsUsageItem{" +
                "identity=" + identity +
                ", bill=" + bill +
                ", lineItem=" + lineItem +
                ", product=" + product +
                ", pricing=" + pricing +
                ", savingsPlans=" + savingsPlan +
                ", costCategory=" + costCategory +
                ", discount=" + discount +
                ", reservation=" + reservation +
                ", customTags=" + customTag +
                '}';
    }

    public Map<String, Object> toFlatMap() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("id", getId());
            data.put("manifestKey", getManifestKey());
            putSubvalues(data, "identity", this.getIdentity());
            putSubvalues(data, "bill", this.getBill());
            putSubvalues(data, "lineItem", this.getLineItem());
            putSubvalues(data, "product", this.getProduct());
            putSubvalues(data, "pricing", this.getPricing());
            putSubvalues(data, "savingsPlans", this.getSavingsPlan());
            putSubvalues(data, "costCategory", this.getCostCategory());
            putSubvalues(data, "discount", this.getDiscount());
            putSubvalues(data, "reservation", this.getReservation());
            putSubvalues(data, "custom", this.getCustomTag());
            return data;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MayaServerException("failed to flatten usageItem");
        }
    }

    private void putSubvalues(Map<String, Object> map, String prefix, Object bean) throws InvocationTargetException, IllegalAccessException {
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);
        for (PropertyDescriptor descriptor : beanWrapper.getPropertyDescriptors()) {
            map.put(prefix + "_" + descriptor.getName(), descriptor.getReadMethod().invoke(bean));
        }
    }
}