package com.ElectionController.Controllers;

import com.ElectionController.Constants.ControllerOperations;
import com.ElectionController.DatabaseConnector.Getter.DBGetter;
import com.ElectionController.DatabaseConnector.Putter.DBPutter;
import com.ElectionController.DatabaseConnector.Updater.DBUpdater;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Structures.Voter;
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
