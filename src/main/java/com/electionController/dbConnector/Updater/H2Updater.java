package com.electionController.dbConnector.Updater;

import com.electionController.constants.ControllerOperation;
import com.electionController.exceptions.InternalServiceException;
import com.electionController.logger.ConsoleLogger;
import com.electionController.structures.Election;
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

    private static final String UPDATE_ELECTION_DESC_QUERY =
            "UPDATE ELECTION SET " +
                    "electionDescription = ?" +
                    "WHERE electionId = ?";

    private static final String UPDATE_ELECTION_TITLE_QUERY =
            "UPDATE ELECTION SET " +
                    "electionTitle = ?" +
                    "WHERE electionId = ?";


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
            ConsoleLogger.Log(ControllerOperation.DB_UPDATE_ELECTION, ex.getMessage(),
                    "ElectionId:", electionId,
                    "Election: ", election);
            throw new InternalServiceException("INTERNAL_ERROR_OCCURED: updateElection");
        }
    }

    @Override
    public void updateElectionDescription(final String electionId, final String electionDescription) {
        try {
            jdbcTemplate.update(
                    UPDATE_ELECTION_DESC_QUERY,
                    electionDescription,
                    electionId
            );
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperation.DB_UPDATE_ELECTION, ex.getMessage(),
                    "ElectionId:", electionId);
            throw new InternalServiceException("INTERNAL_ERROR_OCCURED: updateElectionDescription");
        }
    }

    @Override
    public void updateElectionTitle(final String electionId, final String electionTitle) {
        try {
            jdbcTemplate.update(
                    UPDATE_ELECTION_TITLE_QUERY,
                    electionTitle,
                    electionId
            );
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperation.DB_UPDATE_ELECTION, ex.getMessage(),
                    "ElectionId:", electionId);
            throw new InternalServiceException("INTERNAL_ERROR_OCCURED: updateElectionTitle");
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
            ConsoleLogger.Log(ControllerOperation.DB_UPDATE_INCREMENT_CANDIDATE_VOTE,
                    ex.getMessage(), "PostId:", postId, "ContestantId:", contestantId);
            throw new InternalServiceException("INTERNAL_ERROR_OCCURED: incrementCandidateVote");
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
            ConsoleLogger.Log(ControllerOperation.DB_UPDATE_MARK_VOTER_VOTED_FOR_POST, ex.getMessage(),
                    "VoterId:", voterId, "ElectionId:", electionId, "VotedPosts", votedPosts);
            throw new InternalServiceException("INTERNAL_ERROR_OCCURED: markVoterVotedForPost");
        }
    }
}
