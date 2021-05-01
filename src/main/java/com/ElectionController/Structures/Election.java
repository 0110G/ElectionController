package com.ElectionController.Structures;

import java.util.ArrayList;
import java.util.List;

public class Election {
    String electionTitle;
    String electionId;
    String electionDescription;
    String adminVoterId;
    List<VoterMap> registeredVoters = new ArrayList<VoterMap>();
    List<Post> availablePost = new ArrayList<Post>();

    public String getElectionTitle() {
        return this.electionTitle;
    }

    public void setElectionTitle(String electionTitle) {
        this.electionTitle = electionTitle;
    }

    public String getElectionId() {
        return this.electionId;
    }

    public void setElectionId(String electionId) {
        this.electionId = electionId;
    }

    public String getElectionDescription() {
        return this.electionDescription;
    }

    public void setElectionDescription(String electionDescription) {
        this.electionDescription = electionDescription;
    }

    public String getAdminVoterId() {
        return this.adminVoterId;
    }

    public void setAdminVoterId(String adminVoterId) {
        this.adminVoterId = adminVoterId;
    }


    public List<VoterMap> getRegisteredVoters() {
        return this.registeredVoters;
    }

    public List<Post> getAvailablePost() {
        return this.availablePost;
    }

}
