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

        // Add entry to POSTMAP
        h2Updater.incrementCandidateVote(voteQuery.getPostId(), voteQuery.getCandidateId());

        return Response.Builder()
                .withResponse(null)
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .build();
    }

    // Returns true if given post exists and belongs to election
    private boolean checkIfPostBelongsToElection(final String electionId, final String postId) {
        try {
            h2Getter.getElectionPost(electionId, postId);
            return true;
        } catch (InvalidCredentialException ex) {
            return false;
        }
    }

    private boolean checkIfCandidateBelongsToPost(final String postId, final String contestantId) {
        try {
            h2Getter.getPostCandidate(postId, contestantId);
            return true;
        } catch (InvalidCredentialException ex) {
            return false;
        }
    }

    private boolean checkIfVoterHasVoted(final String voterId, final String electionId) {
        return h2Getter.getVoterMap(voterId, electionId).getVoterEligible();
    }

}
