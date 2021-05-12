package com.ElectionController.Controllers.ElectionControllerEndPoints.ChangeElectionOperations;

import com.ElectionController.Constants.ControllerOperations;
import com.ElectionController.Constants.ResponseCodes;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Exceptions.RestrictedActionException;
import com.ElectionController.Logger.ConsoleLogger;
import com.ElectionController.Structures.APIParams.ChangeElection.ChangeElectionDescriptionQuery;
import com.ElectionController.Structures.Election;
import com.ElectionController.Structures.Response;
import com.ElectionController.Structures.Voter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChangeDescriptionOperation extends ChangeElectionOperation {

    @PostMapping("/ChangeElection/ChangeDescription")
    public Response ChangeElectionDescription(@RequestBody ChangeElectionDescriptionQuery changeDescriptionQuery) {
        ValidateNotNull(changeDescriptionQuery);

        // Voter tries to authenticate himself
        Voter voter = null;
        try {
            voter = getAuthenticatedVoter(changeDescriptionQuery.getVoterId(),
                    changeDescriptionQuery.getVoterPassword(),
                    ControllerOperations.CHANGE_ELECTION_TITLE);
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ControllerOperations.CHANGE_ELECTION_TITLE, ex.getErrorMessage(),
                    changeDescriptionQuery);
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        VoterConsistencyCheck(voter, changeDescriptionQuery);

        Election requestedElection = null;
        try {
            requestedElection = getAuthenticatedElection(changeDescriptionQuery.getElectionId(),
                    changeDescriptionQuery.getVoterId(), ControllerOperations.CHANGE_ELECTION_TITLE);
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ControllerOperations.CHANGE_ELECTION_TITLE,
                    ex.getErrorMessage(), changeDescriptionQuery);
            throw new InvalidCredentialException("User does not have rights to change the election");
        }

        ElectionConsistencyCheck(requestedElection, changeDescriptionQuery);
        requestedElection.setElectionDescription(changeDescriptionQuery.getElectionDescription());
        dbUpdater.updateElection(changeDescriptionQuery.getElectionId(), requestedElection);

        return new Response.Builder()
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .withResponse(requestedElection)
                .build();
    }

    private static void VoterConsistencyCheck(final Voter dbFetched,
                                              final ChangeElectionDescriptionQuery changeElectionDescriptionQuery) {
        if (dbFetched == null ||
            changeElectionDescriptionQuery == null ||
            !dbFetched.getVoterId().equals(changeElectionDescriptionQuery.getVoterId()) ||
            !dbFetched.getVoterPassword().equals(changeElectionDescriptionQuery.getVoterPassword())) {
            throw new RestrictedActionException("INCONSISTENCY FOUND_VOTER");
        }
    }

    private static void ElectionConsistencyCheck(final Election dbFetched,
                                                 final ChangeElectionDescriptionQuery changeElectionDescriptionQuery) {
        if (dbFetched == null ||
            changeElectionDescriptionQuery == null ||
            !dbFetched.getElectionId().equals(changeElectionDescriptionQuery.getElectionId())) {
            throw new RestrictedActionException("INCONSISTENCY_FOUND_ELECTION");
        }
    }

}
