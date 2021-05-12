package com.ElectionController.Controllers.ElectionControllerEndPoints.ChangeElectionOperations;

import com.ElectionController.Constants.ControllerOperations;
import com.ElectionController.Controllers.ElectionControllerEndPoints.ElectionController;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Structures.Election;

public class ChangeElectionOperation extends ElectionController {

    protected Election getAuthenticatedElection(final String electionId,
                                                final String adminVoterId,
                                                final ControllerOperations controllerOperations) {
        Election election = null;
        try {
            election = dbGetter.getElection(electionId);
            if (election == null ||
                election.getAdminVoterId() == null ||
                !election.getAdminVoterId().equals(adminVoterId)) {
                throw new InvalidCredentialException("USER_NOT_ELIGIBLE_TO_CHANGE");
            }
            return election;
        } catch (InvalidCredentialException ex) {
            throw new InvalidCredentialException("ELECTION_DOES_NOT_EXISTS");
        }
    }
}
