package com.cbt.dao;

import com.cbt.model.Answer;
import com.cbt.model.Question;
import com.cbt.model.Result;
import com.cbt.model.ResultItem;
import com.cbt.model.Test;
import com.cbt.model.User;
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

    /**
     *
     * @throws RemoteException
     */
    public TestDaoImpl() throws RemoteException {
        super();
    }

    /**
     *
     * @param test
     */
    @Override
    public void saveTest(Test test) {
        try {
            System.out.print("Test Saved");
            String query = "INSERT INTO TEST (TITLE,PASSWORD,DATE,LEVEL,SEMESTER,PASS_MARKS,FULL_MARKS,DURATION,START_TIME,END_TIME) VALUES(?,?,?,?,?,?,?,?,?,?) ";
            PreparedStatement ps = cn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, test.getTitle());
            ps.setString(2, test.getPassword());
            ps.setDate(3, test.getDate());
            ps.setInt(4, test.getLevel());
            ps.setInt(5, test.getSemester());
            ps.setInt(6, test.getPassMarks());
            ps.setInt(7, test.getFullMarks());
            ps.setInt(8, test.getDuration());
            ps.setTime(9, test.getStartTime());
            ps.setTime(10, test.getEndTime());

            int createdRows = ps.executeUpdate();

            if (createdRows == 0) {
                throw new SQLException("Creating test failed, no rows created.");
            }

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int testId = generatedKeys.getInt(1);
                System.out.println("generated test id " + testId);
                ArrayList<Question> questions = test.getQuestions();
                questions.forEach((value) -> {
                    saveQuestion(testId, value);
                });

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

    /**
     *
     * @param testId
     */
    @Override
    public void removeTest(int testId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @return @throws RemoteException
     */
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
                t.setId(rs.getInt("TID"));
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
    /**
     *
     * @return @throws RemoteException
     */
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
                t.setId(rs.getInt("TID"));
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

    /**
     *
     * @param searchQuery
     * @return
     * @throws RemoteException
     */
    @Override
    public ArrayList<Test> getTestSearch(String searchQuery) throws RemoteException {
        try {
            final int LIMIT = 8;
            System.out.print("print called");
            String query = "SELECT * FROM TEST WHERE TITLE LIKE ? LIMIT ?";
            PreparedStatement ps = cn.prepareStatement(query);

            ps.setString(1, searchQuery + "%");
            ps.setInt(2, LIMIT);

            ResultSet rs = ps.executeQuery();
            ArrayList<Test> testList = new ArrayList();

            while (rs.next()) {
                Test t = new Test();
                t.setId(rs.getInt("TID"));
                t.setTitle(rs.getString("title"));
                t.setDate(rs.getDate("date"));
                testList.add(t);
            }
            return testList;

        } catch (SQLException e) {
            throw new Error(e);
        }
    }

    /**
     *
     * @param testId
     * @return
     * @throws RemoteException
     */
    @Override
    public Test getTest(int testId) throws RemoteException {
        try {

            String query = "SELECT * FROM TEST WHERE TID= ?";
            PreparedStatement ps = cn.prepareStatement(query);

            ps.setInt(1, testId);

            ResultSet rs = ps.executeQuery();

            Test test = new Test();
            rs.next();
            test.setId(rs.getInt("TID"));
            test.setTitle(rs.getString("title"));
            test.setDate(rs.getDate("date"));
            test.setStartTime(rs.getTime("start_time"));
            test.setEndTime(rs.getTime("end_time"));
            test.setLevel(rs.getInt("level"));
            test.setSemester(rs.getInt("semester"));
            test.setPassword(rs.getString("password"));
            test.setDuration(rs.getInt("duration"));
            test.setPassMarks(rs.getInt("pass_marks"));
            test.setFullMarks(rs.getInt("full_marks"));

            String questionQuery = "SELECT * FROM QUESTION WHERE test_id= ?";
            PreparedStatement psq = cn.prepareStatement(questionQuery);

            psq.setInt(1, test.getId());
            ResultSet rsq = psq.executeQuery();

            while (rsq.next()) {
                System.out.println("questoin ran");
                Question q = new Question();

                q.setTitle(rsq.getString("title"));
                q.setMarks(rsq.getInt("marks"));
                q.setId(rsq.getInt("QID"));

                String answerQuery = "SELECT * FROM ANSWER WHERE question_id= ?";
                PreparedStatement psa = cn.prepareStatement(answerQuery);

                psa.setInt(1, q.getId());
                ResultSet rsa = psa.executeQuery();

                while (rsa.next()) {
                    System.out.println("answer ran");
                    q.addAnswer(new Answer(rsa.getInt("AID"), rsa.getString("title"), rsa.getBoolean("correct_status")));
                }

                test.pushQuestion(q);
            }
            System.out.println(test.getQuestions().size());
            return test;

        } catch (SQLException e) {
            throw new Error(e);
        }
    }

    /**
     *
     * @param test
     * @throws RemoteException
     */
    @Override
    public void updateTest(Test test) throws RemoteException {
        try {

            String query = "UPDATE TEST SET TITLE= ?,PASSWORD=?,DATE=?,LEVEL=?,SEMESTER=?,PASS_MARKS=?,FULL_MARKS=?,DURATION=?,START_TIME=?,END_TIME=? WHERE TID=? ";
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, test.getTitle());
            ps.setString(2, test.getPassword());
            ps.setDate(3, test.getDate());
            ps.setInt(4, test.getLevel());
            ps.setInt(5, test.getSemester());
            ps.setInt(6, test.getPassMarks());
            ps.setInt(7, test.getFullMarks());
            ps.setInt(8, test.getDuration());
            ps.setTime(9, test.getStartTime());
            ps.setTime(10, test.getEndTime());
            ps.setInt(11, test.getId());

            int createdRows = ps.executeUpdate();

            if (createdRows == 0) {
                throw new SQLException("Creating test failed, no rows created.");
            }

            ArrayList<Question> questions = test.getQuestions();
            System.out.println(questions.size());
            System.out.println("updated test");
            String removeQuery = "DELETE FROM question WHERE test_id=?";
            PreparedStatement pss = cn.prepareStatement(removeQuery);
            pss.setInt(1, test.getId());
            System.out.println(pss.executeUpdate() + "removed");
            for (Question value : questions) {
                System.out.println(questions.size());
//                if (questions.size() > 1 && value.getId() != null) {
//                    removeQuery += value.getId() + ",";
//                }
//                saveQuestion(test.getId(), value);
                updateQuestion(test.getId(), value);

            }

//            questions.forEach((value) -> {
//              
//                
//            });
        } catch (SQLException e) {
            throw new Error(e);
        }
    }

    private void updateQuestion(int testId, Question question) {
        try {

            if (question.getId() == null) {
                saveQuestion(testId, question);
                System.out.println("added new question from update");
            } else {
                String query = "INSERT INTO question (TITLE,MARKS,TEST_ID,QID) VALUES(?,?,?,?)";
                PreparedStatement ps = cn.prepareStatement(query);
                ps.setString(1, question.getTitle());
                ps.setInt(2, question.getMarks());
                ps.setInt(3, testId);
                ps.setInt(4, question.getId());
                ps.executeUpdate();

                String deleteQuery = "DELETE FROM answer WHERE question_id=?";
                PreparedStatement psdt = cn.prepareStatement(deleteQuery);
                psdt.setInt(1, question.getId());
                psdt.executeUpdate();

                String newAnswerQuery = "INSERT INTO answer (TITLE,CORRECT_STATUS,QUESTION_ID) VALUES(?,?,?)";
                PreparedStatement pmt = cn.prepareStatement(newAnswerQuery);

                ArrayList<Answer> answers = question.getAnswers();
                answers.forEach((answer) -> {
                    System.out.println("item");
                    try {

                        pmt.setString(1, answer.getTitle());
                        pmt.setBoolean(2, answer.getCorrectAnswer());
                        pmt.setInt(3, question.getId());
                        pmt.addBatch();

                    } catch (SQLException ex) {
                        Logger.getLogger(TestDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                pmt.executeBatch();

            }

        } catch (SQLException ex) {
            Logger.getLogger(TestDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param level
     * @param semester
     * @return
     * @throws RemoteException
     */
    @Override
    public ArrayList<Test> getTestByLevelSem(int level, int semester) throws RemoteException {
        ArrayList<Test> testList = new ArrayList();
        try {
            String query = "SELECT * FROM TEST WHERE status=? AND level=? AND semester=?";
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, "active");
            ps.setInt(2, level);
            ps.setInt(3, semester);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Test t = new Test();
                t.setId(rs.getInt("TID"));
                t.setTitle(rs.getString("title"));
                t.setDate(rs.getDate("date"));
                testList.add(t);

            }
            return testList;

        } catch (SQLException ex) {
            Logger.getLogger(TestDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    /**
     *
     * @param testId
     * @param password
     * @return
     * @throws RemoteException
     */
    @Override
    public Boolean verifyPassword(int testId, String password) throws RemoteException {
        try {
            String query = "SELECT * FROM TEST WHERE TID=? AND password=? ";
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, testId);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(TestDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     *
     * @param result
     * @return generated result Id
     * @throws RemoteException
     */
    @Override
    public Integer saveResult(Result result) throws RemoteException {
        try {
            System.out.print("result Saved");
            String query = "INSERT INTO RESULT (test_id,user_id,marks,status) VALUES(?,?,?,?) ";
            PreparedStatement ps = cn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, result.getTestId());
            ps.setInt(2, result.getUserId());
            ps.setInt(3, result.getMarks());
            ps.setBoolean(4, result.getStatus());

            int createdRows = ps.executeUpdate();

            if (createdRows == 0) {
                throw new SQLException("Creating test failed, no rows created.");
            }

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int resultId = generatedKeys.getInt(1);
                System.out.println("generated result id " + resultId);
                ArrayList<ResultItem> results = result.getResultItem();

                String resultQuery = "INSERT INTO result_item (QUESTION_ID,CORRECT_ANSWER,SELECTED_ANSWER,CORRECT,RESULT_ID) VALUES(?,?,?,?,?)";
                PreparedStatement psmt = cn.prepareStatement(resultQuery);

                results.forEach((resultItem) -> {
                    try {
                        psmt.setInt(1, resultItem.getQuestionId());
                        psmt.setString(2, resultItem.getCorrectAnswer());
                        psmt.setString(3, resultItem.getSelectedAnswer());
                        psmt.setBoolean(4, resultItem.getCorrect());
                        psmt.setInt(5, resultId);
                        psmt.addBatch();
                    } catch (SQLException ex) {
                        Logger.getLogger(TestDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                psmt.executeBatch();
                return resultId;

            } else {
                throw new SQLException("Creating test failed, no ID obtained.");
            }

        } catch (SQLException e) {
            throw new Error(e);
        }
    }

    /**
     *
     * @param resultId
     * @return
     * @throws RemoteException
     */
    @Override
    public Result getResult(int resultId) throws RemoteException {
        try {
            Result result = new Result();
            Test test = new Test();
            ArrayList<ResultItem> resultItems = new ArrayList<>();

            String resultQuery = "SELECT * FROM RESULT"
                    + " INNER JOIN TEST"
                    + " ON test.TID = result.test_id"
                    + " WHERE RID=?";
            PreparedStatement psmt = cn.prepareStatement(resultQuery);
            psmt.setInt(1, resultId);

            ResultSet rs = psmt.executeQuery();
            if (rs.next()) {
                result.setId(resultId);
                result.setMarks(rs.getInt("marks"));
                result.setStatus(rs.getBoolean("status"));
                test.setId(rs.getInt("TID"));
                test.setTitle(rs.getString("title"));
                test.setDate(rs.getDate("date"));
                test.setStartTime(rs.getTime("start_time"));
                test.setEndTime(rs.getTime("end_time"));
                test.setLevel(rs.getInt("level"));
                test.setSemester(rs.getInt("semester"));
                test.setPassword(rs.getString("password"));
                test.setDuration(rs.getInt("duration"));
                test.setPassMarks(rs.getInt("pass_marks"));
                test.setFullMarks(rs.getInt("full_marks"));
                result.setTest(test);
            }

            String resultItemsQuery = "SELECT *"
                    + "FROM question "
                    + "INNER JOIN result_item "
                    + "ON result_item.question_id =question.QID "
                    + "WHERE result_id=?";
            PreparedStatement ps = cn.prepareStatement(resultItemsQuery);
            ps.setInt(1, resultId);
            ResultSet rslt = ps.executeQuery();

            while (rslt.next()) {
                Question q = new Question();
                q.setTitle(rslt.getString("title"));
                q.setMarks(rslt.getInt("marks"));
                q.setId(rslt.getInt("QID"));
                resultItems.add(new ResultItem(resultId, rslt.getString("correct_answer"), rslt.getString("selected_answer"), rslt.getBoolean("correct"), q));
            }
            result.setResultItem(resultItems);
            System.out.println(result.toString());
            return result;

        } catch (SQLException ex) {
            Logger.getLogger(TestDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new Result();
    }

    @Override
    public ArrayList<Result> getResultByUser(int userId) throws RemoteException {
        try {

            String resultQuery = "SELECT * FROM RESULT"
                    + " INNER JOIN TEST"
                    + " ON test.TID = result.test_id"
                    + " WHERE user_id=?";
            PreparedStatement psmt = cn.prepareStatement(resultQuery);
            int resultId;
            psmt.setInt(1, userId);

            ResultSet rs = psmt.executeQuery();

            ArrayList<Result> results = new ArrayList<>();
            while (rs.next()) {
                Result result = new Result();
                Test test = new Test();
                result.setId(rs.getInt("RID"));
                result.setMarks(rs.getInt("marks"));
                result.setStatus(rs.getBoolean("status"));
                test.setId(rs.getInt("TID"));
                test.setTitle(rs.getString("title"));
                test.setDate(rs.getDate("date"));
                test.setStartTime(rs.getTime("start_time"));
                test.setEndTime(rs.getTime("end_time"));
                test.setLevel(rs.getInt("level"));
                test.setSemester(rs.getInt("semester"));
                test.setPassword(rs.getString("password"));
                test.setDuration(rs.getInt("duration"));
                test.setPassMarks(rs.getInt("pass_marks"));
                test.setFullMarks(rs.getInt("full_marks"));
                result.setTest(test);
                results.add(result);
                result.toString();
            }
            return results;

        } catch (SQLException ex) {
            Logger.getLogger(TestDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Result> getAllResult() throws RemoteException {
        try {
            String query = "SELECT * FROM RESULT INNER JOIN USERS ON result.user_id = users.UID INNER JOIN Test ON result.test_id = test.TID";

            PreparedStatement psmt = cn.prepareStatement(query);

            ResultSet rs = psmt.executeQuery();

            ArrayList<Result> results = new ArrayList<>();
            while (rs.next()) {
                Result result = new Result();
                Test test = new Test();
                result.setId(rs.getInt("RID"));
                result.setMarks(rs.getInt("marks"));
                result.setStatus(rs.getBoolean("status"));
                User u = new User();
                u.setEmail(rs.getString("email"));
                u.setFirstName(rs.getString("firstname"));
                u.setLastName(rs.getString("lastname"));
                u.setLevel(rs.getInt("level"));
                u.setSemester(rs.getInt("semester"));
                u.setUserID(rs.getInt("UID"));
                u.setRole(rs.getString("role"));
                Test t = new Test();
                t.setTitle(rs.getString("title"));
                result.setUser(u);
                result.setTest(t);
                results.add(result);
            }
            return results;

        } catch (SQLException ex) {
            Logger.getLogger(TestDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
       return new ArrayList<>();

    }

}
