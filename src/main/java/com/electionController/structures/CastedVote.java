package com.electionController.structures;

public class CastedVote {
    private String voterId;
    private String electionId;
    private String postId;
    private String toVoterId;


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

    public String getPostId() {
        return this.postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getToVoterId() {
        return this.toVoterId;
    }

    public void setToVoterId(String toVoterId) {
        this.toVoterId = toVoterId;
    }
}
