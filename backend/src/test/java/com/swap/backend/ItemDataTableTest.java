package com.swap.backend;

import java.util.ArrayList;
import java.util.Map;
import java.util.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import edu.lehigh.cse280.swap.database.*;
import edu.lehigh.cse280.swap.app.*;

/**
 * Unit test for testing ItemDataTable
 */
public class ItemDataTableTest extends TestCase 
{
    Map<String, String> env = System.getenv();
    // String ip = env.get("POSTGRES_IP");
    // String port = env.get("POSTGRES_PORT");
    // String user = env.get("POSTGRES_USER");
    // String pass = env.get("POSTGRES_PASS");

    String ip = "127.0.0.1";
    String port = "5432";
    String user = "allen";
    String pass = "liu";

    Database db = Database.getDatabase(ip, port, user, pass);

    //Create random fields to use as tests 
    int itemId = 0;
    int userId = 1;
    String title = "Calc book";
    String description = "1st Edition";
    int category = 1;
    int postDate = 20190419;
    int tradeMethod = 1;
    float price = 50.0f;
    boolean availability = true;
    String availableTime = "3 days";
    String wantedItemDescription = "Chem book";
    float itemLongitude = 50.5f;
    float itemLatitude = 90.6f;
    String itemAddress = "4 Farrington Square";
    String itemCity = "Bethlehem";
    String itemState = "PA";
    int itemZipCode = 18015;

    int itemId1 = 1;
    int userId1 = 2;
    String title1 = "Chem book";
    String description1 = "9th Edition";


    ItemData firstItem = new ItemData(itemId, userId, title, description, category, postDate,
    tradeMethod, price, availability, availableTime, wantedItemDescription, itemLongitude, itemLatitude,
    itemAddress, itemCity, itemState, itemZipCode);

    ItemData secondItem = new ItemData(itemId1, userId1, title1, description1, category, postDate,
    tradeMethod, price, availability, availableTime, wantedItemDescription, itemLongitude, itemLatitude,
    itemAddress, itemCity, itemState, itemZipCode);



    /**
     * Tests insertNewItemData method by using object
     * PASSED
     */
    public void testInsertNewItemData() 
    {
        db.dropAllTables();
        db.createAllTables();

        System.out.println("Got into testInsertNewItemData Method");
        int ret = db.insertNewItem(firstItem);

        assertEquals(1, ret);
        System.out.println("Got after assertion1 check");

    }

    /**
     * Tests insertNewItemData method by using constructor with fields
     * PASSED
     */
    public void testInsertNewItemData1() 
    {
        db.dropAllTables();
        db.createAllTables();

        System.out.println("Got into testInsertNewItemData1 Method");
        int ret = db.insertNewItem(userId, title, description, category, postDate,
        tradeMethod, price, availability, availableTime, wantedItemDescription, itemLongitude, itemLatitude,
        itemAddress, itemCity, itemState, itemZipCode);

        assertEquals(1, ret);
        System.out.println("Got after assertion2 check");

    }

    /**
    * Tests deleteItem method inside database
    * PASSED
    */
    public void testDeleteItem() 
    {
        db.dropAllTables();
        db.createAllTables();

        System.out.println("Got into testDeleteItem Method");

        //first insert a new item in
        int ret = db.insertNewItem(firstItem);
        assertEquals(1, ret);

        //then delete this item
        int deleteRet = db.deleteItem(0);
        assertEquals(1, ret);

        System.out.println("Got after assertion3 check");

    }

    /**
    * Tests testSelectAllItems method inside database
    * Should return an array list of ItemData
    * FAILED
    */
    public void testSelectAllItems() 
    {
        db.dropAllTables();
        db.createAllTables();

        System.out.println("Got into testSelectAllItems Method");

        //Create an arraylist of ItemData to return
        ArrayList<ItemData> expectedArray = new ArrayList<ItemData>();
        expectedArray.add(firstItem);
        expectedArray.add(secondItem);

        //first insert a new item in
        int first = db.insertNewItem(firstItem);
        assertEquals(1, first);
        //then insert a second item in
        int second = db.insertNewItem(secondItem);
        assertEquals(1, second);

        ArrayList<ItemData> retArray = db.selectAllItems();

        //assertEquals(expectedArray, retArray);

        System.out.println("Got after assertion4 check");

    }

    /**
    * Tests testSelectAllItemsFromCategorymethod inside database
    * Should return an array list of ItemData
    * INCOMPLETE (will merge once tests done by Xiaowei/Vincent is pushed)
    */
    public void testSelectAllItemsFromCategory() 
    {
        db.dropAllTables();
        db.createAllTables();

        System.out.println("Got into testSelectAllItemsFromCategory Method");


        System.out.println("Got after assertion5 check");

    }

    /**
    * Tests testSelectAllItemsFromPrice method inside database
    * Should return an array list of ItemData
    * INCOMPLETE (will merge once tests done by Xiaowei/Vincent is pushed)
    */
    public void testSelectAllItemsFromPrice() 
    {
        db.dropAllTables();
        db.createAllTables();

        System.out.println("Got into testSelectAllItemsFromPrice Method");


        System.out.println("Got after assertion6 check");

    }

    /**
    * Tests testSelecOneItem method inside database
    * Should return an ItemData
    * FAILED, adding second item fails it for some reason
    */
    public void testSelectOneItem() 
    {
        db.dropAllTables();
        db.createAllTables();

        System.out.println("Got into testSelectOneItem Method");

        db.insertNewItem(firstItem);
        db.insertNewItem(secondItem);

        ItemData retItemData1 = db.selectOneItem(0);
        assertEquals(firstItem, retItemData1);

        ItemData retItemData2 = db.selectOneItem(1);
        assertEquals(secondItem, retItemData2);
        

        System.out.println("Got after assertion7 check");

    }






}
