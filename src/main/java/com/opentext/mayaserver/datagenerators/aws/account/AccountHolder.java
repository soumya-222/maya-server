package com.opentext.mayaserver.datagenerators.aws.account;

import com.opentext.mayaserver.models.AccountState;
import lombok.Builder;

@Builder
public class AccountHolder {
    private String masterAccountId;
    private String masterAccountName;
    private String masterAccountArn;
    private String masterAccountDomainName;
    private String masterAccountEmail;
    private String masterAccountStatus;

    public String getMasterAccountId() {
        return masterAccountId;
    }

    public String getMasterAccountName() {
        return masterAccountName;
    }

    public String getMasterAccountArn() {
        return masterAccountArn;
    }

    public String getMasterAccountDomainName() {
        return masterAccountDomainName;
    }

    public String getMasterAccountEmail() {
        return masterAccountEmail;
    }

    public String getMasterAccountStatus() {
        try {
            if (masterAccountStatus != null) {
                return AccountState.valueOf(masterAccountStatus.toUpperCase()).name();
            }
        } catch (IllegalArgumentException e) { }
        return AccountState.ACTIVE.name();
    }
}
