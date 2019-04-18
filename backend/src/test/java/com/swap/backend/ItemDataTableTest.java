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
    String user = "allen123";
    String pass = "liu123";

    Database db = Database.getDatabase(ip, port, user, pass);

    


    /**
     * Tests insertNewItemData method
     */
    public void testInsertNewItemData() {
        assert (true);

    }
}
