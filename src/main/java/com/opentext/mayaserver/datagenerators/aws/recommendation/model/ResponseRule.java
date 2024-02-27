package com.opentext.mayaserver.datagenerators.aws.recommendation.model;

public class ResponseRule {
    private String target;
    private String modifier;
    private String value;
    private Boolean invert = false;
    private String operator;

    public ResponseRule(String target, String modifier, String value, String operator) {
        this.target = target;
        this.modifier = modifier;
        this.value = value;
        this.operator = operator;
        invert = false;
    }

    public String getTarget() {
        return target;
    }

    public String getModifier() {
        return modifier;
    }

    public String getValue() {
        return value;
    }

    public Boolean getInvert() {
        return invert;
    }

    public String getOperator() {
        return operator;
    }
}
