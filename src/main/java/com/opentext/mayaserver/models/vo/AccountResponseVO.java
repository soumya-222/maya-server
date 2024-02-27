package com.opentext.mayaserver.models.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.opentext.mayaserver.models.AccountData;
import lombok.Data;

import java.util.List;

/**
 * @author Soumyaranjan
 */
@Data
public class AccountResponseVO {
    List<AccountDataVO> accountData;
}
