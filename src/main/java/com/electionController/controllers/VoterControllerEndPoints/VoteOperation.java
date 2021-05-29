package com.electionController.controllers.VoterControllerEndPoints;

import com.electionController.constants.ControllerOperations;
import com.electionController.constants.ResponseCodes;
import com.electionController.controllers.ActionController;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.logger.ConsoleLogger;
import com.electionController.structures.APIParams.VoteQuery;
import com.electionController.structures.Response;
import com.electionController.structures.Voter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoteOperation extends ActionController {

    private static final ControllerOperations ACTION = ControllerOperations.VOTER_VOTE;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @PostMapping("/Vote")
    public Response Vote(@RequestBody VoteQuery voteQuery) {
        try {
            authenticationFacade.validateVoterCredentials(voteQuery.getVoterId(), voteQuery.getVoterPassword());
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(), voteQuery);
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        try {
            authenticationFacade.validateElectionPost(voteQuery.getElectionId(), voteQuery.getPostId());
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(), voteQuery);
            throw new InvalidCredentialException("Invalid Election/Post");
        }

//        // Check if election exists
//        if (!checkIfPostBelongsToElection(voteQuery.getElectionId(), voteQuery.getPostId())) {
//            ConsoleLogger.Log(ControllerOperations.VOTER_VOTE, "POST_INVALID", voteQuery);
//            throw new InvalidCredentialException("Invalid Post");
//        }

        try {
            authenticationFacade.validateCandidatePost(voteQuery.getPostId(), voteQuery.getCandidateId());
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(), voteQuery);
            throw new InvalidCredentialException("Invalid Post/Candidate");
        }

//        // Check if Candidate exists for given post
//        if (!checkIfCandidateBelongsToPost(voteQuery.getPostId(), voteQuery.getCandidateId())) {
//            ConsoleLogger.Log(ControllerOperations.VOTER_VOTE, "CANDIDATE_INVALID", voteQuery);
//            throw new InvalidCredentialException("Invalid Candidate");
//        }

        String voted = dbGetter.getVoterMap(voteQuery.getVoterId(), voteQuery.getElectionId()).getHasVoted();

        if(!checkIfVoterEligibleToVoteForGivenPostFromVotedString(voted, voteQuery.getPostId())) {
            ConsoleLogger.Log(ACTION, "ALREADY_VOTED", voted);
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
            dbGetter.getPostContestant(postId, contestantId);
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
