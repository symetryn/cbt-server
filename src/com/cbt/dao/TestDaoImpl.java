package com.cbt.dao;

import com.cbt.bll.Answer;
import com.cbt.bll.Question;
import com.cbt.bll.Test;
import com.cbt.utils.DbConnection;
import com.mysql.jdbc.Statement;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


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

    Connection cn = DbConnection.myConnection();

    public TestDaoImpl() throws RemoteException {
        super();
    }

    @Override
    public void saveTest(Test test) {
        try {
            System.out.print("Test Saved");
            String query = "INSERT INTO TEST (TITLE) VALUES(?) ";
            PreparedStatement ps = cn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, test.getTitle());
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating test failed, no rows affected.");
            }

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int testId = generatedKeys.getInt(1);
                System.out.println("generated test id " + testId);
                ArrayList<Question> questions = test.getQuestions();
                questions.forEach((value) -> {
                    saveQuestion(testId, value);
                });

//                saveQuestion(testId, test);
            } else {
                throw new SQLException("Creating test failed, no ID obtained.");
            }

        } catch (SQLException e) {
            throw new Error(e);
        }
    }

    private void saveQuestion(int testId, Question question) {
        try {
            String query = "INSERT INTO question (TITLE,MARKS,TEST_ID) VALUES(?,?,?)";
            PreparedStatement ps = cn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            try {
                ps.setString(1, question.getTitle());
                ps.setInt(2, question.getMarks());
                ps.setInt(3, testId);
                ps.executeUpdate();
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int questionId = generatedKeys.getInt(1);
                    System.out.println("generated question id " + testId);
                    String answerQuery = "INSERT INTO answer (TITLE,CORRECT_STATUS,QUESTION_ID) VALUES(?,?,?)";
                    PreparedStatement psmt = cn.prepareStatement(answerQuery, Statement.RETURN_GENERATED_KEYS);
                    ArrayList<Answer> answers = question.getAnswers();
                    answers.forEach((answer) -> {
                        try {
                            psmt.setString(1, answer.getTitle());
                            psmt.setBoolean(2, answer.getCorrectAnswer());
                            psmt.setInt(3, questionId);
                            psmt.addBatch();
                        } catch (SQLException ex) {
                            Logger.getLogger(TestDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    int[] numUpdates = psmt.executeBatch();
                    for (int i = 0; i < numUpdates.length; i++) {
                        if (numUpdates[i] == -2) {
                            System.out.println("Execution " + i
                                    + ": unknown number of rows updated");
                        } else {
                            System.out.println("Execution " + i
                                    + "successful: " + numUpdates[i] + " rows updated");
                        }
                    }
                } else {
                    throw new SQLException("Creating test failed, no ID obtained.");
                }

            } catch (SQLException ex) {
                System.out.print(ex);
                Logger.getLogger(TestDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (SQLException ex) {
            Logger.getLogger(TestDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void removeTest(int testId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void getAllQuestions(int tId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Test> getAllTest() throws RemoteException {
        try {
            System.out.print("print called");
            String query = "SELECT * FROM TEST";
            PreparedStatement ps = cn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            ArrayList<Test> testList = new ArrayList();
            while (rs.next()) {
                Test t = new Test();
                t.setTitle(rs.getString("title"));
                t.setDate(rs.getDate("date"));
                testList.add(t);

            }
            return testList;

        } catch (SQLException e) {
            throw new Error(e);
        }
    }

//    @Override
//    public ArrayList<Test> getAllUpcomingTest() throws RemoteException {
        
//    }

    @Override
    public ArrayList<Test> getAllUpcomingTest() throws RemoteException {
        try {
            System.out.print("print called");
            String query = "SELECT * FROM TEST WHERE DATE>=CURDATE()";
            PreparedStatement ps = cn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            ArrayList<Test> testList = new ArrayList();

            while (rs.next()) {
                Test t = new Test();
                t.setTitle(rs.getString("title"));
                t.setDate(rs.getDate("date"));
                testList.add(t);
                System.out.println(t.getTitle());

            }
            return testList;

        } catch (SQLException e) {
            throw new Error(e);
        }
    }
    
     @Override
    public ArrayList<Test> getTestSearch(String searchQuery) throws RemoteException {
        try {
            final int LIMIT=8;
            System.out.print("print called");
            String query = "SELECT * FROM TEST WHERE TITLE LIKE ? LIMIT ?";
            PreparedStatement ps = cn.prepareStatement(query);
            
            ps.setString(1, searchQuery+"%");
            ps.setInt(2, LIMIT);

            ResultSet rs = ps.executeQuery();
            ArrayList<Test> testList = new ArrayList();
            
            while (rs.next()) {
                Test t = new Test();
                t.setTitle(rs.getString("title"));
                t.setDate(rs.getDate("date"));
                testList.add(t);
            }
            return testList;

        } catch (SQLException e) {
            throw new Error(e);
        }
    }

}
