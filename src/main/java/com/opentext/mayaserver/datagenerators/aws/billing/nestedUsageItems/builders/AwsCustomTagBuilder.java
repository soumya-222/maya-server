package com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.builders;

import com.opentext.mayaserver.datagenerators.aws.billing.nestedUsageItems.AwsCustomTag;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

public class AwsCustomTagBuilder<T> {
    private AwsSink<T> sink;

    private String tagColumn0;
    private String tagColumn1;
    private String tagColumn2;
    private String tagColumn3;
    private String tagColumn4;
    private String tagColumn5;
    private String tagColumn6;
    private String tagColumn7;
    private String tagColumn8;
    private String tagColumn9;

    public AwsCustomTagBuilder(AwsSink<T> sink) {
        this.sink = sink;
    }

    public AwsCustomTagBuilder<T> tagColumn0(String tagColumn0) {
        this.tagColumn0 = tagColumn0;
        return this;
    }

    public AwsCustomTagBuilder<T> tagColumn1(String tagColumn1) {
        this.tagColumn1 = tagColumn1;
        return this;
    }

    public AwsCustomTagBuilder<T> tagColumn2(String tagColumn2) {
        this.tagColumn2 = tagColumn2;
        return this;
    }

    public AwsCustomTagBuilder<T> tagColumn3(String tagColumn3) {
        this.tagColumn3 = tagColumn3;
        return this;
    }

    public AwsCustomTagBuilder<T> tagColumn4(String tagColumn4) {
        this.tagColumn4 = tagColumn4;
        return this;
    }

    public AwsCustomTagBuilder<T> tagColumn5(String tagColumn5) {
        this.tagColumn5 = tagColumn5;
        return this;
    }

    public AwsCustomTagBuilder<T> tagColumn6(String tagColumn6) {
        this.tagColumn6 = tagColumn6;
        return this;
    }

    public AwsCustomTagBuilder<T> tagColumn7(String tagColumn7) {
        this.tagColumn7 = tagColumn7;
        return this;
    }

    public AwsCustomTagBuilder<T> tagColumn8(String tagColumn8) {
        this.tagColumn8 = tagColumn8;
        return this;
    }

    public AwsCustomTagBuilder<T> tagColumn9(String tagColumn9) {
        this.tagColumn9 = tagColumn9;
        return this;
    }

    public AwsCustomTagBuilder<T> column(String verticaColumnName, String value) throws IllegalAccessException {
        //Reflect which field for the given vertica column name and set the value of the field
        Class<?> c = this.getClass();
        Optional<Field> fieldOptional = Arrays.stream(c.getDeclaredFields())
                .filter(field -> verticaColumnName.equals("custom_" + field.getName()))
                .findFirst();
        if(fieldOptional.isPresent()) {
            fieldOptional.get().set(this, value);
        }
        return this;
    }

    public T build() {
        return sink.setAwsCustomTag(
                new AwsCustomTag(tagColumn0, tagColumn1, tagColumn2, tagColumn3, tagColumn4, tagColumn5, tagColumn6, tagColumn7, tagColumn8, tagColumn9)
        );
    }
}

