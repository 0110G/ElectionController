##  API Endpoints ##

### Table ###

| **Sr. No.** | **Operation**  | **Endpoint** | **HTTP Method** | **Description** |
|---|---|---|---|---|
|1.|New Election| ```/NewElection```| ```POST``` | Lets user create a new election.|
|2.|Get Election| ```/GetElection```|```POST```|Lets user see an existing election.|
|3.|Change ElectionTitle| ```ChangeElection/ChangeTitle```|```POST```|Lets user change title of an existing election.|
|4.|Change Election Description|```ChangeElection/ChangeDescription```|```POST```|Lets user change description of an existing election|
|5.|Add Registered Voter|```/ChangeElection/AddVoters```|```PUT```|Lets user add new voters to an existing election.|
|6.|Delete Registered Voter|```/ChangeElection/DeleteVoters```|```DELETE```|Lets user delete voters from an existing election.|
|7.|Delete Election|```/DeleteElection```|```DELETE```|Lets user delete an existing election.|
|8.|Get Election Results|```/GetResults```|```POST```|Lets user see the rankings and secured votes of each candidate for each post in an existing election.|
|9.|Ping|```/Ping```|```GET```|Lets user check server status|
|10.|Get Voter|```/GetVoter```|```POST```|Lets user authenticate an existing voter with his/her credentials.|
|11.|New Voter|```/NewVoter```|```PUT```|Lets a user create a new voter with the corresponding details.|
|12.|Vote | ```/Vote``` | ```POST``` | Lets user cast his/her vote to a candidate for a given post.






### 1. New Election ###
* This operation allows users to create a new election with 
following configurable entities:
    * *Election Title:* Text title of your election
    * *Election Description*: Text description of your election.
    * *Registered Voters:* A list of voter id's allowed to vote in the election.
    * *Registered Posts:* A list of posts constituting an election with following
    configurable entities:
        * *Post Description:* Text description for the post.
        * *WinCriteria:* The win criteria to be set for the post.
        * *Registered Contestants*: A list of voter id's of the contestants competing for the post.


* **End Point**: ```/NewElection```


* **HTTP Request**: ```POST```


* **Query Structure**: [NewElectionQuery](https://github.com/0110G/ElectionController/blob/master/docs/structures.md#8-newelectionquery)


* **Return Structure**: [Election](https://github.com/0110G/ElectionController/blob/master/docs/structures.md#6-election)


* **Required Permissions:**
    * Valid Voter Credentials


* **Exceptions**:
    * When user enters invalid credentials in NewElectionQuery, ```InvalidCredentialException``` is thrown.
    * On null / un-parsable json query passed, ```InvalidParameterException``` is thrown.
    * When user enters an invalid voter id for Registered voters or candidates, ```InvalidParameterException``` is thrown.  
    * ```InternalServiceException``` is thrown on other unexpected failures.


* **How to Use?**:
    * Can use ECJavaClient for accessing this API.
    * Can create HTTP POST request to given endpoint using Postman or any other
      software, with ```NewElectionQuery``` json structure in Request body.

### 2. Get Election ###
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
    * On null / un-parsable json query passed, ```InvalidParameterException``` is thrown.
    * When user not registered voter for the requested election or invalid election_id
      entered, ```RestrictedAccessException``` is thrown.
    * ```InternalServiceException``` is thrown on other unexpected failures.


* **How to Use?**:
    * Can use ECJavaClient for accessing this API.
    * Can create HTTP POST request to given endpoint using Postman or any other
      software.


### 3. Change Election Title ###
* This operation allows users to change the title of an existing election.
 An empty or null title passed leads to no change in the title.


* **End Point**: ```/ChangeElection/ChangeTitle```


* **HTTP Request**: ```POST```


* **Query Structure**: [ChangeElectionTitleQuery](https://github.com/0110G/ElectionController/blob/master/docs/structures.md#3-changeelectiontitlequery)


* **Return Structure**: Null


* **Required Permissions:**
    * Valid Voter Credentials
    * User should be an admin for the requested election.


* **Exceptions**:
    * When user enters invalid credentials in NewElectionQuery, ```InvalidCredentialException``` is thrown.
    * On null / un-parsable json query passed, ```InvalidParameterException``` is thrown.
    * When user not admin voter for the requested election or invalid election_id
      entered, ```RestrictedAccessException``` is thrown.
    * ```InternalServiceException``` is thrown on other unexpected failures.


* **How to Use?**:
    * Can use ECJavaClient for accessing this API.
    * Can create HTTP POST request to given endpoint using Postman or any other
      software.

### 4. Change Election Description ###
* This operation allows users to change the description of an existing election.
  An empty or null title passed leads to no change in the description.


* **End Point**: ```/ChangeElection/ChangeTitle```


* **HTTP Request**: ```POST```


* **Query Structure**: [ChangeElectionDescriptionQuery](https://github.com/0110G/ElectionController/blob/master/docs/structures.md#3-changeelectiontitlequery)


* **Return Structure**: Null


* **Required Permissions:**
  * Valid Voter Credentials
  * User should be an admin for the requested election.


* **Exceptions**:
  * When user enters invalid credentials in NewElectionQuery, ```InvalidCredentialException``` is thrown.
  * On null / un-parsable json query passed, ```InvalidParameterException``` is thrown.
  * When user not admin voter for the requested election or invalid election_id
    entered, ```RestrictedAccessException``` is thrown.
  * ```InternalServiceException``` is thrown on other unexpected failures.


* **How to Use?**:
  * Can use ECJavaClient for accessing this API.
  * Can create HTTP POST request to given endpoint using Postman or any other
    software.