package edu.lehigh.cse280.swap.database;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * ItemData will represent the basic data that will be used for any given item
 */
public class ItemCategoryDataTable {
    ItemCategoryDataTable() {

    }
/*
    public int insertNewItemCategoryData(int itemId, String categories) {
        int count = 0;
        try {
            Database.p_insertNewItemCategoryData.setInt(1, itemId);
            Database.p_insertNewItemCategoryData.setString(2, categories);
            count += Database.p_insertNewItemCategoryData.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return count;
    }

    public int deleteItemCategoryData(int itemId) {
        int count = 0;
        try {
            Database.p_deleteItemCategoryData.setInt(1, itemId);
            count += Database.p_deleteItemCategoryData.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return count;
    }

    public ArrayList<ItemData> selectAllFrom(String category) {
        try {
            Database.p_selectAllFrom.setString(1, category);
            ResultSet rs = Database.p_selectAllFrom.executeQuery();
            ArrayList<Integer> idList_1 = new ArrayList<Integer>();
            while (rs.next()) {
                idList_1.add(rs.getInt("id"));
            }
            rs.close();
            Array idList = Database.ConvertToArray(idList_1);
            return Database.getItemDT().selectAllItemDatabyId(idList);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create the item table. If it already exists, this will print an error
     */
    public void createItemCategoryDataTable() {

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
    public void dropItemCategoryDataTable() {
        try {
            Database.p_dropItemCategoryDataTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}