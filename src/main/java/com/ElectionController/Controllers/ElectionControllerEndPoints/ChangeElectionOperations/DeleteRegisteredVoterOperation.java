package com.ElectionController.Controllers.ElectionControllerEndPoints.ChangeElectionOperations;

import com.ElectionController.Constants.ControllerOperations;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Helpers.ElectionControllerHelper;
import com.ElectionController.Logger.ConsoleLogger;
import com.ElectionController.Structures.APIParams.ChangeElection.DeleteRegisteredVoterFromElectionQuery;
import com.ElectionController.Structures.Election;
import com.ElectionController.Structures.Response;
import com.ElectionController.Structures.Voter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteRegisteredVoterOperation extends ChangeElectionOperation {


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

    @Autowired
    private ElectionControllerHelper electionControllerHelper;

    @DeleteMapping("/ChangeElection/DeleteVoters")
    public Response DeleteRegisteredVotersFromElection(@RequestBody DeleteRegisteredVoterFromElectionQuery
                                                       deleteRegisteredVoterFromElectionQuery) {

        ValidateNotNull(deleteRegisteredVoterFromElectionQuery);

        // Voter tries to authenticate himself
        Voter voter = null;
        try {
            voter = getAuthenticatedVoter(deleteRegisteredVoterFromElectionQuery.getVoterId(),
                    deleteRegisteredVoterFromElectionQuery.getVoterPassword(),
                    ControllerOperations.CHANGE_ELECTION_DELETE_VOTERS);
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ControllerOperations.CHANGE_ELECTION_DELETE_VOTERS, ex.getErrorMessage(),
                    deleteRegisteredVoterFromElectionQuery);
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        Election requestedElection = null;
        try {
            requestedElection = getAuthenticatedElection(deleteRegisteredVoterFromElectionQuery.getElectionId(),
                    deleteRegisteredVoterFromElectionQuery.getVoterId(),
                    ControllerOperations.CHANGE_ELECTION_DELETE_VOTERS);
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ControllerOperations.CHANGE_ELECTION_DELETE_VOTERS,
                    ex.getErrorMessage(), deleteRegisteredVoterFromElectionQuery);
            throw new InvalidCredentialException("User does not have rights to change the election");
        }

        electionControllerHelper.deleteVotersFromElection(deleteRegisteredVoterFromElectionQuery.getVotersToDelete(),
                deleteRegisteredVoterFromElectionQuery.getElectionId(),
                deleteRegisteredVoterFromElectionQuery.getVoterId());

        return null;
    }


}
