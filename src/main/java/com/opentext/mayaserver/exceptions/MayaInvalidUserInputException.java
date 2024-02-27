package com.opentext.mayaserver.exceptions;

/**
 * @author Soumyaranjan
 */
public class MayaInvalidUserInputException extends RuntimeException {
    public MayaInvalidUserInputException(String message) {
        super(message);
    }

    public MayaInvalidUserInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
