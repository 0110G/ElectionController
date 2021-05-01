package com.ElectionController.Controllers.ElectionControllerEndPoints;

import com.ElectionController.Constants.ControllerOperations;
import com.ElectionController.Constants.ResponseCodes;
import com.ElectionController.DatabaseConnector.Getter.H2Getter;
import com.ElectionController.DatabaseConnector.Putter.H2Putter;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Exceptions.InvalidParameterException;
import com.ElectionController.Helpers.ElectionControllerHelper;
import com.ElectionController.Logger.ConsoleLogger;
import com.ElectionController.Structures.APIParams.NewElectionQuery;
import com.ElectionController.Structures.Election;
import com.ElectionController.Structures.Response;
import com.ElectionController.Structures.Voter;
import com.ElectionController.Structures.VoterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class NewElectionOperation {

    @Autowired
    private H2Getter h2Getter;

    @Autowired
    private H2Putter h2Putter;

    @Autowired
    private ElectionControllerHelper electionControllerHelper;

    private static int currentId = 0;


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
            ConsoleLogger.Log(ControllerOperations.NEW_ELECTION, "USER_DOES_NOT_EXISTS",
                    "VoterID: ", newElectionQuery.getVoterId());
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        // Verifying Password
        if (voter == null ||
                voter.getVoterPassword() == null ||
                !voter.getVoterPassword().equals(newElectionQuery.getVoterPassword())) {
            ConsoleLogger.Log(ControllerOperations.NEW_ELECTION, "USER_DOES_NOT_EXISTS",
                    "VoterID: ", newElectionQuery.getVoterId());
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

        electionControllerHelper.addVotersToElection(getUniqueEntities(newElectionQuery.getRegisteredVoters()),
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

    private Election mapNewElectionQueryToElection(final NewElectionQuery newElectionQuery, final String electionId) {
        final Election regElection = new Election();
        regElection.setElectionTitle(newElectionQuery.getElectionTitle());
        regElection.setElectionDescription(newElectionQuery.getElectionDescription());
        regElection.setAdminVoterId(newElectionQuery.getVoterId());
        regElection.setElectionId(electionId);
        for (String voterId : newElectionQuery.getRegisteredVoters()) {
            Voter voter = h2Getter.getVoter(voterId);
            voter.setVoterPassword("************");
            voter.setElectionList(null);
            regElection.getEligibleVoters().add(voter);
        }
        return regElection;
    }

    private static void ValidateNotNull(final Object obj) {
        if (obj == null) {
            throw new InvalidParameterException("Invalid Parameter");
        }
    }

    private List<String> getUniqueEntities(final List<String> list) {
        if (list == null) {
            return list;
        }
        return list.stream().distinct().collect(Collectors.toList());
    }
}
