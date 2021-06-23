package com.electionController.controllers.electionController;

import com.electionController.constants.ControllerOperation;
import com.electionController.constants.ResponseCodes;
import com.electionController.controllers.ActionController;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.structures.Election;
import com.electionController.structures.Voter;
import com.electionController.structures.Contestant;
import com.electionController.structures.Post;
import com.electionController.structures.Response;
import com.electionController.structures.APIParams.GetElectionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.electionController.controllers.electionController.ElectionController.ValidateNotNull;

@RestController
public final class GetElectionOperation extends ActionController<GetElectionQuery, Response> {

    private static final ControllerOperation ACTION = ControllerOperation.GET_ELECTION;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public ControllerOperation getControllerOperation() {
        return this.ACTION;
    }

    @Override
    @PostMapping("/GetElection")
    public Response execute(@RequestBody GetElectionQuery getElectionQuery) {
        return super.execute(getElectionQuery);
    }

    @Override
    protected Response executeAction(final GetElectionQuery getElectionQuery) {
        return this.getElection(getElectionQuery);
    }

    @Override
    public void validateActionAccess(final GetElectionQuery getElectionQuery) {
        ValidateNotNull(getElectionQuery);
        authenticationFacade.validateVoterCredentials(getElectionQuery.getVoterId(),
                getElectionQuery.getVoterPassword());
        authenticationFacade.validateElectionViewer(getElectionQuery.getVoterId(),
                getElectionQuery.getElectionId());
    }

    private Response getElection(final GetElectionQuery getElectionQuery) {
        Election election = dbGetter.getElection(getElectionQuery.getElectionId());;
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
