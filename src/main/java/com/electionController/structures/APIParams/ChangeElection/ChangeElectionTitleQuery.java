package com.electionController.structures.APIParams.ChangeElection;

public class ChangeElectionTitleQuery {
    private String voterId;
    private String voterPassword;
    private String electionId;
    private String electionTitle;

    public String getVoterId() {
        return this.voterId;
    }

    public String getVoterPassword() {
        return this.voterPassword;
    }

    public String getElectionId() {
        return this.electionId;
    }

    public String getElectionTitle() {
        return this.electionTitle;
    }

    public static ChangeElectionTitleQuery.Builder builder(){return new Builder();}

    public static class Builder {
        private String voterId;
        private String voterPassword;
        private String electionId;
        private String electionTitle;

        public Builder withVoterId(final String voterId) {
            this.voterId = voterId;
            return this;
        }

        public Builder withVoterPassword(final String voterPassword) {
            this.voterPassword = voterPassword;
            return this;
        }

        public Builder withElectionId(final String electionId) {
            this.electionId = electionId;
            return this;
        }

        public Builder withElectionTitle(final String electionTitle) {
            this.electionTitle = electionTitle;
            return this;
        }

        public ChangeElectionTitleQuery build() {
            ChangeElectionTitleQuery changeElectionTitleQuery = new ChangeElectionTitleQuery();
            changeElectionTitleQuery.voterId = this.voterId;
            changeElectionTitleQuery.voterPassword = this.voterPassword;
            changeElectionTitleQuery.electionId = this.electionId;
            changeElectionTitleQuery.electionTitle = this.electionTitle;
            return changeElectionTitleQuery;
        }
    }
}
