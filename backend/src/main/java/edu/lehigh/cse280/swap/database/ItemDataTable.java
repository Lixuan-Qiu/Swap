package edu.lehigh.cse280.swap.database;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import static java.lang.Math.toIntExact;

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
                rs.getInt("category"), rs.getInt("postDate"), rs.getInt("tradeMethod"), rs.getFloat("price"), rs.getBoolean("availability"), 
                rs.getString("availableTime"), rs.getString("wantedItemDescription"),rs.getFloat("longitude"), rs.getFloat("latitude"), rs.getString("address"), 
                rs.getString("city"), rs.getString("state"), rs.getInt("zipcode")));
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
     * @param itemPerPage how many items to get per page
     * @param pageNum which page to start from
     * @return All rows, as an ArrayList
     */
    public ArrayList<ItemData> selectAllItems(int itemPerPage, int pageNum) {
        ArrayList<ItemData> res = new ArrayList<ItemData>();
        try {
            ResultSet rs = Database.p_selectAllItemData.executeQuery();
            while (rs.next()) {
                res.add(new ItemData(rs.getInt("itemId"), rs.getInt("userId"), rs.getString("title"), rs.getString("description"), 
                rs.getInt("category"), rs.getInt("postDate"), rs.getInt("tradeMethod"), rs.getFloat("price"), rs.getBoolean("availability"), 
                rs.getString("availableTime"), rs.getString("wantedItemDescription"),rs.getFloat("longitude"), rs.getFloat("latitude"), rs.getString("address"), 
                rs.getString("city"), rs.getString("state"), rs.getInt("zipcode")));
            }
            rs.close();
        
        ArrayList<ItemData> limitedItems = new ArrayList<ItemData>();

        if(itemPerPage > res.size())
        {
            itemPerPage = res.size();

            return res;
        }
        else
        {
    
            for(int i = (itemPerPage * pageNum) - itemPerPage; i < itemPerPage * pageNum; i++)
            {
                limitedItems.add(res.get(i));   
            }
        }
        return limitedItems;

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
                rs.getInt("category"), rs.getInt("postDate"), rs.getInt("tradeMethod"), rs.getFloat("price"), rs.getBoolean("availability"), 
                rs.getString("availableTime"), rs.getString("wantedItemDescription"),rs.getFloat("longitude"), rs.getFloat("latitude"), 
                rs.getString("address"), rs.getString("city"), rs.getString("state"), rs.getInt("zipcode")
                ));
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
     * @param itemPerPage how many items to get per page
     * @param pageNum which page to start from
     * @return All rows, as an ArrayList
     */
    public ArrayList<ItemData> selectAllItemDatabyId(Array idList, int itemPerPage, int pageNum) {
        ArrayList<ItemData> res = new ArrayList<ItemData>();
        try {
            Database.p_selectAllItemDataById.setArray(1, idList);
            ResultSet rs = Database.p_selectAllItemDataById.executeQuery();
            while (rs.next()) {
                res.add(new ItemData(rs.getInt("itemId"), rs.getInt("userId"), rs.getString("title"), rs.getString("description"), 
                rs.getInt("category"), rs.getInt("postDate"), rs.getInt("tradeMethod"), rs.getFloat("price"), rs.getBoolean("availability"), 
                rs.getString("availableTime"), rs.getString("wantedItemDescription"),rs.getFloat("longitude"), rs.getFloat("latitude"), 
                rs.getString("address"), rs.getString("city"), rs.getString("state"), rs.getInt("zipcode")
                ));
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
   public ArrayList<ItemData> selectAllItemFromCategory(ArrayList<Integer> categories) {
       ArrayList<ItemData> res = new ArrayList<ItemData>();
       try {
           Database.p_selectAllFromCategory.setArray(1, Database.ConvertToIntArray(categories));
           ResultSet rs = Database.p_selectAllFromCategory.executeQuery();
           while (rs.next()) {
               res.add(new ItemData(rs.getInt("itemId"), rs.getInt("userId"), rs.getString("title"), 
               rs.getString("description"), rs.getInt("category"), rs.getInt("postDate"), 
               rs.getInt("tradeMethod"), rs.getFloat("price"), rs.getBoolean("availability"), 
               rs.getString("availableTime"), rs.getString("wantedItemDescription"), 
               rs.getFloat("longitude"), rs.getFloat("latitude"), rs.getString("address"), rs.getString("city"), 
               rs.getString("state"), rs.getInt("zipcode")
               ));
           }

           rs.close();
       } catch (SQLException e) {
           e.printStackTrace();
           return null;
       }
       return res;
   }

    /**
     * Query the database for a list of all items from some categories
     * @param itemPerPage how many items to get per page
     * @param pageNum which page to start from
     * @param categories the category on which the items will be filtered
     * @return All rows, as an ArrayList
     */
    public ArrayList<ItemData> selectAllItemFromCategory(ArrayList<Integer> categories, int itemPerPage, int pageNum) {
        ArrayList<ItemData> res = new ArrayList<ItemData>();
        try {
            Database.p_selectAllFromCategory.setArray(1, Database.ConvertToIntArray(categories));
            ResultSet rs = Database.p_selectAllFromCategory.executeQuery();
            while (rs.next()) {
                res.add(new ItemData(rs.getInt("itemId"), rs.getInt("userId"), rs.getString("title"), 
                rs.getString("description"), rs.getInt("category"), rs.getInt("postDate"), 
                rs.getInt("tradeMethod"), rs.getFloat("price"), rs.getBoolean("availability"), 
                rs.getString("availableTime"), rs.getString("wantedItemDescription"), 
                rs.getFloat("longitude"), rs.getFloat("latitude"), rs.getString("address"), rs.getString("city"), 
                rs.getString("state"), rs.getInt("zipcode")
                ));
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }

    /**
     * Query the database for a list of all items from some price range
     * 
     * @param price the price on which the items will be filtered
     * @return All rows, as an ArrayList
     */
    public ArrayList<ItemData> selectAllItemFromPrice(float low, float high) {
        ArrayList<ItemData> res = new ArrayList<ItemData>();
        try {
            Database.p_selectAllFromPrice.setFloat(1, low);
            Database.p_selectAllFromPrice.setFloat(2, high);
            ResultSet rs = Database.p_selectAllFromPrice.executeQuery();
            while (rs.next()) 
            {
                res.add(new ItemData(rs.getInt("itemId"), rs.getInt("userId"), rs.getString("title"), rs.getString("description"), rs.getInt("category"), rs.getInt("postDate"), 
                rs.getInt("tradeMethod"), rs.getFloat("price"), rs.getBoolean("availability"), rs.getString("availableTime"), rs.getString("wantedItemDescription"), 
                rs.getFloat("longitude"), rs.getFloat("latitude"), rs.getString("address"), rs.getString("city"), rs.getString("state"), rs.getInt("zipcode")
                ));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }

    /**
     * Query the database for a list of all items from some price range
     * @param itemPerPage how many items to get per page
     * @param pageNum which page to start from
     * @param price the price on which the items will be filtered
     * @return All rows, as an ArrayList
     */
    public ArrayList<ItemData> selectAllItemFromPrice(float low, float high, int itemPerPage, int pageNum) {
        ArrayList<ItemData> res = new ArrayList<ItemData>();
        try {
            Database.p_selectAllFromPrice.setFloat(1, low);
            Database.p_selectAllFromPrice.setFloat(2, high);
            ResultSet rs = Database.p_selectAllFromPrice.executeQuery();
            while (rs.next()) 
            {
                res.add(new ItemData(rs.getInt("itemId"), rs.getInt("userId"), rs.getString("title"), rs.getString("description"), rs.getInt("category"), rs.getInt("postDate"), 
                rs.getInt("tradeMethod"), rs.getFloat("price"), rs.getBoolean("availability"), rs.getString("availableTime"), rs.getString("wantedItemDescription"), 
                rs.getFloat("longitude"), rs.getFloat("latitude"), rs.getString("address"), rs.getString("city"), rs.getString("state"), rs.getInt("zipcode")
                ));
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
                rs.getInt("category"), rs.getInt("postDate"), rs.getInt("tradeMethod"), rs.getFloat("price"), 
                rs.getBoolean("availability"), rs.getString("availableTime"), rs.getString("wantedItemDescription"), 
                rs.getFloat("longitude"), rs.getFloat("latitude"), rs.getString("address"), rs.getString("city"), 
                rs.getString("state"), rs.getInt("zipcode")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }


    /** 
    * Get all item data for a specific user
    * @param userId the id of the user being requested
    * @return All rows of item data for the user being requested
    */

    public ArrayList<ItemData> selectByUserId(int userId)
    {
        ArrayList<ItemData> res = new ArrayList<ItemData>();
        try {
            Database.p_selectAllItemDataByUserId.setInt(1, userId);
            ResultSet rs = Database.p_selectAllItemDataByUserId.executeQuery();
            while (rs.next()) {
                res.add(new ItemData(rs.getInt("itemId"), rs.getInt("userId"), rs.getString("title"), rs.getString("description"), 
                rs.getInt("category"), rs.getInt("postDate"), rs.getInt("tradeMethod"), rs.getFloat("price"), rs.getBoolean("availability"), 
                rs.getString("availableTime"), rs.getString("wantedItemDescription"),rs.getFloat("longitude"), rs.getFloat("latitude"), rs.getString("address"), 
                rs.getString("city"), rs.getString("state"), rs.getInt("zipcode")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** 
    * Get all item data for a specific user
    * @param userId the id of the user being requested
    * @param itemPerPage how many items to get per page
    * @param pageNum which page to start from
    * @return All rows of item data for the user being requested
    */

    public ArrayList<ItemData> selectByUserId(int userId, int itemPerPage, int pageNum)
    {
        ArrayList<ItemData> res = new ArrayList<ItemData>();
        try {
            Database.p_selectAllItemDataByUserId.setInt(1, userId);
            ResultSet rs = Database.p_selectAllItemDataByUserId.executeQuery();
            while (rs.next()) {
                res.add(new ItemData(rs.getInt("itemId"), rs.getInt("userId"), rs.getString("title"), rs.getString("description"), 
                rs.getInt("category"), rs.getInt("postDate"), rs.getInt("tradeMethod"), rs.getFloat("price"), rs.getBoolean("availability"), 
                rs.getString("availableTime"), rs.getString("wantedItemDescription"),rs.getFloat("longitude"), rs.getFloat("latitude"), rs.getString("address"), 
                rs.getString("city"), rs.getString("state"), rs.getInt("zipcode")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }




    /**
     * insert a item for a row in the database
     * 
     * @param userId        The id of the person posting this item
     * @param title         The title for the item
     * @param description   The description of the item
     * @param categories    The int array with all categories
     * @param postDate      The post date of this item
     * @param tradeMethod The method of transaction for the item
     * @param price       The price of the item if applicable
     * @param availability Status of the availability of the item (True being available)
     * @param availableTime How long the item will be available for
     * @param wantedItemDescription The description of the item that the person wants (if applicable) 
     * @param itemLongitude The longitude coordinate of the item
     * @param itemLatitude The latitude coordinate of the item
     * @param itemAddress The address of the item (street, ave, bvld, etc)
     * @param itemCity The city of the item
     * @param itemState The state of the item (PA, CA, etc)
     * @param itemZipCode The zipcode of the item
   
     
     * 
     * @return The number of rows that were updated. -1 indicates an error.
     */
    public int insertNewItemData(int userId, String title, String description, int category, int postDate, int tradeMethod, 
    float price, boolean availability, String availableTime, String wantedItemDescription, float itemLongitude, float itemLatitude, 
    String itemAddress, String itemCity, String itemState, int itemZipCode) {
        try {
            Database.p_insertNewItemData.setInt(1, userId);
            Database.p_insertNewItemData.setString(2, title);
            Database.p_insertNewItemData.setString(3, description);
            Database.p_insertNewItemData.setInt(4, category);     
            Database.p_insertNewItemData.setInt(5, postDate);
            Database.p_insertNewItemData.setInt(6, tradeMethod);
            Database.p_insertNewItemData.setFloat(7, price);
            Database.p_insertNewItemData.setBoolean(8, true);
            Database.p_insertNewItemData.setString(9, availableTime);
            Database.p_insertNewItemData.setString(10, wantedItemDescription);
            Database.p_insertNewItemData.setFloat(11, itemLongitude);
            Database.p_insertNewItemData.setFloat(12, itemLatitude);
            Database.p_insertNewItemData.setString(13, itemAddress);
            Database.p_insertNewItemData.setString(14, itemCity);
            Database.p_insertNewItemData.setString(15, itemState);
            Database.p_insertNewItemData.setInt(16, itemZipCode);

            int affectedRows =  Database.p_insertNewItemData.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating item failed, no rows affected.");
            }
            try (ResultSet generatedKeys = Database.p_insertNewItemData.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    try {
                        int id = toIntExact(generatedKeys.getLong(1));
                        return id;
                    } catch (ArithmeticException e) {
                        throw new ArithmeticException("Overflow caused by casting long to int in insert new item");
                    }

                } else {
                    throw new SQLException("Creating item failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /** 
    * Updates the current item to the one provided in the parameter
    * @param itemToUpdate the new updated item 
    */
    public int updateItemData(ItemData itemToUpdate)
    {
        try {
            Database.p_updateItemData.setInt(1, itemToUpdate.itemSeller);
            Database.p_updateItemData.setString(2, itemToUpdate.itemTitle);
            Database.p_updateItemData.setString(3,itemToUpdate.itemDescription);
            Database.p_updateItemData.setInt(4,itemToUpdate.itemCategory);     
            Database.p_updateItemData.setInt(5,itemToUpdate.itemPostDate);
            Database.p_updateItemData.setInt(6, itemToUpdate.itemTradeMethod);
            Database.p_updateItemData.setFloat(7, itemToUpdate.itemPrice);
            Database.p_updateItemData.setBoolean(8, itemToUpdate.itemAvailability);
            Database.p_updateItemData.setString(9,itemToUpdate.itemAvailableTime);
            Database.p_updateItemData.setString(10,itemToUpdate.itemWantedItemDescription );
            Database.p_updateItemData.setFloat(11,itemToUpdate.itemLongitude);
            Database.p_updateItemData.setFloat(12,itemToUpdate.itemLatitude);
            Database.p_updateItemData.setString(13,itemToUpdate.itemAddress);
            Database.p_updateItemData.setString(14,itemToUpdate.itemCity);
            Database.p_updateItemData.setString(15,itemToUpdate.itemState);
            Database.p_updateItemData.setInt(16,itemToUpdate.itemZipCode);
            Database.p_updateItemData.setInt(17, itemToUpdate.itemId);


            int affectedRows =  Database.p_updateItemData.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating item failed, no rows affected.");
            }
            try (ResultSet generatedKeys = Database.p_updateItemData.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    try {
                        int id = toIntExact(generatedKeys.getLong(1));
                        return id;
                    } catch (ArithmeticException e) {
                        throw new ArithmeticException("Overflow caused by casting long to int in insert new item");
                    }

                } else {
                    throw new SQLException("Updating item failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
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