package com.opentext.mayaserver.datagenerators.aws.billing.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportColumn {
    private String category;
    private String name;
    private String type;
}
