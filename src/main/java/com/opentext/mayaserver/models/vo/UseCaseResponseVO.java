package com.opentext.mayaserver.models.vo;

import lombok.Data;

/**
 * @author Rajiv
 */
@Data
public class UseCaseResponseVO {
    private String useCaseName;
    private String useCaseId;
    private StateEnumVO state;
    private String accountURL;
    private String billingURL;
    private String recommendationURL;
    private UseCaseVO payload;
}
