package com.swap.backend;

import java.util.ArrayList;
import java.util.Map;
import java.util.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import edu.lehigh.cse280.swap.database.*;
/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{

    //String db_url = env.get("DATABASE_URL");
    //"postgres://rkayqmkvmxeeag:87ae71c28cfa14356fb57c0491e10a3b850ca16a6799ddc16706864c8c67c9e8@ec2-174-129-35-61.compute-1.amazonaws.com:5432/d2791nafr3gtkc"
    // Map<String, String> env = System.getenv();
    // String db_url = env.get("DATABASE_URL");
    Database db = Database.getDatabase("postgres://rkayqmkvmxeeag:87ae71c28cfa14356fb57c0491e10a3b850ca16a6799ddc16706864c8c67c9e8@ec2-174-129-35-61.compute-1.amazonaws.com:5432/d2791nafr3gtkc");

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        System.out.println("\n\n\nBEGIN TEST\n\n\n");
        if(db == null){
            return;
        }
        db.dropAllTables();
        db.createAllTables();

        ItemData[] testunits1 = new ItemData[4];
        String[] category = {"a", "b", "c"};
        for(int i = 0; i < 4; i++){
            testunits1[i] = new ItemData(i, "Title", "Description", "Seller", 5.20f, category);
        }
        db.insertNewItem("Title", "Description", "Seller", 5.20f, category);
        System.out.println(testunits1.toString());
        assertEquals(testunits1[0], db.selectOneItem(0));
        ArrayList<ItemData> resData = db.selectAllItems();
        assertEquals(testunits1[0], resData);
        assertEquals(0, db.deleteItem(0));

    }
}
