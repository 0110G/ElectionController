package com.ElectionController.Controllers.VoterControllerEndPoints;

import com.ElectionController.Constants.ControllerOperations;
import com.ElectionController.Constants.ResponseCodes;
import com.ElectionController.Controllers.ActionController;
import com.ElectionController.Exceptions.RestrictedActionException;
import com.ElectionController.Logger.ConsoleLogger;
import com.ElectionController.Structures.APIParams.NewVoterQuery;
import com.ElectionController.Structures.Response;
import com.ElectionController.Structures.Voter;
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
