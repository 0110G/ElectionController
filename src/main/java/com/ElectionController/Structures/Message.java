package com.ElectionController.Structures;

public class Message {
    private String message;
    private int messageCode;

    public int getMessageCode() {
        return this.messageCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessageCode(int messageCode) {
        this.messageCode = messageCode;
    }
}
