<img src="https://image.flaticon.com/icons/png/512/95/95375.png" alt="drawing" width="200"/>

# ElectionController API #

## Informal ##
As an Indian, I am very much used to seeing elections
now and then. They happen at different scales, ranging
from a small poll to choose class monitor to the
Parliamentary ones. This API is my attempt to simulate
an election on cloud and pave way for digital elections
in the future.


## Use ##
The Election Controller API provides set of API's which are required for conducting an
election. This involves creating, changing and editing new elections, posts, voters and candidates.
Following list summarises the exposed API's and its functionality. For detailed use-case visit [endpoints](https://github.com/0110G/ElectionController/blob/master/docs/endpoints.md).

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

## Features ##

<img src="https://cdn2.iconfinder.com/data/icons/leto-blue-gdpr/64/gdpr_data_portability-512.png" alt="drawing" width="100"/>

###Portability ###
Provide user with maximum portability and cross-platform support. Allow user to set up Election Controller API Server on any machine which 
 has JDK 1.8 or above. No other software packages/installations are required. To deploy, just run ```java -jar ElectionControllerv1.jar```
<br><br><br>

<img src="https://www.pngkey.com/png/full/274-2742417_half-life-logo-png-transparent-lambda-half-life.png" alt="drawing" width="100"/>

### Functionality ###
Provide the user with required functionality to successfully host and conduct an election using the API. 
  See [endpoints](https://github.com/0110G/ElectionController/blob/master/docs/endpoints.md) for complete set
   of functionality.
<br><br><br>

<img src="https://www.pngkey.com/png/full/203-2036982_security-icon-transparent-background-security-icon.png" alt="drawing" width="100"/>

### Security ### 
With added access permissions layer for each endpoint, the API ensures that only correct people are able to see the required 
resources.

## Endpoints ##
Refer docs/endpoints




Will be updated in future commits.

# Todo #
1. The design makes it possible for integrating 
    multiple sql/nosql databases. The current version
   supports only h2 database. Add support for AWS DynamoDB.
