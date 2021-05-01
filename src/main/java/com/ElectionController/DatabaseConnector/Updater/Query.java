package com.ElectionController.DatabaseConnector.Updater;

import com.ElectionController.Structures.Election;

public interface Query {
    public Election updateElection(final String electionId, Election election);
}
