package com.opentext.mayaserver.models.vo;

import com.opentext.mayaserver.datagenerators.aws.billing.model.BillCostData;
import lombok.Data;

import java.util.List;
@Data
public class ManagementAccountResponseVO {
    List<BillCostData> data;
}
