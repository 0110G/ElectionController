package com.electionController.controllers;

import com.electionController.constants.ControllerOperation;
import com.electionController.constants.ResponseCodes;
import com.electionController.dbConnector.Getter.DBGetter;
import com.electionController.dbConnector.Putter.DBPutter;
import com.electionController.dbConnector.Updater.DBUpdater;
import com.electionController.exceptions.InvalidParameterException;
import com.electionController.exceptions.InvalidCredentialException;
import com.electionController.exceptions.RestrictedActionException;
import com.electionController.exceptions.InternalServiceException;
import com.electionController.exceptions.EntityNotFoundException;
import com.electionController.logger.ConsoleLogger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ActionController<Query, Response> {

    @Autowired
    protected DBGetter dbGetter;

    @Autowired
    protected DBPutter dbPutter;

    @Autowired
    protected DBUpdater dbUpdater;

    public abstract ControllerOperation getControllerOperation();

    protected abstract Response executeAction(final Query query);

    public abstract void validateActionAccess(final Query query);

    public Response execute(final Query query) {
        try {
            ConsoleLogger.Log(getControllerOperation(), "STARTING_EXECUTION...");
            validateActionAccess(query);
            ConsoleLogger.Log(getControllerOperation(), "SUFFICIENT_PERMISSIONS");
            Response response = executeAction(query);
            ConsoleLogger.Log(getControllerOperation(), "SUCCESSFULLY_EXECUTED");
            return response;
        } catch (InvalidCredentialException ex) {
            ConsoleLogger.Log(getControllerOperation(), "[InvalidCredentialException]", ex.getErrorMessage(),
                    ex.getErrorDetails());
            throw new InvalidCredentialException(ex.getErrorCode(), ex.getErrorMessage());
        } catch (InvalidParameterException ex) {
            ConsoleLogger.Log(getControllerOperation(), "[InvalidParameterException]", ex.getErrorMessage(),
                    ex.getErrorDetails());
            throw ex;
        } catch (RestrictedActionException ex) {
            ConsoleLogger.Log(getControllerOperation(), "[RestrictedActionException]", ex.getErrorMessage(),
                    ex.getErrorDetails());
            throw ex;
        } catch (InternalServiceException ex) {
            ConsoleLogger.Log(getControllerOperation(), "[InternalServiceException]", ex.getErrorMessage(),
                    ex.getErrorDetails());
            throw ex;
        } catch (EntityNotFoundException ex) {
            ConsoleLogger.Log(getControllerOperation(), "[EntityNotFoundException]", ex.getErrorMessage(),
                    ex.getErrorDetails());
            throw new InternalServiceException(ResponseCodes.INTERNAL_ERROR.getResponseCode(),
                    ResponseCodes.INTERNAL_ERROR.getResponse());
        } catch (Exception ex) {
            ConsoleLogger.Log(getControllerOperation(), "[UNKNOWN_EXCEPTION]", ex.getMessage());
            throw new InternalServiceException(ResponseCodes.INTERNAL_ERROR.getResponseCode(),
                    ResponseCodes.INTERNAL_ERROR.getResponse());
        }
    }
}
