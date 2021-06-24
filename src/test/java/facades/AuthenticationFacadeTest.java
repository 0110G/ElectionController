package facades;

import com.electionController.constants.TestConstants;
import com.electionController.dbConnector.Getter.DBGetter;
import com.electionController.exceptions.*;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.structures.Voter;
import com.electionController.structures.VoterMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

    @Test(expected = InvalidParameterException.class)
    public void test_validateVoterIdsShouldThrowInvalidParameterExceptionWhenOneOfTheVoterIdIsInvalid() {
        new TestRunner()
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD)
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID1, TestConstants.CORRECT_PASSWORD)
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID2, TestConstants.CORRECT_PASSWORD)
                .setInvalidVoterId(TestConstants.INVALID_VOTER_ID)
                .callValidateVoterIds(Arrays.asList(
                        TestConstants.VALID_VOTER_ID,
                        TestConstants.INVALID_VOTER_ID,
                        TestConstants.VALID_VOTER_ID1,
                        TestConstants.VALID_VOTER_ID2));
    }

    @Test
    public void test_validateVoterIdsSuccessfulExecution() {
        new TestRunner()
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD)
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID1, TestConstants.CORRECT_PASSWORD)
                .setValidVoterCredentials(TestConstants.VALID_VOTER_ID2, TestConstants.CORRECT_PASSWORD)
                .setInvalidVoterId(TestConstants.INVALID_VOTER_ID)
                .callValidateVoterIds(Arrays.asList(
                        TestConstants.VALID_VOTER_ID,
                        TestConstants.VALID_VOTER_ID1,
                        TestConstants.VALID_VOTER_ID2));
    }

    @Test(expected = RestrictedActionException.class)
    public void test_validateElectionAdminShouldThrowRestrictedAccessExceptionOnPassingInvalidVoterId() {
        new TestRunner()
                .setInvalidVoterId(TestConstants.INVALID_VOTER_ID)
                .callValidateElectionAdmin(TestConstants.INVALID_VOTER_ID, TestConstants.VALID_ELECTION_ID);
    }

    @Test(expected = RestrictedActionException.class)
    public void test_validateElectionAdminShouldThrowRestrictedAccessExceptionOnVoterNotBeingElectionAdmin() {
        new TestRunner()
                .setElectionNonAdminVoter(TestConstants.VALID_VOTER_ID1, TestConstants.VALID_ELECTION_ID)
                .setElectionAdmin(TestConstants.VALID_VOTER_ID, TestConstants.VALID_ELECTION_ID, true)
                .callValidateElectionAdmin(TestConstants.VALID_VOTER_ID1, TestConstants.VALID_ELECTION_ID);
    }

    @Test(expected = RestrictedActionException.class)
    public void test_validateElectionAdminShouldThrowRestrictedAccessExceptionOnInvalidElection() {
        new TestRunner()
                .setInvalidElectionId(TestConstants.INVALID_ELECTION_ID)
                .callValidateElectionAdmin(TestConstants.VALID_VOTER_ID, TestConstants.INVALID_ELECTION_ID);
    }

    @Test
    public void test_validateElectionAdminSuccessfulExecution() {
        new TestRunner()
                .setElectionAdmin(TestConstants.VALID_VOTER_ID, TestConstants.VALID_ELECTION_ID, true)
                .callValidateElectionAdmin(TestConstants.VALID_VOTER_ID, TestConstants.VALID_ELECTION_ID);
    }

    private class TestRunner {

        TestRunner setInvalidVoterId(String voterId) {
            when(dbGetter.getVoter(voterId)).thenThrow(new EntityNotFoundException(""));
            when(dbGetter.getVoterMap(eq(voterId), anyString())).thenThrow(new EntityNotFoundException(""));
            return this;
        }

        TestRunner setValidVoterCredentials(String voterId, String password) {
            Voter voter = new Voter();
            voter.setVoterName(TestConstants.VOTER_NAME);
            voter.setVoterPassword(password);
            voter.setVoterId(voterId);
            when(dbGetter.getVoter(voterId)).thenReturn(voter);
            return this;
        }

        TestRunner setElectionAdmin(String voterId, String electionId, boolean voterEligibility) {
            VoterMap voterMap = new VoterMap();
            voterMap.setVoterId(voterId);
            voterMap.setElectionId(electionId);
            voterMap.setVoterAdmin(true);
            voterMap.setVoterEligible(voterEligibility);
            when(dbGetter.getVoterMap(anyString(), eq(electionId))).thenThrow(new EntityNotFoundException(""));
            doReturn(voterMap).when(dbGetter).getVoterMap(voterId, electionId);
            return this;
        }

        TestRunner setElectionNonAdminVoter(String voterId, String electionId) {
            VoterMap voterMap = new VoterMap();
            voterMap.setVoterId(voterId);
            voterMap.setElectionId(electionId);
            voterMap.setVoterAdmin(false);
            voterMap.setVoterEligible(true);
            doReturn(voterMap).when(dbGetter).getVoterMap(voterId, electionId);
            return this;
        }

        TestRunner setInvalidElectionId(String electionId) {
            when(dbGetter.getVoterMap(anyString(), eq(electionId))).thenThrow(new EntityNotFoundException(""));
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

        TestRunner callValidateVoterIds(List<String> voterIds) {
            authenticationFacade.validateVoterIds(voterIds);
            return this;
        }

        TestRunner callValidateElectionAdmin(String voterId, String electionId) {
            authenticationFacade.validateElectionAdmin(voterId, electionId);
            return this;
        }
    }
}
