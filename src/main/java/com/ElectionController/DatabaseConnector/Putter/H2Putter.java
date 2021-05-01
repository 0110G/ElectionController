package com.ElectionController.DatabaseConnector.Putter;

import com.ElectionController.Exceptions.RestrictedActionException;
import com.ElectionController.Structures.Election;
import com.ElectionController.Structures.Voter;
import com.ElectionController.Structures.VoterMap;
import org.apache.tomcat.util.net.openssl.ciphers.Encryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sun.security.krb5.EncryptionKey;

import java.nio.charset.StandardCharsets;
import java.sql.Types;

@Configuration
@Repository
public class H2Putter implements Query {

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

    @Override
    public Election registerElection(Election election) {
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
            return election;
        } catch (DataAccessException ex) {
            throw new RestrictedActionException("Error While Creating Entry in Election");
        }
    }

    @Override
    public VoterMap registerVoterForElection(VoterMap voterMap) {
        try {
            jdbcTemplate.update(REGISTER_VOTER_ELECTION_QUERY,
                    new Object[]{voterMap.getVoterId(),
                            voterMap.getElectionId(),
                            voterMap.getVoterEligible(),
                            voterMap.getVoterAdmin()
                    },
                    new int[]{Types.VARCHAR, Types.VARCHAR, Types.BIT, Types.BIT}
            );
            return voterMap;
        } catch (DataAccessException ex) {
            throw new RestrictedActionException("Error While Creating Entry in VoterMap");
        }
    }
}
