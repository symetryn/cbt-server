package com.cbt.dao;

import com.cbt.model.ChartItem;
import com.cbt.model.StatItem;
import com.cbt.model.User;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Interface defining method for getting different info to depict it chart form
 * @author Symetryn
 */
public interface StatsDao extends java.rmi.Remote {

    public ArrayList<ChartItem> getTotalStudents() throws RemoteException;

    public ArrayList<ChartItem> getTotalExams() throws RemoteException;

    public ArrayList<ChartItem> getPassRate() throws RemoteException;

    public ArrayList<ChartItem> getUpcomingTests() throws RemoteException;

    public StatItem getTestsData() throws RemoteException;

    public ArrayList<User> getTopStudents() throws RemoteException;

    public ArrayList<ChartItem> getPassCount(int userId) throws RemoteException;

    public ArrayList<ChartItem> getFailedCount(int userId) throws RemoteException;

    public ArrayList<ChartItem> getTotalCount(int userId) throws RemoteException;
    
    public StatItem getUserTestData(int userId) throws RemoteException;
    
    
}
