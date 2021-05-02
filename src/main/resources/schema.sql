DROP TABLE IF EXISTS VOTERS;

CREATE TABLE VOTERS (
    voterId VARCHAR(250) NOT NULL  PRIMARY KEY,
    voterName VARCHAR(250) NOT NULL,
    voterPassword VARCHAR(250) NOT NULL
);

DROP TABLE IF EXISTS VOTERMAP;

CREATE TABLE VOTERMAP (
    voterId VARCHAR(250) NOT NULL,
    electionId VARCHAR(250) NOT NULL,
    isVoterEligible BIT DEFAULT true,
    isVoterAdmin BIT default false
);

DROP TABLE IF EXISTS ELECTION;

CREATE TABLE ELECTION (
    electionTitle VARCHAR(250) DEFAULT 'New Election',
    electionId VARCHAR(250) NOT NULL PRIMARY KEY,
    electionDescription VARCHAR(250) DEFAULT 'Election Description',
    adminVoterId VARCHAR(250) NOT NULL,
    postSetId VARCHAR(250) NOT NULL
);

DROP TABLE IF EXISTS POST;

CREATE TABLE POST (
    postId VARCHAR(250) PRIMARY KEY,
    postDescription VARCHAR(250) DEFAULT 'Election Post',
    electionId VARCHAR(250) NOT NULL,
    totalCandidates INTEGER DEFAULT 0,
    winCriteria INTEGER DEFAULT 0
);

DROP TABLE IF EXISTS POSTMAP;

CREATE TABLE POSTMAP (
    postId VARCHAR(250) NOT NULL,
    contestantId VARCHAR(250) NOT NULL,
    contestantAlias VARCHAR(250) DEFAULT 'Candidate Name',
    votesSecured INTEGER DEFAULT 0
)

--CreateElection Called ->
--1. Voter Authentication
--    IF FAILS, RETURN FAIL RESPONCE
--2. Query consists of post to be created and respective candidates
--3. Traverse through post, for each post, Add an entry to POST table
--    Traverse through each candidate and add the entry to POSTCANDIDATEMAPPER
--    A Candidate contest for multiple posts
--4. Return respose
--
--Vote: Params VoterCredentials, Election, PostId, toVoterId
--1.