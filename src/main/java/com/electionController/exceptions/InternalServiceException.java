package com.electionController.exceptions;

/*
*  Throw on unknown exceptions
* */
public class InternalServiceException extends RuntimeException{
    private final String errorMessage;
    private final String errorDetails;
    private final int errorCode;

    public InternalServiceException(final String message) {
        this.errorMessage = message;
        this.errorDetails = "";
        this.errorCode = 404;
    }

    public InternalServiceException(final int errorCode, final String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorDetails = "";
    }

    public InternalServiceException(final int errorCode, final String errorMessage, final String errorDetails) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.errorDetails = errorDetails;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getErrorDetails() {
        return this.errorDetails;
    }
}
