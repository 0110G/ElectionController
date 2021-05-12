package com.ElectionController.DatabaseConnector.Updater;

import com.ElectionController.Structures.Election;

public interface DBUpdater {
    public void updateElection(final String electionId, final Election election);
    public void incrementCandidateVote(final String postId, final String contestantId);
    public void markVoterVotedForPost(final String voterId, final String electionId, final String votedPosts);
}
