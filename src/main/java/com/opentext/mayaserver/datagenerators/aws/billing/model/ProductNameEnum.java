package com.opentext.mayaserver.datagenerators.aws.billing.model;

/**
 * @author Rajiv
 */
public enum ProductNameEnum {
    AWS_BACKUP("AWS Backup"),
    AWS_CONFIG("AWS Config"),
    AWS_COST_EXPLORER("AWS Cost Explorer"),
    AWS_DATA_TRANSFER("AWS Data Transfer"),
    AWS_KEY_SERVICE("AWS Key Management Service"),
    AWS_LAMBDA("AWS Lambda"),
    AMAZON_ECR("Amazon EC2 Container Registry (ECR)"),
    AMAZON_ECS("Amazon Elastic Container Service for Kubernetes"),
    AMAZON_EFS("Amazon Elastic File System"),
    AMAZON_GUARD_DUTY("Amazon GuardDuty"),
    AMAZON_ROUTE_53("Amazon Route 53"),
    AMAZON_SIMPLE_STORAGE_SERVICE("Amazon Simple Storage Service"),
    AMAZON_CLOUD_WATCH("AmazonCloudWatch"),
    ELASTIC_LOAD_BALANCING("Elastic Load Balancing"),
    AMAZON_ECC("Amazon Elastic Compute Cloud"),
    AMAZON_RELATIONAL_DB("Amazon Relational Database Service");

    private String value;

    ProductNameEnum(String value) {
        this.value = value;
    }

    public String getProductName() {
        return this.value;
    }

}
