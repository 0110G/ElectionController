package ElectionControllerOperations.ChangeElectionOpr;

import com.electionController.constants.ResponseCodes;
import com.electionController.constants.TestConstants;
import com.electionController.controllers.ElectionControllerEndPoints.ChangeElectionOperations.ChangeTitleOperation;
import com.electionController.dbConnector.Updater.DBUpdater;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.exceptions.InvalidParameterException;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.structures.APIParams.ChangeElection.ChangeElectionTitleQuery;
import com.electionController.structures.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

public class ChangeElectionTitleOprTest {

    @InjectMocks
    private ChangeTitleOperation changeTitleOperation;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private DBUpdater dbUpdater;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = InvalidParameterException.class)
    public void test_shouldThrowInvalidParamExceptionWhenNullQueryPassed() {
        new TestRunner()
                .setChangeElectionTitleQuery(null)
                .callChangeElectionTitleOperation();
    }

    @Test(expected = InvalidCredentialException.class)
    public void test_shouldThrowInvalidCredentialExceptionWhenInvalidVoterIdPassed() {
        ChangeElectionTitleQuery changeElectionTitleQuery = ChangeElectionTitleQuery.Builder()
                .withElectionTitle(TestConstants.ELECTION_TITLE)
                .withElectionId(TestConstants.VALID_ELECTION_ID)
                .withVoterId(TestConstants.INVALID_VOTER_ID)
                .withVoterPassword(TestConstants.CORRECT_PASSWORD)
                .build();

        new TestRunner()
                .setChangeElectionTitleQuery(changeElectionTitleQuery)
                .setInvalidVoterId(TestConstants.INVALID_VOTER_ID)
                .callChangeElectionTitleOperation();
    }

    @Test(expected = InvalidCredentialException.class)
    public void test_shouldThrowInvalidCredentialExceptionWhenIncorrectPasswordPassed() {
        ChangeElectionTitleQuery changeElectionTitleQuery = ChangeElectionTitleQuery.Builder()
                .withElectionTitle(TestConstants.ELECTION_TITLE)
                .withElectionId(TestConstants.VALID_ELECTION_ID)
                .withVoterId(TestConstants.VALID_VOTER_ID)
                .withVoterPassword(TestConstants.INCORRECT_VOTER_PASSWORD)
                .build();

        new TestRunner()
                .setChangeElectionTitleQuery(changeElectionTitleQuery)
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD)
                .callChangeElectionTitleOperation();
    }

    @Test(expected = InvalidCredentialException.class)
    public void test_shouldThrowRestrictedActionExceptionWhenInvalidElectionIdPassed() {
        ChangeElectionTitleQuery changeElectionTitleQuery = ChangeElectionTitleQuery.Builder()
                .withElectionTitle(TestConstants.ELECTION_TITLE)
                .withElectionId(TestConstants.INVALID_ELECTION_ID)
                .withVoterId(TestConstants.VALID_VOTER_ID)
                .withVoterPassword(TestConstants.CORRECT_PASSWORD)
                .build();

        new TestRunner()
                .setChangeElectionTitleQuery(changeElectionTitleQuery)
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD)
                .setInvalidElectionId(TestConstants.INVALID_ELECTION_ID)
                .callChangeElectionTitleOperation();
    }

    @Test(expected = InvalidCredentialException.class)
    public void test_shouldThrowInvalidCredentialExceptionWhenVoterNotAdminOfGivenElection() {
        ChangeElectionTitleQuery changeElectionTitleQuery = ChangeElectionTitleQuery.Builder()
                .withElectionTitle(TestConstants.ELECTION_TITLE)
                .withElectionId(TestConstants.VALID_ELECTION_ID)
                .withVoterId(TestConstants.VALID_VOTER_ID1)
                .withVoterPassword(TestConstants.CORRECT_PASSWORD)
                .build();

        new TestRunner()
                .setChangeElectionTitleQuery(changeElectionTitleQuery)
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD)
                .setElectionAdmin(TestConstants.VALID_VOTER_ID, TestConstants.VALID_ELECTION_ID)
                .callChangeElectionTitleOperation();
    }

    @Test
    public void test_shouldNotUpdateElectionTitleAndReturnExpectedResponseOnEmptyElectionTitlePassed() {
        ChangeElectionTitleQuery changeElectionTitleQuery = ChangeElectionTitleQuery.Builder()
                .withElectionTitle(TestConstants.EMPTY_STRING)
                .withElectionId(TestConstants.VALID_ELECTION_ID)
                .withVoterId(TestConstants.VALID_VOTER_ID)
                .withVoterPassword(TestConstants.CORRECT_PASSWORD)
                .build();

        Response expectedResponse = Response.Builder()
                .withResponse(null)
                .withStatusCode(200)
                .withStatus("EMPTY_TITLE_PASSED")
                .build();

        new TestRunner()
                .setChangeElectionTitleQuery(changeElectionTitleQuery)
                .setExpectedResponse(expectedResponse)
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD)
                .setElectionAdmin(TestConstants.VALID_VOTER_ID, TestConstants.VALID_ELECTION_ID)
                .callChangeElectionTitleOperation()
                .verifyThatDBPutterCalledWithTimes(TestConstants.EMPTY_STRING, TestConstants.VALID_ELECTION_ID, 0)
                .verifyChangeElectionTitleResponse();
    }

    @Test
    public void test_shouldUpdateElectionTitleAndReturnExpectedResponseOnNonEmptyElectionTitlePassed() {
        ChangeElectionTitleQuery changeElectionTitleQuery = ChangeElectionTitleQuery.Builder()
                .withElectionTitle(TestConstants.ELECTION_TITLE)
                .withElectionId(TestConstants.VALID_ELECTION_ID)
                .withVoterId(TestConstants.VALID_VOTER_ID)
                .withVoterPassword(TestConstants.CORRECT_PASSWORD)
                .build();

        Response expectedResponse = Response.Builder()
                .withResponse(null)
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .build();

        new TestRunner()
                .setChangeElectionTitleQuery(changeElectionTitleQuery)
                .setExpectedResponse(expectedResponse)
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD)
                .setElectionAdmin(TestConstants.VALID_VOTER_ID, TestConstants.VALID_ELECTION_ID)
                .callChangeElectionTitleOperation()
                .verifyThatDBPutterCalledWithTimes(TestConstants.ELECTION_TITLE, TestConstants.VALID_ELECTION_ID, 1)
                .verifyChangeElectionTitleResponse();
    }

    private class TestRunner {
        private ChangeElectionTitleQuery changeElectionTitleQuery;
        private Response actualResponse;
        private Response expectedResponse;

        TestRunner() {
            doNothing().when(authenticationFacade).validateVoterCredentials(anyString(), anyString());
            doNothing().when(authenticationFacade).validateElectionAdmin(anyString(), anyString());
        }

        TestRunner setChangeElectionTitleQuery(ChangeElectionTitleQuery changeElectionTitleQuery) {
            this.changeElectionTitleQuery = changeElectionTitleQuery;
            return this;
        }

        TestRunner setExpectedResponse(Response response) {
            this.expectedResponse = response;
            return this;
        }

        TestRunner setInvalidVoterId(String voterId) {
            doThrow(new InvalidCredentialException("Invalid Username/password"))
                    .when(authenticationFacade).validateVoterCredentials(eq(voterId), anyString());
            return this;
        }

        TestRunner setValidVoterCredentials(String voterId, String voterPassword) {
            doThrow(new InvalidCredentialException("Invalid_id/password")).when(authenticationFacade).validateVoterCredentials(eq(voterId), anyString());
            doNothing().when(authenticationFacade).validateVoterCredentials(voterId, voterPassword);
            return this;
        }

        TestRunner setInvalidElectionId(String electionId) {
            doThrow(new InvalidCredentialException("")).when(authenticationFacade).validateElectionAdmin(anyString(), eq(electionId));
            return this;
        }

        TestRunner setElectionAdmin(String voterId, String electionId) {
            doThrow(new InvalidCredentialException("")).when(authenticationFacade).validateElectionAdmin(anyString(), eq(electionId));
            doNothing().when(authenticationFacade).validateElectionAdmin(eq(voterId), eq(electionId));
            return this;
        }

        TestRunner verifyThatDBPutterCalledWithTimes(String electionTitle, String electionId, int times) {
            Mockito.verify(dbUpdater, times(times)).updateElectionTitle(electionId, electionTitle);
            return this;
        }

        TestRunner verifyChangeElectionTitleResponse() {
            assert actualResponse.getStatus().equals(expectedResponse.getStatus());
            assert actualResponse.getStatusCode() == expectedResponse.getStatusCode();
            assert actualResponse.getResponse() == null;
            return this;
        }

        TestRunner callChangeElectionTitleOperation() {
            this.actualResponse = changeTitleOperation.ChangeElectionTitle(this.changeElectionTitleQuery);
            return this;
        }
    }
}
