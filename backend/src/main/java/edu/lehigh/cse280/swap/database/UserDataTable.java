package edu.lehigh.cse280.swap.database;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import static java.lang.Math.toIntExact;

/**
 * UserData represents the data used for any given user
 */
public class UserDataTable {

    //empty constructor
    UserDataTable(){

    }

    // /**
    //  * Get all data for a specific user
    //  * 
    //  * @param id The id of the user being requested
    //  * 
    //  * @return The data for the requested user, or null if the ID was invalid
    //  */
    // public UserData selectOneUser(int userId) {
    //     UserData res = null;
    //     try {
    //         Database.p_selectOneUserData.setInt(1, userId);
    //         ResultSet rs = Database.p_selectOneUserData.executeQuery();
    //         if (rs.next()) {
    //             res = new UserData(rs.getInt("userId"),rs.getString("username"), rs.getInt("wishlistId"), rs.getInt("messageId"),
                

    //             );
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return res;
    // }

//     * 
//     * @return The number of rows that were updated. -1 indicates an error.
//     */
//    public int insertNewUserData(String username_, int wishlistId_, int messageId_,
//    int[] postedItemIds_, String contact_, String email_, int[] conversationId_) {
//        try {
//            Database.p_insertNewUserData.setString(1, username_);
//            Database.p_insertNewUserData.setInt(2, wishlistId_);
//            Database.p_insertNewUserData.setInt(3, messageId_);
//            Database.p_insertNewUserData.setArray(4, postedItemIds_);
//            Database.p_insertNewUserData.setString(5, contact_);
//            Database.p_insertNewUserData.setString(5, email_);
//            Database.p_insertNewUserData.setArray(4, conversationId_);

//            int affectedRows =  Database.p_insertNewUserData.executeUpdate();
//            if (affectedRows == 0) {
//                throw new SQLException("Creating user failed, no rows affected.");
//            }
//            try (ResultSet generatedKeys = Database.p_insertNewUserData.getGeneratedKeys()) {
//                if (generatedKeys.next()) {
//                    try {
//                        int id = toIntExact(generatedKeys.getLong(1));
//                        return id;
//                    } catch (ArithmeticException e) {
//                        throw new ArithmeticException("Overflow caused by casting long to int in insert new item");
//                    }

//                } else {
//                    throw new SQLException("Creating user failed, no ID obtained.");
//                }
//            }

//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }


}