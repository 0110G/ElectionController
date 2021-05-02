package com.ElectionController.DatabaseConnector.Updater;

import com.ElectionController.Exceptions.RestrictedActionException;
import com.ElectionController.Structures.Election;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Configuration
@Repository
public class H2Updater implements Query {

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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Election updateElection(String electionId, Election election) {
        try {
            jdbcTemplate.update(
                    UPDATE_ELECTION_QUERY,
                    election.getElectionTitle(),
                    election.getElectionDescription(),
                    election.getElectionId()
            );
            return election;
        } catch (DataAccessException ex) {
            throw new RestrictedActionException("Some Internal error occured");
        }
    }

    public Election updateElection(String electionId, String voterId, Election election) {
        try {
            jdbcTemplate.update(
                    UPDATE_ELECTION_QUERY_SECURE,
                    election.getElectionTitle(),
                    election.getElectionDescription(),
                    election.getElectionId(),
                    election.getAdminVoterId()
            );
            return election;
        } catch (DataAccessException ex) {
            throw new RestrictedActionException("Invalid Admin Entered");
        }
    }
}
