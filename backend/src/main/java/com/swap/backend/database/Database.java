package com.swap.backend.database;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Database 
{
    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
    private Connection mConnection;

    //bunch of prepared statements 

    /**
     * A prepared statement for getting all items in the database
     */
    protected static PreparedStatement p_selectAllItems;

    /**
     * A prepared statement for getting one item in the database
     */
    protected static PreparedStatement p_selectOneItem;

    /**
     * A prepared statement for deleting a item from the database
     */
    protected static PreparedStatement p_deleteOneItem;

    /**
     * A prepared statement for creating a new item in the database
     */
    protected static PreparedStatement p_createNewItem;

    /**
     * A prepared statement for creating the item table in our database
     */
    protected static PreparedStatement p_createItemTable;

    /**
     * A prepared statement for dropping the item table in our database
     */
    protected static PreparedStatement p_dropItemTable;

    /**
     * The Database constructor is private: we only create Database objects 
     * through the getDatabase() method.
     */
    private Database() 
    {
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
            db.mConnection = conn;
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
            // NB: we can easily get ourselves in trouble here by typing the
            //     SQL incorrectly.  We really should have things like "tblData"
            //     as constants, and then build the strings for the statements
            //     from those constants.

            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table 
            // creation/deletion, so multiple executions will cause an exception
            Database.p_createItemTable = db.mConnection.prepareStatement(
                    "CREATE TABLE itemData (id SERIAL PRIMARY KEY, title VARCHAR(50) NOT NULL, description VARCHAR(500) NOT NULL)"
                    +"seller VARCHAR(50) NOT NULL, price FLOAT");
            Database.p_dropItemTable = db.mConnection.prepareStatement("DROP TABLE itemData");

            // Standard CRUD operations for item

            Database.p_deleteOneItem = db.mConnection.prepareStatement("DELETE FROM itemData WHERE id = ?");
            Database.p_createNewItem = db.mConnection.prepareStatement("INSERT INTO itemData VALUES (default, ?, ?, ?, ?)");
            Database.p_selectAllItems = db.mConnection.prepareStatement("SELECT id, title FROM itemData");
            Database.p_selectOneItem = db.mConnection.prepareStatement("SELECT * from itemData WHERE id=?");
        
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


   
}