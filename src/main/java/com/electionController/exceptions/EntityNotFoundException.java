package com.electionController.exceptions;


/*
 * Throw when item not found in Database
 * NOT TO BE USED FOR NON DB USECASES
* */
public class EntityNotFoundException extends RuntimeException {
    private final String errorMessage;
    private final String errorDetails;
    private final int errorCode;

    public EntityNotFoundException(final String message) {
        this.errorMessage = message;
        this.errorDetails = "";
        this.errorCode = 404;
    }

    public EntityNotFoundException(final int errorCode, final String errorMessage, final String errorDetails) {
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
