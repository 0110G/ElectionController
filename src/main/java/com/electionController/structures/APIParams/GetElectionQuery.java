package com.electionController.structures.APIParams;

public class GetElectionQuery {
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
        private String voterPassword;
        private String electionId;

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

        public GetElectionQuery build() {
            GetElectionQuery getElectionQuery = new GetElectionQuery();
            getElectionQuery.voterId = this.voterId;
            getElectionQuery.voterPassword = this.voterPassword;
            getElectionQuery.electionId = this.electionId;
            return getElectionQuery;
        }
    }

    @Override
    public String toString() {
        return "GetElectionQuery{" +
                "voterId='" + voterId + '\'' +
                ", voterPassword='" + voterPassword + '\'' +
                ", electionId='" + electionId + '\'' +
                '}';
    }
}
