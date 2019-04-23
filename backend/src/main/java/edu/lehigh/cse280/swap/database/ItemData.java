package edu.lehigh.cse280.swap.database;

/**
 * This class hold the object fields and constructors for the corresponding
 * object
 */
public class ItemData {
    /**
     * The ID of the item
     */
    public int itemId;

    /**
     * The username of the seller of the item
     */
    public int itemSeller;

    /**
     * The title of the item
     */
    public String itemTitle;

    /**
     * The description of the item
     */
    public String itemDescription;

    /**
     * The category of the seller of the item
     */
    public int itemCategory;

    /**
     * The trading info of this item
     */
    public int itemTradingData;

    /**
     * The post date of this item
     */
    public int itemPostDate;

    /**
     * The trading method of this item
     */
    public int itemTradeMethod;

    /**
     * The price of this item
     */
    public float itemPrice;

    /**
     * The availability of this item
     */
    public boolean itemAvailability;

    /**
     * The availabile time of this item
     */
    public String itemAvailableTime;

    /**
     * The description of the item they hope to trade for
     */
    public String itemWantedItemDescription;

    /**
     * The longitude coordinate of the item 
     */
    public float itemLongitude;
    
    /**
     * The latitude coordinate of the item
     */
    public float itemLatitude;

    /**
     * The address where the item is located
     */
    public String itemAddress;

    /**
     * The city where the item is located
     */
    public String itemCity;

    /**
    * The state where the item is located
    */
    public String itemState;

    /**
     * The zipcode where the item is located
     */
    public int itemZipCode;

    /**
     * Construct a ItemData object by providing values for its fields
     * 
     * @param id_          The ID of the item
     * @param userId_      The ID of the user posting the item
     * @param title_       The title of the item being posted
     * @param description_ The description of the item being posted     
     * @param category_    The category(s) of the item being posted
     * @param postDate_    The date the item was posted
     * @param tradeMethod_ The method of transaction for the item
     * @param price_       The price of the item if applicable
     * @param availability_ Status of the availability of the item (True being available)
     * @param availableTime_ How long the item will be available for
     * @param wantedItemDescription_ The description of the item that the person wants (if applicable) 
     * @param itemLongitude_ The longitude coordinate of the item
     * @param itemLatitude_ The latitude coordinate of the item
     * @param itemAddress_ The address of the item (street, ave, bvld, etc)
     * @param itemCity_ The city of the item
     * @param itemState_ The state of the item (PA, CA, etc)
     * @param itemZipCode_ The zipcode of the item
     */
    public ItemData(int id_, int userId_, String title_, String description_, int category_, int postDate_,
            int tradeMethod_, float price_, boolean availability_, String availableTime_,
            String wantedItemDescription_, float itemLongitude_, float itemLatitude_, String itemAddress_, String itemCity_,
            String itemState_, int itemZipCode_) {
        // item data
        itemId = id_;
        itemSeller = userId_;
        itemTitle = title_;
        itemDescription = description_;
        itemCategory = category_;
        itemPostDate = postDate_;

        // trading info data
        itemTradeMethod = tradeMethod_;
        itemPrice = price_;
        itemAvailability = availability_;
        itemAvailableTime = availableTime_;
        itemWantedItemDescription = wantedItemDescription_;

        //Address data
        itemLongitude = itemLongitude_;
        itemLatitude = itemLatitude_;
        itemAddress = itemAddress_;
        itemCity = itemCity_;
        itemState = itemState_;
        itemZipCode = itemZipCode_;
    }

    /**
     * returns String version of the item with its fields
     */
    public String itemToString()
    {
        return "userId: " + itemSeller + " itemTitle: " + itemTitle + 
        " itemDescription: " + itemDescription + " itemCategory: " + itemCategory + " itemPostDate: " + itemPostDate
        + " itemTradeMethod: " + itemTradeMethod + " itemPrice: " + itemPrice + " itemAvailability: " + itemAvailability +
        " itemAvailableTime: " + itemAvailableTime + " itemWantedItemDescription: " + itemWantedItemDescription + 
        " itemLongitude: " + itemLongitude + " itemLatitude: " + itemLatitude + " itemAddress: " + itemAddress + " itemCity: "
        + itemCity + " itemState: " + itemState + " itemZipCode: " + itemZipCode;
    }

    /**
    * returns String version of the item with its fields (includes itemId)
    */
    public String itemToString1()
    {
        return "itemId: "+ itemId + " userId: " + itemSeller + " itemTitle: " + itemTitle + 
        " itemDescription: " + itemDescription + " itemCategory: " + itemCategory + " itemPostDate: " + itemPostDate
        + " itemTradeMethod: " + itemTradeMethod + " itemPrice: " + itemPrice + " itemAvailability: " + itemAvailability +
        " itemAvailableTime: " + itemAvailableTime + " itemWantedItemDescription: " + itemWantedItemDescription + 
        " itemLongitude: " + itemLongitude + " itemLatitude: " + itemLatitude + " itemAddress: " + itemAddress + " itemCity: "
        + itemCity + " itemState: " + itemState + " itemZipCode: " + itemZipCode;
    }

    // /**
    //  * Compares to see if this item equals another inputted 
    //  * @param item the itemData to compare to
    //  */
    // public boolean itemDataEquals(ItemData item)
    // {
    //     if(this.equals(item))
    //     {
    //         return true;
    //     }
    //     return false;
    // }

    /**
    * Compares to see if this item equals another inputted by comparing their 
    * field values from toString method
    * @param firstItem the first itemData to compare to the secondItem
    * @param secondItem the second itemData to compare to firstItem
    */
    public boolean itemDataStringEquals(ItemData secondItem)
    {
        if(this.itemToString().equals(secondItem.itemToString()))
        {
            return true;
        }
        return false;
    }
}