package com.opentext.mayaserver.exceptions;

import java.util.List;

/**
 * @author Rajiv
 */
public class ApiException extends Exception {
    private int code;

    public ApiException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public static ErrorResponseVO invalidUseCasePayloadException(List message, String... array) {
        ErrorResponseVO errorResponseVO = new ErrorResponseVO();
        errorResponseVO.setErrorMessage(message);
        if (array.length >= 2) {
            errorResponseVO.setStatusCode(array[0]);
            errorResponseVO.setReasonPhrase(array[1]);
        }
        return errorResponseVO;
    }

    public static ErrorResponseVO mayaException(String message, String statusCode, String source) {

        ErrorResponseVO errorResponseVO = new ErrorResponseVO();
        errorResponseVO.setMessage(message);
        errorResponseVO.setStatusCode(statusCode);
        errorResponseVO.setErrorSource(source);
        return errorResponseVO;
    }

    public static ErrorResponseVO recordNotFound(String message, String statusCode, String recommendedActions) {
        ErrorResponseVO errorResponseVO = new ErrorResponseVO();
        errorResponseVO.setMessage(message);
        errorResponseVO.setStatusCode(statusCode);
        errorResponseVO.setRecommendedActions(recommendedActions);
        return errorResponseVO;
    }

    public static ErrorResponseVO useCaseCanNotBeDeletedException(String message, String statusCode, String recommendedActions) {
        ErrorResponseVO errorResponseVO = new ErrorResponseVO();
        errorResponseVO.setMessage(message);
        errorResponseVO.setStatusCode(statusCode);
        errorResponseVO.setRecommendedActions(recommendedActions);
        return errorResponseVO;
    }
}
