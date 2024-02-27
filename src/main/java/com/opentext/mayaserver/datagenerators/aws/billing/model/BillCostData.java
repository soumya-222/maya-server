package com.opentext.mayaserver.datagenerators.aws.billing.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author Rajiv
 */
@Builder
@Data
public class BillCostData {

    private String accountId;
    private String accountType;
    private double amortizedCost;
    private double blendedCost;
    private double unblendedCost;
    private String billPeriod;
}
