package com.electionController.controllers.voterController;

import com.electionController.constants.ControllerOperation;
import com.electionController.constants.ResponseCodes;
import com.electionController.controllers.ActionController;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.structures.APIParams.GetVoterQuery;
import com.electionController.structures.Response;
import com.electionController.structures.Voter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.electionController.controllers.electionController.ElectionController.ValidateNotNull;

@RestController
public final class GetVoterOperation extends ActionController<GetVoterQuery, Response> {

    private static final ControllerOperation ACTION = ControllerOperation.GET_VOTER;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public ControllerOperation getControllerOperation() {
        return this.ACTION;
    }

    @Override
    @PostMapping("/GetVoter")
    public Response execute(@RequestBody GetVoterQuery getVoterQuery) {
        return super.execute(getVoterQuery);
    }

    @Override
    public Response executeAction(final GetVoterQuery getVoterQuery) {
        return this.getVoter(getVoterQuery);
    }

    @Override
    public void validateActionAccess(GetVoterQuery getVoterQuery) {
        ValidateNotNull(getVoterQuery);
        authenticationFacade.validateVoterCredentials(getVoterQuery.getVoterId(), getVoterQuery.getVoterPassword());
    }

    public Response getVoter(@RequestBody GetVoterQuery getVoterQuery) {
        Voter requiredVoter = dbGetter.getVoter(getVoterQuery.getVoterId());
        requiredVoter.setVoterPassword("**************");
        return Response.Builder()
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .withResponse(requiredVoter)
                .build();
    }
}
