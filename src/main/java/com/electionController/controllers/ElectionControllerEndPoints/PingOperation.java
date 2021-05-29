package com.electionController.controllers.ElectionControllerEndPoints;

import com.electionController.structures.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingOperation {
    @GetMapping("/Ping")
    public Response SendPing() {
        return new Response.Builder()
                .withResponse(null)
                .withStatusCode(5505)
                .withStatus("Ping Response")
                .build();
    }
}
