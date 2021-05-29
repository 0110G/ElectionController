package com.electionController.structures;

public class PostMap {
    private String postId;
    private String contestantId;
    private int votesSecured = 0;
    private String contestantAlias;

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostId() {
        return this.postId;
    }

    public void setContestantId(String contestantId) {
        this.contestantId = contestantId;
    }

    public String getContestantId() {
        return this.contestantId;
    }

    public void addVote() {
        this.votesSecured++;
    }

    public int getVotesSecured() {
        return this.votesSecured;
    }

    public String getContestantAlias() {
        return this.contestantAlias;
    }

    public void setContestantAlias(String contestantAlias) {
        this.contestantAlias = contestantAlias;
    }
}
