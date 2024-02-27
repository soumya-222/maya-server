package com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems;

public class AwsTag {
    private String accountId;
    private String manifestKey;
    private String bucket;
    private String key;
    private String columnKey;
    private String val;
    private String itemId;

    public AwsTag() {}

    public AwsTag(String accountId, String manifestKey, String bucket, String key, String columnKey, String val, String itemId) {
        this.accountId = accountId;
        this.manifestKey = manifestKey;
        this.bucket = bucket;
        this.key = key;
        this.columnKey = columnKey;
        this.val = val;
        this.itemId = itemId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getManifestKey() {
        return manifestKey;
    }

    public void setManifestKey(String manifestKey) {
        this.manifestKey = manifestKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
