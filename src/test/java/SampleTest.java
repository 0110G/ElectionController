import com.ElectionController.Constants.ResponseCodes;
import com.ElectionController.Controllers.ElectionControllerEndPoints.NewElectionOperation;
import com.ElectionController.DatabaseConnector.Getter.DBGetter;
import com.ElectionController.DatabaseConnector.Putter.DBPutter;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Exceptions.InvalidParameterException;
import com.ElectionController.Structures.APIParams.NewElectionQuery;
import com.ElectionController.Structures.Election;
import com.ElectionController.Structures.Voter;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SampleTest {



    private static DBGetter dbGetter;
    private static DBPutter dbPutter;

    @BeforeClass
    public static void init() {
        dbGetter = mock(DBGetter.class);
        dbPutter = mock(DBPutter.class);
    }

    @Test(expected = InvalidCredentialException.class)
    public void test_shouldThrowExceptionWhenInvalidVoterIdPassed() {
        new TestRunner()
                .withDBNotHavingGivenVoterId("v-01");

    }

    @Test(expected = InvalidParameterException.class)
    public void test_shouldThrowInvalidParamExceptionWhenNullQueryPassed() {
        new TestRunner()
                .withCallingNewElectionQueryWithNullQuery();
    }

    public static class TestRunner {

        TestRunner withDBNotHavingGivenVoterId(String voterId) {
            when(dbGetter.getVoter(voterId)).
                    thenThrow(new InvalidCredentialException("VOTER_DOES_NOT_EXISTS, VoterId:" + voterId));
            return this;
        }

        TestRunner withCallingNewElectionQueryWithNullQuery() {
            new NewElectionOperation().CreateElection(null);
            return this;
        }

        TestRunner withCallingNewElectionQueryWithInvalidVoterId(String voterId) {
            new NewElectionOperation().CreateElection(null);
            return this;
        }


        TestRunner withDBHavingTheVoter(String voterId) {
            when(dbGetter.getVoter(voterId)).thenReturn(new Voter());
            return this;
        }

    }

    private NewElectionQuery buildNewElectionQuery() {
        return NewElectionQuery.Builder()
                .withRegisteredPost(null)
                .build();
    }

}
