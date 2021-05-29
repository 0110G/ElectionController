package com.electionController.dbConnector.Updater;

import com.electionController.structures.Election;

public interface DBUpdater {
    public void updateElection(final String electionId, final Election election);
    public void updateElectionTitle(final String electionId, final String electionTitle);
    public void updateElectionDescription(final String electionId, final String electionDesc);
    public void incrementCandidateVote(final String postId, final String contestantId);
    public void markVoterVotedForPost(final String voterId, final String electionId, final String votedPosts);
}
