package com.ElectionController.Structures.APIParams;

import java.util.ArrayList;
import java.util.List;

public class NewElectionQuery {

    private String voterId;
    private String voterPassword;
    private String electionTitle;
    private String electionDescription;
    private List<String> registeredVoters = new ArrayList<>();
    private List<Post> registeredPost = new ArrayList<>();

    public static class Post {
        private String postDescription;
        private List<String> registeredContestants = new ArrayList<String>();

        public String getPostDescription() {
            return postDescription;
        }

        public void setPostDescription(String postDescription) {
            this.postDescription = postDescription;
        }

        public List<String> getRegisteredContestants() {
            return registeredContestants;
        }

        public void setRegisteredContestants(List<String> registeredContestants) {
            if (registeredContestants != null) {
                this.registeredContestants = registeredContestants;
            }
        }

        @Override
        public String toString() {
            return "Post{" +
                    "postDescription='" + postDescription + '\'' +
                    ", registeredContestants=" + registeredContestants +
                    '}';
        }
    }

    public String getVoterId() {
        return this.voterId;
    }

    public String getVoterPassword() {
        return this.voterPassword;
    }

    public String getElectionTitle() {
        return this.electionTitle;
    }

    public String getElectionDescription() {
        return this.electionDescription;
    }

    public List<String> getRegisteredVoters() {
        return this.registeredVoters;
    }

    public List<Post> getRegisteredPost() {return this.registeredPost;}

    public static Builder Builder() {return new Builder();}

    public static class Builder {
        private String voterId;
        private String voterPassword;
        private String electionTitle;
        private String electionDescription;
        private List<String> registeredVoters = new ArrayList<>();
        private List<Post> registeredPost = new ArrayList<>();

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

        public Builder withRegisteredPost(final List<Post> registeredPost) {
            if (registeredPost != null) {
                this.registeredPost = registeredPost;
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
            newElectionQuery.registeredPost = this.registeredPost;
            return newElectionQuery;
        }

    }

    @Override
    public String toString() {
        return "NewElectionQuery{" +
                "voterId='" + voterId + '\'' +
                ", voterPassword='" + voterPassword + '\'' +
                ", electionTitle='" + electionTitle + '\'' +
                ", electionDescription='" + electionDescription + '\'' +
                ", registeredVoters=" + registeredVoters +
                ", registeredPost=" + registeredPost +
                '}';
    }
}
