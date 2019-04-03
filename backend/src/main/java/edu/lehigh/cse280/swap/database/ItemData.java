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
     * The category of the seller of the item
     */
    String[] itemCategory;

    /**
     * Construct a ItemData object by providing values for its fields
     * @param id
     * @param title
     * @param description
     * @param seller
     * @param price
     * @param category
     */
    public ItemData(int id, String title, String description, String seller, float price, String[] category) {
        itemId = id;
        itemTitle = title;
        itemDescription = description;
        itemSeller = seller;
        itemPrice = price;
        itemCategory = category;
    }
}