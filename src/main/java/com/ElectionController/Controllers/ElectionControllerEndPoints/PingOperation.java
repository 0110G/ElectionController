package com.ElectionController.Controllers.ElectionControllerEndPoints;

import com.ElectionController.Structures.Response;
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
