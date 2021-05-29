package com.electionController.controllers.VoterControllerEndPoints;

import com.electionController.constants.ResponseCodes;
import com.electionController.controllers.ActionController;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.exceptions.RestrictedActionException;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.structures.APIParams.GetVoterQuery;
import com.electionController.structures.Response;
import com.electionController.structures.Voter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetVoterOperation extends ActionController {

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @GetMapping("/GetVoter")
    public Response GetVoter(@RequestBody GetVoterQuery getVoterQuery) {
        Voter voter = null;
        try {
            voter = dbGetter.getVoter(getVoterQuery.getVoterId());
            if (voter == null) {
                throw new RestrictedActionException(ResponseCodes.INTERNAL_ERROR);
            } else {
                if (voter.getVoterPassword().equals(getVoterQuery.getVoterPassword())) {
                    return new Response.Builder()
                            .withResponse(voter)
                            .withStatus(ResponseCodes.SUCCESS.getResponse())
                            .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                            .build();
                } else {
                    throw new InvalidCredentialException(ResponseCodes.INVALID_VOTER_CREDENTIALS);
                }
            }
        } catch (InvalidCredentialException ex) {
            throw new InvalidCredentialException(ResponseCodes.INVALID_VOTER_CREDENTIALS);
        }
    }

}
