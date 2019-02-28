package com.swap.backend.database;

/**
 * This class hold the object fields and constructors for the corresponding object
 */
public class ItemData {
    /**
     * The ID of the item
     */
    int itemId;

    /**
     * The title of the item
     */
    String itemTitle;

    /**
     * The description of the item
     */
    String itemDescription;

    /**
     * The username of the seller of the item
     */
    String itemSeller;

    /**
     * The seller of the item
     */
    float itemPrice;

    /**
     * Construct a ItemData object by providing values for its fields
     */
    public ItemData(int id, String title, String description, String seller, float price) {
        itemId = id;
        itemTitle = title;
        itemDescription = description;
        itemSeller = seller;
        itemPrice = price;
    }
