package com.swap.backend.database;

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

public class Database 
{
    /****************************************************************/
    /****************************************************************/
    /*******                                                  *******/
    /*******                ITEM DATA TABLE                   *******/
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
    /*******            ITEM CATEGORY DATA TABLE              *******/
    /*******                                                  *******/
    /****************************************************************/
    /****************************************************************/

    /**
     * A prepared statement for creating the item category table in our database
     */
    static PreparedStatement p_createItemCategoryDataTable;

    /**
     * A prepared statement for dropping the item category table in our database
     */
    static PreparedStatement p_dropItemCategoryDataTable;
    
    /**
     * A prepared statement for selecting all items in Car category
     */
    static PreparedStatement p_selectAllCar;

    /**
     * A prepared statement for selecting all items in school category
     */
    static PreparedStatement p_selectAllScool;

    /**
     * A prepared statement for selecting all items in electronics category
     */
    static PreparedStatement p_selectAllElectronics;

    /**
     * A prepared statement for selecting all items in furniture category
     */
    static PreparedStatement p_selectAllFurniture;

    /****************************************************************/
    /****************************************************************/
    /*******                                                  *******/
    /*******                DATABASE CONNECTION               *******/
    /*******                                                  *******/
    /****************************************************************/
    /****************************************************************/

    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
    private static Connection mConnection;

    /**
     * priavate instance for accessing and calling functions in ItemCategoryDataTable
     */
    private static ItemCategoryDataTable itemCDT;

    /**
     * priavate instance for accessing and calling functions in ItemDataTable
     */
    private static ItemDataTable itemDT;

    /**
     * The Database constructor is private: we only create Database objects 
     * through the getDatabase() method.
     */
    private Database() 
    {
        itemCDT = new ItemCategoryDataTable();
        itemDT = new ItemDataTable();
    }

    /**
     * getter for private field itemCDT
     */
    public static ItemCategoryDataTable getItemCDT(){
        return itemCDT;
    }

    /**
     * getter for private field itemDT
     */
    public static ItemDataTable getItemDT(){
        return itemDT;
    }

    /**
     * @param url the url to connect to database
     */
    static Database getDatabase(String url)
    {
        //Create an new unconfigured Database object
        Database db = new Database();

        // Give the Database object a connection, fail if we cannot get one
        try 
        {
            Class.forName("org.postgresql.Driver");
            URI dbUri = new URI(url);
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            Connection conn = DriverManager.getConnection(dbUrl, username, password);
            if (conn == null) 
            {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            mConnection = conn;
        } 
        catch (SQLException e) 
        {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        } 
        catch (ClassNotFoundException cnfe) 
        {
            System.out.println("Unable to find postgresql driver");
            return null;
        } 
        catch (URISyntaxException s) 
        {
            System.out.println("URI Syntax Error");
            return null;
        }

        // Attempt to create all of our prepared statements.  If any of these fail, the whole getDatabase() call should fail
        try 
        {
            //////////////////////////////////////////
            //             Item Data Table
            //////////////////////////////////////////
            Database.p_createItemDataTable = mConnection.prepareStatement(
                    "CREATE TABLE itemData (id SERIAL PRIMARY KEY, title VARCHAR(50) NOT NULL, description VARCHAR(500) NOT NULL),"
                    + "seller VARCHAR(50) NOT NULL, price FLOAT");
            Database.p_dropItemDataTable = mConnection.prepareStatement("DROP TABLE itemData");
            // Standard CRUD operations for item
            Database.p_deleteOneItemData = mConnection.prepareStatement("DELETE FROM itemData WHERE id = ?");
            Database.p_insertNewItemData = mConnection.prepareStatement("INSERT INTO itemData VALUES (default, ?, ?, ?, ?)");
            Database.p_selectAllItemData = mConnection.prepareStatement("SELECT id, title FROM itemData");
            Database.p_selectOneItemData = mConnection.prepareStatement("SELECT * from itemData WHERE id=?");
            Database.p_selectAllItemDataById = mConnection.prepareStatement("SELECT id, title FROM itemData WHERE id in ?");

            //////////////////////////////////////////
            //        Item Category Data Table
            //////////////////////////////////////////
            Database.p_createItemCategoryDataTable = mConnection.prepareStatement(
                    "CREATE TABLE itemCategoryData (id SERIAL PRIMARY KEY, category VARCHAR(200),"
                    + "school BIT, car BIT, electronics BIT, furniture BIT");
            Database.p_dropItemDataTable = mConnection.prepareStatement("DROP TABLE itemCategoryData");
            // Standard CRUD operations for item category data
            Database.p_selectAllElectronics = mConnection.prepareStatement("SELECT id from itemCategoryData WHERE electronics = 1");
            Database.p_selectAllFurniture = mConnection.prepareStatement("SELECT id from itemCategoryData WHERE furniture = 1");
            Database.p_selectAllScool = mConnection.prepareStatement("SELECT id from itemCategoryData WHERE school = 1");
            Database.p_selectAllCar = mConnection.prepareStatement("SELECT id from itemCategoryData WHERE car = 1");
        } 
        catch (SQLException e) {
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
     * NB: The connection will always be null after this call, even if an 
     *     error occurred during the closing operation.
     * 
     * @return True if the connection was cleanly closed, false otherwise
     */
    boolean disconnect() {
        if (mConnection == null) 
        {
            System.err.println("Unable to close connection: Connection was null");
            return false;
        }
        try 
        {
            mConnection.close();
        } 
        catch (SQLException e) 
        {
            System.err.println("Error: Connection.close() threw a SQLException");
            e.printStackTrace();
            mConnection = null;
            return false;
        }
        mConnection = null;
        return true;
    }

    static Array ConvertToArray(ArrayList<Integer> res){
        try{
            return mConnection.createArrayOf("INTEGER", res.toArray());
        }
        catch(SQLException e){
            return null;
        }
    }
}