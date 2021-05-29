package com.electionController.constants;

public enum ResponseCodes {

    SUCCESS("success", 200),
    INVALID_VOTER_CREDENTIALS("invalid_voterId_or_password", 401),
    NULL_RESPONSE("null_response", 403),
    ENTITY_NOT_FOUND("entity_not_found", 404);

    private int responseCode;
    private String response;

    ResponseCodes(String response, int responseCode) {
        this.responseCode = responseCode;
        this.response = response;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public String getResponse() {
        return this.response;
    }
}
