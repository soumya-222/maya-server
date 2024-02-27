package com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems;

public class AwsCostCategory {

    private String project;
    private String team;
    private String environment;

    public AwsCostCategory() {}

    public AwsCostCategory(String project, String team, String environment) {
        this.project = project;
        this.team = team;
        this.environment = environment;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    @Override
    public String toString() {
        return "AwsCostCategory{" +
                "project='" + project + '\'' +
                ", team='" + team + '\'' +
                ", environment='" + environment + '\'' +
                '}';
    }
}
