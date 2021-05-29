package com.electionController.IntegTests.APITests;

import com.electionController.structures.APIParams.NewElectionQuery;
import com.electionController.structures.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class NewElectionTest implements APITest {

    public static void main(String args[]) {
        //MainClass.main(args);
        new NewElectionTest().run();
    }

    private final static String ELECTION_TITLE = "UNIT_TEST_ELECTION_TITLE";
    private final static String ELECTION_DESC = "UNIT_TEST_ELECTION_DESCR";

    @Override
    public void run() {
        succesfullyCreateNewElectionWithCorrectVoterCredentials();
        shouldMakeAnElectionSuccessfullyOnCorrectCredentials();
        shouldEnsureThatRegisteredCandidatesAreAlwaysRegisteredVoters();
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
        System.out.println("ADWadadwadwawdwadwwad");
    }



    public void succesfullyCreateNewElectionWithCorrectVoterCredentials() {
        List<String> registeredVoters = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6"));
        List<String> post1Cand = new ArrayList<>(Arrays.asList("1", "2", "3"));
        List<String> post2Cand = new ArrayList<>(Arrays.asList("3", "4", "5"));
        List<NewElectionQuery.Post>posts = new ArrayList<>(Arrays
        .asList( buildPost("Post1", post1Cand),
                buildPost("Post2", post2Cand)
        ));
        NewElectionQuery electionQuery = NewElectionQuery.Builder()
                .withElectionTitle(ELECTION_TITLE)
                .withElectionDescription(ELECTION_DESC)
                .withRegisteredPost(posts)
                .withRegisteredVoters(registeredVoters)
                .withVoterId("1")
                .withVoterPassword("1141")
                .build();
        Response response = sendRequest(electionQuery);
    }

    private NewElectionQuery.Post buildPost(final String postDesc, final List<String> registeredCandidates) {
        return NewElectionQuery.Post.Builder()
                .withRegisteredContestants(registeredCandidates)
                .withPostDescription(postDesc)
                .build();
    }

    private Response sendRequest(NewElectionQuery newElectionQuery) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,  objectMapper.writeValueAsString(newElectionQuery));
            Request request = new Request.Builder()
                    .url("http://localhost:8080/NewElection")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            okhttp3.Response httpResp = client.newCall(request).execute();
            return objectMapper.readValue(httpResp.body().string(), Response.class);
        } catch (Exception e) {
            return null;
        }
    }

    private String validateResponse(Response receivedResp, Response expectedResp) {
        return null;
    }



}
