package com.electionController.controllers.electionController;

import com.electionController.constants.ControllerOperation;
import com.electionController.constants.ResponseCodes;
import com.electionController.controllers.ActionController;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.structures.APIParams.DeleteElectionQuery;
import com.electionController.structures.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.electionController.controllers.electionController.ElectionController.ValidateNotNull;

@RestController
public final class DeleteElectionOperation extends ActionController<DeleteElectionQuery, Response> {

    private static final ControllerOperation ACTION = ControllerOperation.DB_DELETE_ELECTION;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public ControllerOperation getControllerOperation() {
        return this.ACTION;
    }

    @Override
    @DeleteMapping("/DeleteElection")
    public Response execute(@RequestBody DeleteElectionQuery deleteElectionQuery) {
        return super.execute(deleteElectionQuery);
    }

    @Override
    protected Response executeAction(final DeleteElectionQuery deleteElectionQuery) {
        dbDeletor.deleteElection(deleteElectionQuery.getElectionId());
        return Response.Builder()
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .withResponse(null)
                .build();
    }

    @Override
    public void validateActionAccess(DeleteElectionQuery deleteElectionQuery) {
        ValidateNotNull(deleteElectionQuery);
        authenticationFacade.validateVoterCredentials(deleteElectionQuery.getVoterId(),
                deleteElectionQuery.getVoterPassword());
        authenticationFacade.validateElectionAdmin(deleteElectionQuery.getVoterId(),
                deleteElectionQuery.getElectionId());
    }
}
