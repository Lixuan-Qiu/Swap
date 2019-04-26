//change selectAllItemsFrom 
//change all field in itemdata to public

package edu.lehigh.cse280.swap.app;

//import java.awt.event.*;
import java.io.*;
//import java.sql.*;
import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
import java.util.Map;
import java.util.Set;
// Import Google's JSON library
import com.google.gson.Gson;
//import java.util.Collections;
// Import Elasticsearch java client
import org.apache.http.HttpHost;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.*;
import org.elasticsearch.client.*;
import org.elasticsearch.action.get.*;
import spark.Spark;
import edu.lehigh.cse280.swap.database.Database;
import edu.lehigh.cse280.swap.database.ItemData;
import java.io.FileReader;

import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.auth.*;
import java.net.URL;

//docker run -p5432:5432 --name test -e POSTGRES_PASSWORD=test -e POSTGRES_USER=test -d postgres
//POSTGRES_IP=127.0.0.1 POSTGRES_PORT=5432 POSTGRES_USER=test POSTGRES_PASS=test mvn package
/**
 * For now, our app creates an HTTP server that can only get and add data.
 */
public class App {
    // parameters for trading method
    private static final int sell = 1;
    private static final int trade = 2;
    private static final int rent = 3;
    private static final int giveaway = 4;

    // parameters for categories
    private static final int car = 1;
    private static final int school = 2;
    private static final int electronic = 3;
    private static final int furniture = 4;
    private static final String esEndPointUrl = "44ab9c1bc5cd465d8279ad2f1dc03e8a.us-east-1.aws.found.io";
    private static final String protocol = "https";
    private static final int port = 9243;
    private static final String username = "elastic";
    private static final String password = "cnHyGTUqnZMYpu7saERBAbsV";

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

        // RestHighLevelClient client = buildElasticsearchClient();
        // GetRequest getRequest = new GetRequest("item", "1");
        // try {
        // GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        // String index = getResponse.getIndex();
        // String id = getResponse.getId();
        // if (getResponse.isExists()) {
        // long version = getResponse.getVersion();
        // String sourceAsString = getResponse.getSourceAsString();
        // Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
        // byte[] sourceAsBytes = getResponse.getSourceAsBytes();
        // System.out.println("version: " + version + "\ndocument: " + sourceAsString +
        // "\nindex: " + index);
        // } else {

        // }
        // } catch (IOException e) {
        // System.out.println("fucking error from getResponse: " + e.getMessage());
        // e.printStackTrace();
        // }

        final Database database = Database.getDatabase(db_url);
        if (database == null)
            System.out.println("database object is null");

        String static_location_override = System.getenv("STATIC_LOCATION");

        // hardcode data entries
        database.dropAllTables();

        database.createAllTables();
        // bulkInsertFromLocalFile(database, "sample.txt");
        // int categories = 0;
        // Sheldon:1 Lixuan:2 Allen:3 Xiaowei:4
        database.insertNewItem(1, "logitech mouse", "brand new", electronic, 20190410, sell, 40f, true, " days", "");
        database.insertNewItem(1, "Ferrari 488", "brand new", car, 20190328, giveaway, 0f, false,
                "this should not be shown", "");
        database.insertNewItem(4, "GTX 1060", "near broken", electronic, 20190221, trade, 0f, true, "10 days", "");
        database.insertNewItem(4, "Econ001 textbook", "half new", school, 20190407, sell, 20f, true, "in this semester",
                "");
        database.insertNewItem(2, "Coolermaster keyboard", "brand new", electronic, 20190318, trade, 0f, false,
                "this should not be shown", "");
        database.insertNewItem(2, "Desktop", "80% new", furniture, 20190112, giveaway, 0f, true, "2 months", "");
        database.insertNewItem(3, "Rangerover Sport", "half new", car, 20190409, rent, 100f, false, "a month", "");
        database.insertNewItem(3, "Microfridge", "brand new", furniture, 20190405, trade, 0f, true, "one year", "");

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

