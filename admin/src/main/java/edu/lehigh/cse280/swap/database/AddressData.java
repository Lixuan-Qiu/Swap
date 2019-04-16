package edu.lehigh.cse280.swap.database;

/**
 * This class hold the object fields and constructors for the corresponding object
 */
public class AddressData {
    /**
     * The ID of the particular item's address data
     */
    int addressId;

    /**
    * The id of the item related to this address
    */
    int itemId;

    /**
     * The longitude coordinate of the item 
     */
    float longitude;
    
    /**
     * The latitude coordinate of the item
     */
    float latitude;

    /**
     * The address where the item is located
     */
    String address;

    /**
     * The city where the item is located
     */
    String city;

    /**
    * The state where the item is located
    */
    String state;

    /**
     * The zipcode where the item is located
     */
    int zipcode;

    /**
     * Construct a ItemData object by providing values for its fields
     * @param addressId_
     * @param itemId_
     * @param longitude_
     * @param latitude_
     * @param address_
     * @param city_
     * @param state_
     * @param zipcode_
     */
    public AddressData(int addressId_, int itemId_, float longitude_, float latitude_, String address_, String city_, String state_, int zipcode_){
        addressId = addressId_;
        itemId = itemId_;
        longitude = longitude_;
        address = address_;
        city = city_;
        state = state_;
        zipcode = zipcode_;

    }
}