package com.ElectionController.Structures;

import java.util.ArrayList;
import java.util.List;

public class Election {
    private String electionTitle;
    private String electionId;
    private String electionDescription;
    private String adminVoterId;

    private List<Voter> eligibleVoters = new ArrayList<>();

    /*Not required. Changing design a bit*/
    private List<VoterMap> registeredVoters = new ArrayList<VoterMap>();

    private List<Post> availablePost = new ArrayList<Post>();

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

    public List<Voter> getEligibleVoters() {
        return this.eligibleVoters;
    }

    public void setEligibleVoters(List<Voter> eligibleVoters) {
        this.eligibleVoters = eligibleVoters;
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

    public void setRegisteredVoters(List<VoterMap> registeredVoters) {
        this.registeredVoters = registeredVoters;
    }

    public List<Post> getAvailablePost() {
        return this.availablePost;
    }

}
