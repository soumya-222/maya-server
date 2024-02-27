package com.opentext.mayaserver.datagenerators.aws.account;

import lombok.Data;

@Data
public class AwsDemoCloudAccount {
    private String accountId;
    private String name;
    private String accountScope;
    private String phaseId;
    private String rootAccountId;

    public boolean isRootAccount() {
        return "ROOT".equals(accountScope);
    }

}
