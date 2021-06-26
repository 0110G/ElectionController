package ElectionControllerOperations;

import com.electionController.constants.ResponseCodes;
import com.electionController.constants.TestConstants;
import com.electionController.controllers.electionController.NewElectionOperation;
import com.electionController.dbConnector.Getter.DBGetter;
import com.electionController.dbConnector.Putter.DBPutter;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.exceptions.InvalidParameterException;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.structures.Election;
import com.electionController.structures.Voter;
import com.electionController.structures.Contestant;
import com.electionController.structures.Post;
import com.electionController.structures.Response;
import com.electionController.structures.APIParams.NewElectionQuery;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Collections;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;

public class NewElectionOprTest {
    @InjectMocks
    private NewElectionOperation newElectionOperation;

    @Mock
    private DBGetter dbGetter;

    @Mock
    private DBPutter dbPutter;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = InvalidParameterException.class)
    public void test_shouldThrowInvalidParamExceptionWhenNullQueryPassed() {
        NewElectionQuery newElectionQuery = null;
        new TestRunner()
                .setNewElectionQuery(newElectionQuery)
                .callNewElectionRequest()
                .verifyElectionAddedInDBForTimes(0);
    }

    @Test(expected = InvalidCredentialException.class)
    public void test_shouldThrowInvalidCredentialExceptionWhenInvalidVoterIdPassed() {
        NewElectionQuery newElectionQuery = NewElectionQuery.Builder()
                .withVoterId(TestConstants.INVALID_VOTER_ID)
                .withVoterPassword(TestConstants.CORRECT_PASSWORD)
                .build();
        new TestRunner()
                .setNewElectionQuery(newElectionQuery)
                .setInvalidVoterId(TestConstants.INVALID_VOTER_ID)
                .callNewElectionRequest()
                .verifyElectionAddedInDBForTimes(0);
    }

    @Test(expected = InvalidCredentialException.class)
    public void test_shouldThrowInvalidCredentialExceptionWhenIncorrectPasswordPassed() {
        NewElectionQuery newElectionQuery = NewElectionQuery.Builder()
                .withVoterId(TestConstants.VALID_VOTER_ID)
                .withVoterPassword(TestConstants.INCORRECT_VOTER_PASSWORD)
                .build();
        new TestRunner()
                .setNewElectionQuery(newElectionQuery)
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD)
                .callNewElectionRequest()
                .verifyElectionAddedInDBForTimes(0);
    }

    @Test(expected = InvalidCredentialException.class)
    public void test_shouldThrowInvalidParamExceptionWhenNonValidVoterIdEnteredInRegisteredVotersList() {
        NewElectionQuery newElectionQuery = NewElectionQuery.Builder()
                .withVoterId(TestConstants.VALID_VOTER_ID)
                .withVoterPassword(TestConstants.CORRECT_PASSWORD)
                .withRegisteredVoters(
                        Arrays.asList(TestConstants.VALID_VOTER_ID1, TestConstants.VALID_VOTER_ID2, TestConstants.INVALID_VOTER_ID))
                .build();

        new TestRunner()
                .setNewElectionQuery(newElectionQuery)
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD)
                .withDBHavingTheVoter(TestConstants.VALID_VOTER_ID1)
                .withDBHavingTheVoter(TestConstants.VALID_VOTER_ID2)
                .withDBNotHavingGivenVoterId(TestConstants.INVALID_VOTER_ID)
                .callNewElectionRequest()
                .verifyElectionAddedInDBForTimes(0);
    }

    @Test(expected = InvalidCredentialException.class)
    public void test_shouldThrowInvalidParamExceptionWhenNonValidVoterIdEnteredInRegisteredCandidatesList() {
        NewElectionQuery newElectionQuery = NewElectionQuery.Builder()
                .withVoterId(TestConstants.VALID_VOTER_ID)
                .withVoterPassword(TestConstants.CORRECT_PASSWORD)
                .withRegisteredPost(
                        Arrays.asList(
                                NewElectionQuery.Post.Builder()
                                        .withRegisteredContestants(Arrays.asList(TestConstants.VALID_VOTER_ID))
                                        .withPostDescription("Post Containing valid voters")
                                        .build(),
                                NewElectionQuery.Post.Builder()
                                        .withRegisteredContestants(Arrays.asList(TestConstants.VALID_VOTER_ID1, TestConstants.INVALID_VOTER_ID1))
                                        .withPostDescription("Post Containing invalid voters")
                                        .build()
                        )
                ).build();

        new TestRunner()
                .setNewElectionQuery(newElectionQuery)
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD)
                .withDBHavingTheVoter(TestConstants.VALID_VOTER_ID1)
                .withDBNotHavingGivenVoterId(TestConstants.INVALID_VOTER_ID1)
                .callNewElectionRequest()
                .verifyElectionAddedInDBForTimes(0);
    }

    @Test
    public void test_successfulExecution() {
        NewElectionQuery newElectionQuery = NewElectionQuery.Builder()
                .withVoterId(TestConstants.VALID_VOTER_ID)
                .withVoterPassword(TestConstants.CORRECT_PASSWORD)
                .withElectionTitle("Election Title")
                .withElectionDescription("Election Description")
                .withRegisteredVoters(
                        Arrays.asList(TestConstants.VALID_VOTER_ID1, TestConstants.VALID_VOTER_ID2, TestConstants.VALID_VOTER_ID4, TestConstants.VALID_VOTER_ID3))
                .withRegisteredPost(
                        Arrays.asList(
                                NewElectionQuery.Post.Builder()
                                        .withRegisteredContestants(Arrays.asList(TestConstants.VALID_VOTER_ID3, TestConstants.VALID_VOTER_ID3))
                                        .withPostDescription("Post 1 Description")
                                        .build(),
                                NewElectionQuery.Post.Builder()
                                        .withRegisteredContestants(Arrays.asList(TestConstants.VALID_VOTER_ID3, TestConstants.VALID_VOTER_ID4))
                                        .withPostDescription("Post 2 Description")
                                        .build()
                        )
                ).build();

        Election expectedElection = new Election();
        expectedElection.setElectionId("0");
        expectedElection.setElectionTitle("Election Title");
        expectedElection.setElectionDescription("Election Description");
        expectedElection.setAdminVoterId(TestConstants.VALID_VOTER_ID);

        expectedElection.setEligibleVoters(
                Arrays.asList(
                        getVoter(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD),
                        getVoter(TestConstants.VALID_VOTER_ID1, TestConstants.CORRECT_PASSWORD),
                        getVoter(TestConstants.VALID_VOTER_ID2, TestConstants.CORRECT_PASSWORD),
                        getVoter(TestConstants.VALID_VOTER_ID3, TestConstants.CORRECT_PASSWORD),
                        getVoter(TestConstants.VALID_VOTER_ID4, TestConstants.CORRECT_PASSWORD)
                )
        );

        expectedElection.setAvailablePost(
                Arrays.asList(
                        getPost("P0-0", 0,
                                "Post 1 Description", Arrays.asList(TestConstants.VALID_VOTER_ID3)),
                        getPost("P0-1", 1,
                                "Post 2 Description", Arrays.asList(TestConstants.VALID_VOTER_ID3, TestConstants.VALID_VOTER_ID4))
                )
        );

        Response expectedResponse = Response.Builder()
                .withResponse(expectedElection)
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .build();

        new TestRunner()
                .setNewElectionQuery(newElectionQuery)
                .setExpectedResponse(expectedResponse)
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD)
                .withDBHavingTheVoter(TestConstants.VALID_VOTER_ID1)
                .withDBHavingTheVoter(TestConstants.VALID_VOTER_ID2)
                .withDBHavingTheVoter(TestConstants.VALID_VOTER_ID3)
                .withDBHavingTheVoter(TestConstants.VALID_VOTER_ID4)
                .callNewElectionRequest()
                .verifyNewElectionReponse()
                .verifyElectionAddedInDBForTimes(1);
    }

    private Voter getVoter(String voterId, String voterPassword) {
        Voter voter = new Voter();
        voter.setVoterId(voterId);
        voter.setVoterName(voterId);
        voter.setVoterPassword("************");
        return voter;
    }

    private Post getPost(String postId, int index, String description, List<String> contestantIDs) {
        Post post = new Post();
        post.setPostId(postId);
        post.setPostIndex(index);
        post.setElectionId("0");
        post.setPostDescription(description);
        post.setWinCriteria(Post.WinCriteria.GREATEST_NUMBER_OF_VOTES);
        post.setContestants(getContestants(contestantIDs));
        return post;
    }

    private List<Contestant> getContestants(List<String> contestantIds) {
        List<Contestant> contestants = new ArrayList<>();
        for (String id : contestantIds) {
            Contestant contestant = new Contestant();
            contestant.setRank(1);
            contestant.setVotesSecured(0);
            contestant.setElectionList(null);
            contestant.setVoterId(id);
            contestant.setVoterPassword("************");
            contestant.setVoterName(id);
            contestants.add(contestant);
        }
        return contestants;
    }

    private class TestRunner {
        private NewElectionQuery newElectionQuery;
        private Response expectedResponse;
        private Response actualResponse;

        TestRunner() {
            doNothing().when(authenticationFacade).validateVoterCredentials(anyString(), anyString());
            doNothing().when(authenticationFacade).validateElectionAdmin(anyString(), anyString());
        }

        TestRunner setNewElectionQuery(NewElectionQuery newElectionQuery) {
            this.newElectionQuery = newElectionQuery;
            return this;
        }

        TestRunner setExpectedResponse(Response response) {
            this.expectedResponse = response;
            return this;
        }

        TestRunner setValidVoterCredentials(String voterId, String voterPassword) {
            Voter voter = new Voter();
            voter.setVoterId(voterId);
            voter.setVoterPassword(voterPassword);
            voter.setVoterName(voterId);
            when(dbGetter.getVoter(voterId)).thenReturn(voter);
            doThrow(new InvalidCredentialException("")).when(authenticationFacade).validateVoterCredentials(eq(voterId), anyString());
            doNothing().when(authenticationFacade).validateVoterCredentials(voterId, voterPassword);
            return this;
        }

        TestRunner setInvalidVoterId(String voterId) {
            doThrow(new InvalidCredentialException("Invalid Username/password"))
                    .when(authenticationFacade).validateVoterCredentials(eq(voterId), anyString());
            return this;
        }

        TestRunner callNewElectionRequest() {
            this.actualResponse = newElectionOperation.execute(this.newElectionQuery);
            return this;
        }

        TestRunner verifyNewElectionReponse() {
            assert actualResponse.getStatus().equals(expectedResponse.getStatus());
            assert actualResponse.getStatusCode() == expectedResponse.getStatusCode();
            assert actualResponse.getResponse().getClass().equals(expectedResponse.getResponse().getClass());
            assert actualResponse.getResponse().getClass().equals(Election.class);

            Election election = (Election)  actualResponse.getResponse();
            Election expectedElection = (Election) expectedResponse.getResponse();

            assert election.getElectionId().equals(expectedElection.getElectionId());
            assert election.getElectionTitle().equals(expectedElection.getElectionTitle());
            assert election.getElectionDescription().equals(expectedElection.getElectionDescription());
            assert election.getAdminVoterId().equals(expectedElection.getAdminVoterId());

            // registered voters checks
            assert election.getEligibleVoters().size() == expectedElection.getEligibleVoters().size();
            Collections.sort(election.getEligibleVoters(), new VoterComp());
            Collections.sort(expectedElection.getEligibleVoters(), new VoterComp());
            assert election.getEligibleVoters().equals(expectedElection.getEligibleVoters());

            // registered posts checks
            assert election.getAvailablePost().size() == expectedElection.getAvailablePost().size();
            Collections.sort(election.getAvailablePost(), new PostComp());
            Collections.sort(expectedElection.getAvailablePost(), new PostComp());
            for (int i=0 ; i<election.getAvailablePost().size() ; i++) {
                assert election.getAvailablePost().get(i).getPostId()
                        .equals(expectedElection.getAvailablePost().get(i).getPostId());
                assert election.getAvailablePost().get(i).getPostIndex()
                        == expectedElection.getAvailablePost().get(i).getPostIndex();
                assert election.getAvailablePost().get(i).getPostDescription()
                        .equals(expectedElection.getAvailablePost().get(i).getPostDescription());
                assert election.getAvailablePost().get(i).getElectionId()
                        .equals(expectedElection.getAvailablePost().get(i).getElectionId());
                Collections.sort(election.getAvailablePost().get(i).getContestants(), new ContestantComp());
                Collections.sort(expectedElection.getAvailablePost().get(i).getContestants(), new ContestantComp());
                assert election.getAvailablePost().get(i).getContestants()
                        .equals(expectedElection.getAvailablePost().get(i).getContestants());
            }
            return this;
        }

        TestRunner verifyElectionAddedInDBForTimes(int times) {
            Mockito.verify(dbPutter, times(times)).registerElection(any());
            return this;
        }

        TestRunner withDBNotHavingGivenVoterId(String voterId) {
            when((dbGetter.getVoter(voterId))).thenThrow
                    (new InvalidCredentialException("VOTER_DOES_NOT_EXISTS, VoterId: " + voterId));
            return this;
        }

        TestRunner withDBHavingTheVoter(String voterId) {
            Voter voter = new Voter();
            voter.setVoterId(voterId);
            voter.setVoterPassword(TestConstants.CORRECT_PASSWORD);
            voter.setVoterName(voterId);
            when(dbGetter.getVoter(voterId)).thenReturn(voter);
            return this;
        }
    }

    private class VoterComp implements Comparator<Voter> {
        @Override
        public int compare(Voter voter, Voter t1) {
            return voter.getVoterId().compareTo(t1.getVoterId());
        }
    }

    private class PostComp implements Comparator<Post> {
        @Override
        public int compare(Post post, Post t1) {
            return post.getPostId().compareTo(t1.getPostId());
        }
    }

    private class ContestantComp implements Comparator<Contestant> {
        @Override
        public int compare(Contestant contestant, Contestant t1) {
            return contestant.getVoterId().compareTo(t1.getVoterId());
        }
    }
}