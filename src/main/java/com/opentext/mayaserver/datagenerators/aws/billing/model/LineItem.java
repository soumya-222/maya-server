package com.opentext.mayaserver.datagenerators.aws.billing.model;
/**
 * @author Rajiv
 */
import com.opentext.mayaserver.datagenerators.aws.billing.LineItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LineItem {

    private String id;
    private String memberAccountID;
    private String productCode;
    private String opeartion;  // Enum;
    private LineItemType lineItemType;
    private String billingPeriodStartDate;
    private String billingPeriodEndDate;

}
