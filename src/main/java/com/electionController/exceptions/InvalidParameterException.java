package com.electionController.exceptions;

/*
*  Throw when incorrect parameters, while query validations
*  ENDPOINT exception
* */
public class InvalidParameterException extends RuntimeException {
    private final String errorMessage;
    private final String errorDetails;
    private final int errorCode;

    public InvalidParameterException(final String message) {
        this.errorMessage = message;
        this.errorDetails = message;
        this.errorCode = 404;
    }

    public InvalidParameterException(final int errorCode, final String errorMessage, final String errorDetails) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
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
