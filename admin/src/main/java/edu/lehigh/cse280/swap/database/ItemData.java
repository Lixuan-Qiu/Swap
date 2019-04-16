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
    int itemCategory;

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
     * @param id_            The ID of the item
     * @param userid_ 
     * @param title_
     * @param description_
     * @param userId_
     * @param price_
     * @param category_
     */
    public ItemData(int id_, int userId_, String title_, String description_, int category_, int tradingData_, int postDate_) {
        itemId = id_;
        itemSeller = userId_;
        itemTitle = title_;
        itemCategory = category_;
        itemDescription = description_;
        itemTradingData = tradingData_;
        itemPostDate = postDate_;
    }
}