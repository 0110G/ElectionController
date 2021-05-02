package com.ElectionController.DatabaseConnector.Putter;

import com.ElectionController.Structures.Election;
import com.ElectionController.Structures.VoterMap;
import com.ElectionController.Structures.PostMap;
import com.ElectionController.Structures.Post;

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
}
