package com.swap.backend;

import java.util.ArrayList;
import java.util.Map;

import org.apache.http.nio.entity.NHttpEntityWrapper;

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

    //trading method args 1-sell 2-trade 3-rent 4-giveaway
    private static final int sellTradeMethod = 1;
    private static final int tradeTradeMethod = 2;
    private static final int rentTradeMethod = 3;
    private static final int giveawayTradeMethod = 4;

    //categories 1-cars 2-school 3-electronic 4-furniture
    private static final int carCategory = 1;
    private static final int schoolCategory = 2;
    private static final int electronicCategory = 3;
    private static final int furnitureCategory = 4;
    
    //Create random fields to use as tests 
    int itemId = 1;
    int userId = 1;

    String title = "Calc book";
    String description = "1st Edition";
    //int category = 2;
    int postDate = 20190419;
    //int tradeMethod = 1;
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

    int itemId1 = 2;
    int userId1 = 2;

    String title1 = "Chem book";
    String description1 = "9th Edition";

    int itemId2 = 3;
    int userId2 = 3;

    String title2 = "Macbook Air";
    String description2 = "Late 2017 Version";

    int itemId3 = 4;
    int userId3 = 4;

    String title3 = "Toyota Camry";
    String description3 = "2017 SE Edition 60k Miles";

    int itemId4 = 5;
    int userId4 = 5;

    String title4 = "Old IKEA Sofa";
    String description4 = "Long white sofa, perfect for lounging!";

    int itemId5 = 6;

    float price1 = 70.0f;
    float price3 = 100.0f;



    //calc book
    ItemData firstItem = new ItemData(itemId, userId, title, description, schoolCategory, postDate,
    sellTradeMethod, price, availability, availableTime, wantedItemDescription, itemLongitude, itemLatitude,
    itemAddress, itemCity, itemState, itemZipCode);

    //chem book
    ItemData secondItem = new ItemData(itemId1, userId1, title1, description1, schoolCategory, postDate,
    tradeTradeMethod, price, availability, availableTime, wantedItemDescription, itemLongitude, itemLatitude,
    itemAddress, itemCity, itemState, itemZipCode);

    //macbook air
    ItemData thirdItem = new ItemData(itemId2, userId2, title2, description2, electronicCategory, postDate,
    sellTradeMethod, price3, availability, availableTime, wantedItemDescription, itemLongitude, itemLatitude,
    itemAddress, itemCity, itemState, itemZipCode);

    //toyota camry
    ItemData fourthItem = new ItemData(itemId3, userId3, title3, description3, carCategory, postDate,
    rentTradeMethod, price3, availability, availableTime, wantedItemDescription, itemLongitude, itemLatitude,
    itemAddress, itemCity, itemState, itemZipCode);

    //ikea sofa
    ItemData fifthItem = new ItemData(itemId4, userId4, title4, description4, furnitureCategory, postDate,
    giveawayTradeMethod, price1, availability, availableTime, wantedItemDescription, itemLongitude, itemLatitude,
    itemAddress, itemCity, itemState, itemZipCode);

    ItemData sixthItem = new ItemData(itemId5, userId1, title1, description1, schoolCategory, postDate,
    rentTradeMethod, price, availability, availableTime, wantedItemDescription, itemLongitude, itemLatitude,
    itemAddress, itemCity, itemState, itemZipCode);



    /**
     * Tests insertNewItemData method by using object
     * PASSED
     */
    public void testInsertNewItemData() 
    {
        db.dropAllTables();
        db.createAllTables();

        //System.out.println("Got into testInsertNewItemData Method");
        int ret = db.insertNewItem(firstItem);
        //System.out.println("result of ret after inserting first item: " + ret);

        assertEquals(1, ret);

        ret = db.insertNewItem(secondItem);
        //System.out.println("result of ret after inserting second item: " + ret);

        assertEquals(2, ret);

        ret = db.insertNewItem(thirdItem);
        assertEquals(3, ret);

        ret = db.insertNewItem(fourthItem);
        assertEquals(4, ret);

        ret = db.insertNewItem(fifthItem);
        assertEquals(5, ret);

        ret = db.insertNewItem(sixthItem);

        assertEquals(6, ret);
        
        //System.out.println("Got after assertion1 check");

    }

    /**
     * Tests insertNewItemData method by using constructor with fields
     * PASSED
     */
    public void testInsertNewItemData1() 
    {
        db.dropAllTables();
        db.createAllTables();

        //System.out.println("Got into testInsertNewItemData1 Method");
        int ret = db.insertNewItem(userId, title, description, schoolCategory, postDate,
        sellTradeMethod, price, availability, availableTime, wantedItemDescription, itemLongitude, itemLatitude,
        itemAddress, itemCity, itemState, itemZipCode);

        assertEquals(1, ret);

        ret = db.insertNewItem(userId1, title1, description1, schoolCategory, postDate,
        tradeTradeMethod, price, availability, availableTime, wantedItemDescription, itemLongitude, itemLatitude,
        itemAddress, itemCity, itemState, itemZipCode);

        assertEquals(2, ret);

        ret = db.insertNewItem(userId2, title2, description2, electronicCategory, postDate,
        sellTradeMethod, price3, availability, availableTime, wantedItemDescription, itemLongitude, itemLatitude,
        itemAddress, itemCity, itemState, itemZipCode);

        assertEquals(3, ret);

        ret = db.insertNewItem(userId3, title3, description3, carCategory, postDate,
        rentTradeMethod, price3, availability, availableTime, wantedItemDescription, itemLongitude, itemLatitude,
        itemAddress, itemCity, itemState, itemZipCode);

        assertEquals(4, ret);

        ret = db.insertNewItem(userId4, title4, description4, furnitureCategory, postDate,
        giveawayTradeMethod, price1, availability, availableTime, wantedItemDescription, itemLongitude, itemLatitude,
        itemAddress, itemCity, itemState, itemZipCode);

        assertEquals(5, ret);

        ret = db.insertNewItem(userId1, title1, description1, schoolCategory, postDate,
        rentTradeMethod, price, availability, availableTime, wantedItemDescription, itemLongitude, itemLatitude,
        itemAddress, itemCity, itemState, itemZipCode);

        assertEquals(6, ret);

        //System.out.println("Got after assertion2 check");

    }

    /**
    * Tests deleteItem method inside database
    * PASSED
    */
    public void testDeleteItem() 
    {
        db.dropAllTables();
        db.createAllTables();

        //System.out.println("Got into testDeleteItem Method");

        // //first insert a new item in
        // int ret = db.insertNewItem(firstItem);
        // assertEquals(1, ret);

        // //then delete this item
        // int deleteRet = db.deleteItem(1);
        // assertEquals(1, deleteRet);


        //ArrayList<ItemData> res = new ArrayList<ItemData>();

        db.insertNewItem(firstItem);
        
        //System.out.println(db.selectOneItem(1).itemToString1());

        db.deleteItem(1);

        assertNull(db.selectOneItem(1));

        db.insertNewItem(firstItem);

        //System.out.println(db.selectOneItem(2).itemToString1());

        db.insertNewItem(firstItem);

        //System.out.println(db.selectOneItem(3).itemToString1());

        db.insertNewItem(firstItem);

        //System.out.println(db.selectOneItem(4).itemToString1());

        db.insertNewItem(firstItem);

        db.deleteItem(3);
        db.deleteItem(4);
        assertNull(db.selectOneItem(3));
        assertNull(db.selectOneItem(4));


        //System.out.println("return of deleted item: " + db.selectOneItem(1));
        //System.out.println("return of deleted item: " + db.selectOneItem(1).itemToString1());


        //System.out.println("Got after assertion3 check");

    }

    /**
    * Tests testSelectAllItems method inside database
    * Should return an array list of ItemData
    * PASSED
    */
    public void testSelectAllItems() 
    {
        db.dropAllTables();
        db.createAllTables();

        //System.out.println("Got into testSelectAllItems Method");

        //Create an arraylist of ItemData to return
        ArrayList<ItemData> expectedArray = new ArrayList<ItemData>();
        expectedArray.add(firstItem);
        expectedArray.add(secondItem);
        expectedArray.add(thirdItem);
        expectedArray.add(fourthItem);
        expectedArray.add(fifthItem);
        expectedArray.add(sixthItem);

        //System.out.println("This is expectedArray: " + expectedArray.get(0).itemToString1());

        String expectedArrayString = null;

        //first insert a new item in
        db.insertNewItem(firstItem);
        db.insertNewItem(secondItem);
        db.insertNewItem(thirdItem);
        db.insertNewItem(fourthItem);
        db.insertNewItem(fifthItem);
        db.insertNewItem(sixthItem);
        
        //assertEquals(1, first);
        //then insert a second item in
        //int second = db.insertNewItem(secondItem);
        //assertEquals(1, second);

        ArrayList<ItemData> retArray = db.selectAllItems();
        //System.out.println("This is ret Array " + retArray.get(0).itemToString1());

        String retArrayString = null;

        //test that each item in the array corresponds to the correct items
        for(int i = 0; i < 6; i++)
        {
            expectedArrayString = expectedArray.get(i).itemToString1();
            retArrayString =  retArray.get(i).itemToString1();
            assertEquals(expectedArrayString, retArrayString);
        }

        //try deleting some items and then compare outputs of selectAll
        db.deleteItem(2);
        db.deleteItem(3);
        db.deleteItem(4);

        expectedArray.remove(secondItem);
        expectedArray.remove(thirdItem);
        expectedArray.remove(fourthItem);

        retArray = db.selectAllItems();

        for(int i = 0; i < retArray.size(); i++)
        {
            expectedArrayString = expectedArray.get(i).itemToString1();
            //System.out.println("This is expectedArray: " + i + expectedArray.get(i).itemToString1());
            retArrayString =  retArray.get(i).itemToString1();
            //System.out.println("This is ret Array " + (i) +  retArray.get(i).itemToString1());

            assertEquals(expectedArrayString, retArrayString);
        }

        //assertEquals(expectedArray, retArray); //comparing objects didnt work

        //System.out.println("Got after assertion4 check");

    }

    /**
    * Tests testSelectAllItemsFromCategorymethod inside database
    * Should return an array list of ItemData
    * PASSED
    */
    public void testSelectAllItemsFromCategory() 
    {
        db.dropAllTables();
        db.createAllTables();

        //insert items
        db.insertNewItem(firstItem);
        db.insertNewItem(secondItem);
        db.insertNewItem(thirdItem);
        db.insertNewItem(fourthItem);
        db.insertNewItem(fifthItem);
        db.insertNewItem(sixthItem);

        //String check = null;

        //check null case
        ArrayList<Integer> categories = new ArrayList<Integer>();
        categories.add(5);

        ArrayList<ItemData> res = db.selectAllItemsFromCategory(categories);
    
        //System.out.println("This is res: " + res);
        assertEquals(0, res.size());

        categories.remove(0);

        //check non null case
        categories.add(schoolCategory);
        res = db.selectAllItemsFromCategory(categories);

        assertEquals(3, res.size());

        // for(int i = 0; i < res.size(); i++)
        // {
        //     check = res.get(i).itemToString1();
        //     System.out.println("This is item: "+ i + check + " ");
        // }

        categories.add(electronicCategory);
        categories.add(furnitureCategory);
        res = db.selectAllItemsFromCategory(categories);

        // for(int i = 0; i < res.size(); i++)
        // {
        //     check = res.get(i).itemToString1();
        //     System.out.println("This is item: "+ i + check + " ");
        // }

        assertEquals(5, res.size());

        categories.remove(0);
        categories.remove(1);
        res = db.selectAllItemsFromCategory(categories);

        // for(int i = 0; i < res.size(); i++)
        // {
        //     check = res.get(i).itemToString1();
        //     System.out.println("This is item: "+ i + check + " ");
        // }

        assertEquals(1, res.size());


        //System.out.println("Got into testSelectAllItemsFromCategory Method");


        //System.out.println("Got after assertion5 check");

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

        //System.out.println("Got into testSelectAllItemsFromPrice Method");


        //System.out.println("Got after assertion6 check");

    }

    /**
    * Tests testSelecOneItem method inside database
    * Should return an ItemData
    * PASSED
    */
    public void testSelectOneItem() 
    {
        db.dropAllTables();
        db.createAllTables();

        //System.out.println("Got into testSelectOneItem Method");

        db.insertNewItem(firstItem);
        db.insertNewItem(secondItem);

        ItemData retItemData1 = db.selectOneItem(1);
        //System.out.println("This is firstItem: " + firstItem.itemToString());
        //System.out.println();
        //System.out.println("This is retItemData1: " + retItemData1.itemToString());
        //System.out.println();
        //assertEquals(firstItem, retItemData1);
        //System.out.println("Return of itemDataEquals is: " + firstItem.itemDataEquals(retItemData1));
        //System.out.println("result of comparing strings" + firstItem.itemToString().equals(retItemData1.itemToString()));
        assertTrue(firstItem.itemDataStringEquals(retItemData1));
        

        ItemData retItemData2 = db.selectOneItem(2);
        assertTrue(secondItem.itemDataStringEquals(retItemData2));
        

        //System.out.println("Got after assertion7 check");

    }

    /**
     * Tests selectByUserId method
     * should return an ArrayList of ItemData for the user
     * PASSED
     */ 
    public void testSelectByUserId()
    {
        db.dropAllTables();
        db.createAllTables();

        //System.out.println("got into selectByUserId test");
        //insert items
        db.insertNewItem(firstItem);
        db.insertNewItem(secondItem);
        db.insertNewItem(thirdItem);
        db.insertNewItem(fourthItem);
        db.insertNewItem(fifthItem);
        db.insertNewItem(sixthItem);

        ArrayList<ItemData> firstTest = new ArrayList<ItemData>();
        firstTest = db.selectByUserId(userId1);
        //System.out.println("size of firstTest is: " + firstTest.size());
        assertEquals(2, firstTest.size());

        ArrayList<ItemData> secondTest = new ArrayList<ItemData>();
        secondTest = db.selectByUserId(userId);
        //System.out.println("size of secondTest is: " + secondTest.size());
        assertEquals(1, secondTest.size());

        //check null case
        ArrayList<ItemData> thirdTest = new ArrayList<ItemData>();
        secondTest = db.selectByUserId(9);
        //System.out.println("size of thirdTest is: " + thirdTest.size());
        assertEquals(0, thirdTest.size());

    }


    //test new updateItemData function
    //Currently FAILS. Throws SQL Exception saying it doesn't update
    public void testUpdateItemData()
    {
        db.dropAllTables();
        db.createAllTables();

        //insert items
        db.insertNewItem(firstItem);
        db.insertNewItem(secondItem);

        //System.out.println("First item: " + db.selectOneItem(1).itemToString1());
        //update items

        //update firstItem to become thirdItem

        db.updateItemData(thirdItem);
        //System.out.println("Updated first item: " + db.selectOneItem(1).itemToString1());




    }

    //test overloaded params itemPerPage and pageNum
    //SEEMS TO WORK AS INTENDED. PASS 
    public void testSelectAllItems1()
    {
        db.dropAllTables();
        db.createAllTables();

        //insert items
        db.insertNewItem(firstItem);
        db.insertNewItem(secondItem);
        db.insertNewItem(thirdItem);
        db.insertNewItem(fourthItem);
        db.insertNewItem(fifthItem);
        db.insertNewItem(sixthItem);

        //This should return an array list of item data that 
        //are the first two items
        ArrayList<ItemData> firstTwo = db.selectAllItems(2, 1);
        String check = null;

        for(int i = 0; i < 2; i++)
        {
             check = firstTwo.get(i).itemToString1();
             System.out.println("This is firstTwo: "+ i + " " + check + " ");
        }

        //This should return an array list of item data that are
        //The next two items
        ArrayList<ItemData> secondTwo = db.selectAllItems(2 , 2);

        for(int i = 0; i < 2; i++)
        {
             check = secondTwo.get(i).itemToString1();
             System.out.println("This is secondTwo: " + check + " ");
        }

        //This should return an array list of item data that are
        //The last two items
        ArrayList<ItemData> lastTwo = db.selectAllItems(2 , 3);
        for(int i = 0; i < 2; i++)
        {
             check = lastTwo.get(i).itemToString1();
             System.out.println("This is lastTwo: " + check + " ");
        }

        //This should return an array list of item data that are the
        //first 3
        ArrayList<ItemData> firstThree = db.selectAllItems(3, 1);
        for(int i = 0; i < 3; i++)
        {
             check = firstThree.get(i).itemToString1();
             System.out.println("This is firstThree: " + check + " ");
        }

        //This should return an array list of item data that are the
        //last 3
        ArrayList<ItemData> lastThree = db.selectAllItems(3, 2);
        for(int i = 0; i < 3; i++)
        {
             check = lastThree.get(i).itemToString1();
             System.out.println("This is lastThree: " + check + " ");
        }

        //This should return an array list of item data that shows all
        ArrayList<ItemData> all = db.selectAllItems(8, 1);
        for(int i = 0; i < 6; i++)
        {
             check = all.get(i).itemToString1();
             System.out.println("This is all: " + check + " ");
        }



    
    }

    //test overloaded params itemPerPage and pageNum
    public void testSelectAllItemsFromCategory1()
    {
        //db.dropAllTables();
        //db.createAllTables();
    }

    //test overloaded params itemPerPage and pageNum
    public void testSelectAllItemsFromPrice1()
    {
        // db.dropAllTables();
        // db.createAllTables();
    }

    //test overloaded params itemPerPage and pageNum
    public void testSelectByUserId1()
    {
        // db.dropAllTables();
        // db.createAllTables();  
    }






}
