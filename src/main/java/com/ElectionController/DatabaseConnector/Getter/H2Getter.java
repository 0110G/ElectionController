package com.ElectionController.DatabaseConnector.Getter;

import com.ElectionController.Constants.ControllerOperations;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Exceptions.RestrictedActionException;
import com.ElectionController.Logger.ConsoleLogger;
import com.ElectionController.Structures.Election;
import com.ElectionController.Structures.Post;
import com.ElectionController.Structures.Voter;
import com.ElectionController.Structures.VoterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    private final static String GET_VOTER_MAP_QUERY =
            "SELECT * FROM VOTERMAP WHERE voterId = ? AND electionId = ?";

    private final static String GET_REGISTERED_VOTERS_FOR_ELECTION_QUERY =
            "SELECT * FROM VOTERMAP AS vm, VOTERS AS v WHERE electionId = ? AND vm.voterId = v.voterId";

    private final static String GET_REGISTERED_POSTS_FOR_ELECTION_QUERY =
            "SELECT * FROM POST WHERE electionId = ?";

    private final static String GET_POST_CANDIDATES_QUERY =
            "SELECT * FROM POSTMAP AS pm, VOTERS AS v WHERE postId = ? AND pm.contestantId = v.voterId";

    @Override
    public Election getElection (final String electionId) {
        Election election = null;
        try {
            election = jdbcTemplate.queryForObject(
                    GET_ELECTION_QUERY,
                    new ElectionMapper(),
                    electionId);
            election.setEligibleVoters(getElectionVoters(electionId));
            election.setAvailablePost(getElectionPosts(electionId));
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

    @Override
    public VoterMap getVoterMap(final String voterId, final String electionId) {
        VoterMap voterMap = null;
        try {
            voterMap = jdbcTemplate.queryForObject(
                    GET_VOTER_MAP_QUERY,
                    new VoterMapMapper(),
                    voterId,
                    electionId
            );
            return voterMap;
        } catch (EmptyResultDataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_GET_VOTERMAP, ex.getMessage(),
                    "VoterId: ", voterId,
                    "ElectionId:", electionId);
            throw new InvalidCredentialException("VOTERMAP_ENTRY_DOES_NOT_EXIST");
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_GET_VOTERMAP, ex.getMessage(),
                    "VoterId: ", voterId,
                    "ElectionId:", electionId);
            throw new RestrictedActionException("INTERNAL_ERROR_OCCURED");
        }
    }

    @Override
    public List<Voter> getElectionVoters(final String electionId) {
        List<Voter> registeredVoters = new ArrayList<>();
        try {
            registeredVoters = jdbcTemplate.query(
                    GET_REGISTERED_VOTERS_FOR_ELECTION_QUERY,
                    new VoterMapper(),
                    electionId
            );
            return registeredVoters;
        } catch (DataAccessException ignored) {
            ConsoleLogger.Log(ControllerOperations.DB_GET_ELECTION_VOTERS, ignored.getMessage(),
                    "ElectionId:", electionId);
            return registeredVoters;
        }
    }

    @Override
    public List<Post> getElectionPosts(final String electionId) {
        List<Post> registeredPosts = null;
        try {
            registeredPosts = jdbcTemplate.query(
                    GET_REGISTERED_POSTS_FOR_ELECTION_QUERY,
                    new PostMapper(),
                    electionId
            );
            for (Post post : registeredPosts) {
                post.setContestants(getPostCandidates(post.getPostId()));
            }
            return registeredPosts;
        } catch (DataAccessException ignored) {
            ConsoleLogger.Log(ControllerOperations.DB_GET_ELECTION_POSTS, ignored.getMessage(),
                    "ElectionId:", electionId);
            return new ArrayList<Post>();
        }
    }

    @Override
    public List<Voter> getPostCandidates(final String postId) {
        List<Voter> registeredCandidates = null;
        try {
            registeredCandidates = jdbcTemplate.query(
                    GET_POST_CANDIDATES_QUERY,
                    new VoterMapper(),
                    postId
            );
            return registeredCandidates;
        } catch (DataAccessException ignored) {
            ConsoleLogger.Log(ControllerOperations.DB_GET_ELECTION_POSTS, ignored.getMessage(),
                    "PostId:", postId);
            return new ArrayList<Voter>();
        }
    }

    private static final class VoterMapper implements RowMapper<Voter> {
        private boolean maskPassword;

        VoterMapper() {this.maskPassword = false;}

        VoterMapper(final boolean maskPassword) {this.maskPassword = maskPassword;}

        public Voter mapRow(ResultSet rs, int rowNum) throws SQLException {
            Voter vp = new Voter();
            vp.setVoterId(rs.getString("voterId"));
            if (this.maskPassword) {
                vp.setVoterPassword("******************");
            } else {
                vp.setVoterPassword(rs.getString("voterPassword"));
            }
            vp.setVoterName(rs.getString("voterName"));
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

    private static final class VoterMapMapper implements RowMapper<VoterMap> {
        public VoterMap mapRow(ResultSet rs, int rowNum) throws SQLException {
            VoterMap voterMap = new VoterMap();
            voterMap.setVoterId(rs.getString("voterId"));
            voterMap.setElectionId(rs.getString("electionId"));
            voterMap.setVoterAdmin(rs.getBoolean("isVoterAdmin"));
            voterMap.setVoterEligible(rs.getBoolean("isVoterEligible"));
            return voterMap;
        }
    }

    private static final class PostMapper implements RowMapper<Post> {
        public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
            Post post = new Post();
            post.setPostId(rs.getString("postId"));
            post.setPostDescription(rs.getString("postDescription"));
            post.setElectionId(rs.getString("electionId"));
            post.setWinCriteria(Post.WinCriteria.getWinCriteria(rs.getInt("winCriteria")));
            return post;
        }
    }

    private static final class StringIdMapper implements RowMapper<String> {
        private String field = "voterId";
        StringIdMapper(String field) {
            this.field = field;
        }
        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            String voterId = rs.getString(field);
            return voterId;
        }
    }
}
