package com.electionController.controllers;

import com.electionController.exceptions.EntityNotFoundException;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.exceptions.InvalidParameterException;
import com.electionController.exceptions.RestrictedActionException;
import com.electionController.structures.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response processException(EntityNotFoundException ex) {
        return new Response.Builder()
                .withResponse(EntityNotFoundException.class)
                .withStatusCode(ex.getErrorCode())
                .withStatus(ex.getErrorMessage())
                .build();
    }

    @ExceptionHandler(RestrictedActionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response processException(RestrictedActionException ex) {
        return new Response.Builder()
                .withResponse(RestrictedActionException.class)
                .withStatusCode(ex.getErrorCode())
                .withStatus(ex.getErrorMessage())
                .build();
    }

    @ExceptionHandler(InvalidCredentialException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response processException(InvalidCredentialException ex) {
        return new Response.Builder()
                .withResponse(InvalidCredentialException.class)
                .withStatusCode(ex.getErrorCode())
                .withStatus(ex.getErrorMessage())
                .build();
    }

    @ExceptionHandler(InvalidParameterException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response processException(InvalidParameterException ex) {
        return new Response.Builder()
                .withResponse(InvalidParameterException.class)
                .withStatusCode(ex.getErrorCode())
                .withStatus(ex.getErrorMessage())
                .build();
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response processException(HttpClientErrorException.BadRequest ex) {
        return new Response.Builder()
                .withResponse(null)
                .withStatusCode(404)
                .withStatus("Incomplete Argument Passed")
                .build();

    }
}
