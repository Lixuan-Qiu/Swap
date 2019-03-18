/* Backend to do list:
   1. Finish environment setups, pom.xl 
   2. Compile and run the backend on heroku
   3. Read from 216 and understand which functions are necessary
   4. Make sure the package name is correct
   */


  package com.swap.backend.app;

  // Import the Spark package, so that we can make use of the "get" function to 
  // create an HTTP GET route
  import spark.Spark;
  import com.swap.backend.database.*;
  // Import Google's JSON library
  import com.google.gson.*;
  
  /**
   * For now, our app creates an HTTP server that can only get and add data.
   */
  public class App{
      public static void main(String[] args) {
          // we need to call it before we do anything else with Spark.
          // Our server runs on port 4567. That's the Java Spark default
          Spark.port(getIntFromEnv("PORT", 4567));
  
          // gson provides us with a way to turn JSON into objects, and objects
          // into JSON.
          //
          // NB: it must be final, so that it can be accessed from our lambdas
          //
          // NB: Gson is thread-safe.  See 
          // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
          final Gson gson = new Gson();
          final String herokuUrl = "https://swap-lehigh.herokuapp.com/";
          // database holds all of the data that has been provided via HTTP 
          // requests
          //
          // NB: every time we shut down the server, we will lose all data, and 
          //     every time we start the server, we'll have an empty database,
          //     with IDs starting over from 0.
          final Database database = Database.getDatabase(herokuUrl);
          
          //For front-end test purpose
          Spark.get("/hello", new spark.Route() {
              @Override
              public Object handle(spark.Request request, spark.Response response) {
                  return "Hello World!";
              }
          });
          
          // GET route that returns all message titles and Ids.  All we do is get 
          // the data, embed it in a StructuredResponse, turn it into JSON, and 
          // return it.  If there's no data, we return "[]", so there's no need 
          // for error handling.
          Spark.get("/items", (request, response) -> {
              // ensure status 200 OK, with a MIME type of JSON
              response.status(200);
              response.type("application/json");
              return gson.toJson(new StructuredResponse("ok", null, database.readAll()));
          });
  
          // GET route that returns everything for a single row in the database.
          // The ":id" suffix in the first parameter to get() becomes 
          // request.params("id"), so that we can get the requested row ID.  If 
          // ":id" isn't a number, Spark will reply with a status 500 Internal
          // Server Error.  Otherwise, we have an integer, and the only possible 
          // error is that it doesn't correspond to a row with data.
          Spark.get("/items/:id", (request, response) -> {
              int idx = Integer.parseInt(request.params("id"));
              // ensure status 200 OK, with a MIME type of JSON
              response.status(200);
              response.type("application/json");
              DataRow data = database.readOne(idx);
              if (data == null) {
                  return gson.toJson(new StructuredResponse("error", idx + " not found", null));
              } else {
                  return gson.toJson(new StructuredResponse("ok", null, data));
              }
          });
  
          // POST route for adding a new element to the database.  This will read
          // JSON from the body of the request, turn it into a SimpleRequest 
          // object, extract the title and message, insert them, and return the 
          // ID of the newly created row.
          Spark.post("/item/:user", (request, response) -> {
              // NB: if gson.Json fails, Spark will reply with status 500 Internal 
              // Server Error
              SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
              // ensure status 200 OK, with a MIME type of JSON
              // NB: even on error, we return 200, but with a JSON object that
              //     describes the error.
              response.status(200);
              response.type("application/json");
              // NB: createEntry checks for null title and message
              int newId = database.createEntry(req.mTitle, req.mMessage);
              if (newId == -1) {
                  return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
              } else {
                  return gson.toJson(new StructuredResponse("ok", "" + newId, null));
              }
          });
  
  
          Spark.post("/login", (request, response) -> {
  
              System.out.println("receive request to /login");
              System.out.println("url: " + request.url());
              System.out.println("request body: " + request.body());
              System.out.println("attributes list: " + request.attributes());
              System.out.println("contentType: " + request.contentType());
              System.out.println("headers: " + request.headers());
              System.out.println("params: " + request.params());
              System.out.println("raw: " + request.raw());
              System.out.println("requestMethod" + request.requestMethod());
              
              FirstRequest req = gson.fromJson(request.body(), FirstRequest.class);
              // parse request to FirstRequest
              String email = null;
              String name = null;
              String exp = null;
              String idTokenString = req.id_token;
              response.status(200);
              response.type("application/json");
  
              GoogleIdToken idToken = verifier.verify(idTokenString);
              if (idToken != null) {
                  Payload payload = idToken.getPayload();
  
                  // Print user identifier
                  String userIdString = payload.getSubject();
                  System.out.println("User ID: " + userIdString);
  
                  // Get profile information from payload
                  email = payload.getEmail();
                  boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
                  name = (String) payload.get("name");
                  exp = payload.get("exp").toString();
                  /*
                   * String locale = (String) payload.get("locale"); String familyName = (String)
                   * payload.get("family_name"); String givenName = (String)
                   * payload.get("given_name");
                   */
  
              } else {
                  return gson.toJson(new FirstResponse("error", "login error: Invalid ID token.", null, -1));
              }
              if (email.indexOf("@lehigh.edu") == -1) {
                  return gson
                          .toJson(new FirstResponse("error", "login error: Invalid Email, must be lehigh.edu", null, -1));
              }
              int uId = database.get_userId2(email);
              if (uId == -1) {
                  // If new user, then insert new row
                  int l = database.insert_userRow(name, name, email);
                  System.out.println("insert user row:" + l);
                  uId = database.get_userId2(email);
                  System.out.println("user ID:" + uId);
              } else {
                  // retreive sessionData
                  Database.session_RowData sessionData = database.select_sessionOne(uId);
                  if (sessionData != null) { // if there is existing sessionData, delete old one
                      database.delete_sessionRow(uId);
                  }
              }
  
              exp = exp + uId;
              // Inset new session row
              database.insert_sessionRow(uId, exp);
              // send response back
              return gson.toJson(new FirstResponse("ok", "session key for uid = " + uId + " is sent.",
                      database.get_sessionKey(uId), uId));
          });
  
      }
       /**
        * For Heroku's purpose 
        *
        * Get an integer environment varible if it exists, and otherwise return the
        * default value.
        * 
        * @envar      The name of the environment variable to get.
        * @defaultVal The integer value to use as the default if envar isn't found
        * 
        * @returns The best answer we could come up with for a value for envar
       */
      static int getIntFromEnv(String envar, int defaultVal) {
          ProcessBuilder processBuilder = new ProcessBuilder();
          if (processBuilder.environment().get(envar) != null) {
              return Integer.parseInt(processBuilder.environment().get(envar));
          }
          return defaultVal;
      }
  }