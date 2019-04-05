package com.swap.localServer;

import java.util.*;
import spark.Spark;
import com.google.gson.*;
import com.swap.database.*;

public class App {

    public static void main(String[] args) {
        String static_location_override = System.getenv("STATIC_LOCATION");
        if (static_location_override == null) {
            Spark.staticFileLocation("/web");
        } else {
            Spark.staticFiles.externalLocation(static_location_override);
        }
        final Gson gson = new Gson();
        ItemArrayList items = new ItemArrayList();

        Spark.get("/hello", (request, response) -> {
            return "Hello World!";
        });
        Spark.get("/", (req, res) -> {
            res.redirect("/index.html");
            return "";
        });

        Spark.get("/item/all", (request, response) -> {
            String[] categories = { "sss", "sss" };
            ArrayList<ItemData> selectAll = new ArrayList<ItemData>();
            selectAll.add(new ItemData(1, "logitech mouse", "brand new", "Sheldon", 10.0f, categories));
            selectAll.add(new ItemData(2, "Ferrari 488", "brand new", "Sheldon", 200000.0f, categories));
            selectAll.add(new ItemData(3, "GTX 1060", "near broken", "Xiaowei", 15.0f, categories));
            selectAll.add(new ItemData(4, "Econ001 textbook", "half new", "Xiaowei", 30.0f, categories));
            selectAll.add(new ItemData(5, "Coolermaster keyboard", "brand new", "Lixuan Qiu", 35.0f, categories));
            selectAll.add(new ItemData(6, "Desktop", "80% new", "Lixuan Qiu", 25.0f, categories));
            selectAll.add(new ItemData(7, "Range Rover Sport", "half new", "Allen", 45000.0f, categories));
            selectAll.add(new ItemData(8, "Microfridge", "brand new", "Allen", 30.0f, categories));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(items.getAll());
        });
        Spark.get("/item/school", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON

            response.status(200);
            response.type("application/json");
            return gson.toJson(items.getAnimal());
        });
        Spark.get("/itemList/:id", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON

            response.status(200);
            response.type("application/json");
            return gson.toJson(items.getAnimal());
        });

    }
}
