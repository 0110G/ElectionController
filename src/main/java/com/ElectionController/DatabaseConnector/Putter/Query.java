package com.ElectionController.DatabaseConnector.Putter;

import com.ElectionController.Structures.*;

/*
* Interface for fetching
* items from Data base.
* TODO: Add support for AWS Dynamo DB
*/
public interface Query {
    public Election registerElection(final Election election);
    public VoterMap registerVoterForElection(final VoterMap voterMap);
    public Post registerPostForElection(final Post post);
    public PostMap registerCandidatesForPost(final PostMap postMap);
    public Voter registerVoter(final Voter voter);
}
