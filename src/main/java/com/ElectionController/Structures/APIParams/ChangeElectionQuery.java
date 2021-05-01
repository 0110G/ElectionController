package com.ElectionController.Structures.APIParams;

public class ChangeElectionQuery {
    private String voterId;
    private String voterPassword;
    private String electionTitle;
    private String electionDescription;
    private String electionId;

    public String getVoterId() {
        return voterId;
    }

    public String getVoterPassword() {
        return voterPassword;
    }

    public String getElectionTitle() {
        return electionTitle;
    }

    public String getElectionDescription() {
        return electionDescription;
    }

    public String getElectionId() {
        return electionId;
    }

    public static class Builder {
        private String voterId;
        private String voterPassword;
        private String electionTitle;
        private String electionDescription;
        private String electionId;

        public Builder withVoterId(final String voterId) {
            this.voterId = voterId;
            return this;
        }

        public Builder withVoterPassword(final String voterPassword) {
            this.voterPassword = voterPassword;
            return this;
        }

        public Builder withElectionTitle(final String electionTitle) {
            this.electionTitle = electionTitle;
            return this;
        }

        public Builder withElectionDescription(final String electionDescription) {
            this.electionDescription = electionDescription;
            return this;
        }

        public Builder withElectionId(final String electionId) {
            this.electionId = electionId;
            return this;
        }

        public ChangeElectionQuery build() {
            ChangeElectionQuery changeElectionQuery = new ChangeElectionQuery();
            changeElectionQuery.voterId = this.voterId;
            changeElectionQuery.voterPassword = this.voterPassword;
            changeElectionQuery.electionTitle = this.electionTitle;
            changeElectionQuery.electionDescription = this.electionDescription;
            changeElectionQuery.electionId = this.electionId;
            return changeElectionQuery;
        }
    }

    @Override
    public String toString() {
        return "ChangeElectionQuery{" +
                "voterId='" + voterId + '\'' +
                ", voterPassword='" + voterPassword + '\'' +
                ", electionTitle='" + electionTitle + '\'' +
                ", electionDescription='" + electionDescription + '\'' +
                ", electionId='" + electionId + '\'' +
                '}';
    }
}
