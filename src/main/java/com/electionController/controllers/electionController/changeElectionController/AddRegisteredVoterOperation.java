package com.electionController.controllers.electionController.changeElectionController;

import com.electionController.constants.ControllerOperation;
import com.electionController.controllers.ActionController;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.facades.ElectionControllerFacade;
import com.electionController.structures.APIParams.ChangeElection.AddRegisteredVoterToElectionQuery;
import com.electionController.structures.Response;
import com.electionController.structures.Voter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static com.electionController.controllers.electionController.ElectionController.ValidateNotNull;

@RestController
public class AddRegisteredVoterOperation extends ActionController<AddRegisteredVoterToElectionQuery, Response> {

    private static final ControllerOperation ACTION = ControllerOperation.CHANGE_ELECTION_ADD_VOTERS;

    @Autowired
    private ElectionControllerFacade electionControllerFacade;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public ControllerOperation getControllerOperation() {
        return this.ACTION;
    }

    @Override
    @PutMapping("/ChangeElection/AddVoters")
    public Response execute(@RequestBody AddRegisteredVoterToElectionQuery addRegisteredVoterToElectionQuery) {
        return super.execute(addRegisteredVoterToElectionQuery);
    }

    @Override
    protected Response executeAction(final AddRegisteredVoterToElectionQuery addRegisteredVoterToElectionQuery) {
        return this.addRegisteredVotersToElection(addRegisteredVoterToElectionQuery);
    }

    @Override
    public void validateActionAccess(final AddRegisteredVoterToElectionQuery addRegisteredVoterToElectionQuery) {
        ValidateNotNull(addRegisteredVoterToElectionQuery);
        authenticationFacade.validateVoterCredentials(addRegisteredVoterToElectionQuery.getVoterId(),
                addRegisteredVoterToElectionQuery.getVoterPassword());
        authenticationFacade.validateElectionAdmin(addRegisteredVoterToElectionQuery.getVoterId(),
                addRegisteredVoterToElectionQuery.getElectionId());
    }

    private Response addRegisteredVotersToElection(
            final AddRegisteredVoterToElectionQuery addRegisteredVoterToElectionQuery) {
        // Set of already registered
        Set<String> voters = dbGetter.getElectionVoters(addRegisteredVoterToElectionQuery.getElectionId())
                .stream().map(Voter::getVoterId).collect(Collectors.toSet());

        // There might be voterIds already present in election.
        // due to which copies of entries might get registered intp VOTERMAP
        // table. Hence remove all the pre registered voters
        addRegisteredVoterToElectionQuery.getVoterIdsToAdd().removeAll(voters);

        Set<String> distinctVoterIdsToAdd = new TreeSet<>(addRegisteredVoterToElectionQuery.getVoterIdsToAdd());

        // Query Validation: Validate
        authenticationFacade.validateVoterIds(new ArrayList<>(distinctVoterIdsToAdd));

        electionControllerFacade.addVotersToElection(
                new ArrayList<>(distinctVoterIdsToAdd),
                addRegisteredVoterToElectionQuery.getElectionId(), addRegisteredVoterToElectionQuery.getVoterId());

        return new Response.Builder()
                .withResponse(null)
                .withStatusCode(200)
                .withStatus("SUCCESS")
                .build();
    }
}
