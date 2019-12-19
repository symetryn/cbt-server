
package com.cbt.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DbConnection {


    public static Connection cn;

    /**
     * Create the static method for DB connection
     * @return the Connection 
     */
    public static Connection myConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            cn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/cbt", "root", "");

            return cn;

        }
       
        catch (SQLException|ClassNotFoundException e) {
            
            System.out.println(e.getStackTrace().toString());
            throw new Error(e);
        }

    }
//
//    public static void main(String[] args) {
//        try {
//            Connection con = myConnection();
//            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery("select * from users");
//            while (rs.next()) {
//                System.out.println(rs.getString("firstname"));
//            }
//        } catch (Exception e) {
//            System.out.print(e);
//
//        }
//
//    }

}
