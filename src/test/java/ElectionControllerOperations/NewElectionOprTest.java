package ElectionControllerOperations;

import com.ElectionController.Controllers.ElectionControllerEndPoints.NewElectionOperation;
import com.ElectionController.DatabaseConnector.Getter.DBGetter;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Exceptions.InvalidParameterException;
import com.ElectionController.Structures.APIParams.NewElectionQuery;
import com.ElectionController.Structures.Voter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.mockito.Mockito.when;

public class NewElectionOprTest {

    private static final String VALID_VOTER_ID = "VALID_VOTER_ID";
    private static final String VALID_VOTER_ID1 = "VALID_VOTER_ID1";
    private static final String INVALID_VOTER_ID = "INVALID_VOTER_ID";
    private static final String INVALID_VOTER_ID1 = "INVALID_VOTER_ID1";
    private static final String CORRECT_VOTER_PASSWORD = "CORRECT_PASSWORD";
    private static final String INCORRECT_VOTER_PASSWORD = "INCORRECT_VOTER_PASSWORD";

    @InjectMocks
    NewElectionOperation newElectionOperation;

    @Mock
    DBGetter dbGetter;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = InvalidCredentialException.class)
    public void test_shouldThrowExceptionWhenInvalidVoterIdPassed() {
        new TestRunner()
                .withDBNotHavingGivenVoterId(INVALID_VOTER_ID)
                .withCallingNewElectionOprWithInvalidVoterId(INVALID_VOTER_ID);
    }

    @Test(expected = InvalidParameterException.class)
    public void test_shouldThrowInvalidParamExceptionWhenNullQueryPassed() {
        new TestRunner()
                .withCallingNewElectionOprWithNullQuery();
    }

    @Test(expected = InvalidCredentialException.class)
    public void test_shouldThrowInvalidParamExceptionWhenIncorrectPasswordPassed() {
        new TestRunner()
                .withDBFetchingTheVoterWithGivenVoterCredentials(VALID_VOTER_ID, CORRECT_VOTER_PASSWORD)
                .withCallingNewElectionOprWithIncorrectPassword();
    }

    @Test(expected = InvalidCredentialException.class)
    public void test_shouldThrowInvalidParamExceptionWhenNonValidVoterIdEnteredInRegisteredVotersList() {
        new TestRunner()
                .withDBNotHavingGivenVoterId(INVALID_VOTER_ID1)
                .withDBFetchingTheVoterWithGivenVoterCredentials(VALID_VOTER_ID, CORRECT_VOTER_PASSWORD)
                .withCallingNewElectionOprWithInvalidRegisteredVoter();
    }

    public class TestRunner {
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
