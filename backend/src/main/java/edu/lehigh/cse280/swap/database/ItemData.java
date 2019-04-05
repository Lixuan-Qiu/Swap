package edu.lehigh.cse280.swap.database;

/**
 * This class hold the object fields and constructors for the corresponding object
 */
public class ItemData {
    /**
     * The ID of the item
     */
    int itemId;

    /**
     * The username of the seller of the item
     */
    int itemSeller;
    
    /**
     * The title of the item
     */
    String itemTitle;

    /**
     * The description of the item
     */
    String itemDescription;

    /**
     * The category of the seller of the item
     */
    int[] itemCategory;

    /**
     * The trading info of this item
     */
    int itemTradingData;

    /**
     * The post date of this item
     */
    int itemPostDate;

    /**
     * Construct a ItemData object by providing values for its fields
     * @param userid
     * @param title
     * @param description
     * @param userId
     * @param price
     * @param category
     */
    public ItemData(int id, int userId, String title, String description, int[] category, int tradingData, int postDate) {
        itemId = id;
        itemSeller = userId;
        itemTitle = title;
        itemCategory = category;
        itemDescription = description;
        itemTradingData = tradingData;
        itemPostDate = postDate;
    }
}