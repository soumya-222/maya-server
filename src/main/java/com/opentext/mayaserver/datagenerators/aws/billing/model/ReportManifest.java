package com.opentext.mayaserver.datagenerators.aws.billing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportManifest {
    private String assemblyId;
    private String account;
    private List<ReportColumn> columns;
    private String charset;
    private String compression;
    private String contentType;
    private String reportId;
    private String reportName;
    private ReportBillingPeriod billingPeriod;
    private String bucket;
    private List<String> reportKeys;
    private List<String> additionalArtifactKeys;
}
