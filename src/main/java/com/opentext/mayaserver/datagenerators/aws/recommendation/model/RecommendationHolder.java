package com.opentext.mayaserver.datagenerators.aws.recommendation.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class RecommendationHolder {

    private String name;
    private String description;
    private Map<String, String> headers;
    private List<ResponseRule> rules;
    private String endpointType;
    private String dataFilePath;

    public RecommendationHolder(String name, String description, Map<String, String> headers, List<ResponseRule> rules, String endpointType, String dataFilePath) {
        this.name = name;
        this.description = description;
        this.headers = (headers != null)? headers : new HashMap<>();
        this.rules = (rules != null)? rules : new ArrayList<>();
        this.endpointType = endpointType;
        this.dataFilePath = dataFilePath;
    }

    @Override
    public String toString() {
        return "(" + name + ", "
                + description + ", "
                + endpointType + ", "
                + dataFilePath + ")";
    }
}
