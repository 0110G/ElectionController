package com.ElectionController.Controllers;

import com.ElectionController.Constants.ResponseCodes;
import com.ElectionController.DatabaseConnector.Getter.H2Getter;
import com.ElectionController.DatabaseConnector.Putter.H2Putter;
import com.ElectionController.DatabaseConnector.Updater.H2Updater;
import com.ElectionController.Exceptions.EntityNotFoundException;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Exceptions.InvalidParameterException;
import com.ElectionController.Exceptions.RestrictedActionException;
import com.ElectionController.Helpers.ElectionControllerHelper;
import com.ElectionController.Structures.*;
import com.ElectionController.Structures.APIParams.ChangeElectionQuery;
import com.ElectionController.Structures.APIParams.NewElectionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ElectionController {

    @Autowired
    private H2Getter h2Getter;

    @Autowired
    private H2Putter h2Putter;

    @Autowired
    private H2Updater h2Updater;

    @Autowired
    private ElectionControllerHelper electionControllerHelper;

    int currentId = 1;

    /* CreateElection takes
    * @String voterId as argument
    * and creates and returns an election
    * with given voter as admin for that election
    */
    @PostMapping("/NewElection")
    public Response CreateElection(@RequestBody NewElectionQuery newElectionQuery) {

        // Body contains Valid Parameters
        ValidateNotNull(newElectionQuery);

        // Voter tries authenticates himself
        Voter voter;
        try {
            voter = h2Getter.getVoter(newElectionQuery.getVoterId());
        } catch (InvalidCredentialException ex) {
            Log("[INVALID LOGGER]: USER_DOES_NOT_EXISTS VoterId: " + newElectionQuery.getVoterId());
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        // Verifying Password
        if (voter == null ||
            voter.getVoterPassword() == null ||
            !voter.getVoterPassword().equals(newElectionQuery.getVoterPassword())) {
            Log("[INVALID LOGGER]: " +
                    "[NewElectionQuery] => USER_ENTERED_WRONG_PASSWORD VoterId: " + newElectionQuery.getVoterId());
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        // Creating a new election based on query
        final Election regElection = mapNewElectionQueryToElection(newElectionQuery, Integer.toString(currentId));

        // Inserting the registered election to the table, contacting DBConnector
        Election election =  h2Putter.registerElection(regElection);

        // Adding the admin
        VoterMap voterMap = new VoterMap();
        voterMap.setVoterId(newElectionQuery.getVoterId());
        voterMap.setElectionId(Integer.toString(currentId));
        voterMap.setVoterAdmin(true);
        voterMap.setVoterEligible(true);
        h2Putter.registerVoterForElection(voterMap);

       electionControllerHelper.addVotersToElection(newElectionQuery.getRegisteredVoters(),
                election.getElectionId(),
                newElectionQuery.getVoterId());

        currentId++;
        // Returning responsw
        return new Response.Builder()
                .withResponse(election)
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .build();
    }

    // TODO: Add support for registering new Voters and Posts
    @PostMapping("/ChangeElection")
    public Response ChangeElection(@RequestBody ChangeElectionQuery changeElectionQuery) {

        ValidateNotNull(changeElectionQuery);

        Voter voter;
        try {
            voter = h2Getter.getVoter(changeElectionQuery.getVoterId());
        } catch (InvalidCredentialException ex) {
            Log("[INVALID LOGGER]: " +
                    "[ChangeElectionQuery] => USER_DOES_NOT_EXISTS VoterId: " + changeElectionQuery.getVoterId());
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        // Verifying Password
        if (voter == null ||
                voter.getVoterPassword() == null ||
                !voter.getVoterPassword().equals(changeElectionQuery.getVoterPassword())) {
            Log("[INVALID LOGGER]: " +
                    "[ChangeElectionQuery] => USER_ENTERED_WRONG_PASSWORD VoterId: " +
                    changeElectionQuery.getVoterId());
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        Election requestedElection = null;
        try {
            requestedElection = h2Getter.getElection(changeElectionQuery.getElectionId());
        } catch (InvalidCredentialException e) {
            Log("[INVALID LOGGER]: " +
                    "[ChangeElectionQuery] => ELECTION_DDES_NOT_EXISTS ElectionId: " +
                    changeElectionQuery.getElectionId());
            throw new InvalidCredentialException("Invalid Election id");
        }

        if(requestedElection == null ||
           requestedElection.getAdminVoterId() == null ||
           !requestedElection.getAdminVoterId().equals(changeElectionQuery.getVoterId())) {
            Log("[INVALID LOGGER]: " +
                    "[ChangeElectionQuery] => USER_NOT_ELIGIBLE_TO_CHANGR ElectionId: " +
                    changeElectionQuery.getElectionId() + " VoterId: " + changeElectionQuery.getVoterId());
            throw new InvalidCredentialException("User does not have rights to change this election");
        }

        requestedElection.setElectionTitle(changeElectionQuery.getElectionTitle());
        requestedElection.setElectionDescription(changeElectionQuery.getElectionDescription());
        try {
            h2Updater.updateElection(changeElectionQuery.getElectionId(), requestedElection);
        } catch (RestrictedActionException ex) {
            Log("[INVALID LOGGER]: " +
                    "[ChangeElectionQuery] => UPDATE_FAILED VoterId: " +
                    changeElectionQuery.getVoterId() + " ElectionId: " + changeElectionQuery.getElectionId());
            throw new RestrictedActionException("Cannot update.");
        }

        return new Response.Builder()
                .withResponse(requestedElection)
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .build();
    }

    @PostMapping("")

    @GetMapping("/GetElection/{electionId}")
    public Response GetElection(@PathVariable String electionId) throws EntityNotFoundException {
        // Fetch given election id from the
        throw new EntityNotFoundException("Election does not exists");
    }


    @GetMapping("/error")
    public Response message(){
        return new Response.Builder()
                .withResponse("abaeawda")
                .build();
    }
    
    @GetMapping("/Query")
    public Response GetQuery() {
        return new Response.Builder()
                .withResponse(h2Getter.authenticateVoter1("adw", "awdawd"))
                .build();
    }

    public static void Log(final String message) {
        System.out.println(message);
    }

    public static void ValidateNotNull(final Object obj) {
        if (obj == null) {
            throw new InvalidParameterException("Invalid Parameter");
        }
    }

    private Election mapNewElectionQueryToElection(final NewElectionQuery newElectionQuery, final String electionId) {
        final Election regElection = new Election();
        regElection.setElectionTitle(newElectionQuery.getElectionTitle());
        regElection.setElectionDescription(newElectionQuery.getElectionDescription());
        regElection.setAdminVoterId(newElectionQuery.getVoterId());
        regElection.setElectionId(electionId);
        return regElection;
    }
}
