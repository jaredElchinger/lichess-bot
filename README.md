# lichess-bot overview 
Springboot interface into Lichess, with StockFish backend Chess Engine.

This is a springboot application that interfaces with [Lichess.org Api](https://lichess.org/api) utilizing Spring Web React. 

This application relay's Lichess.org Api events into the [StockFish Chess Engine](https://stockfishchess.org/). Allows for Stockfish Chess engine to calculate the best move with data provided by Lichess.org Api. 

Once best move by StockFish Chess Engine is returned, this information is then relayed back to Lichess.org Api to make move for user.

# Application Version Information
**Java Version**: *17*

**Spring Boot Parent Version**: *3.0.5*

**Current Application Version**: *1.0.0*

## Running Locally from Intellij
### Prerequisites 
* Intellij installed on machine
* Java 17 or higher installed on machine
* Apache Maven 3.8 installed on machine
### Running 
1. Add project as maven project 
2. Update application-local.yml file with needed information
* lichess.web.personal-token needs to be populated with personal access token retreived using below section 'Lichess personal Authentication'
* lichess.web.personal-username needs to align with your personal username registered with Lichess
3. Set following environment variable SPRING_PROFILES_ACTIVE=local

## Lichess Personal Authentication 
Following page provides information on how to generate a personal OAUTH access token: [Personal API access tokens](https://lichess.org/account/oauth/token)
