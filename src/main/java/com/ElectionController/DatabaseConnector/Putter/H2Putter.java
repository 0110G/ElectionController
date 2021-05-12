package com.ElectionController.DatabaseConnector.Putter;

import com.ElectionController.Constants.ControllerOperations;
import com.ElectionController.Exceptions.RestrictedActionException;
import com.ElectionController.Logger.ConsoleLogger;
import com.ElectionController.Structures.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Types;

@Configuration
public class H2Putter implements DBPutter {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String REGISTER_ELECTION_QUERY =
            "INSERT INTO ELECTION (" +
            "electionTitle, " +
            "electionId, " +
            "electionDescription, " +
            "adminVoterId, " +
            "postSetId) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String REGISTER_VOTER_ELECTION_QUERY =
            "INSERT INTO VOTERMAP (" +
            "voterId, " +
            "electionId, " +
            "isVoterEligible, " +
            "isVoterAdmin) " +
            "VALUES (?, ?, ?, ?)";

    private static final String REGISTER_POST_ELECTION_QUERY =
            "INSERT INTO POST (" +
            "postId, " +
            "postDescription, " +
            "electionId, " +
            "totalCandidates, " +
            "winCriteria) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String REGISTER_CANDIDATE_POST_QUERY =
            "INSERT INTO POSTMAP (" +
             "postId, " +
             "contestantId, " +
             "contestantAlias, " +
             "votesSecured) " +
             "VALUES (?, ?, ?, ?)";

    private static final String REGISTER_VOTER_QUERY =
            "INSERT INTO VOTERS (" +
            "voterId, " +
            "voterName, " +
            "voterPassword) " +
            "VALUES (?, ?, ?)";

    @Override
    public void registerElection(final Election election) {
        try {
            jdbcTemplate.update(REGISTER_ELECTION_QUERY,
                    new Object[]{election.getElectionTitle(),
                                 election.getElectionId(),
                                 election.getElectionDescription(),
                                 election.getAdminVoterId(),
                                 "1"
                    },
                    new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR}
            );
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_PUT_ELECTION, ex.getMessage(), election);
            throw new RestrictedActionException("Error While Creating Entry in Election");
        }
    }

    @Override
    public void registerVoterForElection(final VoterMap voterMap) {
        try {
            jdbcTemplate.update(REGISTER_VOTER_ELECTION_QUERY,
                    new Object[]{voterMap.getVoterId(),
                            voterMap.getElectionId(),
                            voterMap.getVoterEligible(),
                            voterMap.getVoterAdmin()
                    },
                    new int[]{Types.VARCHAR, Types.VARCHAR, Types.BIT, Types.BIT}
            );
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_PUT_VOTERMAP, ex.getMessage(), voterMap);
            throw new RestrictedActionException("Error While Creating Entry in VOTERMAP");
        }
    }

    @Override
    public void registerPostForElection(final Post post) {
        try {
            jdbcTemplate.update(REGISTER_POST_ELECTION_QUERY,
                    new Object[]{
                            post.getPostId(),
                            post.getPostDescription(),
                            post.getElectionId(),
                            post.getTotalContestants(),
                            post.getWinCriteria().getCode()
                    },
                    new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER}
            );
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_PUT_POST, ex.getMessage(), post);
            throw new RestrictedActionException("Error While creating Entry in POST");
        }
    }

    @Override
    public void registerCandidatesForPost(final PostMap postMap) {
        try {
            jdbcTemplate.update(REGISTER_CANDIDATE_POST_QUERY,
                    new Object[]{
                            postMap.getPostId(),
                            postMap.getContestantId(),
                            postMap.getContestantAlias(),
                            postMap.getVotesSecured()
                    },
                    new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER}
            );
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_PUT_POSTMAP, ex.getMessage(), postMap);
            throw new RestrictedActionException("Error while registering candidate for post");
        }
    }

    @Override
    public void registerVoter(final Voter voter) {
        try {
            jdbcTemplate.update(REGISTER_VOTER_QUERY,
                    new Object[]{
                            voter.getVoterId(),
                            voter.getVoterName(),
                            voter.getVoterPassword()
                    },
                    new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR}
            );
        } catch (DataAccessException ex) {
            ConsoleLogger.Log(ControllerOperations.DB_PUT_VOTER, ex.getMessage(), voter);
            throw new RestrictedActionException("Error while creating new voter");
        }
    }
}
