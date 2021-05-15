package com.ElectionController.Controllers.ElectionControllerEndPoints;

import com.ElectionController.Constants.ControllerOperations;
import com.ElectionController.Constants.ResponseCodes;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Helpers.ElectionControllerHelper;
import com.ElectionController.Logger.ConsoleLogger;
import com.ElectionController.Structures.Election;
import com.ElectionController.Structures.Voter;
import com.ElectionController.Structures.Response;
import com.ElectionController.Structures.Post;
import com.ElectionController.Structures.APIParams.NewElectionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

        try {
            validateNewElectionQuery(newElectionQuery);
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ControllerOperations.NEW_ELECTION, ex.getErrorMessage());
            throw new InvalidCredentialException("Invalid Request");
        }

        // Creating a new election based on query
        final Election regElection = mapNewElectionQueryToElection(newElectionQuery, Integer.toString(currentId));

        // Inserting the registered election to the table, contacting DBConnector
        dbPutter.registerElection(regElection);

        currentId++;

        return new Response.Builder()
                .withResponse(regElection)
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .build();
    }

    private Election mapNewElectionQueryToElection(final NewElectionQuery newElectionQuery, final String electionId) {
        final Election regElection = new Election();

        // 1
        regElection.setElectionTitle((String)
                getValueOrDefault(newElectionQuery.getElectionTitle(), "New Election"));
        // 2
        regElection.setElectionDescription((String)
                getValueOrDefault(newElectionQuery.getElectionDescription(), "Election Description"));

        // 3
        regElection.setElectionId(electionId);

        // 4
        regElection.setAdminVoterId(newElectionQuery.getVoterId());


        // 5
        TreeSet<String> treeSet = new TreeSet<>(newElectionQuery.getRegisteredVoters());
        // Adding admin and candidates to regersted voters set
        treeSet.add(newElectionQuery.getVoterId());
        for (NewElectionQuery.Post post : newElectionQuery.getRegisteredPost()) {
            treeSet.addAll(post.getRegisteredContestants());
        }
        for (String voterId : treeSet) {
            Voter voter = dbGetter.getVoter(voterId);
            voter.setVoterPassword("************");
            voter.setElectionList(null);
            regElection.getEligibleVoters().add(voter);
        }

        // 6
        List<Post> availablePosts = new ArrayList<>();
        int currentPostIndex = 0;
        for (NewElectionQuery.Post post : newElectionQuery.getRegisteredPost()) {
            if (!postWorthRegistering(post)) {
                continue;
            }
            Post newPost = new Post();
            newPost.setPostId("P" + electionId + "-" + Integer.toString(currentPostIndex));
            newPost.setElectionId(electionId);
            newPost.setWinCriteria(Post.WinCriteria.GREATEST_NUMBER_OF_VOTES);
            newPost.setPostDescription(post.getPostDescription());
            newPost.setPostIndex(currentPostIndex);
            List<Voter> contestants = new ArrayList<>();
            TreeSet<String> uniqueContestantIds = new TreeSet<>(post.getRegisteredContestants());
            for (String contestantId : uniqueContestantIds) {
                Voter v = dbGetter.getVoter(contestantId);
                v.setVoterPassword("****************");
                v.setElectionList(null);
                contestants.add(v);
            }
            newPost.setContestants(contestants);
            currentPostIndex++;
            availablePosts.add(newPost);
        }
        regElection.setAvailablePost(availablePosts);
        return regElection;
    }


    private boolean postWorthRegistering(final NewElectionQuery.Post post) {
        return post != null &&
                post.getRegisteredContestants() != null &&
                !post.getRegisteredContestants().isEmpty();
    }

    // This function runs after the user is authenticated
    // 1. Voter authenticates ->
    // 2. Validate the query ->
    // 2. Election put in the db ->
    // 3. Admin put in voter map ->
    // 4. Registered Voters put in voter map [Includes Verification] ->
    // 5. For each post,
    private void validateNewElectionQuery(NewElectionQuery newElectionQuery) {
        validateIfVoterIdExists(newElectionQuery.getRegisteredVoters());
        validateRegisteredPosts(newElectionQuery.getRegisteredPost());
    }

    // Returns true if every voter present in reg voters is present
    private void validateIfVoterIdExists(final List<String> regVoters) {
        for (String regVoter : regVoters) {
            dbGetter.getVoter(regVoter);
        }
    }

    private void validateRegisteredPosts(final List<NewElectionQuery.Post> regPosts) {
        for (NewElectionQuery.Post post : regPosts) {
            validateIfVoterIdExists(post.getRegisteredContestants());
        }
    }
}
