package com.ElectionController.DatabaseConnector.Deleter;

import org.springframework.context.annotation.Configuration;

public interface DBDeletor {
    public void deleteVoterFromElection(final String voterId, final String electionId);
    public void deleteCandidateFromPost(final String postId, final String candidateId);
}
