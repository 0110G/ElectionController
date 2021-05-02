package com.ElectionController.Constants;

/*
* Use only for logging purposes
* */
public enum ControllerOperations {
    NEW_ELECTION ("ElectionController", "NewElection"),
    CHANGE_ELECTION ("ElectionController", "ChangeElection"),
    GET_ELECTION ("ElectionController", "GetElection"),
    DB_PUT_ELECTION ("DatabaseConnector", "PutElection"),
    DB_PUT_VOTERMAP("DatabaseConnector", "PutVotermap"),
    DB_PUT_POST("DababaseConnector", "PutPost"),
    DB_PUT_POSTMAP("DatabaseController", "PutPostmap"),
    DB_GET_ELECTION("DatabaseConnector", "GetElection"),
    DB_GET_VOTERMAP("DatabaseConnector", "PutVotermap");

    private final String baseController;
    private final String controller;

    private ControllerOperations(final String baseController, final String controller) {
        this.baseController = baseController;
        this.controller = controller;
    }

    public String getBaseController() {
        return this.baseController;
    }

    public String getController() {
        return this.controller;
    }

}
