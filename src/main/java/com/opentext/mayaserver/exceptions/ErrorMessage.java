package com.opentext.mayaserver.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ErrorMessage {
    @JsonProperty("codes")
    private String[] codes;

    @JsonProperty("arguments")
    private List arguments;

    @JsonProperty("defaultMessage")
    private String defaultMessage;

    @JsonProperty("objectName")
    private String objectName;

    @JsonProperty("field")
    private String field;

    @JsonProperty("rejectedValue")
    private String rejectedValue;

    @JsonProperty("bindingFailure")
    private boolean bindingFailure;

    @JsonProperty("code")
    private String code;

}