        // DELETE route for deleting an item that is already in database
        Spark.delete("/item", (request, response) -> {
            response.status(200);
            response.type("application/json");
            int idx;
            try {
                idx = Integer.parseInt(request.queryParams("id"));
            } catch (NumberFormatException e) {
                idx = -1;
            }
            if (idx == -1)
                return gson.toJson(
                        new StructuredResponse("error", "id in json object cannot be parsed into integer", null));
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

        // PUT route that modifies infomation of an item that is already in database
        Spark.put("/item", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            int id = Integer.parseInt(request.queryParams("id"));
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
            // https://self-learning-java-tutorial.blogspot.com/2015/01/spark-java-request-parameter.html
            Set<String> queryParams = request.queryParams();
            // indicate the category to query, if no category in url, flag is -1
            ArrayList<Integer> categories = new ArrayList<Integer>(); // Create an ArrayList object
            // indicate the price order to query, asc is 1, dsc is 2, no price order is -1
            boolean catFlag = false;
            int priceFlag = -1;
            for (String param : queryParams) {
                // aaaaa;
                if (param.equals("pricerank")) {
                    String order = request.queryParams("pricerank");
                    if (order.equals("asc"))
                        priceFlag = 1;
                    else if (order.equals("dsc"))
                        priceFlag = 2;
                    else
                        return gson.toJson(new StructuredResponse("error", "Get by price: "
                                + request.queryParams("pricerank") + " should be either 'asc' or 'dsc'", null));
                }
                if (param.equals("category")) {
                    catFlag = true;
                    String text = request.queryParams("category");
                    String unParsedCategory[] = getStringArrayFromText(text);
                    for (int j = 0; j < unParsedCategory.length; j++) {
                        int cat = convertCategoryToInt(unParsedCategory[j]);
                        if (cat == -1)
                            return gson.toJson(new StructuredResponse("error", "Get by Category: "
                                    + request.queryParams("category") + " is not an integer or is not one of '1,2,3,4'",
                                    null));
                        categories.add(cat);
                    }
                }
                if (param.equals("id")) {
                    int idx;
                    try {
                        idx = Integer.parseInt(request.queryParams("id"));
                    } catch (NumberFormatException e) {
                        return gson.toJson(new StructuredResponse("error",
                                "Get by Id: " + request.queryParams("id") + " cannot be parsed into int", null));
                    }
                    System.out.println("GET /item by id: calls selectOneItem");
                    ItemData select = database.selectOneItem(idx);
                    if (select == null) {
                        String errorMessage = "GET by ID: The specified id" + idx + "does not exist in the database";
                        return gson.toJson(new StructuredResponse("error", errorMessage, null));
                    }
                    response.redirect("item.html");
                    return gson.toJson(new StructuredResponse("ok", null, select));
                }
                if (param.equals("search")) {
                    String keyword = request.queryParams("search");

                }
            }
            // ffff;
            ArrayList<ItemData> result;
            // when the url has query request about category
            if (catFlag) {
                System.out.println("GET /item by category: calls selectAllItesFromCategory");
                result = database.selectAllItemsFromCategory(categories);
                if (result == null)
                    return gson.toJson(new StructuredResponse("error", "GET /item by category returns null", null));
                // when the url has query request about price order
                if (priceFlag != -1) {
                    // reorder the items in result by the requesting order
                    if (priceFlag == 1) {
                        result = sortByAsc(result);
                        if (result == null)
                            return gson.toJson(new StructuredResponse("error",
                                    "GET /item by category after sort asc returns null", null));
                    } else if (priceFlag == 2) {
                        result = sortByDsc(result);
                        if (result == null)
                            return gson.toJson(new StructuredResponse("error",
                                    "GET /item by category after sort dsc returns null", null));
                    }
                }
                return gson.toJson(new StructuredResponse("ok", null, result));
            }
            // when the url only has a query request of sort by price, so do a get all then
            // sort by price
            else if (priceFlag != -1) {
                System.out.println("GET /item without category: calls selectAllItems");
                result = database.selectAllItems();
                if (result == null)
                    return gson.toJson(new StructuredResponse("error",
                            "GET /item sort by price, selectAllItems returns null", null));
                if (priceFlag == 1)
                    result = sortByAsc(result);
                else if (priceFlag == 2)
                    result = sortByDsc(result);
                if (result == null)
                    return gson.toJson(new StructuredResponse("error", "GET /item sort by price returns null", null));
                return gson.toJson(new StructuredResponse("ok", null, result));
            } else {
                response.status(404);
                return gson.toJson(new StructuredResponse("error",
                        "GET /item: The url has to contain id, category, or price. No such parameters were found",
                        null));
            }
        });

        // POST route that allows for web to post a new item and insert it to the table
        // current only supports insertion for itemData table
        // with field seller, title, description, category, and postDate
        // int string string int int
        Spark.post("/item/new", (request, response) -> {
            response.status(200);
            response.type("application/json");
            ItemData req = gson.fromJson(request.body(), ItemData.class);
            int id = req.itemId;
            String title = req.itemTitle;
            String description = req.itemDescription;
            int category = req.itemCategory;
            int date = req.itemPostDate;
            int method = req.itemTradeMethod;
            float price = req.itemPrice;
            boolean available = req.itemAvailability;
            String availableTime = req.itemAvailableTime;
            String wanted = req.itemWantedItemDescription;

            int res = database.insertNewItem(id, title, description, category, date, method, price, available,
                    availableTime, wanted);
            if (res < 0) {
                String errorMessage = "fail to insert new item";
                return gson.toJson(new StructuredResponse("error", errorMessage, null));
            }
            return gson.toJson(new StructuredResponse("ok", null, res));
        });

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    /**
     * This functions access to the filename.txt in the same folder as App.java, and
     * read itemData from the text file and insert them into database
     * 
     * @param db       the database instance we need to populate
     * @param filename the name of the file where we stored all our sample data
     */
    private static void bulkInsertFromLocalTxt(Database db, String filename) {
        URL path = App.class.getResource(filename);
        File f = new File(path.getFile());
        BufferedReader reader;
        String line;
        int i = 0;
        try {
            reader = new BufferedReader(new FileReader(f));
            try {
                while ((line = reader.readLine()) != null) {
                    String[] columns = line.split(",");
                    int userId = Integer.parseInt(columns[0]);
                    String title = columns[1];
                    String description = columns[2];
                    int category = convertCategoryToInt(columns[3]);
                    int date = Integer.parseInt(columns[4]);
                    int trademethod = convertMethodToInt(columns[5]);
                    float price = Float.parseFloat(columns[6]);
                    boolean av;
                    if (columns[7] == "true")
                        av = true;
                    else {
                        av = false;
                    }
                    String avTime = columns[7];
                    String itemWant = columns[8];
                    int id = db.insertNewItem(userId, title, description, category, date, trademethod, price, av,
                            avTime, itemWant);
                    if (id <= 0)
                        System.out.println("Failed to parse text in line " + i + ", insertion failed");
                    i++;
                }
            } catch (IOException e) {
                System.out.println("Error reading file");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found when doing bulk insert");
        }

    }

    /**
     * Helper method to convert an trade method string into the corresponding int in
     * the database
     * 
     * @param text String that contains the trade method
     * @return the int that respresents the input trade method in the database
     */
    private static int convertMethodToInt(String text) {
        if (text.equals("sell"))
            return 1;
        else if (text.equals("trade"))
            return 2;
        else if (text.equals("rent"))
            return 3;
        else if (text.equals("giveaway"))
            return 4;
        else
            return -1;
    }

    /**
     * Split the input into string array using delimiter "-"
     * 
     * @param text Unparsed string with different elements connected by a single "-"
     * @return String array that contains each element
     */
    private static String[] getStringArrayFromText(String text) {
        String[] list = text.split("-");
        return list;
    }

    /**
     * Helper method to convert an category string into the corresponding int in the
     * database
     * 
     * @param text String that contains the category
     * @return the int that respresents the input category in the database
     */
    private static int convertCategoryToInt(String text) {
        if (text.equals("car"))
            return 1;
        else if (text.equals("school"))
            return 2;
        else if (text.equals("electronics"))
            return 3;
        else if (text.equals("furniture"))
            return 4;
        else
            return -1;
    }

    /**
     * Very inefficient insertion sort helper method. However, since we will only
     * deal with small dataset in this stage we will be fine. Future sorting will be
     * handled by Elasticsearch
     * 
     * @param list the ItemData list we want to sort in descending order
     * @return the ItemData in descending order
     */
    private static ArrayList<ItemData> sortByDsc(ArrayList<ItemData> list) {
        int i, j;
        ItemData key;
        for (i = 1; i < list.size(); i++) {
            key = list.get(i);
            j = i - 1;
            /*
             * Move elements of arr[0..i-1], that are greater than key, to one position
             * ahead of their current position
             */
            while (j >= 0 && list.get(j).itemPrice < key.itemPrice) {
                list.set(j + 1, list.get(j));
                j = j - 1;
            }
            list.set(j + 1, key);
        }
        return list;
    }

    /**
     * Very inefficient insertion sort helper method. However, since we will only
     * deal with small dataset in this stage we will be fine. Future sorting will be
     * handled by Elasticsearch
     * 
     * @param list the ItemData list we want to sort in ascending order
     * @return the ItemData in descending order
     */
    private static ArrayList<ItemData> sortByAsc(ArrayList<ItemData> list) {
        int i, j;
        ItemData key;
        for (i = 1; i < list.size(); i++) {
            key = list.get(i);
            j = i - 1;
            /*
             * Move elements of arr[0..i-1], that are greater than key, to one position
             * ahead of their current position
             */
            while (j >= 0 && list.get(j).itemPrice > key.itemPrice) {
                list.set(j + 1, list.get(j));
                j = j - 1;
            }
            list.set(j + 1, key);
        }
        return list;
    }

    private static RestHighLevelClient buildElasticsearchClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        RestClientBuilder builder = RestClient.builder(new HttpHost(esEndPointUrl, port, protocol))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });

        return new RestHighLevelClient(builder);
    }
}