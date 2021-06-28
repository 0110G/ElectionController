package com.electionController.structures.APIParams;

public class DeleteElectionQuery {
    private String voterId;
    private String voterPassword;
    private String electionId;

    public String getVoterId() {
        return this.voterId;
    }

    public String getVoterPassword() {
        return this.voterPassword;
    }

    public String getElectionId() {
        return this.electionId;
    }

    public static Builder Builder() {return new Builder();}

    public static class Builder {
        private String voterId;
        public Builder withVoterId(final String voterId) {
            this.voterId = voterId;
            return this;
        }

        private String voterPassword;
        public Builder withVoterPassword(final String voterPassword) {
            this.voterPassword = voterPassword;
            return this;
        }

        private String electionId;
        public Builder withElectionId(final String electionId) {
            this.electionId = electionId;
            return this;
        }

        public DeleteElectionQuery build() {
            DeleteElectionQuery deleteElectionQuery = new DeleteElectionQuery();
            deleteElectionQuery.voterId = this.voterId;
            deleteElectionQuery.voterPassword = this.voterPassword;
            deleteElectionQuery.electionId = this.electionId;
            return deleteElectionQuery;
        }
    }
}
