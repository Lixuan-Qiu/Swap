package edu.lehigh.cse280.swap.database;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.heroku.api.request.RequestConfig.Data;

public class Database {
    /**
     * A prepared statement used for getting the most recently created itemData
     * entry id
     */
    static PreparedStatement p_getMostRecentId;
    /****************************************************************/
    /****************************************************************/
    /*******                                                  *******/
    /******* ITEM DATA TABLE *******/
    /*******                                                  *******/
    /****************************************************************/
    /****************************************************************/

    /**
     * A prepared statement for getting all items in the database
     */
    static PreparedStatement p_selectAllItemData;

    /**
     * A prepared statement for getting one item in the database
     */
    static PreparedStatement p_selectOneItemData;

    /**
     * A prepared statement for getting all item by provided id list
     */
    static PreparedStatement p_selectAllItemDataById;

    /**
     * A prepared statement for getting all item by provided userId
     */
    static PreparedStatement p_selectAllItemDataByUserId;

    /**
     * A prepared statement for deleting a item from the database
     */
    static PreparedStatement p_deleteOneItemData;

    /**
     * A prepared statement for creating a new item in the database
     */
    static PreparedStatement p_insertNewItemData;

    /**
     * A prepared statement for selecting all items in one category
     */
    static PreparedStatement p_selectAllFromCategory;

    /**
     * A prepared statement for selecting all items in one category
     */
    static PreparedStatement p_selectAllFromPrice;

    /**
     * A prepared statement for updating info for itemData
     */
    static PreparedStatement p_updateItemData;

    /**
     * A prepared statement for creating the item table in our database
     */
    static PreparedStatement p_createItemDataTable;

    /**
     * A prepared statement for dropping the item table in our database
     */
    static PreparedStatement p_dropItemDataTable;

    /****************************************************************/
    /****************************************************************/
    /*******                                                  *******/
    /******* DATABASE CONNECTION *******/
    /*******                                                  *******/
    /****************************************************************/
    /****************************************************************/

    /**
     * The connection to the database. When there is no connection, it should be
     * null. Otherwise, there is a valid open connection
     */
    private static Connection mConnection;

    /**
     * priavate instance for accessing and calling functions in ItemDataTable
     */
    private static ItemDataTable itemDT;

    /**
     * The Database constructor is private: we only create Database objects through
     * the getDatabase() method.
     */
    private Database() {
        itemDT = new ItemDataTable();
    }

