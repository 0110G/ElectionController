package com.ElectionController.DatabaseConnector.Getter;

import com.ElectionController.Constants.ControllerOperations;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Exceptions.RestrictedActionException;
import com.ElectionController.Logger.ConsoleLogger;
import com.ElectionController.Structures.Election;
import com.ElectionController.Structures.Voter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Configuration
@Repository
public class H2Getter implements Query{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final static String GET_ELECTION_QUERY =
            "SELECT * FROM ELECTION WHERE electionId = ?";

    private final static String GET_VOTER_QUERY =
            "SELECT * FROM VOTERS WHERE voterId = ?";

    @Override
    public Election getElection (final String electionId) {
        Election election = null;
        try {
            election = jdbcTemplate.queryForObject(
                    GET_ELECTION_QUERY,
                    new ElectionMapper(),
                    electionId);
            return election;
        } catch (EmptyResultDataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_GET_ELECTION, ex.getMessage(),
                    "ElectionId: ", electionId);
            throw new InvalidCredentialException("ELECTION_DOES_NOT_EXISTS");
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_GET_ELECTION, ex.getMessage(),
                    "ElectionId:", electionId);
            throw new RestrictedActionException("INTERNAL_ERROR_OCCURED");
        }
    }

    @Override
    public Voter getVoter (final String voterId) {
        Voter voter = null;
        try {
            voter = jdbcTemplate.queryForObject(
                    GET_VOTER_QUERY,
                    new VoterMapper(),
                    voterId);
            return voter;
        } catch (EmptyResultDataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_GET_VOTER, ex.getMessage(),
                    "VoterId: ", voterId);
            throw new InvalidCredentialException("VOTER_DOES_NOT_EXISTS");
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_GET_VOTER, ex.getMessage(),
                    "VoterId: ", voterId);
            throw new RestrictedActionException("INTERNAL_ERROR_OCCURED");
        }
    }

    public List<Voter> authenticateVoter1(final String enteredVoterId, final String enteredPassword) {
        String query = "SELECT voterId, voterName, voterPassword FROM VOTERS";
        return jdbcTemplate.query(
                query,
                new VoterMapper()
        );
    }

    private static final class VoterMapper implements RowMapper<Voter> {
        public Voter mapRow(ResultSet rs, int rowNum) throws SQLException {
            Voter vp = new Voter();
            vp.setVoterId(rs.getString("voterId"));
            vp.setVoterName(rs.getString("voterName"));
            vp.setVoterPassword(rs.getString("voterPassword"));
            return vp;
        }
    }

    private static final class ElectionMapper implements RowMapper<Election> {
        public Election mapRow(ResultSet rs, int rowNum) throws SQLException {
            Election el = new Election();
            el.setElectionTitle(rs.getString("electionTitle"));
            el.setElectionId(rs.getString("electionId"));
            el.setElectionDescription(rs.getString("electionDescription"));
            el.setAdminVoterId(rs.getString("adminVoterId"));
            return el;
        }
    }
}
