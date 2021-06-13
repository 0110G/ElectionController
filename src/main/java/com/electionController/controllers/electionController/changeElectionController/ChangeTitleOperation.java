package com.electionController.controllers.electionController.changeElectionController;

import com.electionController.constants.ControllerOperations;
import com.electionController.constants.ResponseCodes;
import com.electionController.controllers.ActionController;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.structures.APIParams.ChangeElection.ChangeElectionTitleQuery;
import com.electionController.structures.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.electionController.controllers.electionController.ElectionController.ValidateNotNull;

@RestController
public class ChangeTitleOperation extends ActionController<ChangeElectionTitleQuery, Response> {

    private static final ControllerOperations ACTION = ControllerOperations.CHANGE_ELECTION_TITLE;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public ControllerOperations getControllerOperation() {
        return this.ACTION;
    }

    @Override
    @PostMapping("ChangeElection/ChangeTitle")
    public Response execute(@RequestBody ChangeElectionTitleQuery changeElectionTitleQuery) {
        return super.execute(changeElectionTitleQuery);
    }

    @Override
    public Response executeAction(final ChangeElectionTitleQuery changeElectionTitleQuery) {
        return this.changeElectionTitle(changeElectionTitleQuery);
    }

    @Override
    public void validateActionAccess(final ChangeElectionTitleQuery changeElectionTitleQuery) {
        ValidateNotNull(changeElectionTitleQuery);
        authenticationFacade.validateVoterCredentials(changeElectionTitleQuery.getVoterId(),
                changeElectionTitleQuery.getVoterPassword());
        authenticationFacade.validateElectionAdmin(changeElectionTitleQuery.getVoterId(),
                changeElectionTitleQuery.getElectionId());
    }

    public Response changeElectionTitle(final ChangeElectionTitleQuery changeElectionTitleQuery) {
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
}
