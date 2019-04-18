package edu.lehigh.cse280.swap.app;

import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// Import Google's JSON library
import com.google.gson.*;

import org.apache.http.*;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.*;
import org.elasticsearch.client.*;

import edu.lehigh.cse280.swap.database.*;
// Import the Spark package, so that we can make use of the "get" function to 
import spark.Spark;
import edu.lehigh.cse280.swap.database.Database;
import edu.lehigh.cse280.swap.database.ItemData;

/**
 * For now, our app creates an HTTP server that can only get and add data.
 */
public class App {
    public static void main(String[] args) {

    }

    /**
     * For Heroku's purpose
     *
     * Get an integer environment varible if it exists, and otherwise return the
     * default value.
     * 
     * @envar The name of the environment variable to get.
     * @defaultVal The integer value to use as the default if envar isn't found
     * 
     * @returns The best answer we could come up with for a value for envar
     */
    static int getIntFromEnv(String envar, int defaultVal) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get(envar) != null) {
            return Integer.parseInt(processBuilder.environment().get(envar));
        }
        return defaultVal;
    }

    static String[] getStringArrayFromText(String text) {
        String[] categories = text.split("-");
        return categories;
    }
}