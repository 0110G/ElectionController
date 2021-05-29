package com.electionController.structures.APIParams;

public class GetElectionResultsQuery {
    private String voterId;
    private String voterPassword;
    private String electionId;

    public String getVoterId() {
        return this.voterId;
    }

    public String getElectionId() {
        return this.electionId;
    }

    public String getVoterPassword() {
        return this.voterPassword;
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

        public GetElectionResultsQuery build() {
            GetElectionResultsQuery getElectionResultsQuery = new GetElectionResultsQuery();
            getElectionResultsQuery.electionId = this.electionId;
            getElectionResultsQuery.voterId = this.voterId;
            getElectionResultsQuery.voterPassword = this.voterPassword;
            return getElectionResultsQuery;
        }
    }

    @Override
    public String toString() {
        return "GetElectionResultsQuery{" +
                "voterId='" + voterId + '\'' +
                ", voterPassword='" + voterPassword + '\'' +
                ", electionId='" + electionId + '\'' +
                '}';
    }
}
