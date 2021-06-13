package com.electionController.controllers.ElectionControllerEndPoints.ChangeElectionOperations;

import com.electionController.constants.ControllerOperations;
import com.electionController.constants.ResponseCodes;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.exceptions.RestrictedActionException;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.helpers.ElectionControllerHelper;
import com.electionController.logger.ConsoleLogger;
import com.electionController.structures.APIParams.ChangeElection.DeleteRegisteredVoterFromElectionQuery;
import com.electionController.structures.Election;
import com.electionController.structures.Response;
import com.electionController.structures.Voter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** DeleteRegisteredVoter API deletes specified voters from the specified election
* @author : bhavya saraf
*/
@RestController
public class DeleteRegisteredVoterOperation extends ChangeElectionOperation {

    private static final ControllerOperations ACTION = ControllerOperations.CHANGE_ELECTION_DELETE_VOTERS;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private ElectionControllerHelper electionControllerHelper;

    /*
    *  1. Only admin can delete some participant
    *  2. For a given voter, following cases present
    *       2a. VoterId is invalid => ignore
    *       2b. VoterId is valid but not a part of election => ignore
    *       2c. VoterId is part of election
    *           if admin, ignore
    *           Remove from voterMap
    *           2c1. VoterId is a candidate in one or many posts
    *               For all posts in an election,
    *                   If the exists a given voter, remove it
    *
    * */

    @DeleteMapping("/ChangeElection/DeleteVoters")
    public Response DeleteRegisteredVotersFromElection(@RequestBody DeleteRegisteredVoterFromElectionQuery
                                                       deleteRegisteredVoterFromElectionQuery) {
        ValidateNotNull(deleteRegisteredVoterFromElectionQuery);

        try {
            authenticationFacade.validateVoterCredentials(deleteRegisteredVoterFromElectionQuery.getVoterId(),
                    deleteRegisteredVoterFromElectionQuery.getVoterPassword());
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(), deleteRegisteredVoterFromElectionQuery);
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        try {
            authenticationFacade.validateElectionAdmin(deleteRegisteredVoterFromElectionQuery.getVoterId(),
                    deleteRegisteredVoterFromElectionQuery.getElectionId());
        } catch (RestrictedActionException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(), deleteRegisteredVoterFromElectionQuery);
            throw new RestrictedActionException("User does not have rights to change the election");
        }

        electionControllerHelper.deleteVotersFromElection(deleteRegisteredVoterFromElectionQuery.getVotersToDelete(),
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
