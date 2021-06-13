package com.electionController.exceptions;

import com.electionController.constants.ResponseCodes;

/*
 * Throw when item not found in Database
 * NOT TO BE USED FOR NON DB USECASES
* */
public class EntityNotFoundException extends RuntimeException {
    private String errorMessage;
    private int errorCode;

    public EntityNotFoundException(final String message) {
        this.errorMessage = message;
        this.errorCode = 404;
    }

    public EntityNotFoundException(final ResponseCodes responseCode) {
        this.errorCode = responseCode.getResponseCode();
        this.errorMessage = responseCode.getResponse();
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
