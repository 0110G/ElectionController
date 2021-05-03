package com.ElectionController.Controllers.ElectionControllerEndPoints;

import com.ElectionController.Constants.ControllerOperations;
import com.ElectionController.DatabaseConnector.Getter.H2Getter;
import com.ElectionController.DatabaseConnector.Putter.H2Putter;
import com.ElectionController.DatabaseConnector.Updater.H2Updater;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Exceptions.InvalidParameterException;
import com.ElectionController.Structures.Voter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

// TODO: Remove this class eventually
public class ElectionController {

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

    protected static void ValidateNotNull(final Object obj) {
        if (obj == null) {
            throw new InvalidParameterException("Invalid Parameter");
        }
    }

    protected static List<String> getUniqueEntities(final List<String> list) {
        if (list == null) {
            return list;
        }
        return list.stream().distinct().collect(Collectors.toList());
    }

    protected static Object getValueOrDefault(final Object value, final Object defaultVal) {
        if (value == null) {return defaultVal;}
        return value;
    }


}
