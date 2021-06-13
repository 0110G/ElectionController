package com.electionController.exceptions;

import com.electionController.constants.ResponseCodes;

/*
*  Throw on unknown exceptions
* */
public class InternalServiceException extends RuntimeException{
    private String errorMessage;
    private int errorCode;

    public InternalServiceException(String message) {
        this.errorMessage = message;
        this.errorCode = 404;
    }

    public InternalServiceException(final ResponseCodes responseCode) {
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