    public static Database getDatabase(String ip, String port, String user, String pass) {
        // conn is a connection to the database. In this simple example, it is
        // a local variable, though in a realistic program it might not be

        Database db = new Database();
        // Connect to the database or fail
        System.out.print("Connecting to " + ip + ":" + port);
        try {
            // Open a connection, fail if we cannot get one
            Connection conn = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" + port + "/", user, pass);
            if (conn == null) {
                System.out.println("\n\tError: DriverManager.getConnection() returned a null object");
                return null;
            }
            mConnection = conn;
        } catch (SQLException e) {
            System.out.println("\n\tError: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        }
        System.out.println(" ... successfully connected");

        SetUpPrepareStatement();
        return db;
    }

    /**
     * @param url the url to connect to database
     */
    public static Database getDatabase(String url) {
        // Create an new unconfigured Database object
        Database db = new Database();

        // Give the Database object a connection, fail if we cannot get one
        try {
            Class.forName("org.postgresql.Driver");
            URI dbUri = new URI(url);
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            Connection conn = DriverManager.getConnection(dbUrl, username, password);
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Unable to find postgresql driver");
            return null;
        } catch (URISyntaxException s) {
            System.out.println("URI Syntax Error");
            return null;
        }
        
        SetUpPrepareStatement();
        return db;

        // // Attempt to create all of our prepared statements. If any of these fail, the
        // // whole getDatabase() call should fail
        // try {
        //     Database.p_getMostRecentId = mConnection.prepareStatement("SELECT MAX(itemId) FROM itemData");
        //     //////////////////////////////////////////
        //     // Item Data Table
        //     //////////////////////////////////////////
        //     Database.p_createItemDataTable = mConnection.prepareStatement("CREATE TABLE itemData"
        //             + "(itemId SERIAL PRIMARY KEY," + " userId INTEGER," + " title VARCHAR(50) NOT NULL,"
        //             + " description VARCHAR(500) NOT NULL," + " category INTEGER," + " postDate INTEGER,"
        //             + " tradeMethod INTEGER," + " price float," + " availability boolean,"
        //             + " availableTime VARCHAR(40)," + " wantedItemDescription VARCHAR(50)," + "longitude float," 
        //             + "latitude float," + "address VARCHAR(50)," + "city VARCHAR(30)," + "state VARCHAR(2)," 
        //             + "zipcode INTEGER)");
        //     Database.p_dropItemDataTable = mConnection.prepareStatement("DROP TABLE itemData");
        //     // Standard CRUD operations for item
        //     Database.p_deleteOneItemData = mConnection.prepareStatement("DELETE FROM itemData WHERE itemId = ?");
        //     Database.p_insertNewItemData = 
        //     mConnection.prepareStatement("INSERT INTO itemData VALUES (default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        //     Database.p_selectAllItemData = mConnection.prepareStatement("SELECT * FROM itemData");
        //     Database.p_selectOneItemData = mConnection.prepareStatement("SELECT * FROM itemData WHERE itemId=?");
        //     Database.p_selectAllItemDataById = mConnection.prepareStatement("SELECT * FROM itemData WHERE itemId in ?");
        //     Database.p_selectAllFromCategory = mConnection.prepareStatement("SELECT * FROM itemData WHERE category = ANY(?)");
        //     Database.p_selectAllFromPrice = mConnection.prepareStatement("SELECT * FROM itemData WHERE price BETWEEN ? AND ?");
        //     Database.p_updateItemData = mConnection.prepareStatement("UPDATE");

        // } catch (SQLException e) {
        //     System.err.println("Error creating prepared statement");
        //     e.printStackTrace();
        //     //db.disconnect();
        //     //return null;
        // }
        // return db;
    }

    private static void SetUpPrepareStatement() 
    {
        // Attempt to create all of our prepared statements. If any of these fail, the
        // whole getDatabase() call should fail
        try {
            Database.p_getMostRecentId = mConnection.prepareStatement("SELECT MAX(itemId) FROM itemData");
            //////////////////////////////////////////
            // Item Data Table
            //////////////////////////////////////////
            Database.p_createItemDataTable = mConnection.prepareStatement("CREATE TABLE itemData"
                    + "(itemId SERIAL PRIMARY KEY," + " userId INTEGER," + " title VARCHAR(50) NOT NULL,"
                    + " description VARCHAR(500) NOT NULL," + " category INTEGER," + " postDate INTEGER,"
                    + " tradeMethod INTEGER," + " price float," + " availability boolean,"
                    + " availableTime VARCHAR(40)," + " wantedItemDescription VARCHAR(50)," + "longitude float," 
                    + "latitude float," + "address VARCHAR(50)," + "city VARCHAR(30)," + "state VARCHAR(2)," 
                    + "zipcode INTEGER)");
            Database.p_dropItemDataTable = mConnection.prepareStatement("DROP TABLE itemData");
            // Standard CRUD operations for item
            Database.p_deleteOneItemData = mConnection.prepareStatement("DELETE FROM itemData WHERE itemId = ?");
            Database.p_insertNewItemData = mConnection.prepareStatement("INSERT INTO itemData VALUES (default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            , RETURN_GENERATED_KEYS);
            Database.p_selectAllItemData = mConnection.prepareStatement("SELECT * FROM itemData");
            Database.p_selectOneItemData = mConnection.prepareStatement("SELECT * FROM itemData WHERE itemId=?");
            Database.p_selectAllItemDataById = mConnection.prepareStatement("SELECT * FROM itemData WHERE itemId in ?");
            Database.p_selectAllFromCategory = mConnection.prepareStatement("SELECT * FROM itemData WHERE category = ANY(?)");
            Database.p_selectAllFromPrice = mConnection.prepareStatement("SELECT * FROM itemData WHERE price BETWEEN ? AND ?");
            Database.p_updateItemData = mConnection.prepareStatement("UPDATE");
            Database.p_selectAllItemDataByUserId = mConnection.prepareStatement("SELECT * FROM itemData WHERE userId=?");

        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            //db.disconnect();
            //return null;
        }
        return;
    }
    



    /**
     * Close the current connection to the database, if one exists.
     * 
     * NB: The connection will always be null after this call, even if an error
     * occurred during the closing operation.
     * 
     * @return True if the connection was cleanly closed, false otherwise
     */
    boolean disconnect() {
        if (mConnection == null) {
            System.err.println("Unable to close connection: Connection was null");
            return false;
        }
        try {
            mConnection.close();
        } catch (SQLException e) {
            System.err.println("Error: Connection.close() threw a SQLException");
            e.printStackTrace();
            mConnection = null;
            return false;
        }
        mConnection = null;
        return true;
    }

    /**
     * @param res the arraylist to be converted to SQL Array type
     * @return the converted Array type of res
     */
    static Array ConvertToIntArray(ArrayList<Integer> res) {
        try {
            return mConnection.createArrayOf("INTEGER", res.toArray());
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * @param res the arraylist to be converted to SQL Array type
     * @return the converted Array type of res
     */
    static Array ConvertToStringArray(String[] res) {
        try {
            return mConnection.createArrayOf("text", res);
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Creating all tables in the database
     */
    public void createAllTables() {
        itemDT.createItemDataTable();
    }

    /**
     * Dropping all tables in the database
     */
    public void dropAllTables() {
        itemDT.dropItemDataTable();
    }

    /**
     * 
     * @param userId                Id of the user who posted this item
     * @param title                 Title of the item
     * @param description           Description of the item
     * @param categories            Category of the item (Only 4 currently)
     *                              Categories: 1 for car, 2 for school, 3 for
     *                              electronics, 4 for furnitures
     * @param postDate              Date of the when the item was posted, format is
     *                              YYYYMMDD
     * @param tradeMethod           How the item is going to be traded, 1 for "Sell", 2
     *                              for "Trade", 3 for "Rent", 4 for "GiveAway"
     * @param price                 Price of the item
     * @param availability          Whether the item is currently available
     * @param availableTime         Available amount of time, standard unit will be day(s)
     * @param wantedItemDescription Some keywords of wanted item (description of the item being sold)
     * @param itemLongitude         The longitude coordinate of the item
     * @param itemLatitude          The latitude coordinate of the item
     * @param itemAddress           The address of the item (street, ave, bvld, etc)
     * @param itemCity              The city of the item
     * @param itemState             The state of the item (PA, CA, etc)
     * @param itemZipCode           The zipcode of the item
     * 
     * @return the id of the new items
     */
    public int insertNewItem(int userId, String title, String description, int categories, int postDate,
            int tradeMethod, float price, boolean availability, String availableTime, String wantedItemDescription,
            float itemLongitude, float itemLatitude, String itemAddress, String itemCity,
            String itemState, int itemZipCode) {
        int res = itemDT.insertNewItemData(userId, title, description, categories, postDate, tradeMethod, price,
                availability, availableTime, wantedItemDescription, itemLongitude, itemLatitude, itemAddress, itemCity,
                itemState, itemZipCode);
        return res;
    }

    /**
     * 
     * @param item An ItemData object that will be inserted
     * @return if non negative if successfully inserted
     */
    public int insertNewItem(ItemData item) {
        int res = itemDT.insertNewItemData(item.itemSeller, item.itemTitle, item.itemDescription, item.itemCategory,
                item.itemPostDate, item.itemTradeMethod, item.itemPrice, item.itemAvailability, item.itemAvailableTime,
                item.itemWantedItemDescription, item.itemLongitude, item.itemLatitude, item.itemAddress, item.itemCity,
                item.itemState, item.itemZipCode);
        return res;
    }

    /**
     * 
     * @param item The ItemData object to be updated
     * @return a non neg if succesfully updated
     */
    public int updateItemData(ItemData item)
    {
        int res = itemDT.updateItemData(item);
        return res;
    }

    public int deleteItem(int itemId) {
        int res = itemDT.deleteItem(itemId);
        return res;
    }

    public ArrayList<ItemData> selectAllItems() {
        return itemDT.selectAllItems();
    }

    public ArrayList<ItemData> selectAllItemsFromCategory(ArrayList<Integer> category) {
        return itemDT.selectAllItemFromCategory(category);
    }

    public ArrayList<ItemData> selectAllItemsFromPrice(float low, float high) {
        return itemDT.selectAllItemFromPrice(low, high);
    }

    public ItemData selectOneItem(int itemId) {
        return itemDT.selectOneItem(itemId);
    }

    public ArrayList<ItemData> selectByUserId(int userId)
    {
        return itemDT.selectByUserId(userId);
    }

    public ArrayList<ItemData> selectAllItems(int itemPerPage, int pageNum) {
        return itemDT.selectAllItems(itemPerPage, pageNum);
    }

    public ArrayList<ItemData> selectAllItemsFromCategory(ArrayList<Integer> category, int itemPerPage, int pageNum) {
        return itemDT.selectAllItemFromCategory(category, itemPerPage, pageNum);
    }

    public ArrayList<ItemData> selectAllItemsFromPrice(float low, float high, int itemPerPage, int pageNum) {
        return itemDT.selectAllItemFromPrice(low, high, itemPerPage, pageNum);
    }

    public ArrayList<ItemData> selectFromUserId(int userId, int itemPerPage, int pageNum)
    {
        return itemDT.selectByUserId(userId, itemPerPage, pageNum);
    }
}