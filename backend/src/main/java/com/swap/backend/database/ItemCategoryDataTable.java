package com.swap.backend.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * ItemData will represent the basic data that will be used for any given item
 */
public class ItemCategoryDataTable {




    /**
     * Create the item table. If it already exists, this will print an error
     */
    void createItemCategoryDataTable() {

        try {
            Database.p_createItemCategoryDataTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove the item table from the database. If it does not exist, this will
     * print an error.
     */
    void dropItemCategoryDataTable() {
        try {
            Database.p_dropItemCategoryDataTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}