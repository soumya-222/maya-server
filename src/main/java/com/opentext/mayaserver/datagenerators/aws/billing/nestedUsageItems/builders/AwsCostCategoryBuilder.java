package com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.builders;

import com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.AwsCostCategory;

public class AwsCostCategoryBuilder<T> {
    private AwsSink<T> sink;

    private String project;
    private String team;
    private String environment;

    public AwsCostCategoryBuilder(AwsSink<T> sink) {
        this.sink = sink;
    }

    public AwsCostCategoryBuilder<T> project(String project) {
        this.project = project;
        return this;
    }

    public AwsCostCategoryBuilder<T> team(String team) {
        this.team = team;
        return this;
    }

    public AwsCostCategoryBuilder<T> environment(String environment) {
        this.environment = environment;
        return this;
    }

    public T build() {
        return sink.setAwsCostCategory(new AwsCostCategory(project, team, environment));
    }

}
