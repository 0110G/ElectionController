package com.electionController.controllers.VoterControllerEndPoints;

import com.electionController.constants.ControllerOperations;
import com.electionController.constants.ResponseCodes;
import com.electionController.controllers.ActionController;
import com.electionController.exceptions.EntityNotFoundException;
import com.electionController.exceptions.InternalServiceException;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.exceptions.RestrictedActionException;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.logger.ConsoleLogger;
import com.electionController.structures.APIParams.GetVoterQuery;
import com.electionController.structures.Response;
import com.electionController.structures.Voter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetVoterOperation extends ActionController {

    private static final ControllerOperations ACTION = ControllerOperations.GET_VOTER;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @GetMapping("/GetVoter")
    public Response GetVoter(@RequestBody GetVoterQuery getVoterQuery) {
        Voter voter = null;
        try {
            voter = dbGetter.getVoter(getVoterQuery.getVoterId());
            if (voter == null) {
                ConsoleLogger.Log(ACTION, "NULL_VOTER_FETCHED_FROM_DB", getVoterQuery);
                throw new InternalServiceException(ResponseCodes.INTERNAL_ERROR);
            } else {
                if (voter.getVoterPassword().equals(getVoterQuery.getVoterPassword())) {
                    return new Response.Builder()
                            .withResponse(voter)
                            .withStatus(ResponseCodes.SUCCESS.getResponse())
                            .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                            .build();
                } else {
                    ConsoleLogger.Log(ACTION, "INCORRECT_PASSWORD", getVoterQuery);
                    throw new InvalidCredentialException(ResponseCodes.INVALID_VOTER_CREDENTIALS);
                }
            }
        } catch (EntityNotFoundException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(), getVoterQuery);
            throw new InvalidCredentialException(ResponseCodes.INVALID_VOTER_CREDENTIALS);
        }
    }

}
