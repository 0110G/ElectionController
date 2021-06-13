package com.electionController.controllers;

import com.electionController.constants.ControllerOperation;
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

    public abstract Response executeAction(final Query query);

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
            ConsoleLogger.Log(getControllerOperation(), "[InvalidCredentialException]", ex.getErrorMessage());
            throw new InvalidCredentialException("INCORRECT_USERNAME/PASSWORD");
        } catch (InvalidParameterException ex) {
            ConsoleLogger.Log(getControllerOperation(), "[InvalidParameterException]", ex.getErrorMessage());
            throw ex;
        } catch (RestrictedActionException ex) {
            ConsoleLogger.Log(getControllerOperation(), "[RestrictedActionException]", ex.getErrorMessage());
            throw new RestrictedActionException("INSUFFICIENT_PERMISSIONS_FOR_ACTION");
        } catch (InternalServiceException ex) {
            ConsoleLogger.Log(getControllerOperation(), "[InternalServiceException]", ex.getErrorMessage());
            throw ex;
        } catch (EntityNotFoundException ex) {
            ConsoleLogger.Log(getControllerOperation(), "[EntityNotFoundException]", ex.getErrorMessage());
            throw new InternalServiceException("ENTITY_ERROR");
        } catch (Exception ex) {
            ConsoleLogger.Log(getControllerOperation(), "[UNKNOWN_EXCEPTION]", ex.getMessage());
            throw new InternalServiceException("UNKNOWN_ERROR");
        }
    }
}
