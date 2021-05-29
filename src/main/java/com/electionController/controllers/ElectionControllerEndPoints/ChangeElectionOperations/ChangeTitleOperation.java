package com.electionController.controllers.ElectionControllerEndPoints.ChangeElectionOperations;

import com.electionController.constants.ControllerOperations;
import com.electionController.constants.ResponseCodes;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.exceptions.RestrictedActionException;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.logger.ConsoleLogger;
import com.electionController.structures.APIParams.ChangeElection.ChangeElectionTitleQuery;
import com.electionController.structures.Election;
import com.electionController.structures.Response;
import com.electionController.structures.Voter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChangeTitleOperation extends ChangeElectionOperation {

    @Autowired
    private AuthenticationFacade authenticationFacade;

    private static final ControllerOperations ACTION = ControllerOperations.CHANGE_ELECTION_TITLE;

    @PostMapping("ChangeElection/ChangeTitle")
    public Response ChangeElectionTitle(@RequestBody ChangeElectionTitleQuery changeElectionTitleQuery) {
        ValidateNotNull(changeElectionTitleQuery);

        // Voter authenticates his credentials
        try {
            authenticationFacade.validateVoterCredentials(changeElectionTitleQuery.getVoterId(),
                    changeElectionTitleQuery.getVoterPassword());
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(),
                    changeElectionTitleQuery);
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        // Voter is admin of given election
        try {
            authenticationFacade.validateElectionAdmin(changeElectionTitleQuery.getVoterId(),
                    changeElectionTitleQuery.getElectionId());
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(), changeElectionTitleQuery);
            throw new InvalidCredentialException("User does not have rights to change the election");
        }

        if (changeElectionTitleQuery.getElectionTitle() == null ||
            changeElectionTitleQuery.getElectionTitle().isEmpty()) {
            return Response.Builder()
                    .withStatus("EMPTY_TITLE_PASSED")
                    .withStatusCode(200)
                    .withResponse(null)
                    .build();
        }

        dbUpdater.updateElectionTitle(changeElectionTitleQuery.getElectionId(),
                changeElectionTitleQuery.getElectionTitle());

        return new Response.Builder()
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .withResponse(null)
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
