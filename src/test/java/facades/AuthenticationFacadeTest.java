package facades;

import com.electionController.constants.TestConstants;
import com.electionController.dbConnector.Getter.DBGetter;
import com.electionController.exceptions.EntityNotFoundException;
import com.electionController.exceptions.InternalServiceException;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.structures.Voter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AuthenticationFacadeTest {

    @InjectMocks
    private AuthenticationFacade authenticationFacade;

    @Mock
    private DBGetter dbGetter;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = InvalidCredentialException.class)
    public void test_validateVoterCredentialsShouldThrowInvalidCredentialExceptionOnPassingInvalidVoterId() {
        new TestRunner()
                .setInvalidVoterId(TestConstants.INVALID_VOTER_ID)
                .callValidateVoterCredentials(TestConstants.INVALID_VOTER_ID, TestConstants.ANY_STRING);
    }

    @Test(expected = InvalidCredentialException.class)
    public void test_validateVoterCredentialsShouldThrowInvalidCredentialExceptionOnPassingIncorrectPassword() {
        new TestRunner()
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD)
                .callValidateVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.INCORRECT_VOTER_PASSWORD);
    }

    @Test(expected = InternalServiceException.class)
    public void test_validateVoterCredentialsShouldThrowInternalServiceExceptionOnDBReturningNullVoter() {
        new TestRunner()
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD)
                .mockDBGetterToReturnNullValuesForValidCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD)
                .callValidateVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD);
    }

    @Test(expected = InternalServiceException.class)
    public void test_validateVoterCredentialsShouldThrowInternalServiceExceptionOnDBOtherDBFailure() {
        new TestRunner()
                .mockDBGetterFailure()
                .callValidateVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD);
    }

    @Test
    public void test_validateVoterCredentialsShouldThrowNoExceptionOnValidVoterCredentials() {
        new TestRunner()
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD)
                .callValidateVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD);
    }

    private class TestRunner {

        TestRunner setInvalidVoterId(String voterId) {
            when(dbGetter.getVoter(voterId)).thenThrow(new EntityNotFoundException(""));
            return this;
        }

        TestRunner setValidVoterCredentials(String voterId, String password) {
            Voter voter = new Voter();
            voter.setVoterName(TestConstants.VOTER_NAME);
            voter.setVoterPassword(TestConstants.CORRECT_PASSWORD);
            voter.setVoterId(TestConstants.VALID_VOTER_ID);
            when(dbGetter.getVoter(voterId)).thenReturn(voter);
            return this;
        }

        TestRunner mockDBGetterToReturnNullValuesForValidCredentials(String voterId, String voterPassword) {
            when(dbGetter.getVoter(voterId)).thenReturn(null);
            return this;
        }

        TestRunner mockDBGetterFailure() {
            when(dbGetter.getVoter(anyString())).thenThrow(new InternalServiceException(""));
            return this;
        }

        TestRunner callValidateVoterCredentials(String voterId, String voterPassword) {
            authenticationFacade.validateVoterCredentials(voterId, voterPassword);
            return this;
        }
    }
}
