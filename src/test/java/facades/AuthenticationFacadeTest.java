package facades;

import com.electionController.constants.TestConstants;
import com.electionController.dbConnector.Getter.DBGetter;
import com.electionController.exceptions.InvalidParameterException;
import com.electionController.exceptions.RestrictedActionException;
import com.electionController.exceptions.InternalServiceException;
import com.electionController.exceptions.EntityNotFoundException;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.facades.AuthenticationFacade;
import com.electionController.structures.Contestant;
import com.electionController.structures.Post;
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
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doReturn;

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
                .mockDBGetVoterToReturnNullValuesForValidCredentials(TestConstants.VALID_VOTER_ID, TestConstants.CORRECT_PASSWORD)
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

    @Test(expected = RestrictedActionException.class)
    public void test_validateElectionViewerShouldThrowRestrictedAccessExceptionOnInvalidVoterId() {
        new TestRunner()
                .setInvalidVoterId(TestConstants.INVALID_VOTER_ID)
                .callValidateElectionViewer(TestConstants.INVALID_VOTER_ID, TestConstants.VALID_ELECTION_ID);

    }

    @Test(expected = RestrictedActionException.class)
    public void test_validateElectionViewerShouldThrowRestrictedAccessExceptionOnInvalidElectionId() {
        new TestRunner()
                .setInvalidElectionId(TestConstants.INVALID_ELECTION_ID)
                .callValidateElectionViewer(TestConstants.VALID_VOTER_ID, TestConstants.INVALID_ELECTION_ID);
    }

    @Test(expected = RestrictedActionException.class)
    public void test_validateElectionViewerShouldThrowRestrictedAccessExceptionOnVoterNotRegisteredForElection() {
        new TestRunner()
                .setUnregisteredVoter(TestConstants.VALID_VOTER_ID, TestConstants.VALID_ELECTION_ID)
                .callValidateElectionViewer(TestConstants.VALID_VOTER_ID, TestConstants.VALID_ELECTION_ID);
    }

    @Test
    public void test_validateElectionViewerSuccessfulExecution() {
        new TestRunner()
                .setElectionNonAdminVoter(TestConstants.VALID_VOTER_ID, TestConstants.VALID_ELECTION_ID)
                .callValidateElectionViewer(TestConstants.VALID_VOTER_ID, TestConstants.VALID_ELECTION_ID);
    }

    @Test(expected = InvalidParameterException.class)
    public void test_validateElectionPostShouldThrowInvalidParameterExceptionOnInvalidElectionId() {
        new TestRunner()
                .setInvalidElectionId(TestConstants.INVALID_ELECTION_ID)
                .callValidateElectionPost(TestConstants.INVALID_ELECTION_ID, TestConstants.VALID_POST_ID);
    }

    @Test(expected = InvalidParameterException.class)
    public void test_validateElectionPostShouldThrowInvalidParameterExceptionOnPostNotBelongingToElection() {
        new TestRunner()
                .setPostNotBelongingToElection(TestConstants.VALID_ELECTION_ID, TestConstants.VALID_POST_ID)
                .setPostBelongingToElection(TestConstants.VALID_ELECTION_ID, TestConstants.VALID_POST_ID1)
                .callValidateElectionPost(TestConstants.VALID_ELECTION_ID, TestConstants.VALID_POST_ID);
    }

    @Test
    public void test_validateElectionPostSuccessfulExecution() {
        new TestRunner()
                .setPostBelongingToElection(TestConstants.VALID_ELECTION_ID, TestConstants.VALID_POST_ID)
                .callValidateElectionPost(TestConstants.VALID_ELECTION_ID, TestConstants.VALID_POST_ID);
    }

    @Test(expected = InvalidParameterException.class)
    public void test_validateCandidatePostShouldThrowInvalidParameterExceptionForInvalidVoterId() {
        new TestRunner()
                .setInvalidVoterId(TestConstants.INVALID_VOTER_ID)
                .callvalidateCandidatePost(TestConstants.VALID_POST_ID, TestConstants.INVALID_VOTER_ID);
    }

    @Test(expected = InvalidParameterException.class)
    public void test_validateCandidatePostShouldThrowInvalidParameterExceptionForInvalidPostId() {
        new TestRunner()
                .setInvalidPostId(TestConstants.INVALID_POST_ID)
                .callvalidateCandidatePost(TestConstants.INVALID_POST_ID, TestConstants.VALID_VOTER_ID);
    }

    @Test(expected = InvalidParameterException.class)
    public void test_validateCandidatePostShouldThrowInvalidParameterExceptionForCanidateNotBelongingToPost() {
        new TestRunner()
                .setCandidateNotBelongingToPost(TestConstants.VALID_POST_ID, TestConstants.VALID_VOTER_ID)
                .callvalidateCandidatePost(TestConstants.VALID_POST_ID, TestConstants.VALID_VOTER_ID);
    }

    @Test
    public void test_validateCandidatePostSuccessfulExecution() {
        new TestRunner()
                .setCandidateBelongingToPost(TestConstants.VALID_POST_ID, TestConstants.VALID_VOTER_ID)
                .callvalidateCandidatePost(TestConstants.VALID_POST_ID, TestConstants.VALID_VOTER_ID);

    }

    private class TestRunner {

        TestRunner setInvalidVoterId(String voterId) {
            when(dbGetter.getVoter(voterId)).thenThrow(new EntityNotFoundException(""));
            when(dbGetter.getVoterMap(eq(voterId), anyString())).thenThrow(new EntityNotFoundException(""));
            when(dbGetter.getPostContestant(anyString(), eq(voterId))).thenThrow(new EntityNotFoundException(""));
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
            doThrow(new EntityNotFoundException("")).when(dbGetter).getVoterMap(anyString(), eq(electionId));
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
            when(dbGetter.getElectionPost(eq(electionId), anyString())).thenThrow(new EntityNotFoundException(""));
            return this;
        }

        TestRunner setInvalidPostId(String postId) {
            doThrow(new EntityNotFoundException("")).when(dbGetter).getPostContestant(eq(postId), anyString());
            return this;
        }

        TestRunner setCandidateNotBelongingToPost(String postId, String candidateVoterId) {
            doThrow(new EntityNotFoundException("")).when(dbGetter).getPostContestant(postId, candidateVoterId);
            return this;
        }

        TestRunner setPostNotBelongingToElection(String electionId, String postId) {
            doThrow(new EntityNotFoundException("")).when(dbGetter).getElectionPost(electionId, postId);
            return this;
        }

        TestRunner setPostBelongingToElection(String electionId, String postId) {
            doReturn(new Post()).when(dbGetter).getElectionPost(electionId, postId);
            return this;
        }

        TestRunner setCandidateBelongingToPost(String postId, String candidateVoterId) {
            doReturn(new Contestant()).when(dbGetter).getPostContestant(postId, candidateVoterId);
            return this;
        }

        TestRunner setUnregisteredVoter(String voterId, String electionId) {
            when(dbGetter.getVoterMap(voterId, electionId)).thenThrow(new EntityNotFoundException(""));
            return this;
        }

        TestRunner mockDBGetVoterToReturnNullValuesForValidCredentials(String voterId, String voterPassword) {
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

        TestRunner callValidateElectionViewer(String voterId, String electionId) {
            authenticationFacade.validateElectionViewer(voterId, electionId);
            return this;
        }

        TestRunner callValidateElectionPost(String electionId, String postId) {
            authenticationFacade.validateElectionPost(electionId, postId);
            return this;
        }

        TestRunner callvalidateCandidatePost(String postId, String candidateVoterId) {
            authenticationFacade.validateCandidatePost(postId, candidateVoterId);
            return this;
        }
    }
}
