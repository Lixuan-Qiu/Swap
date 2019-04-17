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
     * The trading method of this item
     */
    int itemTradeMethod;

    /**
     * The price of this item
     */
    float itemPrice;

    /**
     * The availability of this item
     */
    boolean itemAvailability;

    /**
     * The availabile time of this item
     */
    String itemAvailabileTime;

    /**
     * The description of the item they hope to trade for
     */
    String itemWantedItemDescription;

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
    public ItemData(int id_, int userId_, String title_, String description_, int category_, int postDate_, int tradeMethod_, float price_, boolean availability_, String availableTime_, String wantedItemDescription_) {
        //item data
        itemId = id_;
        itemSeller = userId_;
        itemTitle = title_;
        itemCategory = category_;
        itemDescription = description_;
        itemPostDate = postDate_;

        //trading info data
        itemTradeMethod = tradeMethod_;
        itemPrice = price_;
        itemAvailability = availability_;
        itemAvailabileTime = availableTime_;
        itemWantedItemDescription = wantedItemDescription_;
    }
}