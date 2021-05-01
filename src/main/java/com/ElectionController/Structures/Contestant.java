package com.ElectionController.Structures;

public class Contestant extends Voter{
    private int numVotes;

    public void addVote() {
        this.numVotes++;
    }

    public int getNumVotes() {
        return numVotes;
    }
}
