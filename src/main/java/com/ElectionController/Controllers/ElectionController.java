package com.ElectionController.Controllers;

import com.ElectionController.DatabaseConnector.Getter.H2Getter;
import com.ElectionController.DatabaseConnector.Putter.H2Putter;
import com.ElectionController.DatabaseConnector.Updater.H2Updater;
import com.ElectionController.Exceptions.EntityNotFoundException;
import com.ElectionController.Exceptions.InvalidParameterException;
import com.ElectionController.Helpers.ElectionControllerHelper;
import com.ElectionController.Structures.Election;
import com.ElectionController.Structures.Voter;
import com.ElectionController.Structures.Response;
import com.ElectionController.Structures.APIParams.NewElectionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


// TODO: Remove this class eventually
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

    @GetMapping("/GetElection/{electionId}")
    public Response GetElection(@PathVariable String electionId) throws EntityNotFoundException {
        // Fetch given election id from the
        throw new EntityNotFoundException("Election does not exists");
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
        for (String voterId : newElectionQuery.getRegisteredVoters()) {
            Voter voter = h2Getter.getVoter(voterId);
            voter.setVoterPassword("************");
            voter.setElectionList(null);
            regElection.getEligibleVoters().add(voter);
        }
        return regElection;
    }

    private List<String> getUniqueEntities(final List<String> list) {
        if (list == null) {
            return list;
        }
        return list.stream().distinct().collect(Collectors.toList());
    }

    private Object getValueOrDefault(final Object value, final Object defaultVal) {
        if (value == null) {return defaultVal;}
        return value;
    }
}
