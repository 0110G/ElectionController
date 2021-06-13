package com.electionController.controllers.electionController.changeElectionController;

import com.electionController.constants.ControllerOperation;
import com.electionController.constants.ResponseCodes;
import com.electionController.controllers.ActionController;
import com.electionController.structures.APIParams.ChangeElection.ChangeElectionDescriptionQuery;
import com.electionController.structures.Response;
import com.electionController.facades.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.electionController.controllers.electionController.ElectionController.ValidateNotNull;

@RestController
public class ChangeDescriptionOperation extends ActionController<ChangeElectionDescriptionQuery, Response> {

    private static final ControllerOperation ACTION = ControllerOperation.CHANGE_ELECTION_DESCRIPTION;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public ControllerOperation getControllerOperation() {
        return this.ACTION;
    }

    @Override
    @PostMapping("/ChangeElection/ChangeDescription")
    public Response execute(@RequestBody ChangeElectionDescriptionQuery changeElectionDescriptionQuery) {
        return super.execute(changeElectionDescriptionQuery);
    }

    @Override
    public Response executeAction(final ChangeElectionDescriptionQuery changeElectionDescriptionQuery) {
        return this.changeElectionDescription(changeElectionDescriptionQuery);
    }

    @Override
    public void validateActionAccess(final ChangeElectionDescriptionQuery changeDescriptionQuery) {
        ValidateNotNull(changeDescriptionQuery);
        authenticationFacade.validateVoterCredentials(changeDescriptionQuery.getVoterId(),
                changeDescriptionQuery.getVoterPassword());
        authenticationFacade.validateElectionAdmin(changeDescriptionQuery.getVoterId(),
                changeDescriptionQuery.getElectionId());
    }

    public Response changeElectionDescription(final ChangeElectionDescriptionQuery changeDescriptionQuery) {
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
