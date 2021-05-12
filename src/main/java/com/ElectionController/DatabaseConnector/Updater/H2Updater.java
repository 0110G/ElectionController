package com.ElectionController.DatabaseConnector.Updater;

import com.ElectionController.Constants.ControllerOperations;
import com.ElectionController.Exceptions.RestrictedActionException;
import com.ElectionController.Logger.ConsoleLogger;
import com.ElectionController.Structures.Election;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Configuration
@Repository
public class H2Updater implements DBUpdater {

    private static final String UPDATE_ELECTION_QUERY =
            "UPDATE ELECTION SET " +
            "electionTitle = ?," +
            "electionDescription = ?" +
            "WHERE electionId = ?";

    private static final String UPDATE_ELECTION_QUERY_SECURE =
            "UPDATE ELECTION SET " +
            "electionTitle = ?," +
            "electionDescription = ?" +
            "WHERE electionId = ? " +
            "AND adminVoterId = ?";

    private static final String UPDATE_CANDIDATE_ADD_VOTE_QUERY =
            "UPDATE POSTMAP SET " +
            "votesSecured = votesSecured + 1" +
            "WHERE postId = ? " +
            "AND contestantId = ?";

    private static final String UPDATE_MARK_VOTING_DONE_QUERY =
            "UPDATE VOTERMAP SET " +
            "votedPosts = ?" +
            "WHERE voterId = ? " +
            "AND electionId = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void updateElection(final String electionId, final Election election) {
        try {
            jdbcTemplate.update(
                    UPDATE_ELECTION_QUERY,
                    election.getElectionTitle(),
                    election.getElectionDescription(),
                    election.getElectionId()
            );
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_UPDATE_ELECTION, ex.getMessage(),
                    "ElectionId:", electionId,
                    "Election: ", election);
            throw new RestrictedActionException("INTERNAL_ERROR_OCCURED");
        }
    }

    public void updateElection(final String electionId, final String voterId, final Election election) {
        try {
            jdbcTemplate.update(
                    UPDATE_ELECTION_QUERY_SECURE,
                    election.getElectionTitle(),
                    election.getElectionDescription(),
                    election.getElectionId(),
                    election.getAdminVoterId()
            );
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_UPDATE_ELECTION, ex.getMessage(),
                   "ElectionId:", electionId,
                    "VoterId:", voterId,
                    "Election: ", election);
            throw new RestrictedActionException("Invalid Admin Entered");
        }
    }

    @Override
    public void incrementCandidateVote(final String postId, final String contestantId) {
        try {
            jdbcTemplate.update(
                    UPDATE_CANDIDATE_ADD_VOTE_QUERY,
                    postId,
                    contestantId
            );
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_UPDATE_INCREMENT_CANDIDATE_VOTE,
                    ex.getMessage(), "PostId:", postId, "ContestantId:", contestantId);
            throw new RestrictedActionException("INTERNAL_ERROR_OCCURED");
        }
    }

    @Override
    public void markVoterVotedForPost(final String voterId,
                                      final String electionId,
                                      final String votedPosts) {
        try {
            jdbcTemplate.update(
                    UPDATE_MARK_VOTING_DONE_QUERY,
                    votedPosts,
                    voterId,
                    electionId
            );
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_UPDATE_MARK_VOTER_VOTED_FOR_POST, ex.getMessage(),
                    "VoterId:", voterId, "ElectionId:", electionId, "VotedPosts", votedPosts);
            throw new RestrictedActionException("INTERNAL_ERROR_OCCURED");
        }
    }
}
