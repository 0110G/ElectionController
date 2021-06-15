package ElectionControllerOperations.ChangeElectionOpr;

import com.electionController.constants.ResponseCodes;
import com.electionController.constants.TestConstants;
import com.electionController.controllers.electionController.changeElectionController.ChangeDescriptionOperation;
import com.electionController.dbConnector.Updater.DBUpdater;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.exceptions.InvalidParameterException;
import com.electionController.exceptions.RestrictedActionException;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.structures.APIParams.ChangeElection.ChangeElectionDescriptionQuery;
import com.electionController.structures.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

public class ChangeElectionDescOprTest {

    @InjectMocks
    private ChangeDescriptionOperation changeDescriptionOperation;

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
                .setElectionDescQuery(null)
                .callChangeElectionDescriptionOperation();
    }

    @Test(expected = InvalidCredentialException.class)
    public void test_shouldThrowInvalidCredentialExceptionWhenInvalidVoterIdPassed() {
        ChangeElectionDescriptionQuery changeElectionDescriptionQuery = ChangeElectionDescriptionQuery.Builder()
                .withElectionDescription(TestConstants.ELECTION_DESCRIPTION)
                .withElectionId(TestConstants.VALID_ELECTION_ID)
                .withVoterId(TestConstants.INVALID_VOTER_ID)
                .withVoterPassword(TestConstants.CORRECT_PASSWORD)
                .build();

        new TestRunner()
                .setElectionDescQuery(changeElectionDescriptionQuery)
                .setInvalidVoterId(TestConstants.INVALID_VOTER_ID)
                .callChangeElectionDescriptionOperation();
    }

    @Test(expected = InvalidCredentialException.class)
    public void test_shouldThrowInvalidCredentialExceptionWhenIncorrectPasswordPassed() {
        ChangeElectionDescriptionQuery changeElectionDescriptionQuery = ChangeElectionDescriptionQuery.Builder()
                .withElectionDescription(TestConstants.ELECTION_DESCRIPTION)
                .withElectionId(TestConstants.VALID_ELECTION_ID)
                .withVoterId(TestConstants.VALID_VOTER_ID)
                .withVoterPassword(TestConstants.INCORRECT_VOTER_PASSWORD)
                .build();

        new TestRunner()
                .setElectionDescQuery(changeElectionDescriptionQuery)
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD)
                .callChangeElectionDescriptionOperation();
    }

    @Test(expected = RestrictedActionException.class)
    public void test_shouldThrowRestrictedActionExceptionWhenInvalidElectionIdPassed() {
        ChangeElectionDescriptionQuery changeElectionDescriptionQuery = ChangeElectionDescriptionQuery.Builder()
                .withElectionDescription(TestConstants.ELECTION_DESCRIPTION)
                .withElectionId(TestConstants.INVALID_ELECTION_ID)
                .withVoterId(TestConstants.VALID_VOTER_ID)
                .withVoterPassword(TestConstants.CORRECT_PASSWORD)
                .build();

        new TestRunner()
                .setElectionDescQuery(changeElectionDescriptionQuery)
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD)
                .setInvalidElectionId(TestConstants.INVALID_ELECTION_ID)
                .callChangeElectionDescriptionOperation();
    }

    @Test(expected = RestrictedActionException.class)
    public void test_shouldThrowRestrictedAccessExceptionWhenVoterNotAdminOfGivenElection() {
        ChangeElectionDescriptionQuery changeElectionDescriptionQuery = ChangeElectionDescriptionQuery.Builder()
                .withElectionDescription(TestConstants.ELECTION_DESCRIPTION)
                .withElectionId(TestConstants.VALID_ELECTION_ID)
                .withVoterId(TestConstants.VALID_VOTER_ID1)
                .withVoterPassword(TestConstants.CORRECT_PASSWORD)
                .build();

        new TestRunner()
                .setElectionDescQuery(changeElectionDescriptionQuery)
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD)
                .setElectionAdmin(TestConstants.VALID_VOTER_ID, TestConstants.VALID_ELECTION_ID)
                .callChangeElectionDescriptionOperation();
    }

    @Test
    public void test_shouldNotUpdateElectionTitleAndReturnExpectedResponseOnEmptyElectionTitlePassed() {
        ChangeElectionDescriptionQuery changeElectionDescriptionQuery = ChangeElectionDescriptionQuery.Builder()
                .withElectionDescription(TestConstants.EMPTY_STRING)
                .withElectionId(TestConstants.VALID_ELECTION_ID)
                .withVoterId(TestConstants.VALID_VOTER_ID)
                .withVoterPassword(TestConstants.CORRECT_PASSWORD)
                .build();

        Response expectedResponse = Response.Builder()
                .withResponse(null)
                .withStatusCode(200)
                .withStatus("EMPTY_DESCRIPTION_PASSED")
                .build();

        new TestRunner()
                .setElectionDescQuery(changeElectionDescriptionQuery)
                .setExpectedResponse(expectedResponse)
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD)
                .setElectionAdmin(TestConstants.VALID_VOTER_ID, TestConstants.VALID_ELECTION_ID)
                .callChangeElectionDescriptionOperation()
                .verifyThatDBPutterCalledWithTimes(TestConstants.EMPTY_STRING, TestConstants.VALID_ELECTION_ID, 0)
                .verifyChangeElectionDescriptionResponse();
    }

    @Test
    public void test_shouldUpdateElectionTitleAndReturnExpectedResponseOnNonEmptyElectionTitlePassed() {
        ChangeElectionDescriptionQuery changeElectionDescriptionQuery = ChangeElectionDescriptionQuery.Builder()
                .withElectionDescription(TestConstants.ELECTION_DESCRIPTION)
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
                .setElectionDescQuery(changeElectionDescriptionQuery)
                .setExpectedResponse(expectedResponse)
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD)
                .setElectionAdmin(TestConstants.VALID_VOTER_ID, TestConstants.VALID_ELECTION_ID)
                .callChangeElectionDescriptionOperation()
                .verifyThatDBPutterCalledWithTimes(TestConstants.ELECTION_DESCRIPTION, TestConstants.VALID_ELECTION_ID, 1)
                .verifyChangeElectionDescriptionResponse();
    }

    private class TestRunner {
        private ChangeElectionDescriptionQuery changeElectionDescriptionQuery;
        private Response actualResponse;
        private Response expectedResponse;

        TestRunner() {
            doNothing().when(authenticationFacade).validateVoterCredentials(anyString(), anyString());
            doNothing().when(authenticationFacade).validateElectionAdmin(anyString(), anyString());
        }

        TestRunner setElectionDescQuery(ChangeElectionDescriptionQuery changeElectionDescriptionQuery) {
            this.changeElectionDescriptionQuery = changeElectionDescriptionQuery;
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
            doThrow(new RestrictedActionException("")).when(authenticationFacade).validateElectionAdmin(anyString(), eq(electionId));
            return this;
        }

        TestRunner setElectionAdmin(String voterId, String electionId) {
            doThrow(new RestrictedActionException("")).when(authenticationFacade).validateElectionAdmin(anyString(), eq(electionId));
            doNothing().when(authenticationFacade).validateElectionAdmin(eq(voterId), eq(electionId));
            return this;
        }

        TestRunner verifyThatDBPutterCalledWithTimes(String electionDecription, String electionId, int times) {
            Mockito.verify(dbUpdater, times(times)).updateElectionDescription(electionId, electionDecription);
            return this;
        }

        TestRunner verifyChangeElectionDescriptionResponse() {
            assert actualResponse.getStatus().equals(expectedResponse.getStatus());
            assert actualResponse.getStatusCode() == expectedResponse.getStatusCode();
            assert actualResponse.getResponse() == null;
            return this;
        }

        TestRunner callChangeElectionDescriptionOperation() {
            this.actualResponse = changeDescriptionOperation.execute(this.changeElectionDescriptionQuery);
            return this;
        }
    }
}