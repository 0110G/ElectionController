package com.ElectionController.Controllers.ElectionControllerEndPoints;

import com.ElectionController.Constants.ControllerOperations;
import com.ElectionController.Constants.ResponseCodes;
import com.ElectionController.DatabaseConnector.Getter.H2Getter;
import com.ElectionController.DatabaseConnector.Putter.H2Putter;
import com.ElectionController.DatabaseConnector.Updater.H2Updater;
import com.ElectionController.Exceptions.EntityNotFoundException;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Exceptions.InvalidParameterException;
import com.ElectionController.Exceptions.RestrictedActionException;
import com.ElectionController.Logger.ConsoleLogger;
import com.ElectionController.Structures.APIParams.ChangeElectionQuery;
import com.ElectionController.Structures.Election;
import com.ElectionController.Structures.Response;
import com.ElectionController.Structures.Voter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class ChangeElectionOperation {

    @Autowired
    private H2Getter h2Getter;

    @Autowired
    private H2Updater h2Updater;

    // TODO: Add support for registering new Posts
    @PostMapping("/ChangeElection")
    public Response ChangeElection(@RequestBody ChangeElectionQuery changeElectionQuery) {

        ValidateNotNull(changeElectionQuery);

        Voter voter = null;
        try {
            voter = h2Getter.getVoter(changeElectionQuery.getVoterId());
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ControllerOperations.CHANGE_ELECTION, "USER_DOES_NOT_EXISTS", changeElectionQuery);
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        // Verifying Password
        if (voter == null ||
                voter.getVoterPassword() == null ||
                !voter.getVoterPassword().equals(changeElectionQuery.getVoterPassword())) {
            ConsoleLogger.Log(ControllerOperations.CHANGE_ELECTION, "USER_ENTERED_WRONG_PASSWORD",
                    "VoterID:", changeElectionQuery.getVoterId());
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        Election requestedElection = null;
        try {
            requestedElection = h2Getter.getElection(changeElectionQuery.getElectionId());
        } catch (InvalidCredentialException e) {
            ConsoleLogger.Log(ControllerOperations.CHANGE_ELECTION, "ELECTION_DDES_NOT_EXISTS",
                    "ElectionId:", changeElectionQuery.getElectionId());
            throw new EntityNotFoundException("Invalid Election id");
        }

        if(requestedElection == null ||
                requestedElection.getAdminVoterId() == null ||
                !requestedElection.getAdminVoterId().equals(changeElectionQuery.getVoterId())) {
            ConsoleLogger.Log(ControllerOperations.CHANGE_ELECTION, "USER_NOT_ELIGIBLE_TO_CHANGE",
                    "VoterId:", changeElectionQuery.getVoterId());
            throw new InvalidCredentialException("User does not have rights to change this election");
        }


        /*
         * The user might not want to change the electionTitle or electionId, in
         * in which we use the original values for these fields
         */
        requestedElection.setElectionTitle((String) getValueOrDefault(changeElectionQuery.getElectionTitle(),
                requestedElection.getElectionTitle()));
        requestedElection.setElectionDescription((String)
                getValueOrDefault(changeElectionQuery.getElectionDescription(),
                        requestedElection.getElectionDescription()));
        try {
            h2Updater.updateElection(changeElectionQuery.getElectionId(), requestedElection);
        } catch (RestrictedActionException ex) {
            ConsoleLogger.Log(ControllerOperations.CHANGE_ELECTION, "USER_NOT_ELIGIBLE_TO_CHANGE",
                    "ElectionId:", changeElectionQuery.getElectionId());
            throw new RestrictedActionException("Cannot update.");
        }

        return new Response.Builder()
                .withResponse(requestedElection)
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .build();
    }

    private static void ValidateNotNull(final Object obj) {
        if (obj == null) {
            throw new InvalidParameterException("Invalid Parameter");
        }
    }

    private Object getValueOrDefault(final Object value, final Object defaultVal) {
        if (value == null) {return defaultVal;}
        return value;
    }
}
