package com.opentext.mayaserver.models.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rajiv
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDataVO {

    private String id;

    private String rootAccount;

    private List<String> memberAccounts;
}
