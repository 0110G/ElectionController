package com.ElectionController.Structures;

public class VoterMap {
    private String voterId;
    private String electionId;
    private boolean isVoterEligible;
    private boolean isVoterAdmin;

    public String getVoterId() {
        return this.voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public String getElectionId() {
        return this.electionId;
    }

    public void setElectionId(String electionId) {
        this.electionId = electionId;
    }

    public boolean getVoterEligible() {
        return this.isVoterEligible;
    }

    public void setVoterEligible(boolean voterEligible) {
        isVoterEligible = voterEligible;
    }

    public boolean getVoterAdmin() {
        return this.isVoterAdmin;
    }

    public void setVoterAdmin(boolean voterAdmin) {
        isVoterAdmin = voterAdmin;
    }

    @Override
    public String toString() {
        return "VoterMap{" +
                "voterId='" + voterId + '\'' +
                ", electionId='" + electionId + '\'' +
                ", isVoterEligible=" + isVoterEligible +
                ", isVoterAdmin=" + isVoterAdmin +
                '}';
    }
}
