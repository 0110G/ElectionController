package com.electionController.controllers.ElectionControllerEndPoints;

import com.electionController.constants.ControllerOperations;
import com.electionController.constants.ResponseCodes;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.exceptions.RestrictedActionException;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.logger.ConsoleLogger;
import com.electionController.structures.Election;
import com.electionController.structures.Voter;
import com.electionController.structures.Contestant;
import com.electionController.structures.Post;
import com.electionController.structures.Response;
import com.electionController.structures.APIParams.GetElectionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetElectionOperation extends ElectionController {

    private static final ControllerOperations ACTION = ControllerOperations.GET_ELECTION;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @GetMapping("/GetElection")
    public Response GetElection(@RequestBody GetElectionQuery getElectionQuery) {

        ValidateNotNull(getElectionQuery);

        try {
            authenticationFacade.validateVoterCredentials(getElectionQuery.getVoterId(),
                    getElectionQuery.getVoterPassword());
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(), getElectionQuery);
            throw new InvalidCredentialException("Invalid Username/Password");
        }

        try {
            authenticationFacade.validateElectionViewer(getElectionQuery.getVoterId(),
                    getElectionQuery.getElectionId());
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(), getElectionQuery);
            throw new RestrictedActionException("Voter Ineligible to view given election");
        }

        // This is not required
        Election election = null;
        try {
            election = dbGetter.getElection(getElectionQuery.getElectionId());
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(ACTION, ex.getErrorMessage(),
                    getElectionQuery);
            throw new InvalidCredentialException("Cannot find election");
        }

        assert election != null;

        election.setEligibleVoters((List<Voter>) maskVoterPassword(election.getEligibleVoters()));
        for (Post post : election.getAvailablePost()) {
            post.setContestants((List<Contestant>) maskVoterPassword(post.getContestants()));
        }

        return new Response.Builder()
                .withResponse(election)
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .build();

    }

    private List<? extends Voter> maskVoterPassword(List<? extends Voter> unmaskedVoters) {
        for (Voter v : unmaskedVoters) {
            v.setVoterPassword("**********");
        }
        return unmaskedVoters;
    }
}
