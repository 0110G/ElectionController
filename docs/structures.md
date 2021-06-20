## Models ##

### API Input Parameters ###

#### 1. AddRegisteredVoterToElectionQuery ####

    Structure AddRegisteredVoterToElectionQuery {
        @Required
        String voterId;             /* Admin credentials */
        String voterPassword;
        String electionId;
        
        @NonEmpty
        List<String> voterIdsToAdd;
    }

#### 2. ChangeElectionDescriptionQuery ####

    Structure ChangeElectionDescriptionQuery {
        @Required
        String voterId;             /* Admin credentials */
        String voterPassword;
        String electionId;
        
        @NonEmpty
        String electionDescription;
    }

#### 3. ChangeElectionTitleQuery ####

    Structure ChangeElectionDescriptionQuery {
        @Required
        String voterId;             /* Admin credentials */
        String voterPassword;
        String electionId;
        
        @NonEmpty
        String electionTitle;
    }

#### 4. DeleteRegisteredVoterFromElectionQuery ####

    Structure DeleteRegisteredVoterFromElectionQuery {
        @Required
        String voterId;             /* Admin credentials */
        String voterPassword;
        String electionId;
        
        @NonEmpty
        List<String> votersToDelete;

        @Optional
        boolean forceDelete;        /* set false by default */
    }

#### 5. GetElectionQuery ####
    Structure GetElectionQuery {
        @Required
        String voterId;
        String voterPassword;
        String electionId;
    }

#### 6. GetElectionResultsQuery ####
    Structure GetElectionQuery {
        @Required
        String voterId;
        String voterPassword;
        String electionId;
    }

#### 7. GetVoterQuery ####
    Structure GetVoterQuery {
        @Required
        String voterId;
        String voterPassword;
    }

#### 8. NewElectionQuery ####
    Structure NewElectionQuery {
        @Required
        String voterId;
        String voterPassword;
        
        @Optional
        String electionTitle;
        String electionDescription;
        List<String> registeredVoters;
        List<Post> registeredPost;
    
        Structure Post {
            @Optional
            String postDescription;
            Enum WinCriteria; /* GREATEST_NUMBER_OF_VOTES(default), LOWEST_NUMBER_OF_VOTES, MAJORTY*/        

            @NonEmpty
            List<String> registeredContestants; /*Voter ids of contestants*/
        }
    }

#### 9. NewVoterQuery ####
    Structure NewVoterQuery {
        @Required
        String voterName;
        String voterPassword;
    }

#### 10. VoteQuery ####
    Structure VoteQuery {
        @Required
        String voterId;
        String voterPassword;
        String electionId;
        String postId;
        String candidateId;
    }

### API Responses ###
All the API's return the ```Response``` structure.

    Structure Response {
        <Structure> response;
        String status;
        Integer statusCode;
    }

**Note:**
* The ```statusCode``` and ```status``` fields in the ```Response```
  structure represent ElectionController specific status. They are to **NOT** meant to be used as HTTP status codes or something else.
  
* The ```response``` field depends on the API called.

### Response Structures ###

#### 1. Voter ####
    Structure Voter {
        String voterId;
        String voterName;
        String voterPassword; /* can be masked depending on API*/
    }

#### 2. Contestant ####
    Structure Contestant extends Structure Voter {
        @Inherited
        String voterId;
        String voterName;
        String voterPassword; /* Masked */
                
        Integer votesSecured;
        Integer rank;
    }

#### 3. PostResults ####
    Structure PostResults {
        String postId;
        String postDescription;
        Enum postWinCriteria;
        List<Contestant> contestantList;
    }

#### 4. ElectionResults ####
    Structure ElectionResults {
        String electionId;
        String electionTitle;
        String electionDescription;
        List<PostResults> electionPostResults;
    }

#### 5. Post ####
    Structure Post {
        String postId;
        String electionId;
        Integer postIndex;
        String postDescription;
        Enum WinCriteria;
        Integer totalContestants;
        List<Contestant> contestants;
    }

#### 6. Election ####
    Structure Election {
        String electionTitle;
        String electionId;
        String electionDescription;
        String adminVoterId; /* voter id of the voter who created the election */
        List<Voter> eligibleVoters;
        List<Post> availablePost;
        Date createdOn;
    }

