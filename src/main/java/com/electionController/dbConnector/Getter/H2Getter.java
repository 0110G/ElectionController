package com.electionController.dbConnector.Getter;

import com.electionController.constants.ControllerOperations;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.exceptions.RestrictedActionException;
import com.electionController.logger.ConsoleLogger;
import com.electionController.structures.Election;
import com.electionController.structures.Post;
import com.electionController.structures.Voter;
import com.electionController.structures.VoterMap;
import com.electionController.structures.PostMap;
import com.electionController.structures.Contestant;
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
public class H2Getter implements DBGetter {

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

    private final static String GET_REGISTERED_POST_FOR_ELECTION_QUERY =
            "SELECT * FROM POST WHERE electionId = ? AND postId = ?";

    private final static String GET_POST_CANDIDATES_QUERY =
            "SELECT * FROM POSTMAP AS pm, VOTERS AS v WHERE pm.postId = ? AND pm.contestantId = v.voterId";

    private final static String GET_POST_CANDIDATE_QUERY =
            "SELECT * FROM POSTMAP AS pm, VOTERS AS v WHERE pm.postId = ? AND pm.contestantId = ? " +
                    "AND pm.contestantId = v.voterId";

    private final static String GET_POST_MAP_QUERY =
            "SELECT * FROM POSTMAP where ";


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
    public Voter getVoter(final String voterId) {
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
            throw new InvalidCredentialException("VOTER_DOES_NOT_EXISTS, VoterId: " + voterId);
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_GET_VOTER, ex.getMessage(),
                    "VoterId: ", voterId);
            throw new RestrictedActionException("INTERNAL_ERROR_OCCURED, VoterId: " + voterId);
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
                post.setContestants(getPostContestants(post.getPostId()));
            }
            return registeredPosts;
        } catch (DataAccessException ignored) {
            ConsoleLogger.Log(ControllerOperations.DB_GET_ELECTION_POSTS, ignored.getMessage(),
                    "ElectionId:", electionId);
            return new ArrayList<Post>();
        }
    }

    @Override
    public Post getElectionPost(final String electionId, final String postId) {
        Post post = null;
        try {
            post = jdbcTemplate.queryForObject(
                    GET_REGISTERED_POST_FOR_ELECTION_QUERY,
                    new PostMapper(),
                    electionId,
                    postId
            );
            if (post != null) {
                post.setContestants(getPostContestants(postId));
            }
            return post;
        } catch (EmptyResultDataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_GET_ELECTION_POST,
                    ex.getMessage(), "ElectionId:", electionId, "PostId:", postId);
            throw new InvalidCredentialException("NOT_FOUND_POST_FOR_GIVEN_ELECTION");
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_GET_ELECTION_POST,
                    ex.getMessage(), "ElectionId:", electionId, "PostId:", postId);
            throw new RestrictedActionException("INTERNAL_ERROR_OCCURED");
        }
    }

    @Override
    public List<Contestant> getPostContestants(final String postId) {
        List<Contestant> registeredCandidates = null;
        try {
            registeredCandidates = jdbcTemplate.query(
                    GET_POST_CANDIDATES_QUERY,
                    new ContestantMapper(),
                    postId
            );
            return registeredCandidates;
        } catch (DataAccessException ignored) {
            ConsoleLogger.Log(ControllerOperations.DB_GET_POST_CANDIDATES, ignored.getMessage(),
                    "PostId:", postId);
            return new ArrayList<Contestant>();
        }
    }

    @Override
    public Contestant getPostContestant(final String postId, final String contestantId) {
        Contestant contestant = null;
        try {
            contestant = jdbcTemplate.queryForObject(
                    GET_POST_CANDIDATE_QUERY,
                    new ContestantMapper(true),
                    postId,
                    contestantId
            );
            return contestant;
        } catch (EmptyResultDataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_GET_POST_CANDIDATE, ex.getMessage(),
                    "PostId:", postId, "ContestantId:", contestantId);
            throw new InvalidCredentialException("CANDIDATE_NOT_REGISTERED_FOR_POSTS");
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_GET_POST_CANDIDATE, ex.getMessage(),
                    "PostId:", postId, "ContestantId:", contestantId);
            throw new RestrictedActionException("INTERNAL_ERROR");
        }
    }

    // TODO: Add implementation when required.
    @Override
    public PostMap getPostMap(final String electionId, final String postId) {
        return null;
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
            voterMap.setHasVoted(rs.getString("votedPosts"));
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

    private static final class ContestantMapper implements RowMapper<Contestant> {
        private boolean maskPassword;
        ContestantMapper() {this.maskPassword = false;}
        ContestantMapper(final boolean maskPassword) {this.maskPassword = maskPassword;}
        public Contestant mapRow(ResultSet rs, int rowNum) throws SQLException {
            Contestant contestant = new Contestant();
            contestant.setVoterId(rs.getString("voterId"));
            if (this.maskPassword) {
                contestant.setVoterPassword("******************");
            } else {
                contestant.setVoterPassword(rs.getString("voterPassword"));
            }
            contestant.setVoterName(rs.getString("voterName"));
            contestant.setVotesSecured(rs.getInt("votesSecured"));
            return contestant;
        }
    }
}
