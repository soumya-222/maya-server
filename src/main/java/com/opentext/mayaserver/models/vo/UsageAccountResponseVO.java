package com.opentext.mayaserver.models.vo;

import com.opentext.mayaserver.datagenerators.aws.billing.model.BillCostData;
import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class UsageAccountResponseVO {
    Map<String, List<BillCostData>> data;
}
