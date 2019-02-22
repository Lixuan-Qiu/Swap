package com.swap.backend.database;

/**
 * SimpleRequest provides a format for clients to present title and message
 * strings to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 * do not need a constructor.
 */
public class SimpleRequest {
    /**
     * The title being provided by the client.
     */
    //user id
    public int uid;
    // session key
    public String key;

    
    // The message being provided by the client.
    public String mMessage;

    public String img;

    public String file;

    public String fileName;
    
    public String mfileID;

    public String mlink;

}