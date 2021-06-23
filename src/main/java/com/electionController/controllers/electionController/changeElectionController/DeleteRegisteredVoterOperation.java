package com.electionController.controllers.electionController.changeElectionController;

import com.electionController.constants.ControllerOperation;
import com.electionController.constants.ResponseCodes;
import com.electionController.controllers.ActionController;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.facades.ElectionControllerFacade;
import com.electionController.structures.APIParams.ChangeElection.DeleteRegisteredVoterFromElectionQuery;
import com.electionController.structures.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.electionController.controllers.electionController.ElectionController.ValidateNotNull;

/** DeleteRegisteredVoter API deletes specified voters from the specified election
* @author : bhavya saraf
*/
@RestController
public final class DeleteRegisteredVoterOperation extends ActionController<DeleteRegisteredVoterFromElectionQuery, Response> {

    private static final ControllerOperation ACTION = ControllerOperation.CHANGE_ELECTION_DELETE_VOTERS;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private ElectionControllerFacade electionControllerFacade;

    @Override
    public ControllerOperation getControllerOperation() {
        return this.ACTION;
    }

    @Override
    @DeleteMapping("/ChangeElection/DeleteVoters")
    public Response execute(@RequestBody DeleteRegisteredVoterFromElectionQuery deleteRegisteredVoterQuery) {
        return super.execute(deleteRegisteredVoterQuery);
    }

    @Override
    protected Response executeAction(final DeleteRegisteredVoterFromElectionQuery deleteRegisteredVoterQuery) {
        return this.deleteRegisteredVotersFromElection(deleteRegisteredVoterQuery);
    }

    @Override
    public void validateActionAccess(final DeleteRegisteredVoterFromElectionQuery deleteRegisteredVoterQuery) {
        ValidateNotNull(deleteRegisteredVoterQuery);
        authenticationFacade.validateVoterCredentials(deleteRegisteredVoterQuery.getVoterId(),
                deleteRegisteredVoterQuery.getVoterPassword());
        authenticationFacade.validateElectionAdmin(deleteRegisteredVoterQuery.getVoterId(),
                deleteRegisteredVoterQuery.getElectionId());
    }

    private Response deleteRegisteredVotersFromElection(final DeleteRegisteredVoterFromElectionQuery
                                                       deleteRegisteredVoterFromElectionQuery) {
        electionControllerFacade.deleteVotersFromElection(deleteRegisteredVoterFromElectionQuery.getVotersToDelete(),
                deleteRegisteredVoterFromElectionQuery.getElectionId(),
                deleteRegisteredVoterFromElectionQuery.getVoterId(),
                deleteRegisteredVoterFromElectionQuery.getForceDelete());

        return new Response.Builder()
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .withResponse(null)
                .build();
    }
}
