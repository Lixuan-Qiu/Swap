package edu.lehigh.cse280.swap.admin;

public class DatabaseStructures {

    /**
     * String array that holds actual category names for the categoryData in
     * database
     */
    String[] category;

    /**
     * String array that holds actual trading methods for the tradingMethod in
     * database
     */
    String[] tradingMethod;

    public DatabaseStructures() {
        category = new String[] { "Car", "School", "Furniture", "Electronics" };

        tradingMethod = new String[] { "Sell", "Trade", "Rent", "GiveAway" };
    }
}