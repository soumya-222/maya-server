package com.opentext.mayaserver.utility;

import com.opentext.mayaserver.models.vo.AccountVO;
import com.opentext.mayaserver.models.vo.BillingVO;

import java.time.LocalDate;

import static com.opentext.mayaserver.utility.Constants.*;

/**
 * @author Rajiv
 */
public class DefaultData {

    public static final int ROOT_ACCOUNT_SIZE = 1;
    public static final int NUMBER_OF_ACCOUNT = 5000;
    public static final int PAGE_SIZE = 20;
    public static final int DEFAULT_RECORDS_PER_CSV = 500000;

    public static AccountVO getAccountData() {
        AccountVO account = new AccountVO();
        account.setRootAccountSize(ROOT_ACCOUNT_SIZE);
        account.setNumberOfAccounts(NUMBER_OF_ACCOUNT);
        account.setPageSize(PAGE_SIZE);
        return account;
    }

    public static BillingVO getBillingData() {
        BillingVO billingVO = new BillingVO();
        billingVO.setRecordsPerDay(DEFAULT_RECORDS_PER_DAY);
        billingVO.setStartDate(CommonUtils.toLocalDateFormat(DEFAULT_BILLING_START_DATE));
        billingVO.setEndDate(CommonUtils.toLocalDateFormat(DEFAULT_BILLING_END_DATE));
        billingVO.setDayOfInvoice(CommonUtils.toLocalDateFormat(DEFAULT_BILLING_DATE_OF_INVOICE));
        billingVO.setCurrentDate(LocalDate.now());
        return billingVO;
    }

    /**
     * Overriding individual attribute if not provided by user.
     *
     * @param account
     */
    public static void overrideAccountPropertyData(AccountVO account) {
        if (account.getRootAccountSize() == 0) {
            account.setRootAccountSize(ROOT_ACCOUNT_SIZE);
        }
        if (account.getNumberOfAccounts() == 0) {
            account.setNumberOfAccounts(NUMBER_OF_ACCOUNT);
        }
        if (account.getPageSize() == 0) {
            account.setPageSize(PAGE_SIZE);
        }
    }

    /**
     * Overriding individual attribute if not provided by user.
     *
     * @param account
     * @param demoRootAccountId
     */
    public static void overrideDemoAccountPropertyData(AccountVO account, int memberAccountSize) {
        account.setRootAccountSize(ROOT_ACCOUNT_SIZE);
        account.setNumberOfAccounts(memberAccountSize);
    }

    public static void overrideBillingPropertyData(BillingVO billingVO) {
        if (billingVO.getStartDate() == null || billingVO.getEndDate() == null) {
            billingVO.setStartDate(CommonUtils.toLocalDateFormat(DEFAULT_BILLING_START_DATE));
            billingVO.setEndDate(CommonUtils.toLocalDateFormat(DEFAULT_BILLING_END_DATE));
        }
        if (billingVO.getRecordsPerDay() == 0) {
            billingVO.setRecordsPerDay(DEFAULT_RECORDS_PER_DAY);
        }
        if (billingVO.getDayOfInvoice() == null) {
            billingVO.setDayOfInvoice(CommonUtils.toLocalDateFormat(DEFAULT_BILLING_DATE_OF_INVOICE));
        }
        if (billingVO.getCurrentDate() == null) {
            billingVO.setCurrentDate(LocalDate.now());
        }
    }
}
