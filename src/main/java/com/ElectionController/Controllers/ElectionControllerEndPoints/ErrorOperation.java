package com.ElectionController.Controllers.ElectionControllerEndPoints;

import com.ElectionController.Structures.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorOperation {
    @GetMapping("/error")
    public Response message(){
        return new Response.Builder()
                .withResponse("abaeawda")
                .build();
    }
}
