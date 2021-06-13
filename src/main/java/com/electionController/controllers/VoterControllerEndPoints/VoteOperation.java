package com.electionController.controllers.VoterControllerEndPoints;

import com.electionController.constants.ControllerOperations;
import com.electionController.constants.ResponseCodes;
import com.electionController.controllers.ActionController;
import com.electionController.exceptions.InternalServiceException;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.exceptions.InvalidParameterException;
import com.electionController.exceptions.RestrictedActionException;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.logger.ConsoleLogger;
import com.electionController.structures.APIParams.VoteQuery;
import com.electionController.structures.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoteOperation extends ActionController {

    private static final ControllerOperations ACTION = ControllerOperations.VOTER_VOTE;

    @Autowired
    private AuthenticationFacade authenticationFacade;

   /**
    * Vote allows a voter to caste his/her voter for a particular candidate stading on a particular post.
    * A voter can vote only once for a given post. The choice of voter is not logged and is secretive.
    *
    * @param voteQuery - voteQuery consists of:
    *                  voterId - voter id of the voter to vote
    *                  voterPassword: voter password of the voter to vote
    *                  electionId: election id of the election voter plans to vote in
    *                  postId: post id of the post of the chosen election voter wants to vote
    *                  candidateId: voter id of the candidate of the post voter wants to vote for
    *
    * @return Response
    *
    * @exception  InvalidCredentialException - when incorrect voterId/voterName passed
    * @exception  InvalidParameterException - when incorrect electionid/postid/candidate id passed,
    *                                         when post does not belong to election and
    *                                         when candidate does not contest from given post
    * @exception  RestrictedActionException - when voter has already voter for given posts
    *                                         when voter is not elligible to vote for given post
    * @exception  InternalServiceException - on unexpected service failures
    * */
    @PostMapping("/Vote")
    public Response Vote(@RequestBody VoteQuery voteQuery) {
        try {
            authenticationFacade.validateVoterCredentials(voteQuery.getVoterId(), voteQuery.getVoterPassword());
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(), voteQuery);
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        try {
            authenticationFacade.validateElectionViewer(voteQuery.getVoterId(), voteQuery.getElectionId());
        } catch (RestrictedActionException ex) {
            throw new RestrictedActionException("Not eligible to vote for election");
        }

        try {
            authenticationFacade.validateElectionPost(voteQuery.getElectionId(), voteQuery.getPostId());
        } catch (InvalidParameterException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(), voteQuery);
            throw new InvalidParameterException("Invalid Election/Post");
        }

        try {
            authenticationFacade.validateCandidatePost(voteQuery.getPostId(), voteQuery.getCandidateId());
        } catch (InvalidParameterException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(), voteQuery);
            throw new InvalidParameterException("Invalid Post/Candidate");
        }

        String voted = dbGetter.getVoterMap(voteQuery.getVoterId(), voteQuery.getElectionId()).getHasVoted();

        if(!checkIfVoterEligibleToVoteForGivenPostFromVotedString(voted, voteQuery.getPostId())) {
            ConsoleLogger.Log(ACTION, "ALREADY_VOTED", voted);
            throw new RestrictedActionException("ALREADY_VOTED_FOR_POST");
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

    private boolean checkIfVoterEligibleToVoteForGivenPostFromVotedString(final String votedPosts,
                                                       final String postId) {
        int postIndex = getPostIndexFromPostId(postId);
        if (postIndex >= votedPosts.length()) {return false;}
        return votedPosts.charAt(postIndex) == '0';
    }

    private String getVotedString(final String originalVotedString, final int postVotedFor) {
        if (postVotedFor < 0 || postVotedFor >= originalVotedString.length()) {
            throw new InternalServiceException("CANNOT_GENERATE_VOTED_STRING");
        }
        StringBuilder newVotedBuilder = new StringBuilder(originalVotedString);
        newVotedBuilder.setCharAt(postVotedFor, '1');
        return newVotedBuilder.toString();
    }

    private int getPostIndexFromPostId(final String postId) {
        String[] tokens = postId.split("-");
        if (tokens.length < 2) {
            throw new InternalServiceException("CANNOT_PARSE_POST_ID");
        }
        return Integer.parseInt(tokens[1]);
    }


}
