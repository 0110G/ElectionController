package com.ElectionController.DatabaseConnector.Deleter;

import com.ElectionController.Constants.ControllerOperations;
import com.ElectionController.Exceptions.RestrictedActionException;
import com.ElectionController.Logger.ConsoleLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class H2Deleter implements Query {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final static String DELETE_VOTER_FROM_ELECTION_QUERY =
            "DELETE FROM VOTERMAP WHERE voterId = ? AND electionId = ?";

    private final static String DELETE_CANDIDATE_FROM_POST_QUERY =
            "DELETE FROM POSTMAP WHERE postId = ? AND contestantId = ?";

    @Override
    public void deleteVoterFromElection(final String voterId, final String electionId) {
        try {
            jdbcTemplate.update(
                    DELETE_VOTER_FROM_ELECTION_QUERY,
                    voterId, electionId
            );
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_DELETE_VOTER_FROM_ELECTION,
                    ex.getMessage(),
                    "VoterId:", voterId,
                    "ElectionId", electionId);
            throw new RestrictedActionException("Cannot delete entry");
        }
    }

    public void deleteCandidateFromPost(final String postId, final String candidateId) {
        try {
            jdbcTemplate.update(
                    DELETE_CANDIDATE_FROM_POST_QUERY,
                    postId, candidateId
            );
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_DELETE_CANDIDATE_FROM_POST,
                    ex.getMessage(), "PostId:", postId, "VoterID:", candidateId);
            throw new RestrictedActionException("Cannot delete entry");
        }
    }
}
