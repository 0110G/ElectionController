package com.electionController.constants;

public enum ResponseCodes {

    SUCCESS("success", 200),
    NULL_RESPONSE("null_response", 201),

    // InvalidParamException Range 300 - 399
    NULL_QUERY("NULL_QUERY", 301),
    INVALID_VOTER("INVALID_VOTER", 302),
    CANDIDATE_NOT_PART_OF_POST("INVALID_CANDIDATE/POST", 303),
    POST_NOT_PART_OF_ELECTION("INVALID_POST/ELECTION", 304),

    // InternalServiceException [400 - 499]
    INTERNAL_ERROR("INTERNAL_ERROR_OCCURRED", 401),

    // RestrictedActionException [500 - 599]
    NOT_ELIGIBLE_TO_VOTE("NOT_ELIGIBLE_TO_VOTE", 501),
    NOT_ADMIN("NOT_ADMIN", 502),
    NOT_ELIGIBLE_VIEWER("NOT_ELIGIBLE_TO_VIEW", 503),

    // EntityNotFoundException [600 - 699]
    ENTITY_NOT_FOUND("ENTITY_NOT_FOUND", 601),

    // InvalidCredentialExceptions
    INVALID_VOTER_CREDENTIALS("INVALID_VOTERID/PASSWORD", 701);


    private final int responseCode;
    private final String response;

    private ResponseCodes(String response, int responseCode) {
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
