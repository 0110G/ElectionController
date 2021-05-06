package com.ElectionController.Controllers.ElectionControllerEndPoints.ChangeElectionOperations;

import com.ElectionController.Constants.ControllerOperations;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Helpers.ElectionControllerHelper;
import com.ElectionController.Logger.ConsoleLogger;
import com.ElectionController.Structures.APIParams.ChangeElection.AddRegisteredVoterToElectionQuery;
import com.ElectionController.Structures.Election;
import com.ElectionController.Structures.Response;
import com.ElectionController.Structures.Voter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class AddRegisteredVoterOperation extends ChangeElectionOperation {

    @Autowired
    private ElectionControllerHelper electionControllerHelper;

    @PutMapping("/ChangeElection/AddVoters")
    public Response AddRegisteredVotersToElection(
            @RequestBody AddRegisteredVoterToElectionQuery addRegisteredVoterToElectionQuery) {

        ValidateNotNull(addRegisteredVoterToElectionQuery);

        // Voter tries to authenticate himself
        Voter voter = null;
        try {
            voter = getAuthenticatedVoter(addRegisteredVoterToElectionQuery.getVoterId(),
                    addRegisteredVoterToElectionQuery.getVoterPassword(),
                    ControllerOperations.CHANGE_ELECTION_ADD_VOTERS);
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ControllerOperations.CHANGE_ELECTION_ADD_VOTERS, ex.getErrorMessage(),
                    addRegisteredVoterToElectionQuery);
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        // If he/she is electipm admin
        Election requestedElection = null;
        try {
            requestedElection = getAuthenticatedElection(addRegisteredVoterToElectionQuery.getElectionId(),
                    addRegisteredVoterToElectionQuery.getVoterId(),
                    ControllerOperations.CHANGE_ELECTION_ADD_VOTERS);
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ControllerOperations.CHANGE_ELECTION_ADD_VOTERS,
                    ex.getErrorMessage(), addRegisteredVoterToElectionQuery);
            throw new InvalidCredentialException("User does not have rights to change the election");
        }

        // Set of already registered
        Set<String> voters = h2Getter.getElectionVoters(addRegisteredVoterToElectionQuery.getElectionId())
                .stream().map(Voter::getVoterId).collect(Collectors.toSet());

        // There might be voterIds already present in election.
        // due to which copies of entries might get registered intp VOTERMAP
        // table. Hence remove all the pre registered voters
        addRegisteredVoterToElectionQuery.getVoterIdsToAdd().removeAll(voters);

        electionControllerHelper.addVotersToElection(
                new ArrayList<>(addRegisteredVoterToElectionQuery.getVoterIdsToAdd()),
                addRegisteredVoterToElectionQuery.getElectionId(), addRegisteredVoterToElectionQuery.getVoterId());

        return new Response.Builder()
                .withResponse(null)
                .withStatusCode(200)
                .withStatus("SUCCESS")
                .build();

    }

    // Check if voter Exists => New Election logic
}
