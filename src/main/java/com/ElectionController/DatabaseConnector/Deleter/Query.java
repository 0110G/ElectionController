package com.ElectionController.DatabaseConnector.Deleter;

public interface Query {

    public void deleteVoterFromElection(final String voterId, final String electionId);

}
