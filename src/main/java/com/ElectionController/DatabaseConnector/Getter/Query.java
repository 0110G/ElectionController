package com.ElectionController.DatabaseConnector.Getter;

import com.ElectionController.Structures.APIParams.NewElectionQuery;
import com.ElectionController.Structures.Election;
import com.ElectionController.Structures.Post;
import com.ElectionController.Structures.Voter;
import com.ElectionController.Structures.VoterMap;

import java.util.List;

public interface Query {
    public Voter getVoter (final String voterId);
    public Election getElection (final String electionId);
    public VoterMap getVoterMap (final String voterId, final String electionId);
    public List<Voter> getElectionVoters(final String electionId);
    public List<Post> getElectionPosts(final String electionId);
    public List<Voter> getPostCandidates(final String postId);
}
