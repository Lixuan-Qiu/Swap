package com.swap.backend;

import edu.lehigh.cse280.swap.database.*;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Map;
import java.util.*;
import java.io.IOException;
import java.io.FileNotFoundException;
import junit.framework.TestCase;
import java.net.URL;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.*;
import org.elasticsearch.client.*;
import org.elasticsearch.action.get.*;
import org.elasticsearch.common.util.IntArray;

import java.util.HashMap;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.http.auth.*;
import com.google.gson.Gson;

import spark.Spark;

/**
 * Unit test for simple App.
 */
// docker run -p5432:5432 --name test -e POSTGRES_PASSWORD=test -e
// POSTGRES_USER=test -d postgres
// POSTGRES_IP=127.0.0.1 POSTGRES_PORT=5432 POSTGRES_USER=test
// POSTGRES_PASS=test mvn package
public class AppTest extends TestCase {
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
    Map<String, String> env = System.getenv();
    String ip = env.get("POSTGRES_IP");
    String portNum = env.get("POSTGRES_PORT");
    String user = env.get("POSTGRES_USER");
    String pass = env.get("POSTGRES_PASS");
    Database db = Database.getDatabase(ip, portNum, user, pass);

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    public void testSpark() {
        System.out.println("\nTest Spark");
        System.out.println("--------------------------------------");
        Spark.get("/hello", (request, response) -> {
            return 99;
        });
        String test = "";
        int i = -1;
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL("http://localhost:4567/hello");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            System.out.println("Hmmmm? " + result.toString());
            test = result.toString();
            i = Integer.parseInt(test);
            rd.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("aaaawtf? " + test);
        assert (test.equals("99"));
        System.out.println("wtf? " + i);

        assert (i == 99);
        // assert (false);
    }

