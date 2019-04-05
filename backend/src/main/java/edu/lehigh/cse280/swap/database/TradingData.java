package edu.lehigh.cse280.swap.database;

/**
 * This class hold the object fields and constructors for the corresponding object
 */
public class TradingData {
    /**
     * Transaction ID
     */
    int tradingInfoId;

    /**
     * The id of the item involved in the transaction 
     */
    int itemId;

    /**
     * The type of transaction where it corresponds 0-3 (ex: 0 being sell, 2 being rent)
     * "Sell","Trade","Rent","GiveAway"
     */
    int tradingMethod;

    /**
     * The price of the item in the traction 
     */
    float price;

    /**
     * The availability of the item (false for unavailable, true for available)
     */
    boolean availability;

    /**
     * The duration of the availability of the item 
     */
    String availableTime;

    /**
     * The description of the item that the person wants
     */
    String wantedItemDescription;



    /**
     * Construct a ItemData object by providing values for its fields
     * @param tradingInfoId_
     * @param itemId_
     * @param tradingMethod_
     * @param price_
     * @param availability_
     * @param availableTime_
     * @param wantedItemDescription_
     */
    public TradingData(int tradingInfoId_, int itemId_, int tradingMethod_, float price_, boolean availability_, String availableTime_, String wantedItemDescription_) {
        tradingInfoId = tradingInfoId_;
        itemId = itemId_;
        tradingMethod = tradingMethod_;
        price = price_;
        availability = availability_;
        availableTime = availableTime_;
        wantedItemDescription = wantedItemDescription_;
    }
}