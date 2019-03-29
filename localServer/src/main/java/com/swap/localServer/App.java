package com.swap.localServer;
import java.util.*;
import spark.Spark;
import com.google.gson.*;


public class App {

    public static void main(String[] args) {
        /*
        String static_location_override = System.getenv("STATIC_LOCATION");
        if (static_location_override == null) {
            Spark.staticFileLocation("/web");
        } else {
            Spark.staticFiles.externalLocation(static_location_override);
        }
        */
        final Gson gson = new Gson();
        ItemArrayList items = new ItemArrayList();
        
        Spark.get("/hello", (request, response) -> {
            return "Hello World!";
        });
        Spark.get("/", (req, res) -> {
            res.redirect("/index.html");
            return "";
        });
        /*
        Spark.get("/itemlist/all", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(items.getAll());
        });
        Spark.get("/itemist/:school", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(items.getAnimal());
        });
        */
    }
}
