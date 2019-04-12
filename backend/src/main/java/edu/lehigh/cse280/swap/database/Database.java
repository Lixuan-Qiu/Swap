package edu.lehigh.cse280.swap.database;

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
    static PreparedStatement p_selectAllFrom;

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
    /******* TRADING INFO DATA TABLE *******/
    /*******                                                  *******/
    /****************************************************************/
    /****************************************************************/

    /**
     * A prepared statement for creating the trading info table in our database
     */
    static PreparedStatement p_createTradingInfoDataTable;

    /**
     * A prepared statement for dropping the trading info table in our database
     */
    static PreparedStatement p_dropTradingInfoDataTable;

    /**
     * A prepared statement for inserting new trading info to the table
     */
    static PreparedStatement p_insertNewTradingInfoData;

    /**
     * A prepared statement for selecting all trading info to the table
     */
    static PreparedStatement p_selectAllTradingInfoData;

    /**
     * A prepared statement for selecting all trading info by idList to the table
     */
    static PreparedStatement p_selectAllTradingInfoDataById;

    /**
     * A prepared statement for selecting one trading info to the table
     */
    static PreparedStatement p_selectOneTradingInfoData;
    /**
     * A prepared statement for inserting new item category to the table
     */
    static PreparedStatement p_deleteTradingInfoData;

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
     * priavate instance for accessing and calling functions in
     * ItemCategoryDataTable
     */
    private static TradingInfoDataTable itemTIDT;

    /**
     * priavate instance for accessing and calling functions in ItemDataTable
     */
    private static ItemDataTable itemDT;

    /**
     * The Database constructor is private: we only create Database objects through
     * the getDatabase() method.
     */
    private Database() {
        itemTIDT = new TradingInfoDataTable();
        itemDT = new ItemDataTable();
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

        // Attempt to create all of our prepared statements. If any of these fail, the
        // whole getDatabase() call should fail
        try {
            //////////////////////////////////////////
            // Item Data Table
            //////////////////////////////////////////
            Database.p_createItemDataTable = mConnection.prepareStatement(
                    "CREATE TABLE itemData (itemId SERIAL PRIMARY KEY, userId INTEGER, title VARCHAR(50) NOT NULL, description VARCHAR(500) NOT NULL),"
                            + "category INTEGER[], tradingInfoId INTEGER, postDate INTEGER");
            Database.p_dropItemDataTable = mConnection.prepareStatement("DROP TABLE itemData");
            // Standard CRUD operations for item
            Database.p_deleteOneItemData = mConnection.prepareStatement("DELETE FROM itemData WHERE itemId = ?");
            Database.p_insertNewItemData = mConnection
                    .prepareStatement("INSERT INTO itemData VALUES (default, ?, ?, ?, ?, ?, ?)");
            Database.p_selectAllItemData = mConnection.prepareStatement("SELECT * FROM itemData");
            Database.p_selectOneItemData = mConnection.prepareStatement("SELECT * from itemData WHERE itemId=?");
            Database.p_selectAllItemDataById = mConnection.prepareStatement("SELECT * FROM itemData WHERE itemId in ?");
            Database.p_selectAllFrom = mConnection.prepareStatement("SELECT * from itemData WHERE ? in category");
            //////////////////////////////////////////
            // Trading Info Data Table
            //////////////////////////////////////////

            Database.p_createTradingInfoDataTable = mConnection.prepareStatement(
                    "CREATE TABLE tradingInfoData (tradingInfoId SERIAL PRIMARY KEY, itemId INTEGER, tradeMethod INTEGER,"
                            + "price float, availability boolean, availableTime VARCHAR(40), wantedItemDescription VARCHAR(50)");
            Database.p_dropTradingInfoDataTable = mConnection.prepareStatement("DROP TABLE tradingInfoData");
            // Standard CRUD operations for item category data
            Database.p_insertNewTradingInfoData = mConnection
                    .prepareStatement("INSERT INTO tradingInfoData VALUES (default, ?, ?, ?, ?, ?, ?)");
            Database.p_selectAllTradingInfoData = mConnection.prepareStatement("SELECT * FROM tradingInfoData");
            Database.p_selectAllTradingInfoDataById = mConnection
                    .prepareStatement("SELECT * FROM tradingInfoData WHERE tradingInfoId in ?");
            Database.p_selectOneTradingInfoData = mConnection
                    .prepareStatement("SELECT * FROM tradingInfoData WHERE tradingInfoId = ?");
            Database.p_deleteTradingInfoData = mConnection
                    .prepareStatement("DELETE FROM tradingInfoData WHERE tradingInfoId = ?");

        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            db.disconnect();
            return null;
        }
        return db;
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
        itemTIDT.createTradingInfoDataTable();
    }

    /**
     * Dropping all tables in the database
     */
    public void dropAllTables() {
        itemDT.dropItemDataTable();
        itemTIDT.dropTradingInfoDataTable();
    }

    public int insertNewItem(int userId, String title, String description, ArrayList<Integer> categories,
            int tradingInfoId, int postDate) {
        int res = itemDT.insertNewItemData(userId, title, description, categories, tradingInfoId, postDate);
        return res;
    }

    public int insertNewTradingInfoData(int itemId, int tradingMethod, float price, boolean availability,
            String availableTime, String wantedItemDescription) {
        int pk = itemTIDT.insertNewTradingInfoData(itemId, tradingMethod, price, availability, availableTime,
                wantedItemDescription);
        return pk;
    }

    public int deleteItem(int itemId) {
        int res = itemDT.deleteItem(itemId);
        return res;
    }

    public ArrayList<ItemData> selectAllItems() {
        return itemDT.selectAllItems();
    }

    public ArrayList<ItemData> selectAllItemsFrom(ArrayList<Integer> category) {
        return itemDT.selectAllItemFrom(category);
    }

    public ItemData selectOneItem(int itemId) {
        return itemDT.selectOneItem(itemId);
    }
}