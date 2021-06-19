package com.electionController.controllers.electionController;

import com.electionController.constants.ControllerOperation;
import com.electionController.constants.ResponseCodes;
import com.electionController.controllers.ActionController;
import com.electionController.structures.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingOperation extends ActionController<Object, Response> {

    private static final ControllerOperation ACTION = ControllerOperation.PING;

    @Override
    public ControllerOperation getControllerOperation() {
        return this.ACTION;
    }

    @Override
    @GetMapping("/Ping")
    public Response execute(final Object ignore) {
        return super.execute(null);
    }

    @Override
    protected Response executeAction(final Object payload) {
        return this.sendPing(null);
    }

    @Override
    public void validateActionAccess(final Object o) {
        // No permissions required
    }

    private Response sendPing(final Object payload) {
        return new Response.Builder()
                .withResponse("pong")
                .withStatusCode(ResponseCodes.SUCCESS.getResponseCode())
                .withStatus(ResponseCodes.SUCCESS.getResponse())
                .build();
    }
}
