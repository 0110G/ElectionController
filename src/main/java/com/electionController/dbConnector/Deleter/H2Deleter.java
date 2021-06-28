package com.electionController.dbConnector.Deleter;

import com.electionController.constants.ControllerOperation;
import com.electionController.exceptions.InternalServiceException;
import com.electionController.logger.ConsoleLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Configuration
@Repository
public class H2Deleter implements DBDeletor {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String DELETE_VOTER_FROM_ELECTION_QUERY =
            "DELETE FROM VOTERMAP WHERE voterId = ? AND electionId = ?";

    private static final String DELETE_ELECTION_VOTERS_QUERY =
            "DELETE FROM VOTERMAP WHERE electionId = ?";

    private static final String DELETE_POSTS_FROM_ELECTION_QUERY =
            "DELETE FROM POST WHERE electionId = ?";

    private static final String DELETE_ELECTION_CANDIDATES_QUERY =
            "DELETE FROM POSTMAP WHERE postId IN (SELECT p.postId FROM POST AS p WHERE p.electionId = ?)";

    private static final String DELETE_ELECTION_QUERY =
            "DELETE FROM ELECTION WHERE electionId = ?";

    private static final String DELETE_CANDIDATE_FROM_POST_QUERY =
            "DELETE FROM POSTMAP WHERE postId = ? AND contestantId = ?";

    @Override
    public void deleteVoterFromElection(final String voterId, final String electionId) {
        try {
            jdbcTemplate.update(
                    DELETE_VOTER_FROM_ELECTION_QUERY,
                    voterId, electionId
            );
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperation.DB_DELETE_VOTER_FROM_ELECTION,
                    ex.getMessage(),
                    "VoterId:", voterId,
                    "ElectionId", electionId);
            throw new InternalServiceException("Cannot delete entry");
        }
    }

    @Override
    public void deleteCandidateFromPost(final String postId, final String candidateId) {
        try {
            jdbcTemplate.update(
                    DELETE_CANDIDATE_FROM_POST_QUERY,
                    postId, candidateId
            );
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperation.DB_DELETE_CANDIDATE_FROM_POST,
                    ex.getMessage(), "PostId:", postId, "VoterID:", candidateId);
            throw new InternalServiceException("Cannot delete entry");
        }
    }

    @Override
    public void deleteElection(final String electionId) {
        try {
            jdbcTemplate.update(
                    DELETE_ELECTION_QUERY,
                    electionId
            );
            deleteElectionVoters(electionId);
            deleteElectionPosts(electionId);
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperation.DB_DELETE_ELECTION, ex.getMessage(), electionId);
            throw new InternalServiceException("Cannot delete election electionId:" + electionId);
        }
    }

    private void deleteElectionVoters(final String electionId) {
        try {
            jdbcTemplate.update(
                    DELETE_ELECTION_VOTERS_QUERY,
                    electionId
            );
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperation.DB_DELETE_ELECTION, ex.getMessage(), electionId);
            throw new InternalServiceException("Cannot delete election voters: electionId:" + electionId);
        }
    }

    private void deleteElectionPosts(final String electionId) {
        try {
            deleteElectionCandidates(electionId);
            jdbcTemplate.update(
                    DELETE_POSTS_FROM_ELECTION_QUERY,
                    electionId
            );
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperation.DB_DELETE_ELECTION, ex.getMessage(), electionId);
            throw new InternalServiceException("Cannot delete election posts: electionId:" + electionId);
        }
    }

    private void deleteElectionCandidates(final String electionId) {
        try {
            jdbcTemplate.update(
                    DELETE_ELECTION_CANDIDATES_QUERY,
                    electionId
            );
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperation.DB_DELETE_ELECTION, ex.getMessage(), electionId,
                    "deleteElectionCandidates");
            throw new InternalServiceException("Cannot delete election candidates: electionId:" + electionId);
        }
    }
}
