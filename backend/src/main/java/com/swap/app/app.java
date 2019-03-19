/* Backend to do list:
   1. Finish environment setups, pom.xl 
   2. Compile and run the backend on heroku
   3. Read from 216 and understand which functions are necessary
   4. Make sure the package name is correct
   */


package edu.lehigh.cse280.swap.app;
import java.util.Map;
import java.util.HashMap;
// Import the Spark package, so that we can make use of the "get" function to 
// create an HTTP GET route
import spark.Spark;
import edu.lehigh.cse280.swap.database.*;
// Import Google's JSON library
import com.google.gson.*;

/**
 * For now, our app creates an HTTP server that can only get and add data.
 */
public class App{
    public static void main(String[] args) {
        // we need to call it before we do anything else with Spark.
        // Our server runs on port 4567. That's the Java Spark default
        Map<String, String> env = System.getenv();
        String db_url = env.get("DATABASE_URL");
        //String db_url = "postgres://cblibdzhsvqshl:d48272553306c7cbc287d6f9a97550f9bdd98153a87caeac5d4b98ac0cd59438@ec2-23-21-130-182.compute-1.amazonaws.com:5432/d3pvjv0qor9898";
        Spark.port(getIntFromEnv("PORT", 4567));
        // gson provides us with a way to turn JSON into objects, and objects
        // into JSON.
        //
        // NB: it must be final, so that it can be accessed from our lambdas
        //
        // NB: Gson is thread-safe.  See 
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
        final Gson gson = new Gson();
        //final String herokuUrl = "https://swap-lehigh.herokuapp.com/";
        // database holds all of the data that has been provided via HTTP 
        // requests
        //
        // NB: every time we shut down the server, we will lose all data, and 
        //     every time we start the server, we'll have an empty database,
        //     with IDs starting over from 0.
        final Database database = Database.getDatabase(db_url);
        if (database==null) System.out.println("database object is null");
        final ItemCategoryDataTable CDT= Database.getItemCDT();
        if (CDT==null) System.out.println("ItemCategoryDataTable object is null");
        final ItemDataTable DT= Database.getItemDT();
        if (DT==null) System.out.println("ItemDataTable object is null");
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
        Spark.get("/items/all", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, DT.selectAllItems()));
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
            return gson.toJson(new StructuredResponse("ok", null, DT.selectOneItem(idx)));
        });

        // POST route for adding a new element to the database.  This will read
        // JSON from the body of the request, turn it into a SimpleRequest 
        // object, extract the title and message, insert them, and return the 
        // ID of the newly created row.
        Spark.get("/itemlist/:tag", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal 
            // Server Error'
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            //     describes the error.
            response.status(200);
            response.type("application/json");
            String tag = request.params("tag").toString();
            switch (tag) {
                case "car":
                    return gson.toJson(new StructuredResponse("ok", null, CDT.selectAllCar()));
                case "electronic":
                    return gson.toJson(new StructuredResponse("ok", null, CDT.selectAllElectronic()));
                case "furniture":
                    return gson.toJson(new StructuredResponse("ok", null, CDT.selectAllFurniture()));
                case "school":
                    return gson.toJson(new StructuredResponse("ok", null, CDT.selectAllSchool()));
                default:
                    return gson.toJson(new StructuredResponse("error", "no matching tags in database", null));
            }
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