/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbt.dao;

import com.cbt.utils.DbConnection;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Symetryn
 */
public class UserDaoImpl extends java.rmi.server.UnicastRemoteObject implements UserDao {
    Connection cn= DbConnection.myConnection();

    public UserDaoImpl() throws RemoteException{
        super();
    }
    
    @Override
    public Boolean validateLogin(String Username, String Password) {
       try{
           System.out.print("print called");
           String query="SELECT * FROM USERS WHERE UID=? AND PASSWORD=?";
           PreparedStatement ps= cn.prepareStatement(query);
           ps.setString(1,Username);
           ps.setString(2,Password);
           ResultSet rs=ps.executeQuery();
           
           return rs.next();
       }
       catch(SQLException e){
           throw new Error(e);
       }
    }
    
}
