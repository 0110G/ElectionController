## Data Structures ##

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
        
        @Optional
        String electionDescription;
    }

#### 3. ChangeElectionTitleQuery ####

    Structure ChangeElectionDescriptionQuery {
        @Required
        String voterId;             /* Admin credentials */
        String voterPassword;
        String electionId;
        
        @Optional
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