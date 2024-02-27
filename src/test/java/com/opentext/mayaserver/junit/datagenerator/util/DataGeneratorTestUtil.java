package com.opentext.mayaserver.junit.datagenerator.util;

import com.opentext.mayaserver.datagenerators.aws.account.AccountHolder;
import com.opentext.mayaserver.utility.UseCaseServiceHelper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.opentext.mayaserver.utility.Constants.FILE_PATH_SEPARATOR;

public class DataGeneratorTestUtil {

    public AccountHolder getAccountHolder() {
        return AccountHolder.builder()
                .masterAccountName("ACME INC")
                .masterAccountArn("arn:aws:organizations::745496560928:account/o-r85162u2oc0/745496560928")
                .masterAccountId("745496560928")
                .masterAccountDomainName("acmeinc")
                .masterAccountEmail("acme-inc@acmeinc.com")
                .build();
    }

    public List<String> getMemberAccountIdList() {
        List<String> memberAccountIdList = new ArrayList<>();
        UseCaseServiceHelper useCaseServiceHelper = new UseCaseServiceHelper();
        for (int i = 0; i < 9; i++) {
            String memberAccountId = useCaseServiceHelper.generateRandomAccountId();
            memberAccountIdList.add(memberAccountId);
        }
        return memberAccountIdList;
    }

    public List<String> getListAccountFilePaths(String path) {
        List<String> listAccountFilePaths = new ArrayList<>();
        listAccountFilePaths.add(path + FILE_PATH_SEPARATOR + "nova-1_AWS_ListAccounts_1-5.json");
        listAccountFilePaths.add(path + FILE_PATH_SEPARATOR + "nova-1_AWS_ListAccounts_6-10.json");
        return listAccountFilePaths;
    }

    public LocalDate getLocalDate() {
        return LocalDate.parse("2023-06-14");
    }

    public String getOrganizationId() {
        return "o-r85162u2oc0";
    }

    public BigDecimal getTimeStamp() {
        return new BigDecimal("1655145000.021");
    }
}
