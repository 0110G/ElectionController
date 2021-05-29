package ElectionControllerOperations;

import com.electionController.controllers.ElectionControllerEndPoints.NewElectionOperation;
import com.electionController.dbConnector.Getter.DBGetter;
import com.electionController.dbConnector.Putter.DBPutter;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.exceptions.InvalidParameterException;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.structures.APIParams.NewElectionQuery;
import com.electionController.structures.Response;
import com.electionController.structures.Voter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.annotation.Resource;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class NewElectionOprTest {

    private static final String VALID_VOTER_ID = "VALID_VOTER_ID";
    private static final String VALID_VOTER_ID1 = "VALID_VOTER_ID1";
    private static final String INVALID_VOTER_ID = "INVALID_VOTER_ID";
    private static final String INVALID_VOTER_ID1 = "INVALID_VOTER_ID1";
    private static final String CORRECT_VOTER_PASSWORD = "CORRECT_PASSWORD";
    private static final String INCORRECT_VOTER_PASSWORD = "INCORRECT_VOTER_PASSWORD";

    @InjectMocks
    private NewElectionOperation newElectionOperation;

    @Mock
    private DBGetter dbGetter;

    @Mock
    AuthenticationFacade authenticationFacade;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = InvalidParameterException.class)
    public void test_shouldThrowInvalidParamExceptionWhenNullQueryPassed() {
        NewElectionQuery newElectionQuery = null;
        new TestRunner()
                .setNewElectionQuery(newElectionQuery)
                .callNewElectionRequest();
    }

    @Test(expected = InvalidCredentialException.class)
    public void test_shouldThrowExceptionWhenInvalidVoterIdPassed() {
        NewElectionQuery newElectionQuery = NewElectionQuery.Builder()
                .withVoterId(INVALID_VOTER_ID)
                .withVoterPassword(CORRECT_VOTER_PASSWORD)
                .build();
        new TestRunner()
                .setNewElectionQuery(newElectionQuery)
                .setInvalidVoterId(INVALID_VOTER_ID)
                .callNewElectionRequest();
    }


    @Test(expected = InvalidCredentialException.class)
    public void test_shouldThrowInvalidParamExceptionWhenIncorrectPasswordPassed() {
        NewElectionQuery newElectionQuery = NewElectionQuery.Builder()
                .withVoterId(VALID_VOTER_ID)
                .withVoterPassword(INCORRECT_VOTER_PASSWORD)
                .build();
        new TestRunner()
                .setNewElectionQuery(newElectionQuery)
                .setValidVoterCredentials(VALID_VOTER_ID, CORRECT_VOTER_PASSWORD)
                .callNewElectionRequest();
    }

    @Test(expected = InvalidCredentialException.class)
    public void test_shouldThrowInvalidParamExceptionWhenNonValidVoterIdEnteredInRegisteredVotersList() {
        new TestRunner()
                .withDBNotHavingGivenVoterId(INVALID_VOTER_ID1)
                .withDBFetchingTheVoterWithGivenVoterCredentials(VALID_VOTER_ID, CORRECT_VOTER_PASSWORD)
                .withCallingNewElectionOprWithInvalidRegisteredVoter();
    }

    public void test_shouldThrowInvalidParamExceptionWhenNonValidVoterIdEnteredInRegisteredCandidatesList() {
        new TestRunner()
                .withDBNotHavingGivenVoterId(INVALID_VOTER_ID)
                .withDBFetchingTheVoterWithGivenVoterCredentials(VALID_VOTER_ID, CORRECT_VOTER_PASSWORD)
                .withDBFetchingTheVoterWithGivenVoterCredentials(VALID_VOTER_ID1, CORRECT_VOTER_PASSWORD);
    }

    public class TestRunner {
        // -->
        private NewElectionQuery newElectionQuery;
        private Response expectedResponse;
        private Response actualResponse;

        TestRunner() {
            doThrow(new InvalidCredentialException("")).when(authenticationFacade).validateVoterCredentials(anyString(), anyString());
            doThrow(new InvalidCredentialException("")).when(authenticationFacade).validateElectionAdmin(anyString(), anyString());
        }

        //
        TestRunner setNewElectionQuery(NewElectionQuery newElectionQuery) {
            this.newElectionQuery = newElectionQuery;
            return this;
        }

        TestRunner setExpectedResponse(Response response) {
            this.expectedResponse = response;
            return this;
        }

        TestRunner setValidVoterCredentials(String voterId, String voterPassword) {
            doNothing().when(authenticationFacade).validateVoterCredentials(voterId, voterPassword);
            return this;
        }

        TestRunner setInvalidVoterId(String voterId) {
            doThrow(new InvalidCredentialException("Invalid Username/password"))
                    .when(authenticationFacade).validateVoterCredentials(eq(voterId), anyString());
            return this;
        }

        TestRunner callNewElectionRequest() {
            this.actualResponse = newElectionOperation.CreateElection(this.newElectionQuery);
            return this;
        }

        //

        TestRunner withDBNotHavingGivenVoterId(String voterId) {
            when((dbGetter.getVoter(voterId))).thenThrow
                    (new InvalidCredentialException("VOTER_DOES_NOT_EXISTS, VoterId: " + voterId));
            return this;
        }

        TestRunner withCallingNewElectionOprWithNullQuery() {
            newElectionOperation.CreateElection(null);
            return this;
        }

        TestRunner withCallingNewElectionOprWithInvalidVoterId(String voterId) {
            NewElectionQuery newElectionQuery = NewElectionQuery.Builder()
                    .withVoterId(voterId)
                    .withVoterPassword("adw")
                    .build();
            newElectionOperation.CreateElection(newElectionQuery);
            return this;
        }

        TestRunner withCallingNewElectionOprWithInvalidRegisteredVoter() {
            NewElectionQuery newElectionQuery = NewElectionQuery.Builder()
                    .withVoterId(VALID_VOTER_ID)
                    .withVoterPassword(CORRECT_VOTER_PASSWORD)
                    .withRegisteredVoters(Arrays.asList(INVALID_VOTER_ID1))
                    .build();
            newElectionOperation.CreateElection(newElectionQuery);
            return this;
        }

        TestRunner withCallingNewElectionOprWithIncorrectPassword() {
            NewElectionQuery newElectionQuery = NewElectionQuery.Builder()
                    .withVoterId(VALID_VOTER_ID)
                    .withVoterPassword(INCORRECT_VOTER_PASSWORD)
                    .build();
            newElectionOperation.CreateElection(newElectionQuery);
            return this;
        }

        TestRunner withCallingNewElectionOprWithInvalidRegisteredCandidate() {
            NewElectionQuery newElectionQuery = NewElectionQuery.Builder()
                    .withVoterId(VALID_VOTER_ID)
                    .withVoterPassword(CORRECT_VOTER_PASSWORD)
                    .withRegisteredPost(
                            Arrays.asList(
                                    NewElectionQuery.Post.Builder()
                                            .withRegisteredContestants(Arrays.asList(INVALID_VOTER_ID, INVALID_VOTER_ID1))
                                            .withPostDescription("Post Containing invalid voters")
                                            .build(),
                                    NewElectionQuery.Post.Builder()
                                            .withRegisteredContestants(Arrays.asList(VALID_VOTER_ID))
                                            .withPostDescription("Post Containing valid voters")
                                            .build()
                            )
                    ).build();
            newElectionOperation.CreateElection(newElectionQuery);
            return this;
        }

        TestRunner withDBFetchingTheVoterWithGivenVoterCredentials(String voterId, String password) {
            Voter voter = new Voter();
            voter.setVoterId(voterId);
            voter.setVoterPassword(password);
            when(dbGetter.getVoter(voterId)).thenReturn(voter);
            return this;
        }

        TestRunner withDBHavingTheVoter(String voterId) {
            when(dbGetter.getVoter(voterId)).thenReturn(new Voter());
            return this;
        }

    }
}
