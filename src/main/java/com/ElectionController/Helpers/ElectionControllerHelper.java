package com.ElectionController.Helpers;

import com.ElectionController.DatabaseConnector.Deleter.H2Deleter;
import com.ElectionController.DatabaseConnector.Getter.H2Getter;
import com.ElectionController.DatabaseConnector.Putter.H2Putter;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Structures.VoterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ElectionControllerHelper {

    @Autowired
    private H2Putter h2Putter;

    @Autowired
    private H2Getter h2Getter;

    @Autowired
    private H2Deleter h2Deleter;

    // Always called after the following authentications:
    // 1: The admin credentials are authentic
    // 2: The election id is authentic
    // 3: The query requester is election admin
    public void addVotersToElection(final List<String> voters, final String electionId, final String electionAdmin) {

        // Verifying that the set of voters is legitimate
        boolean verifyVotersList = true;
        for (String voterId : voters) {
            verifyVotersList = verifyVotersList && voterIdValid(voterId);
        }

        if (!verifyVotersList) {
            throw new InvalidCredentialException("Requested Voter Does not exists!");
        }

        for (String voterId : voters) {
            if (voterId.equals(electionAdmin)) {
                continue;
            }
            VoterMap voterMap = new VoterMap();
            voterMap.setVoterEligible(true);
            voterMap.setVoterAdmin(false);
            voterMap.setVoterId(voterId);
            voterMap.setElectionId(electionId);
            h2Putter.registerVoterForElection(voterMap);
        }
    }

    public void deleteVotersFromElection(final List<String> votersToDelete,
                                         final String electionId,
                                         final String electionAdmin) {
        for (String voterId : votersToDelete) {
            if (electionAdmin.equals(voterId)) {continue;}
            h2Deleter.deleteVoterFromElection(voterId, electionId);
        }
    }

    private boolean voterIdValid(final String voterId) {
        try {
            h2Getter.getVoter(voterId);
            return true;
        } catch (InvalidCredentialException ex) {
            return false;
        }
    }
}
