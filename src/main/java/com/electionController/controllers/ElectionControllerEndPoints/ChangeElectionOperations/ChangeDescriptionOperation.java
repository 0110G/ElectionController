package com.electionController.controllers.ElectionControllerEndPoints.ChangeElectionOperations;

import com.electionController.constants.ControllerOperations;
import com.electionController.constants.ResponseCodes;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.exceptions.RestrictedActionException;
import com.electionController.logger.ConsoleLogger;
import com.electionController.structures.APIParams.ChangeElection.ChangeElectionDescriptionQuery;
import com.electionController.structures.Response;
import com.electionController.facades.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChangeDescriptionOperation extends ChangeElectionOperation {

    @Autowired
    private AuthenticationFacade authenticationFacade;

    private static final ControllerOperations ACTION = ControllerOperations.CHANGE_ELECTION_DESCRIPTION;

    private ChangeDescriptionOperation() {}

    @PostMapping("/ChangeElection/ChangeDescription")
    public Response ChangeElectionDescription(@RequestBody ChangeElectionDescriptionQuery changeDescriptionQuery) {
        ValidateNotNull(changeDescriptionQuery);

        // Voter tries to authenticate himself
        try {
            authenticationFacade.validateVoterCredentials(
                    changeDescriptionQuery.getVoterId(), changeDescriptionQuery.getVoterPassword());
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(),
                    changeDescriptionQuery);
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        // Verify whether voter is admin of given election
        try {
           authenticationFacade.validateElectionAdmin(
                   changeDescriptionQuery.getVoterId(), changeDescriptionQuery.getElectionId());
        } catch (RestrictedActionException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(), changeDescriptionQuery);
            throw new RestrictedActionException("User does not have rights to change the election");
        }


        if (changeDescriptionQuery.getElectionDescription() == null ||
                changeDescriptionQuery.getElectionDescription().isEmpty()) {
            return new Response.Builder()
                    .withStatus("EMPTY_DESCRIPTION_PASSED")
                    .withStatusCode(200)
                    .withResponse(null)
                    .build();
        }

        dbUpdater.updateElectionDescription(changeDescriptionQuery.getElectionId(),
                changeDescriptionQuery.getElectionDescription());

        return new Response.Builder()
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .withResponse(null)
                .build();
    }

}
