package com.electionController.controllers.voterController;

import com.electionController.constants.ControllerOperation;
import com.electionController.constants.ResponseCodes;
import com.electionController.controllers.ActionController;
import com.electionController.exceptions.InternalServiceException;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.exceptions.InvalidParameterException;
import com.electionController.exceptions.RestrictedActionException;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.structures.APIParams.VoteQuery;
import com.electionController.structures.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.electionController.controllers.electionController.ElectionController.ValidateNotNull;

@RestController
public class VoteOperation extends ActionController<VoteQuery, Response> {

    private static final ControllerOperation ACTION = ControllerOperation.VOTER_VOTE;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public ControllerOperation getControllerOperation() {
        return this.ACTION;
    }

    @Override
    @PostMapping("/Vote")
    public Response execute(@RequestBody VoteQuery voteQuery) {
        return super.execute(voteQuery);
    }

    @Override
    public Response executeAction(VoteQuery voteQuery) {
        return this.vote(voteQuery);
    }

    @Override
    public void validateActionAccess(final VoteQuery voteQuery) {
        ValidateNotNull(voteQuery);
        authenticationFacade.validateVoterCredentials(voteQuery.getVoterId(), voteQuery.getVoterPassword());
        authenticationFacade.validateElectionViewer(voteQuery.getVoterId(), voteQuery.getElectionId());
        authenticationFacade.validateElectionPost(voteQuery.getElectionId(), voteQuery.getPostId());
        authenticationFacade.validateCandidatePost(voteQuery.getPostId(), voteQuery.getCandidateId());
    }

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
    private Response vote(final VoteQuery voteQuery) {
        String voted = dbGetter.getVoterMap(voteQuery.getVoterId(), voteQuery.getElectionId()).getHasVoted();
        if(!checkIfVoterEligibleToVoteForGivenPostFromVotedString(voted, voteQuery.getPostId())) {
            throw new RestrictedActionException(ResponseCodes.NOT_ELIGIBLE_TO_VOTE.getResponseCode(),
                    ResponseCodes.NOT_ELIGIBLE_TO_VOTE.getResponse(), null);
        }
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
