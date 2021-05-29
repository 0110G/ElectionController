package com.electionController.facades;

import com.electionController.dbConnector.Getter.DBGetter;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.exceptions.RestrictedActionException;
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
     * @exception  InvalidCredentialException if given voter id is invalid
     * @exception  RestrictedActionException on internal/unexpected failures
     */
    public void validateVoterCredentials(final String voterId, final String voterPassword) {
       Voter voter = dbGetter.getVoter(voterId);
        if (voter != null) {
            if (voter.getVoterPassword() == null ||
                    !voter.getVoterPassword().equals(voterPassword)) {
                throw new InvalidCredentialException("INVALID_PASSWORD");
            }
        } else {
            throw new RestrictedActionException("NULL_VOTER_RETURNED, voterId:" + voterId);
        }
    }

    /** validateVoterIds validates the voter ids passed in argument
     * @param  voterIds - List of voter ids to validate
     * @exception   InvalidCredentialException if given voter id is invalid, or incorrect password entered;
     * @exception   RestrictedActionException on internal/unexpected failures
     */
    public void validateVoterIds(final List<String> voterIds) {
        for (String voterId : voterIds) {
            dbGetter.getVoter(voterId);
        }
    }

    /** validateElectionAdmin validates the voter ids passed in argument
     * @param  voterId - List of voter ids to validate
     * @param electionId - Election id to validate
     * @exception   InvalidCredentialException if given voter id is invalid, or election id is invalid or voter is
     *              not a part of mentioned election id, or voter not an admin for election asked
     * @exception   RestrictedActionException on internal/unexpected failures
     */
    public void validateElectionAdmin(final String voterId, final String electionId) {
        VoterMap voterMap = dbGetter.getVoterMap(voterId, electionId);
        if (voterMap != null) {
            if (!voterMap.getVoterEligible() || !voterMap.getVoterAdmin()) {
                throw new InvalidCredentialException("VOTER NOT ADMIN");
            }
        } else {
            throw new RestrictedActionException("NULL_VOTER_MAP_RETURNED, voterId:" + voterId);
        }
    }

    /** validateElectionViewer validates the voter permission to view an election
     * @param  voterId - List of voter ids to validate
     * @param electionId - Election id to validate
     * @exception   InvalidCredentialException if given voter id is invalid, or election id is invalid or voter is
     *              not a part of mentioned election id, or voter not an admin for election asked
     * @exception   RestrictedActionException on internal/unexpected failures
     */
    public void validateElectionViewer(final String voterId, final String electionId) {
        VoterMap voterMap = dbGetter.getVoterMap(voterId, electionId);
        if (voterMap == null) {
            throw new RestrictedActionException("NULL_VOTER_MAP_RETURNED, voterId:" + voterId);
        }
    }

    /** validateElectionPost validates if the post belongs to given election
     * @param  electionId - Election id of the election to validate
     * @param  postId - Post id of the post to validate
     * @exception   InvalidCredentialException if given election id is invalid, or post id id is invalid or post is
     *              not a part of mentioned election id
     * @exception   RestrictedActionException on internal/unexpected failures
     */
    public void validateElectionPost(final String electionId, final String postId) {
        dbGetter.getElectionPost(electionId, postId);
    }

    /** validateCandidatePost validates if the candidate is a contestant for the post
     * @param  postId - Post id of the post to validate
     * @param  candidateId - voter id of the voter to validate
     * @exception   InvalidCredentialException if given post id is invalid, or voter id id is invalid or candidate is
     *              not a contestant of mentioned post id
     * @exception   RestrictedActionException on internal/unexpected failures
     */
    public void validateCandidatePost(final String postId, final String candidateId) {
        dbGetter.getPostContestant(postId, candidateId);
    }

}
