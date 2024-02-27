package com.opentext.mayaserver.util;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public static final String HOST = "http://localhost:";
    public static final String BASE_URI = "/v1";
    public static final String CONTEXT_PATH = "/maya";
    public static final String USE_CASE = "usecase";
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int DEFAULT_NUMBER_OF_ACCOUNTS = 5000;
    public static final String DEFAULT_BILLING_START_DATE = "2023-01-01";
    public static final String DEFAULT_BILLING_END_DATE = "2023-03-31";
    public static final String DEFAULT_BILLING_DATE_OF_INVOICE = "2023-01-06";
    public static final int DEFAULT_WAIT = 3000;

    public static final List<String> DEMO_ROOT_ACCOUNT_ID_LIST = Arrays.asList("usageAccount1", "usageAccount3");
    public static final List<String> DEMO_MEMBER_ACCOUNT_ID_LIST = Arrays.asList("usageAccount2", "usageAccount4", "wpfo80dosug", "1lv3a6vkc36", "wiqb9u2ol7f");

}
