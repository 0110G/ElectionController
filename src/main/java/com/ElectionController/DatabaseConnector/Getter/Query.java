package com.ElectionController.DatabaseConnector.Getter;

import com.ElectionController.Structures.Election;
import com.ElectionController.Structures.Voter;

public interface Query {
    public Voter getVoter (final String voteredId);
    public Election getElection (final String electionId);
}
