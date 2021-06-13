package com.electionController.facades;

import com.electionController.dbConnector.Getter.DBGetter;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.exceptions.InvalidParameterException;
import com.electionController.exceptions.RestrictedActionException;
import com.electionController.exceptions.EntityNotFoundException;
import com.electionController.exceptions.InternalServiceException;
import com.electionController.structures.Voter;
import com.electionController.structures.VoterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AuthenticationFacade {

    @Autowired
    private DBGetter dbGetter;

    /** validateVoterCredentials validates
     * @param  voterId - voterId of the user
     * @param  voterPassword - password of the user
     * @exception  InvalidCredentialException if given voter credentials invalid
     * @exception  RestrictedActionException on internal/unexpected failures
     */
    public void validateVoterCredentials(final String voterId, final String voterPassword) {
       try {
           Voter voter = dbGetter.getVoter(voterId);
           if (voter != null) {
               if (voter.getVoterPassword() == null ||
                       !voter.getVoterPassword().equals(voterPassword)) {
                   throw new InvalidCredentialException("INVALID_PASSWORD, voterId:" + voterId);
               }
           } else {
               throw new InternalServiceException("NULL_VOTER_RETURNED, voterId:" + voterId);
           }
       } catch (EntityNotFoundException ex) {
           throw new InvalidCredentialException("INVALID_VOTER_ID" + voterId);
       }
    }

    /** validateVoterIds validates the voter ids passed in argument
     * @param  voterIds - List of voter ids to validate
     * @exception   InvalidParameterException if given voter id is invalid
     * @exception   InternalServiceException on internal/unexpected failures
     */
    public void validateVoterIds(final List<String> voterIds) {
        for (String voterId : voterIds) {
            try {
                dbGetter.getVoter(voterId);
            } catch (EntityNotFoundException ex) {
                throw new InvalidParameterException("INVALID_VOTER_ID voterId:" + voterId);
            }
        }
    }

    /** validateElectionAdmin validates the voter ids passed in argument
     * @param  voterId - List of voter ids to validate
     * @param electionId - Election id to validate
     * @exception   RestrictedActionException if given voter id is invalid, or election id is invalid or voter is
     *              not a part of mentioned election id, or voter not an admin for election asked
     * @exception   InternalServiceException on internal/unexpected failures
     */
    public void validateElectionAdmin(final String voterId, final String electionId) {
        try {
            VoterMap voterMap = dbGetter.getVoterMap(voterId, electionId);
            if (voterMap != null) {
                if (!voterMap.getVoterEligible() || !voterMap.getVoterAdmin()) {
                    throw new RestrictedActionException("VOTER_NOT_ADMIN voterId:" + voterId +
                            " electionId:" + electionId);
                }
            } else {
                throw new InternalServiceException("NULL_VOTER_MAP_RETURNED voterId:" + voterId + " electionId:" +
                        electionId);
            }
        } catch (EntityNotFoundException ex) {
            throw new RestrictedActionException("VOTER_NOT_BELONG_TO_ELECTION voterId:" + voterId + " electionId:" +
                    electionId);
        }
    }

    /** validateElectionViewer validates the voter permission to view an election
     * @param  voterId - List of voter ids to validate
     * @param electionId - Election id to validate
     * @exception   RestrictedActionException if given voter id is invalid, or election id is invalid or voter is
     *              not a part of mentioned election id, or voter not an admin for election asked
     * @exception   InternalServiceException on internal/unexpected failures
     */
    public void validateElectionViewer(final String voterId, final String electionId) {
        try {
            VoterMap voterMap = dbGetter.getVoterMap(voterId, electionId);
            if (voterMap == null) {
                throw new InternalServiceException("NULL_VOTER_MAP_RETURNED, voterId:" + voterId + " electionId:"
                    + electionId);
            }
        } catch (EntityNotFoundException ex) {
            throw new RestrictedActionException("VOTER_NOT_BELONG_TO_ELECTION voterId:" + voterId + " electionId:" +
                    electionId);
        }
    }

    /** validateElectionPost validates if the post belongs to given election
     * @param  electionId - Election id of the election to validate
     * @param  postId - Post id of the post to validate
     * @exception   InvalidParameterException if given election id is invalid, or post id id is invalid or post is
     *              not a part of mentioned election id
     * @exception   InternalServiceException on internal/unexpected failures
     */
    public void validateElectionPost(final String electionId, final String postId) {
        try {
            dbGetter.getElectionPost(electionId, postId);
        } catch (EntityNotFoundException ex) {
            throw new InvalidParameterException("POST_NOT_PART_OF_ELECTION postId: " + postId
            + " electionId:" + electionId);
        }
    }

    /** validateCandidatePost validates if the candidate is a contestant for the post
     * @param  postId - Post id of the post to validate
     * @param  candidateId - voter id of the voter to validate
     * @exception   InvalidParameterException if given post id is invalid, or voter id id is invalid or candidate is
     *              not a contestant of mentioned post id
     * @exception   InternalServiceException on internal/unexpected failures
     */
    public void validateCandidatePost(final String postId, final String candidateId) {
        try {
            dbGetter.getPostContestant(postId, candidateId);
        } catch (EntityNotFoundException ex) {
            throw new InvalidParameterException("CANDIDATE_NOT_REGISTERED_FOR_GIVEN_POST postId:" + postId +
                    "candidateId:" + candidateId);
        }
    }
}
