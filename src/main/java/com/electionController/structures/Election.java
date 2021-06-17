package com.electionController.structures;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Election {
    private String electionTitle;
    private String electionId;
    private String electionDescription;
    private String adminVoterId;
    private List<Voter> eligibleVoters = new ArrayList<>();
    private List<Post> availablePost = new ArrayList<Post>();
    private Date createdOn;

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

    public void setAvailablePost(List<Post> availablePost) {
        this.availablePost = availablePost;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getCreatedOn() {
        return this.createdOn;
    }

    public List<Post> getAvailablePost() {
        return this.availablePost;
    }

    @Override
    public String toString() {
        return "Election{" +
                "electionTitle='" + electionTitle + '\'' +
                ", electionId='" + electionId + '\'' +
                ", electionDescription='" + electionDescription + '\'' +
                ", adminVoterId='" + adminVoterId + '\'' +
                ", eligibleVoters=" + eligibleVoters +
                ", availablePost=" + availablePost +
                '}';
    }
}
