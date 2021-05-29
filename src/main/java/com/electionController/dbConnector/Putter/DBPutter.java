package com.electionController.dbConnector.Putter;

import com.electionController.structures.Election;
import com.electionController.structures.Voter;
import com.electionController.structures.VoterMap;
import com.electionController.structures.PostMap;
import com.electionController.structures.Post;

/*
* Interface for fetching
* items from Data base.
* TODO: Add support for AWS Dynamo DB
*/
public interface DBPutter {
    public void registerElection(final Election election);
    public void registerVoterForElection(final VoterMap voterMap);
    public void registerPostForElection(final Post post);
    public void registerCandidatesForPost(final PostMap postMap);
    public void registerVoter(final Voter voter);
}
