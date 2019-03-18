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

    public ArrayList<ItemData> selectAllCar() {
        try {
            ResultSet rs = Database.p_selectAllCar.executeQuery();
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

    public ArrayList<ItemData> selectAllFurniture() {
        try {
            ResultSet rs = Database.p_selectAllFurniture.executeQuery();
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

    public ArrayList<ItemData> selectAllElectronic() {
        try {
            ResultSet rs = Database.p_selectAllCar.executeQuery();
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

    public ArrayList<ItemData> selectAllSchool() {
        try {
            ResultSet rs = Database.p_selectAllCar.executeQuery();
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