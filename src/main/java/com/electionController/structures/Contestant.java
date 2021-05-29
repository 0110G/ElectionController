package com.electionController.structures;

public class Contestant extends Voter{
    private int votesSecured;
    private int rank;

    public void setVotesSecured(int votesSecured) {
        this.votesSecured = votesSecured;
    }

    public int getVotesSecured() {
        return this.votesSecured;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return this.rank;
    }
}
