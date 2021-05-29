package com.electionController.controllers.VoterControllerEndPoints;

import com.electionController.constants.ControllerOperations;
import com.electionController.constants.ResponseCodes;
import com.electionController.controllers.ActionController;
import com.electionController.exceptions.RestrictedActionException;
import com.electionController.logger.ConsoleLogger;
import com.electionController.structures.APIParams.NewVoterQuery;
import com.electionController.structures.Response;
import com.electionController.structures.Voter;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewVoterOperation extends ActionController {

    static int currentVoterId = 0;

    @PutMapping("/NewVoter")
    public Response CreateVoter(@RequestBody NewVoterQuery newVoterQuery) {
        Voter voter = mapNewVoterQueryToVoter(newVoterQuery, Integer.toString(currentVoterId) + "00");
        try {
            dbPutter.registerVoter(voter);
        } catch (RestrictedActionException ex) {
            ConsoleLogger.Log(ControllerOperations.NEW_VOTER, "Cannot create new voter",
                    newVoterQuery);
            throw new RestrictedActionException("Error while creating voter");
        }
        currentVoterId++;
        return new Response.Builder()
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .withResponse(voter)
                .build();
    }

    private Voter mapNewVoterQueryToVoter(final NewVoterQuery newVoterQuery, final String voterId) {
        Voter voter = new Voter();
        voter.setVoterId(voterId);
        voter.setVoterPassword(newVoterQuery.getVoterPassword());
        voter.setVoterName(newVoterQuery.getVoterName());
        return voter;
    }
}
