package ElectionControllerOperations;

import com.electionController.constants.ResponseCodes;
import com.electionController.controllers.ElectionControllerEndPoints.GetElectionOperation;
import com.electionController.dbConnector.Getter.DBGetter;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.exceptions.InvalidParameterException;
import com.electionController.exceptions.RestrictedActionException;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.structures.Election;
import com.electionController.structures.Voter;
import com.electionController.structures.Response;
import com.electionController.structures.APIParams.GetElectionQuery;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class GetElectionOprTest {

    @InjectMocks
    private GetElectionOperation getElectionOperation;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private DBGetter dbGetter;

    private static final String VALID_VOTER_ID = "VALID_VOTER_ID";
    private static final String VALID_VOTER_ID1 = "VALID_VOTER_ID1";
    private static final String INVALID_VOTER_ID = "INVALID_VOTER_ID";
    private static final String CORRECT_PASSWORD = "CORRECT_PASSWORD";
    private static final String INCORRECT_VOTER_PASSWORD = "INCORRECT_VOTER_PASSWORD";
    private static final String VALID_ELECTION_ID = "VALID_ELECTION_ID";
    private static final String INVALID_ELECTION_ID = "INVALID_ELECTION_ID";

    @Before
    public void init() {MockitoAnnotations.initMocks(this);}

    @Test(expected = InvalidParameterException.class)
    public void test_shouldThrowInvalidParamExceptionWhenNullQueryPassed() {
        new TestRunner()
                .setGetElectionQuery(null)
                .callGetElectionOperation();
    }

    @Test(expected = InvalidCredentialException.class)
    public void test_shouldThrowInvalidCredentialExceptionWhenInvalidVoterIdPassed() {
        GetElectionQuery getElectionQuery = GetElectionQuery.Builder()
                .withVoterId(INVALID_VOTER_ID)
                .withVoterPassword(CORRECT_PASSWORD)
                .build();
        new TestRunner()
                .setGetElectionQuery(getElectionQuery)
                .setInvalidVoterId(INVALID_VOTER_ID)
                .callGetElectionOperation();
    }

    @Test(expected = InvalidCredentialException.class)
    public void test_shouldThrowInvalidCredentialExceptionWhenIncorrectPasswordPassed() {
        GetElectionQuery getElectionQuery = GetElectionQuery.Builder()
                .withVoterId(VALID_VOTER_ID)
                .withVoterPassword(INCORRECT_VOTER_PASSWORD)
                .build();
        new TestRunner()
                .setGetElectionQuery(getElectionQuery)
                .setValidVoterCredentials(VALID_VOTER_ID, CORRECT_PASSWORD)
                .callGetElectionOperation();
    }

    @Test(expected = RestrictedActionException.class)
    public void test_shouldThrowRestrictedActionExceptionWhenInvalidElectionIdPassed() {
        GetElectionQuery getElectionQuery = GetElectionQuery.Builder()
                .withVoterId(VALID_VOTER_ID)
                .withVoterPassword(CORRECT_PASSWORD)
                .withElectionId(INVALID_ELECTION_ID)
                .build();
        new TestRunner()
                .setGetElectionQuery(getElectionQuery)
                .setValidVoterCredentials(VALID_VOTER_ID, CORRECT_PASSWORD)
                .setInvalidElectionId(INVALID_ELECTION_ID)
                .callGetElectionOperation();
    }

    @Test(expected = RestrictedActionException.class)
    public void test_shouldThrowRestrictedActionExceptionWhenVoterNotEligibleToViewGivenElection() {
        GetElectionQuery getElectionQuery = GetElectionQuery.Builder()
                .withVoterId(VALID_VOTER_ID)
                .withVoterPassword(CORRECT_PASSWORD)
                .withElectionId(VALID_ELECTION_ID)
                .build();
        new TestRunner()
                .setGetElectionQuery(getElectionQuery)
                .setValidVoterCredentials(VALID_VOTER_ID, CORRECT_PASSWORD)
                .setVoterIneligibleToViewElection(VALID_VOTER_ID, VALID_ELECTION_ID)
                .callGetElectionOperation();
    }

    @Test
    public void test_successfulExecution() {
        Election expectedElection = new Election();
        expectedElection.setElectionId("0");
        expectedElection.setElectionTitle("Election Title");
        expectedElection.setElectionDescription("Election Description");
        expectedElection.setAdminVoterId(VALID_VOTER_ID);

        Response expectedResponse = Response.Builder()
                .withResponse(expectedElection)
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .build();

        GetElectionQuery getElectionQuery = GetElectionQuery.Builder()
                .withVoterId(VALID_VOTER_ID)
                .withElectionId(VALID_ELECTION_ID)
                .withVoterPassword(CORRECT_PASSWORD)
                .build();

        new TestRunner()
                .setGetElectionQuery(getElectionQuery)
                .setValidVoterCredentials(VALID_VOTER_ID,  CORRECT_PASSWORD)
                .setDbFetchedElection(expectedElection)
                .mockDbToFetchElection(VALID_ELECTION_ID)
                .callGetElectionOperation()
                .setExpectedResponse(expectedResponse)
                .verifyGetElectionOperationResponse();

    }

    private class TestRunner {
        private GetElectionQuery getElectionQuery;
        private Election dbFetched;
        private Response expectedResponse;
        private Response actualResponse;

        TestRunner() {
            doNothing().when(authenticationFacade).validateVoterCredentials(anyString(), anyString());
            doNothing().when(authenticationFacade).validateElectionAdmin(anyString(), anyString());
        }

        TestRunner setGetElectionQuery(GetElectionQuery getElectionQuery) {
            this.getElectionQuery = getElectionQuery;
            return this;
        }

        TestRunner setExpectedResponse(Response response) {
            this.expectedResponse = response;
            return this;
        }

        TestRunner callGetElectionOperation() {
            this.actualResponse = getElectionOperation.GetElection(this.getElectionQuery);
            return this;
        }

        TestRunner setInvalidVoterId(String voterId) {
            doThrow(new InvalidCredentialException("Invalid Username/password"))
                    .when(authenticationFacade).validateVoterCredentials(eq(voterId), anyString());
            return this;
        }

        TestRunner setValidVoterCredentials(String voterId, String voterPassword) {
            Voter voter = new Voter();
            voter.setVoterId(voterId);
            voter.setVoterPassword(voterPassword);
            voter.setVoterName(voterId);
            when(dbGetter.getVoter(voterId)).thenReturn(voter);
            doThrow(new InvalidCredentialException("Invalid_id/password")).when(authenticationFacade).validateVoterCredentials(eq(voterId), anyString());
            doNothing().when(authenticationFacade).validateVoterCredentials(voterId, voterPassword);
            return this;
        }

        TestRunner setInvalidElectionId(String electionId) {
            doThrow(new InvalidCredentialException("")).when(authenticationFacade).validateElectionViewer(anyString(), eq(electionId));
            return this;
        }

        TestRunner setVoterIneligibleToViewElection(String voterId, String electionId) {
            doThrow(new InvalidCredentialException("")).when(authenticationFacade).validateElectionViewer(voterId, electionId);
            return this;
        }

        TestRunner setDbFetchedElection(Election election) {
            this.dbFetched = election;
            return this;
        }

        TestRunner mockDbToFetchElection(String electionId) {
            when(dbGetter.getElection(electionId)).thenReturn(this.dbFetched);
            return this;
        }

        TestRunner verifyGetElectionOperationResponse() {
            System.out.println(actualResponse.getStatus());
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
            return this;
        }

    }

}
