package com.opentext.mayaserver.exceptions;

/**
 * @author Rajiv
 */
public class MayaServerException extends RuntimeException {
    public MayaServerException(String message) {
        super(message);
    }

    public MayaServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
