package com.ElectionController.Exceptions;

public class InvalidCredentialException extends RuntimeException{
    private String errorMessage;
    private int errorCode;

    public InvalidCredentialException(String message) {
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
