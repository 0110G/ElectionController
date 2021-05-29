package com.electionController.exceptions;

import com.electionController.constants.ResponseCodes;

public class RestrictedActionException extends RuntimeException{
    private String errorMessage;
    private int errorCode;

    public RestrictedActionException(String message) {
        this.errorMessage = message;
        this.errorCode = 404;
    }

    public RestrictedActionException(ResponseCodes responseCode) {
        this.errorMessage = responseCode.getResponse();
        this.errorCode = responseCode.getResponseCode();
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
