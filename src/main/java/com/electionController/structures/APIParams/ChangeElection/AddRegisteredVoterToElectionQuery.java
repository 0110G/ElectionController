package com.electionController.structures.APIParams.ChangeElection;

import java.util.ArrayList;
import java.util.List;

public class AddRegisteredVoterToElectionQuery {
    private String voterId;
    private String voterPassword;
    private String electionId;
    private List<String> voterIdsToAdd = new ArrayList<String>();

    public String getVoterId() {
        return this.voterId;
    }

    public String getVoterPassword() {
        return this.voterPassword;
    }

    public String getElectionId() {
        return this.electionId;
    }

    public List<String> getVoterIdsToAdd() {
        return this.voterIdsToAdd;
    }

    public static Builder build() {return new Builder();}

    public static class Builder {
        private String voterId;
        private String voterPassword;
        private String electionId;
        private List<String> voterIdsToAdd = new ArrayList<String>();

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

        public Builder withVoterIdsToAdd(final List<String> voterIdsToAdd) {
            if (voterIdsToAdd != null) {
                this.voterIdsToAdd = voterIdsToAdd;
            }
            return this;
        }

        public AddRegisteredVoterToElectionQuery build() {
            AddRegisteredVoterToElectionQuery addRegisteredVoterToElectionQuery =
                    new AddRegisteredVoterToElectionQuery();
            addRegisteredVoterToElectionQuery.voterId = this.voterId;
            addRegisteredVoterToElectionQuery.voterPassword = this.voterPassword;
            addRegisteredVoterToElectionQuery.electionId = this.electionId;
            addRegisteredVoterToElectionQuery.voterIdsToAdd = this.voterIdsToAdd;
            return addRegisteredVoterToElectionQuery;
        }
    }
}
