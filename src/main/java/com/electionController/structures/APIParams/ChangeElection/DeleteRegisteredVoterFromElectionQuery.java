package com.electionController.structures.APIParams.ChangeElection;

import java.util.ArrayList;
import java.util.List;

public class DeleteRegisteredVoterFromElectionQuery {

    private String voterId;
    private String voterPassword;
    private String electionId;
    private List<String> votersToDelete;
    private boolean forceDelete = false;

    public String getVoterId() {
        return this.voterId;
    }

    public String getVoterPassword() {
        return this.voterPassword;
    }

    public String getElectionId() {
        return this.electionId;
    }

    public List<String> getVotersToDelete() {
        return this.votersToDelete;
    }

    public boolean getForceDelete() {
        return this.forceDelete;
    }

    public static Builder build() {return new Builder();}

    public static class Builder {
        private String voterId;
        private String voterPassword;
        private String electionId;
        private List<String> votersToDelete = new ArrayList<String>();
        private boolean forceDelete;

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

        public Builder withForceDelete(final boolean forceDelete) {
            this.forceDelete = forceDelete;
            return this;
        }

        public Builder withVotersToDelete(final List<String> votersToDelete) {
            if (votersToDelete != null) {
                this.votersToDelete = votersToDelete;
            }
            return this;
        }

        public DeleteRegisteredVoterFromElectionQuery build() {
            DeleteRegisteredVoterFromElectionQuery deleteRegisteredVoterFromElectionQuery =
                    new DeleteRegisteredVoterFromElectionQuery();
            deleteRegisteredVoterFromElectionQuery.voterId = this.voterId;
            deleteRegisteredVoterFromElectionQuery.voterPassword = this.voterPassword;
            deleteRegisteredVoterFromElectionQuery.electionId = this.electionId;
            deleteRegisteredVoterFromElectionQuery.forceDelete = this.forceDelete;
            deleteRegisteredVoterFromElectionQuery.votersToDelete = this.votersToDelete;
            return deleteRegisteredVoterFromElectionQuery;
        }

    }
}
