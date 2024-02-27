package com.opentext.mayaserver.datagenerators.aws.billing;

import java.util.ArrayList;
import java.util.List;

public enum LineItemType {
    /*
     * For tax line items, it has been observed that tax line items are rarely seen except
     * when amazon's telecom services such as amazon chime have been utilized in that billing period;
     * when those services are used, the tax costs are typically seen in the range of < $1.00 (USD)
     * for a billing month even when there is > $100,000 (USD) in usage spend for that month.
     * To try and match that proportion, this returns a tax amount that is 10^-6 the regular usage cost for
     * every usage line item generated.
     */
    TAX (Value.TAX, 0.000001),
    /*
     * For Enterprise Discount Program line items, the enterprise discount cost comes out to roughly -20%
     * of the regular (on-demand) usage cost for a given billing period
     */
    ENTERPRISE_DISCOUNT (Value.ENTERPRISE_DISCOUNT, -0.2),
    /*
     * For Savings Plan line items, the negation is the negative value of covered-usage & for every
     * covered-usage line item generated a negation line item goes along with it. Since covered-usage
     * line item is the default of Savings Plan, we can just use the line item DEFAULT below in its
     * place.
     */
    SAVINGS_PLAN_NEGATION(Value.SAVINGS_PLAN_NEGATION, -1.0),
    // The default LineItemType exists for all costs that do not require special use case multipliers.
    DEFAULT(null, 1.0);

    private final String value;
    private final Double costMultiplier;

    LineItemType(String value, Double costMultiplier) {
        this.value = value;
        this.costMultiplier = costMultiplier;
    };

    public static class Value {
        public static final String TAX = "Tax";
        public static final String ENTERPRISE_DISCOUNT = "EdpDiscount";
        public static final String SAVINGS_PLAN_NEGATION = "SavingsPlanNegation";
    }

    public String getValue(){
        return this.value;
    }

    public Double getCostMultiplier() {
        return costMultiplier;
    }

    public static List<LineItemType> getOnDemandValues() {
        List<LineItemType> lineItemTypes = new ArrayList<>();

        lineItemTypes.add(LineItemType.DEFAULT);
        lineItemTypes.add(LineItemType.ENTERPRISE_DISCOUNT);
        lineItemTypes.add(LineItemType.TAX);

        return lineItemTypes;
    }

    public static List<LineItemType> getSavingsPlanValues() {
        List<LineItemType> lineItemTypes = new ArrayList<>();

        lineItemTypes.add(LineItemType.DEFAULT);
        lineItemTypes.add(LineItemType.SAVINGS_PLAN_NEGATION);

        return lineItemTypes;
    }
}
