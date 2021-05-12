package com.ElectionController.Constants;

/*
* Use only for logging purposes
* */
public enum ControllerOperations {
    NEW_ELECTION ("ElectionController", "NewElection"),
    NEW_VOTER("VoterController", "NewVoter"),

    CHANGE_ELECTION ("ElectionController", "ChangeElection"),
    CHANGE_ELECTION_TITLE("ElectionController", "ChangeElectionTitle"),
    CHANGE_ELECTION_ADD_VOTERS("ElectionController", "AddVotersToElection"),
    CHANGE_ELECTION_DELETE_VOTERS("ElectionController", "DeleteVotersFromElection"),
    CHANGE_ELECTION_DESCRIPTION("ElectionController", "ChangeElectionDescription"),

    GET_ELECTION ("ElectionController", "GetElection"),

    VOTER_VOTE("VoterController", "VoterVote"),

    DB_PUT_ELECTION ("DatabaseConnector", "PutElection"),
    DB_PUT_VOTERMAP("DatabaseConnector", "PutVotermap"),
    DB_PUT_POST("DababaseConnector", "PutPost"),
    DB_PUT_POSTMAP("DababaseConnector", "PutPostmap"),
    DB_PUT_VOTER("DababaseConnector", "PutVoter"),

    DB_GET_ELECTION("DatabaseConnector", "GetElection"),
    DB_GET_VOTERMAP("DatabaseConnector", "GetVotermap"),
    DB_GET_ELECTION_VOTERS("DatabaseConnector", "GetElectionVoters"),
    DB_GET_ELECTION_POSTS("DatabaseConnector", "GetElectionPosts"),
    DB_GET_ELECTION_POST("DatabaseConnector", "GetElectionPost"),
    DB_GET_POST_CANDIDATES("DatabaseConnector", "GetPostCandidates"),
    DB_GET_POST_CANDIDATE("DatabaseConnector", "GetPostCandidate"),
    DB_GET_VOTER("DatabaseConnector", "GetVoter"),

    DB_UPDATE_ELECTION("DatabaseConnector", "UpdateElection"),
    DB_UPDATE_INCREMENT_CANDIDATE_VOTE("DatabaseConnector", "IncrementVote"),
    DB_UPDATE_MARK_VOTER_VOTED_FOR_POST("DatabaseConnector", "MarkVoted"),

    DB_DELETE_VOTER_FROM_ELECTION("DatabaseConnector", "DeleteVoterFromElection"),
    DB_DELETE_CANDIDATE_FROM_POST("DatabaseConnector", "DeleteCandidateFromPost");

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
