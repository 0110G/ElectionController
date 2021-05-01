package com.ElectionController.Exceptions;

public class EntityNotFoundException extends Throwable {
    private String errorMessage;
    private int errorCode;

    public EntityNotFoundException(String message) {
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
