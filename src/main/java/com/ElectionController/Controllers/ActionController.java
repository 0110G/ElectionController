package com.ElectionController.Controllers;

import com.ElectionController.Constants.ControllerOperations;
import com.ElectionController.DatabaseConnector.Getter.H2Getter;
import com.ElectionController.DatabaseConnector.Putter.H2Putter;
import com.ElectionController.DatabaseConnector.Updater.H2Updater;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Structures.Voter;
import org.springframework.beans.factory.annotation.Autowired;

public class ActionController {
    @Autowired
    protected H2Getter h2Getter;

    @Autowired
    protected H2Putter h2Putter;

    @Autowired
    protected H2Updater h2Updater;

    protected Voter getAuthenticatedVoter(final String voterId,
                                          final String voterPassword,
                                          final ControllerOperations controllerOperation) {
        Voter voter = h2Getter.getVoter(voterId);
        if (voter != null) {
            if (voter.getVoterPassword() == null ||
                    !voter.getVoterPassword().equals(voterPassword)) {
                throw new InvalidCredentialException("INVALID_PASSWORD");
            }
            return voter;
        } else {
            throw new InvalidCredentialException("VOTER_ID_INVALID");
        }
    }
}
