package com.electionController.structures.APIParams;

public class VoteQuery {
    private String voterId;
    private String voterPassword;
    private String electionId;
    private String postId;
    private String candidateId;

    public String getVoterId() {
        return this.voterId;
    }

    public String getVoterPassword() {
        return this.voterPassword;
    }

    public String getElectionId() {
        return this.electionId;
    }

    public String getPostId() {
        return this.postId;
    }

    public String getCandidateId() {
        return this.candidateId;
    }

    public static Builder Builder() {return new Builder();}

    public static class Builder {
        private String voterId;
        private String voterPassword;
        private String electionId;
        private String postId;
        private String candidateId;

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

        public Builder withPostId(final String postId) {
            this.postId = postId;
            return this;
        }

        public Builder withCandidateId(final String candidateId) {
            this.candidateId = candidateId;
            return this;
        }

        public VoteQuery build() {
            VoteQuery voteQuery = new VoteQuery();
            voteQuery.voterId = this.voterId;
            voteQuery.voterPassword = this.voterPassword;
            voteQuery.electionId = this.electionId;
            voteQuery.postId = this.postId;
            voteQuery.candidateId = this.candidateId;
            return voteQuery;
        }
    }

    @Override
    public String toString() {
        return "VoteQuery{" +
                "voterId='" + voterId + '\'' +
                ", voterPassword='" + voterPassword + '\'' +
                ", electionId='" + electionId + '\'' +
                ", postId='" + postId + '\'' +
                ", candidateId='" + candidateId + '\'' +
                '}';
    }
}
