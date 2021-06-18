package com.electionController.controllers;

import com.electionController.exceptions.InternalServiceException;
import com.electionController.exceptions.EntityNotFoundException;
import com.electionController.exceptions.InvalidParameterException;
import com.electionController.exceptions.RestrictedActionException;
import com.electionController.exceptions.InvalidCredentialException;
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
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Response processException(final EntityNotFoundException ex) {
        return Response.Builder()
                .withResponse(EntityNotFoundException.class)
                .withStatusCode(ex.getErrorCode())
                .withStatus(ex.getErrorMessage() + " " + ex.getErrorDetails())
                .build();
    }

    @ExceptionHandler(RestrictedActionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public Response processException(final RestrictedActionException ex) {
        return Response.Builder()
                .withResponse(RestrictedActionException.class)
                .withStatusCode(ex.getErrorCode())
                .withStatus(ex.getErrorMessage() + " " + ex.getErrorDetails())
                .build();
    }

    @ExceptionHandler(InvalidCredentialException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Response processException(final InvalidCredentialException ex) {
        return Response.Builder()
                .withResponse(InvalidCredentialException.class)
                .withStatusCode(ex.getErrorCode())
                .withStatus(ex.getErrorMessage() + " " + ex.getErrorDetails())
                .build();
    }

    @ExceptionHandler(InvalidParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Response processException(final InvalidParameterException ex) {
        return Response.Builder()
                .withResponse(InvalidParameterException.class)
                .withStatusCode(ex.getErrorCode())
                .withStatus(ex.getErrorMessage() + " " + ex.getErrorDetails())
                .build();
    }

    @ExceptionHandler(InternalServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Response processException(final InternalServiceException ex) {
        return Response.Builder()
                .withResponse(InternalServiceException.class)
                .withStatusCode(ex.getErrorCode())
                .withStatus(ex.getErrorMessage() + " " + ex.getErrorDetails())
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
