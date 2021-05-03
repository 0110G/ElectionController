package com.ElectionController.Structures.APIParams.ChangeElection;

public class ChangeElectionDescriptionQuery {
    private String voterId;
    private String voterPassword;
    private String electionId;
    private String electionDescription;

    public String getVoterId() {
        return this.voterId;
    }

    public String getVoterPassword() {
        return this.voterPassword;
    }

    public String getElectionId() {
        return this.electionId;
    }

    public String getElectionDescription() {
        return this.electionDescription;
    }

    public static Builder builder(){return new Builder();}

    public static class Builder {
        private String voterId;
        private String voterPassword;
        private String electionId;
        private String electionDescription;

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

        public Builder withElectionDescription(final String electionDescription) {
            this.electionDescription = electionDescription;
            return this;
        }

        public ChangeElectionDescriptionQuery build() {
            ChangeElectionDescriptionQuery changeElectionDescriptionQuery = new ChangeElectionDescriptionQuery();
            changeElectionDescriptionQuery.voterId = this.voterId;
            changeElectionDescriptionQuery.voterPassword = this.voterPassword;
            changeElectionDescriptionQuery.electionId = this.electionId;
            changeElectionDescriptionQuery.electionDescription = this.electionDescription;
            return changeElectionDescriptionQuery;
        }
    }


}
