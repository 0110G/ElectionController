package com.ElectionController.Controllers.ElectionControllerEndPoints;

import com.ElectionController.Constants.ControllerOperations;
import com.ElectionController.Constants.ResponseCodes;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Exceptions.RestrictedActionException;
import com.ElectionController.Helpers.ElectionControllerHelper;
import com.ElectionController.Logger.ConsoleLogger;
import com.ElectionController.Structures.Election;
import com.ElectionController.Structures.Voter;
import com.ElectionController.Structures.Response;
import com.ElectionController.Structures.VoterMap;
import com.ElectionController.Structures.Post;
import com.ElectionController.Structures.PostMap;
import com.ElectionController.Structures.APIParams.NewElectionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

@RestController
public class NewElectionOperation extends ElectionController {

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

        // Voter tries to authenticate himself
        try {
            Voter voter = getAuthenticatedVoter(newElectionQuery.getVoterId(), newElectionQuery.getVoterPassword(),
                    ControllerOperations.NEW_ELECTION);
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ControllerOperations.NEW_ELECTION, ex.getErrorMessage(),
                    newElectionQuery);
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        // Creating a new election based on query
        final Election regElection = mapNewElectionQueryToElection(newElectionQuery, Integer.toString(currentId));

        // Inserting the registered election to the table, contacting DBConnector
        dbPutter.registerElection(regElection);

        Election election = regElection;

        // Adding the admin
        VoterMap voterMap = new VoterMap();
        voterMap.setVoterId(newElectionQuery.getVoterId());
        voterMap.setElectionId(Integer.toString(currentId));
        voterMap.setVoterAdmin(true);
        voterMap.setVoterEligible(true);
        dbPutter.registerVoterForElection(voterMap);

        electionControllerHelper.addVotersToElection(getUniqueEntities(newElectionQuery.getRegisteredVoters()),
                election.getElectionId(),
                newElectionQuery.getVoterId());

        // Added Feature for post map
        int currentPostIndex = 0;
        List<Post> registeredPosts = new ArrayList<>();
        for (NewElectionQuery.Post post : newElectionQuery.getRegisteredPost()) {
            if (!postWorthRegistering(post)) {
                continue;
            }
            Post newPost = new Post();
            newPost.setPostId(election.getElectionId() + Integer.toString(currentPostIndex));
            newPost.setPostDescription(post.getPostDescription());
            newPost.setElectionId(election.getElectionId());
            currentPostIndex++;
            List<String> distinctRegisteredContestants = getUniqueEntities(post.getRegisteredContestants());
            for(String contestantId : distinctRegisteredContestants) {
                Voter contestant = null;
                try {
                    contestant = dbGetter.getVoter(contestantId);
                } catch (InvalidCredentialException ex) {
                    ConsoleLogger.Log(ControllerOperations.NEW_ELECTION, "Invalid contestant",
                            "Contestantid:", contestantId);
                    throw new InvalidCredentialException("Invalid contestant id");
                }
                PostMap postMap = new PostMap();
                postMap.setPostId(newPost.getPostId());
                postMap.setContestantAlias(contestant.getVoterName());
                postMap.setContestantId(contestant.getVoterId());
                dbPutter.registerCandidatesForPost(postMap);
                contestant.setVoterPassword("************");
                contestant.setElectionList(null);
                newPost.getContestants().add(contestant);
            }
            // After newPost is completely build, then only register this new post
            // to db.
            try {
                dbPutter.registerPostForElection(newPost);
            } catch (RestrictedActionException ex) {
                ConsoleLogger.Log(ControllerOperations.NEW_ELECTION, "ERROR_CREATING_POST", post);
                throw new RestrictedActionException("Unable to create post");
            }
            registeredPosts.add(newPost);
        }
        election.setAvailablePost(registeredPosts);
        currentId++;

        return new Response.Builder()
                .withResponse(election)
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .build();
    }

    private Election mapNewElectionQueryToElection(final NewElectionQuery newElectionQuery, final String electionId) {
        final Election regElection = new Election();
        regElection.setElectionTitle((String)
                getValueOrDefault(newElectionQuery.getElectionTitle(), "New Election"));
        regElection.setElectionDescription((String)
                getValueOrDefault(newElectionQuery.getElectionDescription(), "Election Description"));
        regElection.setAdminVoterId(newElectionQuery.getVoterId());
        regElection.setElectionId(electionId);
        TreeSet<String> treeSet = new TreeSet<>(newElectionQuery.getRegisteredVoters());
        treeSet.add(newElectionQuery.getVoterId());
        for (String voterId : treeSet) {
            Voter voter = dbGetter.getVoter(voterId);
            voter.setVoterPassword("************");
            voter.setElectionList(null);
            regElection.getEligibleVoters().add(voter);
        }
        return regElection;
    }

    private boolean postWorthRegistering(final NewElectionQuery.Post post) {
        return post != null &&
                post.getRegisteredContestants() != null &&
                !post.getRegisteredContestants().isEmpty();
    }
}
