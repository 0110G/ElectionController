package com.electionController.controllers.ElectionControllerEndPoints;

import com.electionController.constants.ControllerOperations;
import com.electionController.constants.ResponseCodes;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.logger.ConsoleLogger;
import com.electionController.structures.Election;
import com.electionController.structures.Voter;
import com.electionController.structures.Contestant;
import com.electionController.structures.Post;
import com.electionController.structures.Response;
import com.electionController.structures.APIParams.NewElectionQuery;
import com.electionController.facades.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@RestController
public class NewElectionOperation extends ElectionController {

    private static int currentId = 0;

    private static final ControllerOperations ACTION = ControllerOperations.NEW_ELECTION;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    /** CreateElection takes
     * @param  newElectionQuery
     * and creates and returns an election
     * with given voter as admin for that election
     **/
    @PostMapping("/NewElection")
    public Response CreateElection(@RequestBody NewElectionQuery newElectionQuery) {

        // Body contains Valid Parameters
        ValidateNotNull(newElectionQuery);

        // Voter tries to authenticate himself
        try {
            authenticationFacade.validateVoterCredentials(newElectionQuery.getVoterId(),
                    newElectionQuery.getVoterPassword());
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(),
                    newElectionQuery);
            throw new InvalidCredentialException(ResponseCodes.INVALID_VOTER_CREDENTIALS);
        }

        // Creating a new election based on query
        Election regElection = null;
        try {
            regElection = mapNewElectionQueryToElection(newElectionQuery, Integer.toString(currentId));
            dbPutter.registerElection(regElection);
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(), newElectionQuery);
            throw new InvalidCredentialException(ex.getErrorMessage());
        }

        currentId++;

        return new Response.Builder()
                .withResponse(regElection)
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
        regElection.setElectionId(electionId);
        regElection.setAdminVoterId(newElectionQuery.getVoterId());
        TreeSet<String> treeSet = new TreeSet<>(newElectionQuery.getRegisteredVoters());
        treeSet.add(newElectionQuery.getVoterId());
        for (NewElectionQuery.Post post : newElectionQuery.getRegisteredPost()) {
            treeSet.addAll(post.getRegisteredContestants());
        }

        // TODO: GetAllVoters from db via voterIdlist [voterId]
        for (String voterId : treeSet) {
            Voter voter = dbGetter.getVoter(voterId);
            voter.setVoterPassword("************");
            voter.setElectionList(null);
            regElection.getEligibleVoters().add(voter);
        }

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
            List<Contestant> contestants = new ArrayList<>();
            TreeSet<String> uniqueContestantIds = new TreeSet<>(post.getRegisteredContestants());

            // TODO: GetAllVoters from db via voterIdlist [voterId]
            for (String contestantId : uniqueContestantIds) {
                Voter v = dbGetter.getVoter(contestantId);
                Contestant contestant = new Contestant();
                contestant.setVoterName(v.getVoterName());
                contestant.setVoterPassword("************");
                contestant.setVoterId(contestantId);
                contestant.setElectionList(null);
                contestant.setVotesSecured(0);
                contestant.setRank(1);
                contestants.add(contestant);
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
}
