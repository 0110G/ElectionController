package com.ElectionController.Structures.APIParams;

import java.util.ArrayList;
import java.util.List;

public class NewElectionQuery {

    private String voterId;
    private String voterPassword;
    private String electionTitle;
    private String electionDescription;
    private List<String> registeredVoters = new ArrayList<>();

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

    public List<String> getRegisteredVoters() {
        return registeredVoters;
    }

    public static class Builder {
        private String voterId;
        private String voterPassword;
        private String electionTitle;
        private String electionDescription;
        private List<String> registeredVoters;

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

        public Builder withRegisteredVoters(final List<String> registeredVoters) {
            if (registeredVoters != null) {
                this.registeredVoters = registeredVoters;
            }
            return this;
        }

        public NewElectionQuery build() {
            NewElectionQuery newElectionQuery = new NewElectionQuery();
            newElectionQuery.voterId = this.voterId;
            newElectionQuery.voterPassword = this.voterPassword;
            newElectionQuery.electionTitle = this.electionTitle;
            newElectionQuery.electionDescription = this.electionDescription;
            newElectionQuery.registeredVoters = this.registeredVoters;
            return newElectionQuery;
        }
    }

}
