# lichess-bot overview 
Springboot interface into Lichess, with StockFish backend Chess Engine.

This is a springboot application that interfaces with [Lichess.org Api](https://lichess.org/api) utilizing Spring Web React. 

This application relay's Lichess.org Api events into the [StockFish Chess Engine](https://stockfishchess.org/). Allows for Stockfish Chess engine to calculate the best move with data provided by Lichess.org Api. 

Once best move by StockFish Chess Engine is returned, this information is then relayed back to Lichess.org Api to make move for user.

# Application Version Information
**Java Version**: *17*

**Spring Boot Parent Version**: *3.0.5*

**Current Application Version**: *1.0.0*
