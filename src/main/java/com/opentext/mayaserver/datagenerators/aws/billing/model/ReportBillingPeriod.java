package com.opentext.mayaserver.datagenerators.aws.billing.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReportBillingPeriod {
    private String start;
    private String end;
}
