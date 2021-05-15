package com.ElectionController.IntegTests.APITests;

import com.ElectionController.Structures.APIParams.NewElectionQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class NewElectionTest implements APITest {

    private final static String ELECTION_TITLE = "UNIT_TEST_ELECTION_TITLE";
    private final static String ELECTION_DESC = "UNIT_TEST_ELECTION_DESCR";

    @Override
    public void run() {

    }

    public void shouldMakeAnElectionSuccessfullyOnCorrectCredentials() {

    }

    public void shouldEnsureThatRegisteredCandidatesAreAlwaysRegisteredVoters() {

    }

    public void raiseExceptionWhenUserCredentialsAreInvalid() {

    }

    public void raiseExceptionWhenRegisteredVoterListContainsUnknownVoter() {

    }

    public void raiseExceptionWhenCandidateListContainsUnknownVoter() {

    }



    public void succesfullyCreateNewElectionWithCorrectVoterCredentials() {

        List<String> registeredVoters = new ArrayList<>(Arrays.asList("1, 2, 3, 4, 5, 6"));
        List<String> post1Cand = new ArrayList<>(Arrays.asList("1, 2, 3"));
        List<String> post2Cand = new ArrayList<>(Arrays.asList("3, 4, 5"));
        List<NewElectionQuery.Post>posts = new ArrayList<>(Arrays
        .asList( buildPost("Post1", post1Cand),
                buildPost("Post2", post2Cand)
        ));

        NewElectionQuery electionQuery = NewElectionQuery.Builder()
                .withElectionTitle(ELECTION_TITLE)
                .withElectionDescription(ELECTION_DESC)
                .withRegisteredPost(posts)
                .withRegisteredVoters(null)
                .withVoterId("1")
                .withVoterPassword("111")
                .build();
    }

    private NewElectionQuery.Post buildPost(final String postDesc, final List<String> registeredCandidates) {
        return NewElectionQuery.Post.Builder()
                .withRegisteredContestants(registeredCandidates)
                .withPostDescription(postDesc)
                .build();
    }

}
