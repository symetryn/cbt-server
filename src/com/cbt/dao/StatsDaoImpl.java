package com.cbt.dao;

import com.cbt.model.ChartItem;
import com.cbt.model.StatItem;
import com.cbt.model.User;
import com.cbt.utils.DbConnection;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * 
 * @author Dhruba
 */
public class StatsDaoImpl extends java.rmi.server.UnicastRemoteObject implements StatsDao {

    Connection cn = DbConnection.myConnection();

    /**
     *
     * @throws RemoteException
     */
    public StatsDaoImpl() throws RemoteException {
        super();
    }

    /**
     * Gives the total students
     * @return array list of total students
     * @throws RemoteException
     */
    @Override
    public ArrayList<ChartItem> getTotalStudents() throws RemoteException {
        try {
            System.out.print("Test Saved");
            String query = "SELECT COUNT(*) from users";
            PreparedStatement ps = cn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<ChartItem> chartList = new ArrayList();
            if (rs.next()) {
                ChartItem c = new ChartItem(new Integer[]{rs.getInt(1)}, "grey");

                chartList.add(c);
                return chartList;
            }
            return null;

        } catch (SQLException e) {
            throw new Error(e);
        }
    }

    /**
     * to get total exams
     * @return array list of all exams
     * @throws RemoteException
     */
    @Override
    public ArrayList<ChartItem> getTotalExams() throws RemoteException {
        try {
            System.out.print("Test Saved");
            String query = "SELECT COUNT(*) from test";
            PreparedStatement ps = cn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<ChartItem> chartList = new ArrayList();
            if (rs.next()) {
                ChartItem c = new ChartItem(new Integer[]{rs.getInt(1)}, "grey");

                chartList.add(c);
                return chartList;
            }
            return null;

        } catch (SQLException e) {
            throw new Error(e);
        }
    }

    /**
     * to get the pass rate of the students
     * @return array list of pass students
     * @throws RemoteException
     */
    @Override
    public ArrayList<ChartItem> getPassRate() throws RemoteException {
        try {
            System.out.print("Test Saved");
            String query = "Select (Count(status)* 100 / (Select Count(*) From result)) as Score From result";
            PreparedStatement ps = cn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<ChartItem> chartList = new ArrayList();
            if (rs.next()) {
                ChartItem c = new ChartItem(new Integer[]{rs.getInt(1)}, "grey");

                chartList.add(c);
                return chartList;
            }
            return null;

        } catch (SQLException e) {
            throw new Error(e);
        }
    }

    /**
     * to get all the upcoming tests
     * @return the array list of upcoming test
     * @throws RemoteException
     */
    @Override
    public ArrayList<ChartItem> getUpcomingTests() throws RemoteException {
        try {
            System.out.print("Test Saved");
            String query = "SELECT COUNT(*) FROM TEST WHERE DATE>=CURDATE()";
            PreparedStatement ps = cn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<ChartItem> chartList = new ArrayList();
            if (rs.next()) {
                ChartItem c = new ChartItem(new Integer[]{rs.getInt(1)}, "grey");

                chartList.add(c);
                return chartList;
            }
            return null;

        } catch (SQLException e) {
            throw new Error(e);
        }
    }

//    @Override
//    public ArrayList<ChartItem> getHighestPassedTests() throws RemoteException {
//        try {
//            System.out.print("Test Saved");
//            String query = "SELECT test.title, COUNT(*) FROM result JOIN test ON test.TID=result.test_id WHERE result.status=1 GROUP BY test.title";
//            PreparedStatement ps = cn.prepareStatement(query);
//            ResultSet rs = ps.executeQuery();
//            ArrayList<ChartItem> chartList = new ArrayList();
//            if (rs.next()) {
//                ChartItem c = new ChartItem(new Integer[]{rs.getInt(1)}, "grey");
//
//                chartList.add(c);
//                return chartList;
//            }
//            return null;
//
//        } catch (SQLException e) {
//            throw new Error(e);
//        }
//    }

    /**
     * to get top 10 students from overall students
     * @return the arraylist of top 10 students
     * @throws RemoteException
     */
    @Override
    public ArrayList<User> getTopStudents() throws RemoteException {
        try {
            System.out.print("Test Saved");
            String query = "SELECT a.firstname,a.lastname, COUNT(a.UID)total, "
                    + "((COUNT(CASE WHEN r.status=1 THEN 1 END)*100) / ( COUNT(a.UID)) ) as correct_percent "
                    + "FROM result r "
                    + "LEFT JOIN users a ON (r.user_id = a.UID) "
                    + "GROUP BY a.firstname "
                    + "order by correct_percent desc";
            PreparedStatement ps = cn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<User> chartList = new ArrayList();

            while (rs.next()) {
                User u = new User();
                u.setFirstName(rs.getString(1));
                u.setLastName(rs.getString(2));
                u.setLevel(rs.getInt(4));
                chartList.add(u);
            }

            return chartList;

        } catch (SQLException e) {
            throw new Error(e);
        }
    }

