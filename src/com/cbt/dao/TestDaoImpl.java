package com.cbt.dao;

import java.rmi.RemoteException;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Symetryn
 */
public class TestDaoImpl extends java.rmi.server.UnicastRemoteObject implements TestDao {
    
    public TestDaoImpl() throws RemoteException{
        super();
    }

    @Override
    public void saveTest() {
        System.out.println("hello");
    }

    @Override
    public void removeTest(int testId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void getAllQuestions(int tId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
   
   
    
}
