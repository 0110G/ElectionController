package ElectionControllerOperations;

import com.electionController.constants.ResponseCodes;
import com.electionController.controllers.electionController.PingOperation;
import com.electionController.structures.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class PingOprTest {
    @InjectMocks
    private PingOperation pingOperation;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_pingMustReturnAppropriatePongForAnyCalls() {
        Response expectedResponse = Response.Builder()
                .withResponse("pong")
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .build();

        new TestRunner()
                .setExpectedResponse(expectedResponse)
                .callPingOperation()
                .verifyPingResponse();
    }

    private class TestRunner {
        private Response actualResponse;
        private Response expectedResponse;

        TestRunner callPingOperation() {
            this.actualResponse = pingOperation.execute(null);
            return this;
        }

        TestRunner setExpectedResponse(Response expectedResponse) {
            this.expectedResponse = expectedResponse;
            return this;
        }

        TestRunner verifyPingResponse() {
            assert actualResponse != null;
            assert actualResponse.getStatusCode() == expectedResponse.getStatusCode();
            assert actualResponse.getStatus() != null;
            assert actualResponse.getStatus().equals(expectedResponse.getStatus());
            assert ((String)actualResponse.getResponse()).equals((String)expectedResponse.getResponse());
            return this;
        }
    }
}
