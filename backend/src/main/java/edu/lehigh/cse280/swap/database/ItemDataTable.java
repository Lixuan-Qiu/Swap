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
                res.add(new ItemData(rs.getInt("itemId"), rs.getInt("userId"), rs.getString("title"), rs.getString("description"),
                        rs.getInt("category"), rs.getInt("tradingInfoId"), rs.getInt("postDate")));
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
                res.add(new ItemData(rs.getInt("itemId"), rs.getInt("userId"), rs.getString("title"), rs.getString("description"),
                rs.getInt("category"), rs.getInt("tradingInfoId"), rs.getInt("postDate")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query the database for a list of all items from some categories
     * 
     * @param categories the category on which the items will be filtered
     * @return All rows, as an ArrayList
     */
    public ArrayList<ItemData> selectAllItemFrom(ArrayList<Integer> categories) {
        ArrayList<ItemData> res = new ArrayList<ItemData>();
        ArrayList<Integer> tradingInfoDataidList = new ArrayList<Integer>();
        try {
            Database.p_selectAllFrom.setArray(1, Database.ConvertToIntArray(categories));
            ResultSet rs = Database.p_selectAllFrom.executeQuery();
            while (rs.next()) {
                tradingInfoDataidList.add(rs.getInt("tradingInfoId"));
                res.add(new ItemData(rs.getInt("itemId"), rs.getInt("userId"), rs.getString("title"), rs.getString("description"),
                rs.getInt("category"), rs.getInt("tradingInfoId"), rs.getInt("postDate")));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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
                res = new ItemData(rs.getInt("itemId"), rs.getInt("userId"), rs.getString("title"), rs.getString("description"),
                rs.getInt("category"), rs.getInt("tradingInfoId"), rs.getInt("postDate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * insert a item for a row in the database
     * 
     * @param title         The title for the item
     * @param description   The description of the item
     * @param userId        The id of the person posting this item
     * @param tradingInfoId The id of table entry which includes all trading info about this item
     * @param categories    The int array with all categories
     * @param postDate      The post date of this item
     * 
     * @return The number of rows that were updated. -1 indicates an error.
     */
    public int insertNewItemData(int userId, String title, String description, int category, int postDate) {
        int count = 0;
        try {
            Database.p_insertNewItemData.setInt(1, userId);
            Database.p_insertNewItemData.setString(2, title);
            Database.p_insertNewItemData.setString(3, description);
            Database.p_insertNewItemData.setInt(4, category);
            Database.p_insertNewItemData.setInt(5, 0);            
            Database.p_insertNewItemData.setInt(6, postDate);
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