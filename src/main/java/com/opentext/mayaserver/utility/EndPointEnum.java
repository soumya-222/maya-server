package com.opentext.mayaserver.utility;

/**
 * @author Soumyaranjan
 */

public enum EndPointEnum {
    AWS_LIST_ACCOUNTS("ListAccounts"),
    AWS_DESCRIBE_ACCOUNT("DescribeAccount"),
    AWS_DESCRIBE_ORGANIZATION("DescribeOrganization");

    public final String value;
    EndPointEnum(String value){
        this.value = value;
    }
}
