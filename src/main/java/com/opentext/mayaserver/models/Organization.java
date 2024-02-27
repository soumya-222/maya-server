package com.opentext.mayaserver.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Organization {

    @JsonProperty("MasterAccountEmail")
    private String masterAccountEmail;

    @JsonProperty("MasterAccountArn")
    private String masterAccountArn;

    @JsonProperty("FeatureSet")
    private String featureSet;

    @JsonProperty("MasterAccountId")
    private String masterAccountId;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Arn")
    private String arn;

}
