package edu.lehigh.cse280.swap.app;

import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// Import Google's JSON library
import com.google.gson.*;
import java.util.Collections;
// Import Elasticsearch java client
import org.apache.http.*;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.*;
import org.elasticsearch.client.*;
import org.elasticsearch.action.get.*;
import spark.Spark;
import edu.lehigh.cse280.swap.database.Database;
import edu.lehigh.cse280.swap.database.ItemData;

import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.auth.*;

/**
 * For now, our app creates an HTTP server that can only get and add data.
 */
public class App {
    public static void main(String[] args) {
        // we need to call it before we do anything else with Spark.
        // Our server runs on port 4567. That's the Java Spark default
        Map<String, String> env = System.getenv();
        String db_url = env.get("DATABASE_URL");

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
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", "cnHyGTUqnZMYpu7saERBAbsV"));

        RestClientBuilder builder = RestClient
                .builder(new HttpHost("44ab9c1bc5cd465d8279ad2f1dc03e8a.us-east-1.aws.found.io", 9243, "http"))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });

        RestHighLevelClient client = new RestHighLevelClient(builder);

        // Map<String, Object> jsonMap = new HashMap<>();
        // jsonMap.put("user", "Sheldon");
        // jsonMap.put("postDate", 20190416);
        // jsonMap.put("title", "Ferrari 488");
        // jsonMap.put("price", 200000.0);
        // IndexRequest indexRequest = new IndexRequest("posts", "doc",
        // "1").source(jsonMap);
        GetRequest getRequest = new GetRequest("item", "1");
        ActionListener listener = new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                System.out.println("Post Sheldon's Ferrari 488 successfully");
            }

            @Override
            public void onFailure(Exception e) {
                if (e instanceof IOException) {
                    System.out.print(
                            "Either failing to parse the REST response in the high-level REST client, the request times out or similar cases where there is no response coming back from the server");
                }
            }
        };
        try {
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            String index = getResponse.getIndex();
            String id = getResponse.getId();
            if (getResponse.isExists()) {
                long version = getResponse.getVersion();
                String sourceAsString = getResponse.getSourceAsString();
                Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
                byte[] sourceAsBytes = getResponse.getSourceAsBytes();
                System.out.println("version: " + version + "\ndocument: " + sourceAsString);
            } else {

            }
        } catch (IOException e) {
            System.out.println("fucking error from getResponse: " + e.getMessage());
        }
        // try {
        // Response response = client.getLowLevelClient().performRequest("GET",
        // "/blog/_search");
        // } catch (IOException e) {
        // System.out.println("fucking error from low level client:" + e.getMessage());
        // }
        // Collections.<String, String>emptyMap());
        // client.indexAsync(indexRequest, RequestOptions.DEFAULT, listener);
        // client.getAsync(getRequest, RequestOptions.DEFAULT, listener);
        try {
            client.close();
        } catch (IOException e) {
            System.out.println("fucking error from closing the client:" + e.getMessage());
        }
        final Database database = Database.getDatabase(db_url);
        if (database == null)
            System.out.println("database object is null");

        String static_location_override = System.getenv("STATIC_LOCATION");

        // hardcode data entries
        database.createAllTables();

        // int categories = 0;
        // Sheldon:1 Lixuan:2 Allen:3 Xiaowei:4
        database.insertNewItem(1, "logitech mouse", "brand new", 3, 20190410);
        database.insertNewItem(1, "Ferrari 488", "brand new", 0, 20190328);
        database.insertNewItem(4, "GTX 1060", "near broken", 3, 20190221);
        database.insertNewItem(4, "Econ001 textbook", "half new", 1, 20190407);
        database.insertNewItem(2, "Coolermaster keyboard", "brand new", 3, 20190318);
        database.insertNewItem(2, "Desktop", "80% new", 3, 20190112);
        database.insertNewItem(3, "Rangerover Sport", "half new", 2, 20190409);
        database.insertNewItem(3, "Microfridge", "brand new", 2, 20190405);

        // ArrayList<ItemData> selectAll = new ArrayList<ItemData>();
        // selectAll.add(new ItemData(1, "logitech mouse", "brand new", "Sheldon",
        // 10.0f, categories));
        // selectAll.add(new ItemData(2, "Ferrari 488", "brand new", "Sheldon",
        // 200000.0f, categories));
        // selectAll.add(new ItemData(3, "GTX 1060", "near broken", "Xiaowei", 15.0f,
        // categories));
        // selectAll.add(new ItemData(4, "Econ001 textbook", "half new", "Xiaowei",
        // 30.0f, categories));
        // selectAll.add(new ItemData(5, "Coolermaster keyboard", "brand new", "Lixuan
        // Qiu", 35.0f, categories));
        // selectAll.add(new ItemData(6, "Desktop", "80% new", "Lixuan Qiu", 25.0f,
        // categories));
        // selectAll.add(new ItemData(7, "Range Rover Sport", "half new", "Allen",
        // 45000.0f, categories));
        // selectAll.add(new ItemData(8, "Microfridge", "brand new", "Allen", 30.0f,
        // categories));
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

        Spark.delete("/item?:id", (request, response) -> {
            int idx = Integer.parseInt(request.params(":id"));
            response.status(200);
            response.type("application/json");
            int res = database.deleteItem(idx);
            if (res == -1)
                return gson.toJson(new StructuredResponse("error", "Delete item: " + idx + "failed", null));
            return gson.toJson(new StructuredResponse("ok", null, res));
        });

        // GET route that returns all message titles and Ids. All we do is get
        // the data, embed it in a StructuredResponse, turn it into JSON, and
        // return it. If there's no data, we return "[]", so there's no need
        // for error handling.
        Spark.get("/item/all", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            ArrayList<ItemData> selectAll = database.selectAllItems();
            if (selectAll == null)
                return gson.toJson(new StructuredResponse("error", "Get all item returns null", null));
            return gson.toJson(new StructuredResponse("ok", null, selectAll));
        });

        // GET route that returns corresponding item list according to the parameters
        // specified in the url
        // An example would be /item?category=furniture-school&user_id=2
        Spark.get("/item", (request, response) -> {
            response.status(200);
            response.type("application/json");
            String unParsedCategory = request.queryParams("category");
            int cat = 0;

            if (unParsedCategory.equals("Car"))
                cat = 0;
            else if (unParsedCategory.equals("School"))
                cat = 1;
            else if (unParsedCategory.equals("Furniture"))
                cat = 2;
            else if (unParsedCategory.equals("Electronics"))
                cat = 3;
            ArrayList<Integer> catList = new ArrayList<>();
            catList.add(cat);
            ArrayList<ItemData> selectAll = database.selectAllItemsFrom(catList);
            if (selectAll == null)
                return gson.toJson(new StructuredResponse("error", "Get items from categorys fails", null));
            return gson.toJson(new StructuredResponse("ok", null, selectAll));
            // if (unParsedCategory) {

            // }

            // String unParsedItemId = request.queryParams("item_id");
            // if (unParsedItemId) {

            // }

            // String unParsedUserId = request.queryParams("user_id");
            // if (unParsedUserId) {

            // }
        });

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

        // POST route that allows for web to post a new item and insert it to the table
        // current only supports insertion for itemData table
        // with field seller, title, description, category, and postDate
        // int string string int int
        Spark.post("/item?new", (request, response) -> {
            int unParsedSellerId = Integer.parseInt(request.queryParams("seller"));
            String unParsedTitle = request.queryParams("title");
            String unParsedDescription = request.queryParams("description");
            int unParsedCategory = Integer.parseInt(request.queryParams("category"));
            int unParsedPostDate = Integer.parseInt(request.queryParams("postDate"));

            response.status(200);
            response.type("application/json");
            int res = database.insertNewItem(unParsedSellerId, unParsedTitle, unParsedDescription, unParsedCategory,
                    unParsedPostDate);
            if (res < 0) {
                String errorMessage = "fail to insert new item";
                return gson.toJson(new StructuredResponse("error", errorMessage, null));
            }
            return gson.toJson(new StructuredResponse("ok", null, res));
        });

        Spark.post("/item?new", (request, response) -> {
            int unParsedSellerId = Integer.parseInt(request.queryParams("seller"));
            String unParsedTitle = request.queryParams("title");
            String unParsedDescription = request.queryParams("description");
            int unParsedCategory = Integer.parseInt(request.queryParams("category"));
            int unParsedPostDate = Integer.parseInt(request.queryParams("postDate"));

            response.status(200);
            response.type("application/json");
            int res = database.insertNewItem(unParsedSellerId, unParsedTitle, unParsedDescription, unParsedCategory,
                    unParsedPostDate);
            if (res < 0) {
                String errorMessage = "fail to insert new item";
                return gson.toJson(new StructuredResponse("error", errorMessage, null));
            }
            return gson.toJson(new StructuredResponse("ok", null, res));
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