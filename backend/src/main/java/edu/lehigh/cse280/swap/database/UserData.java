package edu.lehigh.cse280.swap.database;

public class UserData {

    //the id of the user
    public int userId;

    //username of the user
    public String username;

    //the id of their wishlist (can only have 1)
    public int wishlistId;

    //id of their message
    public int messageId;

    //ids of the items they have posted
    public int[] postedItemIds;

    //contact info of the user
    public String contact;

    //email of the user
    public String email;

    //ids of their conversations (Q/A)
    public int[] conversationId;

    //Constructor for userData
    public UserData(int userId_, String username_, int wishlistId_, int messageId_,
    int[] postedItemIds_, String contact_, String email_, int[] conversationId_)
    {
        userId = userId_;
        username = username_;
        wishlistId = wishlistId_;
        messageId = messageId_;
        postedItemIds = postedItemIds_;
        contact = contact_;
        email = email_;
        conversationId = conversationId_;
    }
}