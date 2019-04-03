/* To do list (Mar 29):
   1. Connect to elasticsearch
   2. finsh line 123
   3. 
   4. 
   */

package edu.lehigh.cse280.swap.app;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
// Import the Spark package, so that we can make use of the "get" function to 
import spark.Spark;
import java.net.URI;
import edu.lehigh.cse280.swap.database.*;
// Import Google's JSON library
import com.google.gson.*;
import java.io.*;

/**
 * For now, our app creates an HTTP server that can only get and add data.
 */
public class App {
    public static void main(String[] args) {
        // we need to call it before we do anything else with Spark.
        // Our server runs on port 4567. That's the Java Spark default
        Map<String, String> env = System.getenv();
        String db_url = env.get("DATABASE_URL");
        // String db_url =
        // "postgres://cblibdzhsvqshl:d48272553306c7cbc287d6f9a97550f9bdd98153a87caeac5d4b98ac0cd59438@ec2-23-21-130-182.compute-1.amazonaws.com:5432/d3pvjv0qor9898";
        Spark.port(getIntFromEnv("PORT", 4567));
        // gson provides us with a way to turn JSON into objects, and objects
        // into JSON.
        //
        // NB: it must be final, so that it can be accessed from our lambdas
        //
        // NB: Gson is thread-safe. See
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
        final Gson gson = new Gson();
        // final String herokuUrl = "https://swap-lehigh.herokuapp.com/";
        // database holds all of the data that has been provided via HTTP
        // requests
        //
        // NB: every time we shut down the server, we will lose all data, and
        // every time we start the server, we'll have an empty database,
        // with IDs starting over from 0.
        final Database database = Database.getDatabase(db_url);
        if (database == null)
            System.out.println("database object is null");

        String static_location_override = System.getenv("STATIC_LOCATION");

        // hardcode data entries
        String[] categories = { "sss", "sss" };
        database.insertNewItem("logitech mouse", "brand new", "Sheldon", 10.0, categories);
        database.insertNewItem("Ferrari 488", "brand new", "Sheldon", 200000.0, categories);
        database.insertNewItem("GTX 1060", "near broken", "Xiaowei", 15.0, categories);
        database.insertNewItem("Econ001 textbook", "half new", "Xiaowei", 30.0, categories);
        database.insertNewItem("Coolermaster keyboard", "brand new", "Lixuan Qiu", 35.0, categories);
        database.insertNewItem("Desktop", "80% new", "Lixuan Qiu", 25.0, categories);
        database.insertNewItem("Range Rover Sport", "half new", "Allen", 45000.0, categories);
        database.insertNewItem("Microfridge", "brand new", "Allen", 30.0, categories);

        ArrayList<ItemData> selectAll = new ArrayList<ItemData>();
        selectAll.add(new ItemData(1, "logitech mouse", "brand new", "Sheldon", 10.0f, categories));
        selectAll.add(new ItemData(2, "Ferrari 488", "brand new", "Sheldon", 200000.0f, categories));
        selectAll.add(new ItemData(3, "GTX 1060", "near broken", "Xiaowei", 15.0f, categories));
        selectAll.add(new ItemData(4, "Econ001 textbook", "half new", "Xiaowei", 30.0f, categories));
        selectAll.add(new ItemData(5, "Coolermaster keyboard", "brand new", "Lixuan Qiu", 35.0f, categories));
        selectAll.add(new ItemData(6, "Desktop", "80% new", "Lixuan Qiu", 25.0f, categories));
        selectAll.add(new ItemData(7, "Range Rover Sport", "half new", "Allen", 45000.0f, categories));
        selectAll.add(new ItemData(8, "Microfridge", "brand new", "Allen", 30.0f, categories));
        if (static_location_override == null) {
            Spark.staticFileLocation("/web");
        } else {
            Spark.staticFiles.externalLocation(static_location_override);
        }

        Spark.get("/", (request, response) -> {
            response.redirect("index.html");
            return "";
        });

        // For front-end test purpose
        Spark.get("/hello", new spark.Route() {
            @Override
            public Object handle(spark.Request request, spark.Response response) {
                return "Hello World!";
            }
        });

        // GET route that returns all message titles and Ids. All we do is get
        // the data, embed it in a StructuredResponse, turn it into JSON, and
        // return it. If there's no data, we return "[]", so there's no need
        // for error handling.
        Spark.get("/item/all", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // selectAll = database.selectAllItems();
            if (selectAll == null)
                return gson.toJson(new StructuredResponse("error", "Get all item returns null", null));
            return gson.toJson(new StructuredResponse("ok", null, selectAll));
        });

        // GET route that returns corresponding item list according to the parameters
        // specified in the url
        // An example would be /item?category=furniture-school&user_id=2
        // Spark.get("/item", (request, response) -> {
        // response.status(200);
        // response.type("application/json");
        // String unParsedCategory = request.queryParams("category");
        // if (unParsedCategory) {

        // }

        // String unParsedItemId = request.queryParams("item_id");
        // if (unParsedItemId) {

        // }

        // String unParsedUserId = request.queryParams("user_id");
        // if (unParsedUserId) {

        // }
        // });

        // GET route that returns everything for a single row in the database.
        // The ":id" suffix in the first parameter to get() becomes
        // request.params("id"), so that we can get the requested row ID. If
        // ":id" isn't a number, Spark will reply with a status 500 Internal
        // Server Error. Otherwise, we have an integer, and the only possible
        // error is that it doesn't correspond to a row with data.
        Spark.get("/item?:id", (request, response) -> {
            int idx = Integer.parseInt(request.params(":id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            ItemData select = database.selectOneItem(idx);
            if (select == null) {
                String errorMessage = "The specified id" + idx + "does not exist in the database";
                return gson.toJson(new StructuredResponse("error", errorMessage, database.selectOneItem(idx)));
            }
            return gson.toJson(new StructuredResponse("ok", null, database.selectOneItem(idx)));
        });

        // POST route for adding a new element to the database. This will read
        // JSON from the body of the request, turn it into a SimpleRequest
        // object, extract the title and message, insert them, and return the
        // ID of the newly created row.

        // example:/items/category?tag=furniture-school-new
        // Spark.get("/items/category", "application/json", (request, response) -> {
        // // NB: if gson.Json fails, Spark will reply with status 500 Internal
        // // Server Error'
        // // ensure status 200 OK, with a MIME type of JSON
        // // NB: even on error, we return 200, but with a JSON object that
        // // describes the error.
        // response.status(200);
        // response.type("application/json");
        // String rawCategory = request.queryParams("tag");
        // String[] categoryArray = getStringArrayFromText(rawCategory);
        // return gson.toJson(new StructuredResponse("error", "elasticsearch
        // implementation incomplete", null));
        // });
    }

    /**
     * For Heroku's purpose
     *
     * Get an integer environment varible if it exists, and otherwise return the
     * default value.
     * 
     * @envar The name of the environment variable to get.
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

    static String[] getStringArrayFromText(String text) {
        String[] categories = text.split("-");
        return categories;
    }
}