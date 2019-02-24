CSE 280 Team Swap Progress Report #2 (Friday, Feb 22 2019)

Progress overall (What was accomplished this past week):
Database: 
  Implement the basic architecture for PostgresSQL
  Set up prepared statement for database and implement functions to insert, edit, or delete items in ItemData table
Backend: 
  Created the intermediate end between front-end and back-end (App.java). 
  Created response and request structure files for database (FirstRequest.java, FirstResponse.java, SimpleRequest.java). 
  Approximately 250 lines of code in total 
Frontend:
  Create index.html where it has few two <div> to hold an input text area and a scrollable display area. 
  Create app.ts where has all the functionality including reading input and encoding it as JSON, and get all the messages from the server and display them in a table 
  Implement a general template tool, handlebar to make displaying information more conceptually clear
  Update API endpoints to document all the routes with corresponding request and response
  Create a deploy.sh file to deploy all front-end files to back-end resource folder.
Overall: 
  Wrote front-end and back-end code, but not yet tested
  Created project milestone(subject to change)
  Created API endpoint
  Created table documentation

Problems (What were we unable to complete):
  Database structure is not optimized, questions retain on whether to use private/protected/package private and whether to use static/final prefix for the Database prepared statement
  Connection between front-end and back-end and deploy and run on heroku server.

Goals for next week:
  Be able to demo the function that display all items, and can filter items based on simple categories. 
  Decide on which database to use, elastic search or PostgreSQL
  Lay out all functionalities that Swap wishes to support, and sort out dependencies and decide the priorities of those functions
  Create a diagram of overview of and backend - frontend communication model, be specific on parameters 
  Do research on elasticsearch and evaluate how it would help as the ONLY (or major) database for our project. Create a decent report for other team members to review

Potential Issues:
  Database prepared statements are static & protected & multiple instances of database may be initialized
  Database may need to be rewritten since we are probably going to use elasticsearch
  We still fall behind our schedule. Next week our goals MUST be accomplished




