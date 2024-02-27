package com.opentext.mayaserver.exceptions;

public class MayaUseCaseNotFoundException extends MayaServerException {
    public MayaUseCaseNotFoundException(String message) {
        super(message);
    }

    public MayaUseCaseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
