package com.electionController.exceptions;

public class RestrictedActionException extends RuntimeException{
    private String errorMessage;
    private int errorCode;

    public RestrictedActionException(String message) {
        this.errorMessage = message;
        this.errorCode = 404;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
