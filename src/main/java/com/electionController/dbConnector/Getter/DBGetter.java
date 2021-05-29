package com.electionController.dbConnector.Getter;

import com.electionController.structures.Election;
import com.electionController.structures.Voter;
import com.electionController.structures.VoterMap;
import com.electionController.structures.Post;
import com.electionController.structures.PostMap;
import com.electionController.structures.Contestant;

import java.util.List;

public interface DBGetter {
    public Voter getVoter (final String voterId);
    public Election getElection (final String electionId);
    public VoterMap getVoterMap (final String voterId, final String electionId);
    public List<Voter> getElectionVoters(final String electionId);
    public List<Post> getElectionPosts(final String electionId);
    public Post getElectionPost(final String electionId, final String postId);
    public PostMap getPostMap(final String electionId, final String postMap);
    public Contestant getPostContestant(final String postId, final String contestantId);
    public List<Contestant> getPostContestants(final String postId);
}
