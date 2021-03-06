package com.electionController.structures;

import com.electionController.constants.ResponseCodes;

public class Response {
    private Object response;
    private String status;
    private int statusCode;

    public Response() {
        response = null;
        status = ResponseCodes.NULL_RESPONSE.getResponse();
        statusCode = ResponseCodes.NULL_RESPONSE.getResponseCode();
    }

    public Response(Object response, String status, int statusCode) {
        this.response = response;
        this.status = status;
        this.statusCode = statusCode;
    }

    public Object getResponse() {
        return this.response;
    }

    public void setResponse(Object responce) {
        this.response = responce;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public static Builder Builder() {return new Builder();}

    public static class Builder {
        private Object response = null;
        public Builder withResponse(final Object response) {
            this.response = response;
            return this;
        }

        private String status = null;
        public Builder withStatus(final String status) {
            this.status = status;
            return this;
        }

        private int statusCode = 0;
        public Builder withStatusCode(final int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Response build() {
            Response response = new Response(this.response, this.status, this.statusCode);
            return response;
        }
    }
}
