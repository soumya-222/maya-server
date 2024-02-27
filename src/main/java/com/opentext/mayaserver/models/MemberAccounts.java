package com.opentext.mayaserver.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberAccounts {

    @JsonProperty("Accounts")
    private List<Account> account;

    @JsonProperty("NextToken")
    private String nextToken;
}
