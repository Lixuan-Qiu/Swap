//change selectAllItemsFrom 
//change all field in itemdata to public

package edu.lehigh.cse280.swap.app;

import java.util.HashMap;
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
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.*;
import org.elasticsearch.client.*;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.apache.lucene.search.TotalHits;

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

        //
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
        RestHighLevelClient bigClient = buildElasticsearchClient();
        // esInsert(database, bigClient, userId, title, description, categories,
        // postDate, tradeMethod, price,
        // availability, availableTime, wantedItemDescription);
        esInsert(database, bigClient, 1, "desk", "Beautiful weathered dark Gray Finish 48 Wide x 30 high x 24 deep", 4,
                20190425, 1, 20, true, "", "");
        esInsert(database, bigClient, 1, "Ferrari 488",
                "The Ferrari 488 Spider is the latest chapter in Maranello’s ongoing history of open-top V8 sports cars",
                1, 20190202, 1, 200000, true, "", "");
        esInsert(database, bigClient, 1, "Range rover sport",
                "The 2019 Range Rover Sport is both luxurious and exceptionally functional. Explore the most dynamic Range Rover Sport yet, and customize yours today.",
                1, 20190101, 1, 60000, true, "", "");
        esInsert(database, bigClient, 1, "Calc 001 textbook",
                "Chapters: 1: Introduction to Calculus, 2: Derivatives, 3: Applications of the Derivative, 4: The Chain ... ",
                2, 20190729, 1, 10.7f, true, "", "");
        esInsert(database, bigClient, 1, "Coffee Maker",
                "Brews three different cup sizes (6, 8 and 10 ounce). Removable 48-ounce water reservoir offers easy filling and cleaning.",
                3, 20190329, 1, 40, true, "", "");
        esInsert(database, bigClient, 1, "Lenovo Laptop",
                "Lenovo ThinkPad X1 Carbon (6th Gen) Though it's buillt for business, the ThinkPad X1 Carbon (6th Gen) is a great choice for anyone who needs to be productive on the go",
                3, 20180927, 2, 0, true, "", "");
        esInsert(database, bigClient, 1, "Logitech Gaming Mouse", "Logitech 910-005527 MX Master RTL Wireless Mouse", 3,
                20190127, 2, 0, true, "", "");
        esInsert(database, bigClient, 1, "Casio watch", "Casio F91w-1 Classic Resin Strap Digital Sport Watch - Black",
                3, 20180803, 2, 0, true, "", "");
        esInsert(database, bigClient, 1, "Macbook Pro", "Apple MacBook Pro with Retina display 13.3", 3, 20190101, 2, 0,
                true, "", "");
        esInsert(database, bigClient, 1, "Ipad Mini", "Apple iPad mini 5 - 64 GB - Space Gray - Wi-Fi", 3, 20190223, 2,
                0, true, "", "");
        esInsert(database, bigClient, 1, "Nvidia 1060",
                "EVGA GeForce GTX 1060 0 SC Gaming Graphics Card - 3 GB GDDR5 - 192-bit - 1607 MHz", 3, 20190207, 3, 10,
                true, "", "");
        esInsert(database, bigClient, 1, "Chair",
                "Langley Street Altigarron Swivel Side Chair Upholstery: Black, Finish: Light Grey", 4, 20180303, 3, 5,
                true, "", "");
        esInsert(database, bigClient, 1, "Mini Fridge", "Sunbeam 1.7 cu ft Mini Refrigerator - Black REFSB17B", 4,
                20190301, 3, 10.5f, true, "", "");
        esInsert(database, bigClient, 1, "Keyboard",
                "Cooler Master MasterKeys MK750 USB Mechanical Keyboard - Gunmetal Black", 3, 20190302, 3, 2, true, "",
                "");
        esInsert(database, bigClient, 1, "Foam Mattress", "Modern Sleep Cool Gel Memory Foam Mattress, White, Twin", 4,
                20180221, 3, 4, true, "", "");
        esInsert(database, bigClient, 1, "iphone6 silicon case",
                "Verizon High Gloss Silicone Case for Apple iPhone 6/6S - Black", 3, 20181212, 4, 0, true, "", "");
        esInsert(database, bigClient, 1, "iphone6", "near broken", 3, 20190101, 4, 0, true, "", "");
        esInsert(database, bigClient, 1, "iphone6 charger",
                "Apple Lightning USB Cable for iPhone 8 / 8 Plus/ 7/ 7 Plus/ 6S/ 6S Plus/ 6/ 6 Plus/ iPad/ iPod (Bulk Packaging)",
                3, 20190123, 4, 0, true, "", "");
        esInsert(database, bigClient, 1, "Econ Textbook",
                "US Edition textbook. Sporadic writing/highlights on some pages in the book. Binding of the book is perfect",
                2, 20180808, 4, 0, true, "", "");
        esInsert(database, bigClient, 1, "Sofa",
                "Sit back and relax with a versatile furniture piece designed for optimal comfort", 4, 20190425, 4, 0,
                true, "", "");
        esInsert(database, bigClient, 3, "Algorithm textbook",
                "Expanding on the first edition, the book now serves as the primary textbook of choice for algorithm design courses while maintaining its status as the premier practical reference guide to algorithms for programmers, researchers, and students.",
                2, 20190411, 1, 15, true, "", "");
        esInsert(database, bigClient, 3, "Drum class music sheet", "Rock Songs for Kids: Drum Play-Along [Book]", 2,
                20180808, 1, 30, true, "", "");
        esInsert(database, bigClient, 3, "Honda Civic 2017",
                "Advanced Brembo Brakes. 20-in Performance Wheels. Intelligent Aerodynamics. 2 Liter 306 HP Engine.", 1,
                20190426, 1, 30000, true, "", "");
        esInsert(database, bigClient, 3, "Water heater",
                "EcoSmart ECO Tankless Water Heater - Electric - 11 kW, installation fee is $99, subject to change based how beautiful you are",
                4, 20190302, 1, 100, true, "", "");
        esInsert(database, bigClient, 3, "Laptop cooler",
                "Dual fan double performance, Easy control on the side, Ergonomic humanity, Hexagon shape mesh for maximize airflow, Dual USB port",
                3, 20180420, 1, 34, true, "", "");
        esInsert(database, bigClient, 3, "R2D2", "Star Wars Smart App Enabled R2-D2 Remote Control Robot RC", 3,
                20190212, 2, 0, true, "", "");
        esInsert(database, bigClient, 3, "Chrome book",
                "Intel Celeron N3060 Dual-core, HD Display, Intel HD Graphics 400, Chrome OS, 11 hr 30 min battery life",
                3, 20190101, 2, 0, true, "", "");
        esInsert(database, bigClient, 3, "Mercedes Benz AMG G63",
                "Flashy exhaust system aside, the G63 has received a complete aftermarket package and an extra set of LED lights on the roof",
                1, 20190420, 2, 0, true, "", "");
        esInsert(database, bigClient, 3, "Asus gaming laptop",
                "Full HD 8th-Gen Intel Core i5-8300H up to 3.9GHz GeForce GTX 1050 2GB 8GB DDR4", 3, 20190320, 2, 0,
                true, "", "");
        esInsert(database, bigClient, 3, "Power bank",
                "Standard USB-A output for smartphones, tablets, headphones, cameras and tons of other devices", 3,
                20180321, 2, 0, true, "", "");
        esInsert(database, bigClient, 3, "Bus002 textbook", "Business Management for the IB Diploma Coursebook [Book]",
                2, 20190201, 3, 3, true, "", "");
        esInsert(database, bigClient, 3, "Pan", "Farbeware Reliance Aluminum Nonstick 3-Piece Fry Pan Set, Red", 4,
                20190302, 3, 1, true, "", "");
        esInsert(database, bigClient, 3, "iRobot create",
                "Dirt Detect Sensor, Cleans carpets and hard floors, iRobot HOME App lets you clean and schedule conveniently, Works with Amazon Alexa and the Google Assistant",
                3, 20190212, 3, 10, true, "", "");
        esInsert(database, bigClient, 3, "Space Shuttle",
                "The Space Shuttle was a partially reusable low Earth orbital spacecraft system operated by the U.S. National Aeronautics and Space Administration (NASA) as part of the Space Shuttle program.",
                1, 20190426, 3, 1000000, true, "", "");
        esInsert(database, bigClient, 3, "Study board",
                "WallPops! Sandy Wall Calendar Wall Mounted Dry Erase Board, Gold", 4, 20190107, 3, 5, true, "", "");
        esInsert(database, bigClient, 3, "notebook",
                "Mead Products 1-Subject Wide Rule Spiral Bound Notebook, Assorted Colors", 2, 20190203, 4, 0, true, "",
                "");
        esInsert(database, bigClient, 3, "Dxracer chair",
                "DXRacer GC-R90-INW-Z1 Seat Racing R90 Playstation - Black/Violet", 4, 20190304, 4, 0, true, "", "");
        esInsert(database, bigClient, 3, "Nintendo Switch", "Nintendo Switch with Joy-Con - 32 GB - Neon Blue/Neon Red",
                3, 20181231, 4, 0, true, "", "");
        esInsert(database, bigClient, 3, "Pen Used",
                "Pilot G2 Retractable Gel Rolling Ball Pens, Extra Fine Point, Black Ink - 5 pack", 2, 20190101, 4, 0,
                true, "", "");
        esInsert(database, bigClient, 3, "Ikea bookshelf",
                "(High Gloss White) - Ikea Kallax Bookcase Shelving Unit Display High Gloss white Shelf", 4, 20190222,
                4, 0, true, "", "");

        esInsert(database, bigClient, 2, "iPhone XR",
                "Apple iPhone XR (64GB) - (PRODUCT)RED [works exclusively with Simple Mobile]", 3, 20181124, 1, 648.88f,
                true, "", "");
        esInsert(database, bigClient, 2, "Linear Algebra Book", "Introduction to Linear Algebra, Fifth Edition", 2,
                20181125, 4, 0, true, "", "");
        esInsert(database, bigClient, 2, "Pencil", "unused pencils, free to give away", 2, 20180518, 4, 0, true, "",
                "");
        esInsert(database, bigClient, 2, "Machine Learning Book",
                "Machine Learning For Absolute Beginners: A Plain English Introduction (Machine Learning For Beginners)",
                2, 20170510, 3, 20, true, "", "");
        esInsert(database, bigClient, 2, "Math205 Book",
                "Introduction to Applied Linear Algebra: Vectors, Matrices, and Least Squares", 2, 20190323, 1, 50,
                true, "", "");
        esInsert(database, bigClient, 2, "Finance Book", "Bad Blood: Secrets and Lies in a Silicon Valley Startup", 2,
                20190224, 1, 100, true, "", "");
        esInsert(database, bigClient, 2, "Fin001 Book",
                "The Intelligent Investor: The Definitive Book on Value Investing. A Book of Practical Counsel (Revised Edition) (Collins Business Essentials) ",
                2, 20190323, 4, 0, true, "", "");
        esInsert(database, bigClient, 2, "Finance & Accounting for Nonfinancial Managers",
                "Fanatical Prospecting: The Ultimate Guide to Opening Sales Conversations and Filling the Pipeline by Leveraging Social Selling, Telephone, Email, Text, and Cold Calling",
                2, 20190323, 4, 0, true, "", "");
        esInsert(database, bigClient, 2, "Real Estate Book",
                "The Book on Rental Property Investing: How to Create Wealth and Passive Income Through Smart Buy & Hold Real Estate Investing",
                2, 20190403, 1, 98.88f, true, "", "");
        esInsert(database, bigClient, 2, "Bus005 textbook",
                "The Millionaire Real Estate Agent: It's Not About the Money...It's About Being the Best You Can Be!",
                2, 20190403, 1, 88.88f, true, "", "");
        esInsert(database, bigClient, 2, "Bus010 textbook",
                "The Book on Rental Property Investing: How to Create Wealth and Passive Income Through Smart Buy & Hold Real Estate Investing",
                2, 20190403, 1, 98.88f, true, "", "");
        esInsert(database, bigClient, 2, "Bus100 textbook",
                "The Book on Managing Rental Properties: A Proven System for Finding, Screening, and Managing Tenants with Fewer Headaches and Maximum Profits",
                2, 20190403, 4, 0, true, "", "");
        esInsert(database, bigClient, 2, "Eraser", "Used for three years", 2, 20180103, 4, 0, true, "", "");
        esInsert(database, bigClient, 2, "Pencil case", "used pencil case but rather new", 2, 20180603, 1, 6.99f, true,
                "", "");
        esInsert(database, bigClient, 2, "Pencil case again", "new new new, new pencil case", 2, 20180326, 1, 10.77f,
                true, "", "");
        esInsert(database, bigClient, 2, "Pen", "pen with no ink", 2, 20171103, 3, 1, true, "", "");
        esInsert(database, bigClient, 2, "Pen", "pen with ink", 2, 2016513, 1, 30, true, "", "");
        esInsert(database, bigClient, 2, "C3PO", "feel free to dissemble it", 3, 20170707, 3, 300, true, "", "");
        esInsert(database, bigClient, 2, "C3P0 version 2.0", "please don't dissemble it", 3, 20181203, 3, 200, true, "",
                "");
        esInsert(database, bigClient, 2, "R2D2",
                "C3PO's friend, if you want to rent them together, you can get discount", 3, 20171203, 3, 199, true, "",
                "");
        try {
            bigClient.close();
        } catch (IOException e) {
            System.out.println("error when trying to close Elasticsearch");
            e.printStackTrace();
        }
        if (static_location_override == null) {
            Spark.staticFileLocation("/web");
        } else {
            Spark.staticFiles.externalLocation(static_location_override);
        }

        Spark.get("/", (request, response) -> {
            response.redirect("index.html");
            return "";
        });

        Spark.get("/login", (request, response) -> {
            response.redirect("itemList.html");
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

            response.redirect("itemList.html");

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

                    String keyword = request.queryParams("search").toLowerCase();
                    ArrayList<ItemData> result = new ArrayList<>();

                    RestHighLevelClient client = buildElasticsearchClient();
                    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
                    QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", keyword)
                            .fuzziness(Fuzziness.AUTO).prefixLength(3).maxExpansions(50);
                    sourceBuilder.query(matchQueryBuilder);

                    // sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
                    SearchRequest searchRequest = new SearchRequest();
                    searchRequest.indices("item");
                    searchRequest.source(sourceBuilder);
                    try {
                        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
                        SearchHits hits = searchResponse.getHits();
                        System.out.println("Entered search in title");
                        int totalShards = searchResponse.getTotalShards();
                        System.out.println("Total Shards: " + totalShards);
                        SearchHit[] searchHits = hits.getHits();
                        TotalHits totalHits = hits.getTotalHits();
                        // the total number of hits, must be interpreted in the context of
                        // totalHits.relation
                        long numHits = totalHits.value;
                        // whether the number of hits is accurate (EQUAL_TO) or a lower bound of the
                        // total (GREATER_THAN_OR_EQUAL_TO)
                        TotalHits.Relation relation = totalHits.relation;
                        System.out.println("Searching: " + keyword + " from title, total hits: " + numHits);
                        for (SearchHit hit : searchHits) {
                            System.out.println("entered title search hits");
                            String sourceAsString = hit.getSourceAsString();
                            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                            String documentTitle = (String) sourceAsMap.get("title");
                            int pKey = (int) sourceAsMap.get("key");

                            result.add(database.selectOneItem(pKey));
                            // List<Object> users = (List<Object>) sourceAsMap.get("user");
                            // Map<String, Object> innerObject = (Map<String, Object>)
                            // sourceAsMap.get("innerObject");
                            System.out.println("\ndocument: " + sourceAsString + "\ntitle: " + documentTitle);
                        }
                        client.close();
                        return gson.toJson(new StructuredResponse("ok", null, result));
                    } catch (IOException e) {
                        System.out.println("fucking error from getResponse: " + e.getMessage());
                        e.printStackTrace();
                    }
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
            int id = req.itemSeller;
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
            RestHighLevelClient client = buildElasticsearchClient();

            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("key", res);
            jsonMap.put("user", id);
            jsonMap.put("title", title.toLowerCase());
            jsonMap.put("description", description.toLowerCase());
            jsonMap.put("category", convertIntToCategory(category));
            jsonMap.put("postDate", date);
            jsonMap.put("price", price);
            IndexRequest indexRequest = new IndexRequest("item").id(Integer.toString(res)).source(jsonMap);
            try {
                IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
                String rid = indexResponse.getId();
                if (Integer.parseInt(rid) != res)
                    return gson.toJson(
                            new StructuredResponse("error", "Error from Inserting new item to Elasticsearch", null));
                client.close();
            } catch (IOException e) {
                System.out.println("Error from Inserting new item to Elasticsearch: " + e.getMessage());
                e.printStackTrace();
            }
            return gson.toJson(new StructuredResponse("ok", null, res));
        });

        // try {
        // client.close();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
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

    private static String convertIntToCategory(int cat) {
        switch (cat) {
        case 1:
            return "car";
        case 2:
            return "school";
        case 3:
            return "electronics";
        case 4:
            return "furniture";
        default:
            return "";
        }
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

    // Function to remove duplicates from an ArrayList
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {

        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
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

    private static void esInsert(Database db, RestHighLevelClient client, int userId, String title, String description,
            int categories, int postDate, int tradeMethod, float price, boolean availability, String availableTime,
            String wantedItemDescription) {
        int idx = db.insertNewItem(userId, title, description, categories, postDate, tradeMethod, price, availability,
                availableTime, wantedItemDescription);
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("key", idx);
        jsonMap.put("user", convertIntToUser(userId));
        jsonMap.put("title", title.toLowerCase());
        jsonMap.put("description", description.toLowerCase());
        jsonMap.put("category", convertIntToCategory(categories));
        jsonMap.put("postDate", postDate);
        jsonMap.put("price", price);
        IndexRequest indexRequest = new IndexRequest("item").id(Integer.toString(idx)).source(jsonMap);
        try {
            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            System.out.println("Error from Inserting new item to Elasticsearch: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String convertIntToUser(int id) {
        switch (id) {
        case 1:
            return "Sheldon";
        case 2:
            return "Xiaowei";
        case 3:
            return "Lixuan";
        case 4:
            return "Allen";
        default:
            return "";
        }
    }
}