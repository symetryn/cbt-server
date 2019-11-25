/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbt.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Symetryn
 */
public class DbConnection {

    public static Connection cn;

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
