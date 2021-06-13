package com.electionController.controllers.voterController;

import com.electionController.constants.ControllerOperation;
import com.electionController.constants.ResponseCodes;
import com.electionController.controllers.ActionController;
import com.electionController.structures.APIParams.NewVoterQuery;
import com.electionController.structures.Response;
import com.electionController.structures.Voter;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.electionController.controllers.electionController.ElectionController.ValidateNotNull;

@RestController
public class NewVoterOperation extends ActionController<NewVoterQuery, Response> {

    static int currentVoterId = 0;

    private static final ControllerOperation ACTION = ControllerOperation.NEW_VOTER;

    @Override
    public ControllerOperation getControllerOperation() {
        return this.ACTION;
    }

    @Override
    @PutMapping("/NewVoter")
    public Response execute(@RequestBody NewVoterQuery newVoterQuery) {
        return super.execute(newVoterQuery);
    }

    @Override
    public Response executeAction(final NewVoterQuery newVoterQuery) {
        return this.createVoter(newVoterQuery);
    }

    @Override
    public void validateActionAccess(final NewVoterQuery newVoterQuery) {
        ValidateNotNull(newVoterQuery);
    }

    private Response createVoter(final NewVoterQuery newVoterQuery) {
        Voter voter = mapNewVoterQueryToVoter(newVoterQuery, Integer.toString(currentVoterId) + "00");
        dbPutter.registerVoter(voter);
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
