package com.ElectionController.Controllers.ElectionControllerEndPoints;

import com.ElectionController.Constants.ControllerOperations;
import com.ElectionController.Constants.ResponseCodes;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Exceptions.RestrictedActionException;
import com.ElectionController.Logger.ConsoleLogger;
import com.ElectionController.Structures.APIParams.GetElectionQuery;
import com.ElectionController.Structures.Election;
import com.ElectionController.Structures.Post;
import com.ElectionController.Structures.Response;
import com.ElectionController.Structures.Voter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetElectionOperation extends ElectionController {
    @GetMapping("/GetElection")
    public Response GetElection(@RequestBody GetElectionQuery getElectionQuery) {

        ValidateNotNull(getElectionQuery);

        Voter voter = null;
        try {
            voter = getAuthenticatedVoter(getElectionQuery.getVoterId(), getElectionQuery.getVoterPassword(),
                    ControllerOperations.GET_ELECTION);
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ControllerOperations.GET_ELECTION, ex.getErrorMessage(), getElectionQuery);
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        VoterConsitencyCheck(voter, getElectionQuery);

        try {
            h2Getter.getVoterMap(getElectionQuery.getVoterId(),  getElectionQuery.getElectionId());
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ControllerOperations.GET_ELECTION, ex.getErrorMessage(),
                    getElectionQuery);
            throw new RestrictedActionException("Voter Ineligible to view given election");
        }

        // This is not required
        Election election = null;
        try {
            election = h2Getter.getElection(getElectionQuery.getElectionId());
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ControllerOperations.GET_ELECTION, ex.getErrorMessage(),
                    getElectionQuery);
            throw new InvalidCredentialException("Cannot find election");
        }

        List<Voter> registeredVoters = maskVoterPassword(h2Getter.getElectionVoters(getElectionQuery.getElectionId()));
        election.setEligibleVoters(registeredVoters);
        List<Post> registeredPosts = h2Getter.getElectionPosts(getElectionQuery.getElectionId());

        int index = 0;
        for (Post post : registeredPosts) {
            List<Voter> registeredCandidatesForPost = maskVoterPassword(h2Getter.getPostCandidates(post.getPostId()));
            registeredPosts.get(index).setContestants(registeredCandidatesForPost);
            index++;
        }

        election.setAvailablePost(registeredPosts);
        return new Response.Builder()
                .withResponse(election)
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .build();

    }

    private static void VoterConsitencyCheck(final Voter dbFetched,
                                             final GetElectionQuery getElectionQuery) {
        if (dbFetched == null ||
            dbFetched.getVoterId() == null ||
            dbFetched.getVoterPassword() == null ||
            !dbFetched.getVoterId().equals(getElectionQuery.getVoterId()) ||
            !dbFetched.getVoterPassword().equals(getElectionQuery.getVoterPassword())) {
            throw new RestrictedActionException("INCONSISTENCY_FOUND");
        }
    }

    private List<Voter> maskVoterPassword(List<Voter> unmaksedVoters) {
        for (Voter v : unmaksedVoters) {
            v.setVoterPassword("**********");
        }
        return unmaksedVoters;
    }
}
