package edu.lehigh.cse280.swap.admin;

import edu.lehigh.cse280.swap.database.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Map;

/**
 * App is our basic admin app. For now, it is a demonstration of the six key
 * operations on a database: connect, insert, update, query, delete, disconnect
 */
public class App {

    /**
     * Print the menu for our program
     */
    static void menu() {
        System.out.println("Main Menu");
        System.out.println("  [T] Create tblData");
        System.out.println("  [D] Drop tblData");
        System.out.println("  [1] Query for a specific row");
        System.out.println("  [*] Query for all rows");
        System.out.println("  [-] Delete a row");
        System.out.println("  [+] Insert a new row");
        System.out.println("  [~] Update a row");
        System.out.println("  [q] Quit Program");
        System.out.println("  [?] Help (this message)");
    }

    /**
     * Ask the user to enter a menu option; repeat until we get a valid option
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * 
     * @return The character corresponding to the chosen menu option
     */
    static char prompt(BufferedReader in) {
        // The valid actions:
        String actions = "TD1*-+~q?";

        // We repeat until a valid single-character option is selected
        while (true) {
            System.out.print("[" + actions + "] :> ");
            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

    /**
     * Ask the user to enter a String message
     * 
     * @param in      A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The string that the user provided. May be "".
     */
    static String getString(BufferedReader in, String message) {
        String s;
        try {
            System.out.print(message + " :> ");
            s = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return s;
    }

    /**
     * Ask the user to enter a Float message
     * 
     * @param in      A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The float that the user provided. On error, it will be -1.
     */
    static float getFloat(BufferedReader in, String message) {
        float i = -1;
        try {
            System.out.print(message + " :> ");
            i = Float.parseFloat(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * Ask the user to enter an integer
     * 
     * @param in      A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The integer that the user provided. On error, it will be -1
     */
    static int getInt(BufferedReader in, String message) {
        int i = -1;
        try {
            System.out.print(message + " :> ");
            i = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * Ask the user to enter an integer array delimited by space
     * 
     * @param in      A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The integer array that the user provided. On error, it will be -1
     */
    static int[] getIntArray(BufferedReader in, String message) {
        int i[];
        try {
            System.out.print(message + " :> ");
            String raw[] = in.readLine().split(" ");
            i = new int[raw.length];
            for (int j = 0; j < raw.length; j++)
                i[j] = Integer.parseInt(raw[j]);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * The main routine runs a loop that gets a request from the user and processes
     * it
     * 
     * @param argv Command-line options. Ignored by this program.
     */
    public static void main(String[] argv) {

        // set database url
        String db_url = "postgres://cblibdzhsvqshl:d48272553306c7cbc287d6f9a97550f9bdd98153a87caeac5d4b98ac0cd59438@ec2-23-21-130-182.compute-1.amazonaws.com:5432/d3pvjv0qor9898";

        // Get a fully-configured connection to the database
        Database db = Database.getDatabase(db_url);
        if (db == null)
            return;

        // Start our basic command-line interpreter:
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Get the user's request, and do it
            //
            // NB: for better testability, each action should be a separate
            // function call
            char action = prompt(in);
            if (action == '?') {
                menu();
            } else if (action == 'q') {
                break;
            } else if (action == 'T') {
                db.createAllTables();
            } else if (action == 'D') {
                db.dropAllTables();
            } else if (action == '1') {
                int id = getInt(in, "Enter the item ID you want to query");
                if (id <= 0)
                    continue;
                ItemData res = db.selectOneItem(id);
                if (res != null) {
                    System.out.println(
                            "  [" + res.itemId + "] " + " User: " + res.itemSeller + " Price: " + res.itemPrice);
                    System.out.println("  --> " + res.itemDescription);
                } else {
                    System.out.println("Item returned is null, exiting ...");
                    return;
                }
            } else if (action == '*') {
                ArrayList<ItemData> res = db.selectAllItems();
                if (res == null)
                    continue;
                System.out.println("  Current Database Contents");
                System.out.println("  -------------------------");
                for (ItemData rd : res) {
                    if (rd != null) {
                        System.out.println(
                                "  [" + res.itemId + "] " + " User: " + res.itemSeller + " Price: " + res.itemPrice);
                        System.out.println("  --> " + res.itemDescription);
                    }
                }
            } else if (action == '-') {
                int id = getInt(in, "Enter the item ID");
                if (id == -1)
                    continue;
                int res = db.deleteItem(id);
                if (res == -1)
                    continue;
                System.out.println("item " + res + " deleted");
            } else if (action == '+') {
                String title = getString(in, "Enter the item title");
                String description = getString(in, "Enter the item description");
                float inputPrice = getFloat(in, "Enter the item price");
                int userID = getInt(in, "Enter the user ID who posted this item");
                int category[] = getIntArray(in, "Enter the category int array delimited by space");
                int postdate = getInt(in, "Enter the date when the item was posted");

                if (title.equals("") || description.equals("") || inputPrice < 0 || userID < 0 || postdate < 0)
                    continue;
                int res = db.insertRow(subject, message);
                System.out.println(res + " rows added");
            } else if (action == '~') {
                String title = getString(in, "Enter the field you want to change, available fields are: ");
                int id = getInt(in, "Enter the row ID :> ");
                if (id == -1)
                    continue;
                String newMessage = getString(in, "Enter the new message");
                int res = db.updateOne(id, newMessage);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");
            }
        }
        // Always remember to disconnect from the database when the program
        // exits
        db.disconnect();
    }
}