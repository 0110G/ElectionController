package com.electionController.structures.APIParams;

public class GetVoterQuery {
    private String voterId;
    private String voterPassword;

    public String getVoterId() {
        return this.voterId;
    }

    public String getVoterPassword() {
        return this.voterPassword;
    }

    public static Builder Builder() {return new Builder();}

    public static class Builder {
        private String voterId;
        private String voterPassword;

        public Builder withVoterId(final String voterId) {
            this.voterId = voterId;
            return this;
        }

        public Builder withVoterPassword(final String voterPassword) {
            this.voterPassword = voterPassword;
            return this;
        }

        public GetVoterQuery build() {
            GetVoterQuery getVoterQuery = new GetVoterQuery();
            getVoterQuery.voterId = this.voterId;
            getVoterQuery.voterPassword = this.voterPassword;
            return getVoterQuery;
        }
    }
}
