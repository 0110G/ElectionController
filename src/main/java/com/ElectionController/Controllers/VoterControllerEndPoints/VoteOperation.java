package com.ElectionController.Controllers.VoterControllerEndPoints;

import com.ElectionController.Constants.ControllerOperations;
import com.ElectionController.Constants.ResponseCodes;
import com.ElectionController.Controllers.ActionController;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Logger.ConsoleLogger;
import com.ElectionController.Structures.APIParams.VoteQuery;
import com.ElectionController.Structures.Response;
import com.ElectionController.Structures.Voter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoteOperation extends ActionController {

    @PostMapping("/Vote")
    public Response vote(@RequestBody VoteQuery voteQuery) {
        Voter voter = null;
        try {
            voter = getAuthenticatedVoter(voteQuery.getVoterId(), voteQuery.getVoterPassword(),
                    ControllerOperations.VOTER_VOTE);
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ControllerOperations.VOTER_VOTE, ex.getErrorMessage(),
                    voteQuery);
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        // Check if election exists
        if (!checkIfPostBelongsToElection(voteQuery.getElectionId(), voteQuery.getPostId())) {
            ConsoleLogger.Log(ControllerOperations.VOTER_VOTE, "POST_INVALID", voteQuery);
            throw new InvalidCredentialException("Invalid Post");
        }

        // Check if Candidate exists for given post
        if (!checkIfCandidateBelongsToPost(voteQuery.getPostId(), voteQuery.getCandidateId())) {
            ConsoleLogger.Log(ControllerOperations.VOTER_VOTE, "CANDIDATE_INVALID", voteQuery);
            throw new InvalidCredentialException("Invalid Candidate");
        }

        String voted = dbGetter.getVoterMap(voteQuery.getVoterId(), voteQuery.getElectionId()).getHasVoted();

        if(!checkIfVoterEligibleToVoteForGivenPostFromVotedString(voted, voteQuery.getPostId())) {
            ConsoleLogger.Log(ControllerOperations.VOTER_VOTE, "ALREADY_VOTED", voted);
            throw new InvalidCredentialException("ALREADY_VOTED_FOR_POST");
        }

        // Add entry to POSTMAP
        dbUpdater.incrementCandidateVote(voteQuery.getPostId(), voteQuery.getCandidateId());
        dbUpdater.markVoterVotedForPost(voteQuery.getVoterId(), voteQuery.getElectionId(),
                getVotedString(voted, getPostIndexFromPostId(voteQuery.getPostId())));

        return Response.Builder()
                .withResponse(null)
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .build();
    }

    // Returns true if given post exists and belongs to election
    private boolean checkIfPostBelongsToElection(final String electionId, final String postId) {
        try {
            dbGetter.getElectionPost(electionId, postId);
            return true;
        } catch (InvalidCredentialException ex) {
            return false;
        }
    }

    private boolean checkIfCandidateBelongsToPost(final String postId, final String contestantId) {
        try {
            dbGetter.getPostCandidate(postId, contestantId);
            return true;
        } catch (InvalidCredentialException ex) {
            return false;
        }
    }

    private boolean checkIfVoterEligibleToVoteForGivenPostFromVotedString(final String votedPosts,
                                                       final String postId) {
        int postIndex = getPostIndexFromPostId(postId);
        if (postIndex >= votedPosts.length()) {return false;}
        return votedPosts.charAt(postIndex) == '0';
    }

    private String getVotedString(final String originalVotedString, final int postVotedFor) {
        if (postVotedFor < 0 || postVotedFor >= originalVotedString.length()) {
            throw new InvalidCredentialException("CANNOT_GENERATE_VOTED_STRING");
        }
        StringBuilder newVotedBuilder = new StringBuilder(originalVotedString);
        newVotedBuilder.setCharAt(postVotedFor, '1');
        return newVotedBuilder.toString();
    }

    private int getPostIndexFromPostId(final String postId) {
        String[] tokens = postId.split("-");
        if (tokens.length < 2) {
            throw new InvalidCredentialException("CANNOT_PARSE_POST_ID");
        }
        return Integer.parseInt(tokens[1]);
    }


}
