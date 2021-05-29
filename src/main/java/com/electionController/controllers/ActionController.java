package com.electionController.controllers;

import com.electionController.constants.ControllerOperations;
import com.electionController.dbConnector.Getter.DBGetter;
import com.electionController.dbConnector.Putter.DBPutter;
import com.electionController.dbConnector.Updater.DBUpdater;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.structures.Voter;
import org.springframework.beans.factory.annotation.Autowired;

public class ActionController {
    @Autowired
    protected DBGetter dbGetter;

    @Autowired
    protected DBPutter dbPutter;

    @Autowired
    protected DBUpdater dbUpdater;

    protected Voter getAuthenticatedVoter(final String voterId,
                                          final String voterPassword,
                                          final ControllerOperations controllerOperation) {
        Voter voter = dbGetter.getVoter(voterId);
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
