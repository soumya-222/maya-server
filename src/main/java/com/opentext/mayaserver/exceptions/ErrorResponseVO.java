package com.opentext.mayaserver.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author Rajiv
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseVO {

    private String statusCode;
    private String reasonPhrase;
    private String message;
    private List<ErrorMessage> errorMessage;
    private String details;
    private String recommendedActions;
    private String errorSource;

}