    /**
     * stats for pass percentage in the test
     * @return StatItem of test
     * @throws RemoteException
     */
    @Override
    public StatItem getTestsData() throws RemoteException {
        try {
            String query = "SELECT t.title, COUNT(t.TID) total, ( ( COUNT( CASE WHEN r.status = 1 THEN 1 END ) * 100 ) /(COUNT(t.TID)) ) AS correct_percent FROM result r LEFT JOIN users a ON (r.user_id = a.UID) LEFT JOIN test t ON (t.TID = r.test_Id) GROUP BY t.title ORDER BY correct_percent DESC";
            PreparedStatement ps = cn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<Integer> valueList = new ArrayList();
            ArrayList<String> labelList = new ArrayList();

            while (rs.next()) {
                labelList.add(rs.getString(1));

                valueList.add(rs.getInt(3));
//         new Integer[]{rs.getInt(3)};
            }
            ArrayList<ChartItem> chartList = new ArrayList<>();
            chartList.add(new ChartItem(new String[]{"Pass Percentage"}, valueList.toArray(new Integer[chartList.size()]), null, null));

            StatItem stat = new StatItem(labelList.toArray(new String[labelList.size()]), chartList);

            return stat;
        } catch (SQLException ex) {
            Logger.getLogger(StatsDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * to get the pass count of the student
     * @param userId 
     * @return array list of total pass student
     * @throws RemoteException
     */
    @Override
    public ArrayList<ChartItem> getPassCount(int userId) throws RemoteException {
        try {
            String query = "SELECT COUNT(*) FROM RESULT WHERE user_id=? AND status=1";

            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            ArrayList<ChartItem> chartList = new ArrayList();
            if (rs.next()) {
                ChartItem c = new ChartItem(new Integer[]{rs.getInt(1)}, "grey");

                chartList.add(c);
                return chartList;
            }

        } catch (SQLException ex) {
            Logger.getLogger(StatsDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * to get the failed count of the user
     * @param userId gives the total count of the failed result for particular id
     * @return array list for total fail
     * @throws RemoteException
     */
    @Override
    public ArrayList<ChartItem> getFailedCount(int userId) throws RemoteException {
        try {
            String query = "SELECT COUNT(*) FROM RESULT WHERE user_id=? and status=0 ";

            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            ArrayList<ChartItem> chartList = new ArrayList();
            if (rs.next()) {
                ChartItem c = new ChartItem(new Integer[]{rs.getInt(1)}, "grey");

                chartList.add(c);
                return chartList;
            }

        } catch (SQLException ex) {
            Logger.getLogger(StatsDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * To give the total count of result for user
     * @param userId gives the total count of the result for particular id
     * @return array list for the total count of the result
     * @throws RemoteException
     */
    @Override
    public ArrayList<ChartItem> getTotalCount(int userId) throws RemoteException {
        try {
            System.out.println(userId);
            String query = "SELECT COUNT(*) FROM RESULT WHERE user_id=? ";

            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            ArrayList<ChartItem> chartList = new ArrayList();
            if (rs.next()) {

                ChartItem c = new ChartItem(new Integer[]{rs.getInt(1)}, "grey");

                System.out.println(c.toString());
                chartList.add(c);

                return chartList;
            }

        } catch (SQLException ex) {
            Logger.getLogger(StatsDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * to get user test data
     * @param userId id for which the user test data is required
     * @return the StatItem to get user test data
     * @throws RemoteException
     */
    @Override
    public StatItem getUserTestData(int userId) throws RemoteException {
        try {
            String query = "SELECT t.title,  ( ( r.marks * 100 ) /(t.full_marks) ) AS correct_percent FROM result r  LEFT JOIN test t ON (t.TID = r.test_Id) where user_id=?";
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            ArrayList<Integer> valueList = new ArrayList();
            ArrayList<String> labelList = new ArrayList();

            while (rs.next()) {
                labelList.add(rs.getString(1));

                valueList.add(rs.getInt(2));
            }
            ArrayList<ChartItem> chartList = new ArrayList<>();
            chartList.add(new ChartItem(new String[]{"Recent Exam Percentages"}, valueList.toArray(new Integer[chartList.size()]), false, "red"));

            StatItem stat = new StatItem(labelList.toArray(new String[labelList.size()]), chartList);

            return stat;
        } catch (SQLException ex) {
            Logger.getLogger(StatsDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
