package com.opentext.mayaserver.models.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
public class CloudCostDataResponseVO {
    @JsonInclude(NON_NULL)
    ManagementAccountResponseVO managementAccountIdCostData;
    @JsonInclude(NON_NULL)
    UsageAccountResponseVO usageAccountIdCostData;

}
