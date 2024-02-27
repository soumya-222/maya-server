package com.opentext.mayaserver.models.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.opentext.mayaserver.utility.CheckValidName;
import com.opentext.mayaserver.utility.CloudProvider;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * @author Rajiv
 */

@Data
public class UseCaseVO {

    @NotEmpty(message = "Usecase name can't be empty")
    private String useCaseName;

    @CheckValidName(enumClass = CloudProvider.class, message = "Invalid cloud provider. Supported cloud providers are : aws,azure and gcp")
    private String cloudProvider;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Valid
    private AccountVO account;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Valid
    private BillingVO billing;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isDemoModeEnabled;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String demoRootAccountId;

    @Valid
    private ModeEnumVO mode;            // generate/demo/byod

    @JsonIgnore
    public boolean isDemoModeEnabled() {
        return (isDemoModeEnabled != null)? isDemoModeEnabled : false;
    }
}
