##  API Endpoints ##

#### 1. New Election ####
* This operation allows users to create a new election with 
following configurable entities:
    * *Election Title:* Text title of your election
    * *Election Description*: Text description of your election.
    * *Registered Voters:* A list of voter id's allowed to vote election.
    * *Registered Posts:* A list of posts constituting an election with following
    configurable entities:
        * *Post Description:* Text description for the post.
        * *WinCriteria:* The win criteria to be set for the post.
        * *Registed Contenstants*: A list of voter id's of the contestants competing for the post.


* **End Point**: ```/NewElection```


* **HTTP Request**: ```POST```


* **Query Structure**: [NewElectionQuery](https://github.com/0110G/ElectionController/blob/master/docs/structures.md#8-newelectionquery)


* **Return Structure**: [Election](https://github.com/0110G/ElectionController/blob/master/docs/structures.md#6-election)


* **Required Permissions:**
    * Valid Voter Credentials


* **Exceptions**:
    * When user enters invalid credentials in NewElectionQuery, ```InvalidCredentialException``` is thrown.
    * On null / unparsable json query passed, ```InvalidParameterException``` is thrown.
    * When user enters an invalid voter id for Registered voters or candidates, ```InvalidParameterException``` is thrown.  
    * ```InternalServiceException``` is thrown on other unexpected failures.


* **How to Use?**:
    * Can use ECJavaClient for accessing this API.
    * Can create HTTP POST request to given endpoint using Postman or any other
      software, with ```NewElectionQuery``` json structure in Request body.

#### 2. Get Election ####
* This operation allows users to get an existing election based on
  election_id.
    

* **End Point**: ```/GetElection```


* **HTTP Request**: ```POST```


* **Query Structure**: [GetElectionQuery](https://github.com/0110G/ElectionController/blob/master/docs/structures.md#5-getelectionquery)


* **Return Structure**: [Election](https://github.com/0110G/ElectionController/blob/master/docs/structures.md#6-election)


* **Required Permissions:**
    * Valid Voter Credentials
    * User should be a registered voter for the requested election.
    

* **Exceptions**:
    * When user enters invalid credentials in NewElectionQuery, ```InvalidCredentialException``` is thrown.
    * On null / unparsable json query passed, ```InvalidParameterException``` is thrown.
    * When user not registered voter for the requested election or invalid election_id
      entered, ```RestrictedAccessException``` is thrown.
    * ```InternalServiceException``` is thrown on other unexpected failures.


* **How to Use?**:
    * Can use ECJavaClient for accessing this API.
    * Can create HTTP POST request to given endpoint using Postman or any other
      software.


#### 3. Change Election Title ####
* This operation allows users to change the title of an existing election.
 If empty title is passed, the title is not changed.


* **End Point**: ```/ChangeElection/ChangeTitle```


* **HTTP Request**: ```POST```


* **Query Structure**: [ChangeElectionTitleQuery](https://github.com/0110G/ElectionController/blob/master/docs/structures.md#3-changeelectiontitlequery)


* **Return Structure**: Null


* **Required Permissions:**
    * Valid Voter Credentials
    * User should be a admin for the requested election.


* **Exceptions**:
    * When user enters invalid credentials in NewElectionQuery, ```InvalidCredentialException``` is thrown.
    * On null / unparsable json query passed, ```InvalidParameterException``` is thrown.
    * When user not admin voter for the requested election or invalid election_id
      entered, ```RestrictedAccessException``` is thrown.
    * ```InternalServiceException``` is thrown on other unexpected failures.


* **How to Use?**:
    * Can use ECJavaClient for accessing this API.
    * Can create HTTP POST request to given endpoint using Postman or any other
      software.

