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
public class Account {
    @JsonProperty("Arn")
    private String arn;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("JoinedMethod")
    private String joinedMethod;

    @JsonProperty("JoinedTimestamp")
    private Number joinedTimestamp;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Status")
    private String status;

}