    public void testSparkPostItem() {
        db.dropAllTables();
        db.createAllTables();
        System.out.println("\nTest Spark Post Item");
        System.out.println("--------------------------------------");

        Gson gson = new Gson();

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

            int idx = db.insertNewItem(id, title, description, category, date, method, price, available, availableTime,
                    wanted);
            if (idx < 0) {
                String errorMessage = "fail to insert new item";
                return gson.toJson(new StructuredResponse("error", errorMessage, null));
            }
            return gson.toJson(new StructuredResponse("ok", null, idx));
        });

        ItemData item = new ItemData(1, 1, "logitech mouse", "brand new", electronic, 20190410, sell, 40f, true,
                " days", "");
        String postUrl = "http://localhost:4567/item/new";
        String json = "";
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(postUrl);
        try {
            StringEntity postingString = new StringEntity(gson.toJson(item));// gson.tojson() converts your pojo to json
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpResponse res = httpClient.execute(post);
            json = EntityUtils.toString(res.getEntity());
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StructuredResponse p = gson.fromJson(json, StructuredResponse.class);
        Double unparsed = (Double) p.mData;
        int id = unparsed.intValue();

        System.out.println("Response: " + json);
        System.out.println("Parsed int: " + id);
        assertEquals(db.selectOneItem(id).itemTitle, "logitech mouse");
    }

    /**
     * Rigourous Test :-)
     */
    public void testDeleteItem() {
        System.out.println("\nTest Delete");
        System.out.println("--------------------------------------");
        db.dropAllTables();
        db.createAllTables();
        int res;
        ArrayList<ItemData> result = new ArrayList<>();
        int id = db.insertNewItem(1, "logitech mouse", "brand new", electronic, 20190410, sell, 40f, true, " days", "");
        System.out.println("id: " + id);
        res = db.deleteItem(id);
        System.out.println(res);
        assert (db.selectOneItem(id) == null);
        result = db.selectAllItems();
        System.out.println("delete test result size: " + result.size());
        assert (result.size() == 0);
        res = db.deleteItem(99);
        System.out.println("delete 99 return " + res);
        // assert (res == -1);

        int id1 = db.insertNewItem(1, "Apple mouse", "brand new", electronic, 20190410, sell, 40f, true, " days", "");
        int id2 = db.insertNewItem(1, "Ferrari 488", "brand new", car, 20190328, giveaway, 0f, false,
                "this should not be shown", "");
        int id3 = db.insertNewItem(4, "GTX 1060", "near broken", electronic, 20190221, trade, 0f, true, "10 days", "");
        int id4 = db.insertNewItem(4, "Econ001 textbook", "half new", school, 20190407, sell, 20f, true,
                "in this semester", "");
        System.out.println("id1 " + id1);
        System.out.println("id2 " + id2);

        System.out.println("id3 " + id3);
        System.out.println("id4 " + id4);
        res = db.deleteItem(id4);
        System.out.println(res);
        assert (db.selectOneItem(id4) == null);

        result = db.selectAllItems();
        for (ItemData item : result) {
            System.out.println(item.itemTitle);
        }
        System.out.println("delete test result size: " + result.size());
        assert (result.size() == 3);
    }

    /**
     * Rigourous Test :-)
     */
    public void testInsertAndGet() {
        System.out.println("\nTest Insert and Get");
        System.out.println("--------------------------------------");
        db.dropAllTables();
        db.createAllTables();
        int id = db.insertNewItem(1, "logitech mouse", "brand new", electronic, 20190410, sell, 40f, true, " days", "");
        int id1 = db.insertNewItem(1, "Apple mouse", "brand new", electronic, 20190410, sell, 40f, true, " days", "");
        int id2 = db.insertNewItem(1, "Ferrari 488", "brand new", car, 20190328, giveaway, 0f, false,
                "this should not be shown", "");
        int id3 = db.insertNewItem(4, "GTX 1060", "near broken", electronic, 20190221, trade, 0f, true, "10 days", "");
        int id4 = db.insertNewItem(4, "Econ001 textbook", "half new", school, 20190407, sell, 20f, true,
                "in this semester", "");
        ItemData test = db.selectOneItem(id);
        assertEquals(test.itemSeller, 1);
        assertEquals(test.itemDescription, "brand new");
        assertEquals(test.itemCategory, electronic);
        assertEquals(test.itemPostDate, 20190410);
        assertEquals(test.itemTitle, "logitech mouse");
        assertEquals(test.itemTradeMethod, sell);
        assertEquals(test.itemPrice, 40f);
        assertEquals(id1, id + 1);
        assertEquals(id2, id1 + 1);
        assertEquals(id3, id2 + 1);
        assertEquals(id4, id3 + 1);
    }

    public void testSelectByCategory() {
        System.out.println("\nTest Select By Category");
        System.out.println("--------------------------------------");
        db.dropAllTables();
        db.createAllTables();
        populateDatabase(db);
        ArrayList<ItemData> res = new ArrayList<>();
        res = db.selectAllItemsFromCategory(new ArrayList<Integer>(Arrays.asList(electronic)));
        assert (res.size() == 3);
        res = db.selectAllItemsFromCategory(new ArrayList<Integer>(Arrays.asList(school)));
        assert (res.size() == 1);
        res = db.selectAllItemsFromCategory(new ArrayList<Integer>(Arrays.asList(furniture)));
        assert (res.size() == 2);
        res = db.selectAllItemsFromCategory(new ArrayList<Integer>(Arrays.asList(car)));
        assert (res.size() == 2);
        res = db.selectAllItemsFromCategory(new ArrayList<Integer>(Arrays.asList(electronic, car)));
        assert (res.size() == 5);
        res = db.selectAllItemsFromPrice(80, 101);
        assert (res.size() == 1);
        res = db.selectAllItemsFromPrice(200, 300);
        assert (res.size() == 0);
        res = db.selectAllItemsFromPrice(0, 50);
        assert (res.size() == 7);
        ArrayList<Integer> categories = new ArrayList<Integer>(); // Create an ArrayList object
        categories.add(-1);
        String text = "electronics";
        int cat = convertCategoryToInt(text);
        categories.set(0, cat);
        res = db.selectAllItemsFromCategory(categories);
        assert (res.size() == 3);
    }

    public void testSelectAll() {
        System.out.println("\nTest Select All");
        System.out.println("--------------------------------------");
        db.dropAllTables();
        db.createAllTables();
        populateDatabase(db);
        ArrayList<ItemData> result = db.selectAllItems();
        assert (result.size() == 8);
    }

    public void testBulkInsert() {
        System.out.println("\nTest Bulk Insert");
        System.out.println("--------------------------------------");
        db.dropAllTables();
        db.createAllTables();
        URL path = AppTest.class.getResource("sample.txt");
        System.out.println("Path: " + path);
        File f = new File(path.getFile());
        BufferedReader reader;
        String line;
        int i = 0;
        try {
            reader = new BufferedReader(new FileReader(f));
            try {
                while ((line = reader.readLine()) != null) {
                    String[] columns = line.split(",");
                    // System.out.println(line);
                    int userId = Integer.parseInt(columns[0]);
                    String title = columns[1];
                    String description = columns[2];
                    int category = convertCategoryToInt(columns[3]);
                    assert (category != -1);
                    int date = Integer.parseInt(columns[4]);
                    int trademethod = convertMethodToInt(columns[5]);
                    assert (trademethod != -1);
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
                    // System.out.println(id);
                    if (id <= 0) {
                        System.out.println("Failed to parse text in line " + i + ", insertion failed");
                        assert (false);
                    }
                    i++;
                }
                reader.close();
            } catch (IOException e) {
                System.out.println("Error reading file");
                assert (false);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found when doing bulk insert");
            assert (false);
        }
        System.out.println("total insert: " + db.selectAllItems().size());
        System.out.println("total lines of input: " + i);

        assert (db.selectAllItems().size() == i);
    }

    public void testElasticSearch() {
        System.out.println("\nTest Elasticsearch");
        System.out.println("--------------------------------------");
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        RestClientBuilder builder = RestClient.builder(new HttpHost(esEndPointUrl, port, protocol))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });

        RestHighLevelClient client = new RestHighLevelClient(builder);

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("key", 3);
        jsonMap.put("user", "Sheldon");
        jsonMap.put("title", "Range Rover");
        jsonMap.put("description",
                "The Ferrari 488 Spider is the latest chapter in Maranello’s ongoing history of open-top V8 sports cars, a story that started with the targa-top version of the 308 GTB, which ultimately resulted in the full convertible Spider architecture.");
        jsonMap.put("category", "car");
        jsonMap.put("postDate", 20190425);
        jsonMap.put("price", 0);
        IndexRequest indexRequest = new IndexRequest("item").id("3").source(jsonMap);
        try {
            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            System.out.println("fucking error from getResponse: " + e.getMessage());
            e.printStackTrace();
        }
        GetRequest getRequest = new GetRequest("item", "2");

        try {
            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            String index = getResponse.getIndex();
            String id = getResponse.getId();
            if (getResponse.isExists()) {
                long version = getResponse.getVersion();
                String sourceAsString = getResponse.getSourceAsString();
                Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
                byte[] sourceAsBytes = getResponse.getSourceAsBytes();
                System.out.println("version: " + version + "\ndocument: " + sourceAsString + "\nindex: " + index);
                assertEquals(sourceAsMap.get("category"), "car");

            } else {

            }
            // client.close();

        } catch (IOException e) {
            System.out.println("fucking error from getResponse: " + e.getMessage());

            e.printStackTrace();
        }

        // SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // sourceBuilder.query(QueryBuilders.termQuery("category", "car"));
        // // sourceBuilder.query(QueryBuilders.matchAllQuery());
        // sourceBuilder.from(0);
        // sourceBuilder.size(5);
        // // sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        // SearchRequest searchRequest = new SearchRequest();
        // searchRequest.indices("item");
        // searchRequest.source(sourceBuilder);
        // try {
        // SearchResponse searchResponse = client.search(searchRequest,
        // RequestOptions.DEFAULT);
        // SearchHits hits = searchResponse.getHits();
        // System.out.println("entered search");
        // int totalShards = searchResponse.getTotalShards();
        // System.out.println("Total Shards: " + totalShards);

        // SearchHit[] searchHits = hits.getHits();
        // TotalHits totalHits = hits.getTotalHits();
        // // the total number of hits, must be interpreted in the context of
        // // totalHits.relation
        // long numHits = totalHits.value;
        // // whether the number of hits is accurate (EQUAL_TO) or a lower bound of the
        // // total (GREATER_THAN_OR_EQUAL_TO)
        // TotalHits.Relation relation = totalHits.relation;
        // System.out.println("total hits: " + totalHits);
        // for (SearchHit hit : searchHits) {
        // System.out.println("entered hits");

        // String sourceAsString = hit.getSourceAsString();
        // Map<String, Object> sourceAsMap = hit.getSourceAsMap();
        // String documentTitle = (String) sourceAsMap.get("title");
        // // List<Object> users = (List<Object>) sourceAsMap.get("user");
        // // Map<String, Object> innerObject = (Map<String, Object>)
        // // sourceAsMap.get("innerObject");
        // System.out.println("\ndocument: " + sourceAsString + "\ntitle: " +
        // documentTitle);

        // assertEquals(sourceAsMap.get("title"), "Ferrari 488");

        // assert (false);
        // }

        // } catch (IOException e) {
        // System.out.println("fucking error from getResponse: " + e.getMessage());
        // e.printStackTrace();
        // }

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", "Range Rove ").fuzziness(Fuzziness.AUTO)
                .prefixLength(3).maxExpansions(50);
        sourceBuilder.query(matchQueryBuilder);

        // sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("item");
        searchRequest.source(sourceBuilder);
        ArrayList<ItemData> result = new ArrayList<>();

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
            System.out.println("Searching: Ferrari from title, total hits: " + numHits);
            for (SearchHit hit : searchHits) {
                System.out.println("entered title search hits");
                String sourceAsString = hit.getSourceAsString();
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                String documentTitle = (String) sourceAsMap.get("title");
                int pKey = (int) sourceAsMap.get("key");

                result.add(db.selectOneItem(pKey));
                // List<Object> users = (List<Object>) sourceAsMap.get("user");
                // Map<String, Object> innerObject = (Map<String, Object>)
                // sourceAsMap.get("innerObject");
                System.out.println("\ndocument: " + sourceAsString + "\ntitle: " + documentTitle);
            }
            client.close();
            for (ItemData item : result) {
                System.out.println("Search result title: " + item.itemTitle);
            }
        } catch (IOException e) {
            System.out.println("fucking error from getResponse: " + e.getMessage());
            e.printStackTrace();
        }
        try {
            client.close();
            assert (false);
        } catch (IOException e) {
            // TODO: handle exception
        }
    }
    // public void testNotFound() {
    // System.out.println("\nTest 404 Not Found");
    // System.out.println("--------------------------------------");
    // Gson gson = new Gson();

    // // Spark.get("/hello", (request, response) -> {
    // // return 99;
    // // });

    // Spark.get("/item", (request, response) -> {
    // return 99;
    // });

    // Spark.get("*", (req, res) -> {
    // if (!req.pathInfo().startsWith("/item") &&
    // !req.pathInfo().startsWith("/hello")) {
    // res.status(404);
    // System.out.println("Entered get*");
    // }
    // return "123";
    // });

    // Spark.notFound((request, response) -> {
    // System.out.println("Entered not found");
    // response.type("application/json");
    // return "404 Not Found!";
    // });

    // String test = "";
    // try {
    // StringBuilder result = new StringBuilder();
    // URL url = new URL("http://localhost:4567/hmmmm");
    // HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    // conn.setRequestMethod("GET");
    // BufferedReader rd = new BufferedReader(new
    // InputStreamReader(conn.getInputStream()));
    // String line;
    // while ((line = rd.readLine()) != null) {
    // result.append(line);
    // }
    // System.out.println("Hmmmm? " + result.toString());
    // test = result.toString();
    // rd.close();

    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // System.out.println("aaaawtf? " + test);
    // assert (test.equals("123"));

    // }

    public void testDscSortByPrice() {
        System.out.println("\nTest Sort By Descending Price");
        System.out.println("--------------------------------------");
        db.dropAllTables();
        db.createAllTables();
        populateDatabase(db);
        ArrayList<ItemData> list = db.selectAllItems();
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
        assertEquals(list.get(0).itemTitle, "Rangerover Sport");
        assertEquals(list.get(1).itemTitle, "logitech mouse");
        assertEquals(list.get(2).itemTitle, "Econ001 textbook");

    }

    public void testAscSortByPrice() {
        System.out.println("\nTest Sort By Ascending Price");
        System.out.println("--------------------------------------");
        db.dropAllTables();
        db.createAllTables();
        populateDatabase(db);
        ArrayList<ItemData> list = db.selectAllItems();
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
        assertEquals(list.get(7).itemTitle, "Rangerover Sport");
        assertEquals(list.get(6).itemTitle, "logitech mouse");
        assertEquals(list.get(5).itemTitle, "Econ001 textbook");

    }

    private static void populateDatabase(Database db) {
        db.insertNewItem(1, "logitech mouse", "brand new", electronic, 20190410, sell, 40f, true, " days", "");
        db.insertNewItem(1, "Ferrari 488", "brand new", car, 20190328, giveaway, 0f, false, "this should not be shown",
                "");
        db.insertNewItem(4, "GTX 1060", "near broken", electronic, 20190221, trade, 0f, true, "10 days", "");
        db.insertNewItem(4, "Econ001 textbook", "half new", school, 20190407, sell, 20f, true, "in this semester", "");
        db.insertNewItem(2, "Coolermaster keyboard", "brand new", electronic, 20190318, trade, 0f, false,
                "this should not be shown", "");
        db.insertNewItem(2, "Desktop", "80% new", furniture, 20190112, giveaway, 0f, true, "2 months", "");
        db.insertNewItem(3, "Rangerover Sport", "half new", car, 20190409, rent, 100f, false, "a month", "");
        db.insertNewItem(3, "Microfridge", "brand new", furniture, 20190405, trade, 0f, true, "one year", "");
    }

    private static ArrayList<ItemData> actualList() {
        ArrayList<ItemData> result = new ArrayList<ItemData>();

        result.add(
                new ItemData(1, 1, "logitech mouse", "brand new", electronic, 20190410, sell, 40f, true, " days", ""));
        result.add(new ItemData(2, 1, "Ferrari 488", "brand new", car, 20190328, giveaway, 0f, false,
                "this should not be shown", ""));
        result.add(new ItemData(3, 4, "GTX 1060", "near broken", electronic, 20190221, trade, 0f, true, "10 days", ""));
        result.add(new ItemData(4, 4, "Econ001 textbook", "half new", school, 20190407, sell, 20f, true,
                "in this semester", ""));
        result.add(new ItemData(5, 2, "Coolermaster keyboard", "brand new", electronic, 20190318, trade, 0f, false,
                "this should not be shown", ""));
        result.add(new ItemData(6, 2, "Desktop", "80% new", furniture, 20190112, giveaway, 0f, true, "2 months", ""));
        result.add(new ItemData(7, 3, "Rangerover Sport", "half new", car, 20190409, rent, 100f, false, "a month", ""));
        result.add(
                new ItemData(8, 3, "Microfridge", "brand new", furniture, 20190405, trade, 0f, true, "one year", ""));
        return result;
    }

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

    public class StructuredResponse {
        /**
         * The status is a string that the application can use to quickly determine if
         * the response indicates an error. Values will probably just be "ok" or
         * "error", but that may evolve over time.
         */
        public String mStatus;

        /**
         * The message is only useful when this is an error, or when data is null.
         */
        public String mMessage;

        public Object mData;

        /**
         * Construct a StructuredResponse by providing a status, message, and data. If
         * the status is not provided, set it to "invalid".
         * 
         * @param status  The status of the response, typically "ok" or "error"
         * @param message The message to go along with an error status
         * @param object  An object with additional data to send to the client
         */
        public StructuredResponse(String status, String message, Object any) {
            mStatus = (status != null) ? status : "invalid";
            mMessage = message;
            mData = any;
        }
    }
}
