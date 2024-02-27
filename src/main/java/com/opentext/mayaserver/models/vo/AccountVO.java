package com.opentext.mayaserver.models.vo;

import lombok.Data;

import javax.validation.constraints.PositiveOrZero;

/**
 * @author Rajiv
 */
@Data
public class AccountVO {

    @PositiveOrZero(message = "Root account size can't be negative value")
    private int rootAccountSize;
    @PositiveOrZero(message = "Number of Accounts can't be negative value")
    private int numberOfAccounts;
    @PositiveOrZero(message = "Page size can't be negative value")
    private int pageSize;
}
