package edu.lehigh.cse280.swap.database;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class contains all PostgreSQL functions that deal with trading info data table
 * Currently is untested {@TODO} make sure code works
 */
public class TradingInfoDataTable{
    TradingInfoDataTable() {

    }
    // ---------------------Methods for ItemData---------------------

    /**
     * Query the database for a list of all trading info data in the database
     * 
     * @return All rows, as an ArrayList<TradingData>
     */
    public ArrayList<TradingData> selectAllTradingInfoData() {
        ArrayList<TradingData> res = new ArrayList<TradingData>();
        try {
            ResultSet rs = Database.p_selectAllTradingInfoData.executeQuery();
            while (rs.next()) {
                res.add(new TradingData(rs.getInt("tradingInfoId"), rs.getInt("itemId"), rs.getInt("tradingMethod"), rs.getFloat("price"),
                        rs.getBoolean("availability"), rs.getString("availableTime"), rs.getString("wantedItemDescription")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Query the database for a list of all trading info data with provided idList in the database
     * 
     * @param idList the idList for which the data will be selected base upon
     * @return All rows, as an ArrayList<TradingData>
     */
    public ArrayList<TradingData> selectAllItemDatabyId(Array idList) {
        ArrayList<TradingData> res = new ArrayList<TradingData>();
        try {
            Database.p_selectAllTradingInfoDataById.setArray(1, idList);
            ResultSet rs = Database.p_selectAllTradingInfoDataById.executeQuery();
            while (rs.next()) {
                res.add(new TradingData(rs.getInt("tradingInfoId"), rs.getInt("itemId"), rs.getInt("tradingMethod"), rs.getFloat("price"),
                        rs.getBoolean("availability"), rs.getString("availableTime"), rs.getString("wantedItemDescription")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all data for a specific row of trading info data
     * 
     * @param id The id of the trading info data being requested
     * 
     * @return The trading info data for the requested item, or null if the ID was invalid
     */
    public TradingData selectOneTradingInfo(int id) {
        TradingData res = null;
        try {
            Database.p_selectOneTradingInfoData.setInt(1, id);
            ResultSet rs = Database.p_selectOneTradingInfoData.executeQuery();
            if (rs.next()) {
                res = (new TradingData(rs.getInt("tradingInfoId"), rs.getInt("itemId"), rs.getInt("tradingMethod"), rs.getFloat("price"),
                        rs.getBoolean("availability"), rs.getString("availableTime"), rs.getString("wantedItemDescription")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * insert a row of trading info data to the database
     * 
     * @param itemId           The title for the item
     * @param tradingMethod    The description of the item
     * @param price            The id of the person posting this item
     * @param availability     The id of table entry which includes all trading info about this item
     * @param availableTime    The int array with all categories
     * @param wantedItemDescription  The post date of this item
     * 
     * @return The number of rows that were updated. -1 indicates an error.
     */
    public int insertNewTradingInfoData(int itemId, int tradingMethod, float price, boolean availability, String availableTime, String wantedItemDescription) {
        int count = 0;
        try {
            Database.p_insertNewTradingInfoData.setInt(1, itemId);
            Database.p_insertNewTradingInfoData.setInt(2, tradingMethod);
            Database.p_insertNewTradingInfoData.setFloat(3, price);
            Database.p_insertNewTradingInfoData.setBoolean(4, availability);
            Database.p_insertNewTradingInfoData.setString(5, availableTime);
            Database.p_insertNewTradingInfoData.setString(6, wantedItemDescription);
            count += Database.p_insertNewItemData.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Delete one row of trading info data by id
     * 
     * @param id The id of the item to delete
     * 
     * @return The number of rows that were deleted. -1 indicates an error.
     */
    public int deleteTradingInfoData(int id) {
        int res = -1;
        try {
            Database.p_deleteTradingInfoData.setInt(1, id);
            res = Database.p_deleteOneItemData.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Create the trading info data table. If it already exists, this will print an error
     */
    public void createTradingInfoDataTable() {

        try {
            Database.p_createTradingInfoDataTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove the trading info data table from the database. If it does not exist, this will
     * print an error.
     */
    public void dropTradingInfoDataTable() {
        try {
            Database.p_dropTradingInfoDataTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}