/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbt.dao;

import com.cbt.model.User;
import com.cbt.utils.DbConnection;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Symetryn
 */
public class UserDaoImpl extends java.rmi.server.UnicastRemoteObject implements UserDao {

    Connection cn = DbConnection.myConnection();

    public UserDaoImpl() throws RemoteException {
        super();
    }

    @Override
    public User validateLogin(String Username, String Password) {
        try {
            System.out.print("print called");
            String query = "SELECT * FROM USERS WHERE UID=? AND PASSWORD=?";
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, Username);
            ps.setString(2, Password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setEmail(rs.getString("email"));
                u.setFirstName(rs.getString("firstname"));
                u.setLastName(rs.getString("lastname"));
                u.setLevel(rs.getInt("level"));
                u.setSemester(rs.getInt("semester"));
                u.setUserID(rs.getInt("UID"));
                u.setRole(rs.getString("role"));

                return u;
            }

        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void registerUser(User user) throws RemoteException {
        try {
            System.out.print("print called");
            String query = "INSERT INTO USERS (UID,FIRSTNAME,LASTNAME,EMAIL,PHOTO,PASSWORD,LEVEL,SEMESTER) VALUES(?,?,?,?,?,?,?,?) ";
            PreparedStatement ps = cn.prepareStatement(query);

            ps.setInt(1, user.getUserID());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getImageUrl());
            ps.setString(6, user.getPassword());
            ps.setInt(7, user.getLevel());
            ps.setInt(8, user.getSemester());

            ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
