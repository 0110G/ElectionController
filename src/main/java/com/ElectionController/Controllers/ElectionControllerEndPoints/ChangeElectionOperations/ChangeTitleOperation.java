package com.ElectionController.Controllers.ElectionControllerEndPoints.ChangeElectionOperations;

import com.ElectionController.Constants.ControllerOperations;
import com.ElectionController.Constants.ResponseCodes;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Exceptions.RestrictedActionException;
import com.ElectionController.Logger.ConsoleLogger;
import com.ElectionController.Structures.APIParams.ChangeElection.ChangeElectionTitleQuery;
import com.ElectionController.Structures.Election;
import com.ElectionController.Structures.Response;
import com.ElectionController.Structures.Voter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChangeTitleOperation extends ChangeElectionOperation {

    @PostMapping("ChangeElection/ChangeTitle")
    public Response ChangeElectionTitle(@RequestBody ChangeElectionTitleQuery changeElectionTitleQuery) {
        ValidateNotNull(changeElectionTitleQuery);

        // Voter tries to authenticate himself
        Voter voter = null;
        try {
            voter = getAuthenticatedVoter(changeElectionTitleQuery.getVoterId(),
                    changeElectionTitleQuery.getVoterPassword(),
                    ControllerOperations.CHANGE_ELECTION_TITLE);
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ControllerOperations.CHANGE_ELECTION_TITLE, ex.getErrorMessage(),
                    changeElectionTitleQuery);
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        VoterConsistencyCheck(voter,  changeElectionTitleQuery);

        Election requestedElection = null;
        try {
            requestedElection = getAuthenticatedElection(changeElectionTitleQuery.getElectionId(),
                    changeElectionTitleQuery.getVoterId(), ControllerOperations.CHANGE_ELECTION_TITLE);
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ControllerOperations.CHANGE_ELECTION_TITLE,
                    ex.getErrorMessage(), changeElectionTitleQuery);
            throw new InvalidCredentialException("User does not have rights to change the election");
        }

        ElectionConsistencyCheck(requestedElection, changeElectionTitleQuery);
        requestedElection.setElectionTitle(changeElectionTitleQuery.getElectionTitle());
        h2Updater.updateElection(changeElectionTitleQuery.getElectionId(), requestedElection);

        return new Response.Builder()
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .withResponse(requestedElection)
                .build();
    }

    private static void VoterConsistencyCheck(final Voter dbFetched,
                                              final ChangeElectionTitleQuery changeElectionTitleQuery) {
        if (dbFetched == null ||
            changeElectionTitleQuery == null ||
            !dbFetched.getVoterId().equals(changeElectionTitleQuery.getVoterId()) ||
            !dbFetched.getVoterPassword().equals(changeElectionTitleQuery.getVoterPassword())) {
            throw new RestrictedActionException("INCONSISTENCY FOUND_VOTER");
        }
    }

    private static void ElectionConsistencyCheck(final Election dbFetched,
                                                 final ChangeElectionTitleQuery changeElectionTitleQuery) {
        if (dbFetched == null ||
           changeElectionTitleQuery == null ||
           !dbFetched.getElectionId().equals(changeElectionTitleQuery.getElectionId())) {
            throw new RestrictedActionException("INCONSISTENCY_FOUND_ELECTION");
        }
    }
}
