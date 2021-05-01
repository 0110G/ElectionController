package com.ElectionController.DatabaseConnector.Putter;

import com.ElectionController.Structures.Election;
import com.ElectionController.Structures.Voter;
import com.ElectionController.Structures.VoterMap;

public interface Query {
    public Election registerElection(Election election);
    public VoterMap registerVoterForElection(VoterMap voterMap);
}
