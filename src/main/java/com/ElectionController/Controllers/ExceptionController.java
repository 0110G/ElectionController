package com.ElectionController.Controllers;

import com.ElectionController.Exceptions.EntityNotFoundException;
import com.ElectionController.Exceptions.InvalidCredentialException;
import com.ElectionController.Exceptions.RestrictedActionException;
import com.ElectionController.Structures.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response processException(EntityNotFoundException ex) {
        return new Response.Builder()
                .withResponse(ex)
                .withStatusCode(ex.getErrorCode())
                .withStatus(ex.getErrorMessage())
                .build();
    }

    @ExceptionHandler(RestrictedActionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response processException(RestrictedActionException ex) {
        return new Response.Builder()
                .withResponse(ex)
                .withStatusCode(ex.getErrorCode())
                .withStatus(ex.getErrorMessage())
                .build();
    }

    @ExceptionHandler(InvalidCredentialException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response processException(InvalidCredentialException ex) {
        return new Response.Builder()
                .withResponse(ex)
                .withStatusCode(ex.getErrorCode())
                .withStatus(ex.getErrorMessage())
                .build();
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response processException(HttpClientErrorException.BadRequest ex) {
        return new Response.Builder()
                .withResponse(ex)
                .withStatusCode(404)
                .withStatus("Incomplete Argument Passed")
                .build();

    }
}
