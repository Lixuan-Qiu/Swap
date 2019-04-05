package edu.lehigh.cse280.swap.database;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * ItemData will represent the basic data that will be used for any given item
 */
public class ItemDataTable {
    ItemDataTable() {

    }
    // ---------------------Methods for ItemData---------------------

    /**
     * Query the database for a list of all items in our homepage along with its
     * description
     * 
     * @return All rows, as an ArrayList
     */
    public ArrayList<ItemData> selectAllItems() {
        ArrayList<ItemData> res = new ArrayList<ItemData>();
        try {
            ResultSet rs = Database.p_selectAllItemData.executeQuery();
            while (rs.next()) {
                res.add(new ItemData(rs.getInt("id"), rs.getString("title"), rs.getString("description"),
                        rs.getString("seller"), rs.getFloat("price"), (String[]) (rs.getArray("category")).getArray()));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query the database for a list of all items in our homepage along with its
     * description
     * 
     * @return All rows, as an ArrayList
     */
    public ArrayList<ItemData> selectAllItemDatabyId(Array idList) {
        ArrayList<ItemData> res = new ArrayList<ItemData>();
        try {
            Database.p_selectAllItemDataById.setArray(1, idList);
            ResultSet rs = Database.p_selectAllItemDataById.executeQuery();
            while (rs.next()) {
                res.add(new ItemData(rs.getInt("id"), rs.getString("title"), rs.getString("description"),
                        rs.getString("seller"), rs.getFloat("price"), (String[]) (rs.getArray("category")).getArray()));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query the database for a list of all items in our homepage along with its
     * description
     * 
     * @return All rows, as an ArrayList
     */
    public ArrayList<ItemData> selectAllItemFrom(String[] categories) {
        ArrayList<ItemData> res = new ArrayList<ItemData>();
        int length = categories.length;
        ArrayList<Integer> idList = new ArrayList<Integer>();
        try {
            Database.p_selectAllFrom.setString(1, categories[0]);
            ResultSet rs = Database.p_selectAllFrom.executeQuery();
            while (rs.next()) {
                idList.add(rs.getInt("id"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        for (int i = 1; i < length; i++) {
            try {
                Database.p_selectAllFrom.setString(1, categories[i]);
                ResultSet rs = Database.p_selectAllFrom.executeQuery();
                ArrayList<Integer> idList_1 = new ArrayList<Integer>();
                while (rs.next()) {
                    idList_1.add(rs.getInt("id"));
                }
                rs.close();
                idList.retainAll(idList_1);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        Array idList_2 = Database.ConvertToIntArray(idList);
        res = selectAllItemDatabyId(idList_2);
        return res;
    }

    /**
     * Get all data for a specific item
     * 
     * @param id The id of the item being requested
     * 
     * @return The data for the requested item, or null if the ID was invalid
     */
    public ItemData selectOneItem(int id) {
        ItemData res = null;
        try {
            Database.p_selectOneItemData.setInt(1, id);
            ResultSet rs = Database.p_selectOneItemData.executeQuery();
            if (rs.next()) {
                res = new ItemData(rs.getInt("id"), rs.getString("title"), rs.getString("description"),
                        rs.getString("seller"), rs.getFloat("price"), (String[]) (rs.getArray("category")).getArray());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * insert a item for a row in the database
     * 
     * @param title       The title for the item
     * @param description The description of the item
     * @param seller      The username of the person posting
     * @param price       The price of the item
     * 
     * @return The number of rows that were updated. -1 indicates an error.
     */
    public int insertNewItemData(String title, String description, String seller, double price, String[] categories) {
        int count = 0;
        try {
            Database.p_insertNewItemData.setString(1, title);
            Database.p_insertNewItemData.setString(2, description);
            Database.p_insertNewItemData.setString(3, seller);
            Database.p_insertNewItemData.setDouble(4, price);
            Database.p_insertNewItemData.setArray(5, Database.ConvertToStringArray(categories));
            count += Database.p_insertNewItemData.executeUpdate();
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
    public int deleteItem(int id) {
        int res = -1;
        try {
            Database.p_deleteOneItemData.setInt(1, id);
            res = Database.p_deleteOneItemData.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Create the item table. If it already exists, this will print an error
     */
    public void createItemDataTable() {

        try {
            Database.p_createItemDataTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove the item table from the database. If it does not exist, this will
     * print an error.
     */
    public void dropItemDataTable() {
        try {
            Database.p_dropItemDataTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}