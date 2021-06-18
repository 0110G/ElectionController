package com.electionController.exceptions;

import com.electionController.constants.ResponseCodes;

/**
* Throw when user credentials are incorrect
* ENDPOINT Exception
**/
public class InvalidCredentialException extends RuntimeException{
    private final String errorMessage;
    private final String errorDetails;
    private final int errorCode;

    public InvalidCredentialException(final String message) {
        this.errorMessage = message;
        this.errorDetails = message;
        this.errorCode = 404;
    }

    public InvalidCredentialException(final int errorCode, final String errorMessage) {
        this.errorMessage = errorMessage;
        this.errorDetails = "";
        this.errorCode = errorCode;
    }

    public InvalidCredentialException(final int errorCode, final String errorMessage, final String errorDetails) {
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
