package com.ElectionController.Exceptions;

import com.ElectionController.Constants.ResponseCodes;

public class InvalidCredentialException extends RuntimeException{
    private String errorMessage;
    private int errorCode;

    public InvalidCredentialException(String message) {
        this.errorMessage = message;
        this.errorCode = 404;
    }

    public InvalidCredentialException(final ResponseCodes responseCode) {
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
