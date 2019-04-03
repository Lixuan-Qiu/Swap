package com.swap.backend.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * ItemData will represent the basic data that will be used for any given item
 */
public class ItemData {
    /**
     * The ID of the item
     */
    int itemId;

    /**
     * The title of the item
     */
    String itemTitle;

    /**
     * The description of the item
     */
    String itemDescription;

    /**
     * The username of the seller of the item
     */
    String itemSeller;

    /**
     * The seller of the item
     */
    float itemPrice;

    /**
     * Construct a ItemData object by providing values for its fields
     */
    public ItemData(int id, String title, String description, String seller, float price) {
        itemId = id;
        itemTitle = title;
        itemDescription = description;
        itemSeller = seller;
        itemPrice = price;
    }

     // ---------------------Methods for ItemData---------------------

    /**
     * Query the database for a list of all items in our homepage along with its
     * description
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<ItemData> selectAllItems() {
        ArrayList<ItemData> res = new ArrayList<ItemData>();
        try {
            ResultSet rs = Database.p_selectAllItems.executeQuery();
            while (rs.next()) {
                res.add(new ItemData(rs.getInt("id"), rs.getString("title"), null, null, 0.f));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all data for a specific item
     * 
     * @param id The id of the item being requested
     * 
     * @return The data for the requested item, or null if the ID was invalid
     */
    ItemData selectOneItem(int id) {
        ItemData res = null;
        try {
            Database.p_selectOneItem.setInt(1, id);
            ResultSet rs = Database.p_selectOneItem.executeQuery();
            if (rs.next()) {
                res = new ItemData(rs.getInt("id"), rs.getString("title"), rs.getString("description"),
                        rs.getString("seller"),  rs.getFloat("price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * insert a item for a row in the database
     * 
     * @param title  The title for the item
     * @param description  The description of the item 
     * @param seller The username of the person posting
     * @param price   The price of the item
     * 
     * @return The number of rows that were updated. -1 indicates an error.
     */
    int createNewItem(String title, String description, String seller, float price) {
        int count = 0;
        try {
            Database.p_createNewItem.setString(1, title);
            Database.p_createNewItem.setString(2, description);
            Database.p_createNewItem.setString(3, seller);
            Database.p_createNewItem.setFloat(4, price);
            count += Database.p_createNewItem.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Delete a item by id
     * 
     * @param id The id of the item to delete
     * 
     * @return The number of rows that were deleted. -1 indicates an error.
     */
    int deleteItem(int id) {
        int res = -1;
        try {
            Database.p_deleteOneItem.setInt(1, id);
            res = Database.p_deleteOneItem.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Create the item table. If it already exists, this will print an error
     */
    void createItemTable() {

        try {
            Database.p_createItemTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove the item table from the database. If it does not exist, this will
     * print an error.
     */
    void dropItemTable() {
        try {
            Database.p_dropItemTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}