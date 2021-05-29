package com.electionController.controllers.ElectionControllerEndPoints.ChangeElectionOperations;

import com.electionController.constants.ControllerOperations;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.helpers.ElectionControllerHelper;
import com.electionController.logger.ConsoleLogger;
import com.electionController.structures.APIParams.ChangeElection.AddRegisteredVoterToElectionQuery;
import com.electionController.structures.Response;
import com.electionController.structures.Voter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@RestController
public class AddRegisteredVoterOperation extends ChangeElectionOperation {

    @Autowired
    private ElectionControllerHelper electionControllerHelper;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    private static final ControllerOperations ACTION = ControllerOperations.CHANGE_ELECTION_ADD_VOTERS;

    @PutMapping("/ChangeElection/AddVoters")
    public Response AddRegisteredVotersToElection(
            @RequestBody AddRegisteredVoterToElectionQuery addRegisteredVoterToElectionQuery) {

        ValidateNotNull(addRegisteredVoterToElectionQuery);

        // Voter tries to authenticate himself
        try {
            authenticationFacade.validateVoterCredentials(addRegisteredVoterToElectionQuery.getVoterId(),
                    addRegisteredVoterToElectionQuery.getVoterPassword());
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(), addRegisteredVoterToElectionQuery);
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        // If he/she is electipm admin
        try {
            authenticationFacade.validateElectionAdmin(addRegisteredVoterToElectionQuery.getVoterId(),
                    addRegisteredVoterToElectionQuery.getElectionId());
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(), addRegisteredVoterToElectionQuery);
            throw new InvalidCredentialException("User does not have rights to change the election");
        }

        // Set of already registered
        Set<String> voters = dbGetter.getElectionVoters(addRegisteredVoterToElectionQuery.getElectionId())
                .stream().map(Voter::getVoterId).collect(Collectors.toSet());

        // There might be voterIds already present in election.
        // due to which copies of entries might get registered intp VOTERMAP
        // table. Hence remove all the pre registered voters
        addRegisteredVoterToElectionQuery.getVoterIdsToAdd().removeAll(voters);

        Set<String> distinctVoterIdsToAdd = new TreeSet<>(addRegisteredVoterToElectionQuery.getVoterIdsToAdd());

        // Query Validation: Validate
        try {
            authenticationFacade.validateVoterIds(new ArrayList<>(distinctVoterIdsToAdd));
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(), addRegisteredVoterToElectionQuery);
            throw new InvalidCredentialException(ex.getErrorMessage());
        }

        electionControllerHelper.addVotersToElection(
                new ArrayList<>(distinctVoterIdsToAdd),
                addRegisteredVoterToElectionQuery.getElectionId(), addRegisteredVoterToElectionQuery.getVoterId());

        return new Response.Builder()
                .withResponse(null)
                .withStatusCode(200)
                .withStatus("SUCCESS")
                .build();

    }
}
