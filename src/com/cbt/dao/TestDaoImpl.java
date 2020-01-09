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

public class TestDaoImpl extends java.rmi.server.UnicastRemoteObject implements TestDao {

    Connection cn = DbConnection.myConnection();

    /**
     * Initializing constructor of base class
     * 
     * @throws RemoteException
     */
    public TestDaoImpl() throws RemoteException {
        super();
    }

    /**
     * Save the test into the test,question and answer tables
     *
     * @param test: Object of the test
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

    // saves each question item with respect to testId passed
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
                            System.out.println("Execution " + i + ": unknown number of rows updated");
                        } else {
                            System.out.println("Execution " + i + "successful: " + numUpdates[i] + " rows updated");
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
     * Remove the test with reference to testId
     *
     * @param testId
     */
    @Override
    public void removeTest(int testId) {
        try {
            System.out.print("print called");
            String query = "DELETE FROM TEST WHERE TID=?";
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, testId);
            int rs = ps.executeUpdate();
            if (rs == 0) {
                throw new SQLException("test doesnot exist");
            }

        } catch (SQLException e) {
            throw new Error(e);
        }
    }

    /**
     * Get all test from the Test table
     *
     * @return all the test in the arraylist of Test object
     * @throws RemoteException
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

    /**
     * Get all the upcoming test from test table with reference to current date
     *
     * @return all the upcoming test in the arraylist of Test object
     * @throws RemoteException
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
            }
            return testList;

        } catch (SQLException e) {
            throw new Error(e);
        }
    }

    /**
     * Get the searched test from the test table with reference to value in search
     * bar
     *
     * @param searchQuery searched value in search bar
     * @return the search result in the arraylist of Test object
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
     * Get the specific test from Test table with reference to testId
     *
     * @param testId value of the testId
     * @return returns the specific test with reference to testId
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
                    q.addAnswer(
                            new Answer(rsa.getInt("AID"), rsa.getString("title"), rsa.getBoolean("correct_status")));
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
     * Update the various attributes of the Test table
     *
     * @param test Object of the Test
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
                updateQuestion(test.getId(), value);

            }
        } catch (SQLException e) {
            throw new Error(e);
        }
    }

    // updates question for each testid with question parameter
    private void updateQuestion(int testId, Question question) {
        try {

            if (question.getId() == null) {
                saveQuestion(testId, question);
                System.out.println("added new question from update");
            } else {
                // insert question
                String query = "INSERT INTO question (TITLE,MARKS,TEST_ID,QID) VALUES(?,?,?,?)";
                PreparedStatement ps = cn.prepareStatement(query);
                ps.setString(1, question.getTitle());
                ps.setInt(2, question.getMarks());
                ps.setInt(3, testId);
                ps.setInt(4, question.getId());
                ps.executeUpdate();
                // deleted prexisting answers
                String deleteQuery = "DELETE FROM answer WHERE question_id=?";
                PreparedStatement psdt = cn.prepareStatement(deleteQuery);
                psdt.setInt(1, question.getId());
                psdt.executeUpdate();

                // enter all answers
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
     * Get the test by level and semester from test table
     *
     * @param userId
     * @param level    value of the specific level
     * @param semester value of the specific semester
     * @return the available Test with reference to level and semester in the
     *         arraylist of Test object
     * @throws RemoteException
     */
    @Override
    public ArrayList<Test> getTestByLevelSem(int userId, int level, int semester) throws RemoteException {
        ArrayList<Test> testList = new ArrayList();
        try {
            String query = "SELECT * from test WHERE test.TID NOT IN (SELECT result.test_id FROM result WHERE result.user_id=? ) AND level=? AND semester=?  AND test.date>=CURDATE()";
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, userId);
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
     * verify the password of the test before accessing it
     * 
     * @param testId   value of the testId
     * @param password password of specific test to be inserted
     * @return return the boolean value as a result
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
     * Save the result into result and resultItem table
     *
     * @param result Object of result
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

                        if (resultItem.getSelectedAnswer() != null) {
                            psmt.setString(3, resultItem.getSelectedAnswer());
                        } else {
                            psmt.setString(3, "none");
                        }
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
     * Get the result with test object
     *
     * @param resultId value of the resultId
     * @return result with reference to resultId
     * @throws RemoteException
     */
    @Override
    public Result getResult(int resultId) throws RemoteException {
        try {
            Result result = new Result();
            Test test = new Test();
            ArrayList<ResultItem> resultItems = new ArrayList<>();

            String resultQuery = "SELECT * FROM RESULT" + " INNER JOIN TEST" + " ON test.TID = result.test_id"
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

            String resultItemsQuery = "SELECT *" + "FROM question " + "INNER JOIN result_item "
                    + "ON result_item.question_id =question.QID " + "WHERE result_id=?";
            PreparedStatement ps = cn.prepareStatement(resultItemsQuery);
            ps.setInt(1, resultId);
            ResultSet rslt = ps.executeQuery();
            // creating result items
            while (rslt.next()) {
                Question q = new Question();
                q.setTitle(rslt.getString("title"));
                q.setMarks(rslt.getInt("marks"));
                q.setId(rslt.getInt("QID"));
                resultItems.add(new ResultItem(resultId, rslt.getString("correct_answer"),
                        rslt.getString("selected_answer"), rslt.getBoolean("correct"), q));
            }
            result.setResultItem(resultItems);
            System.out.println(result.toString());
            return result;

        } catch (SQLException ex) {
            Logger.getLogger(TestDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new Result();
    }

    /**
     * Get the result of the user with reference to userID
     * 
     * @param userId id of the student to view their respective result
     * @return result arraylist of the user
     * @throws RemoteException
     */
    @Override
    public ArrayList<Result> getResultByUser(int userId) throws RemoteException {
        try {
            String resultQuery = "SELECT * FROM RESULT" + " INNER JOIN TEST" + " ON test.TID = result.test_id"
                    + " WHERE user_id=?";
            PreparedStatement psmt = cn.prepareStatement(resultQuery);
            psmt.setInt(1, userId);

            ResultSet rs = psmt.executeQuery();

            ArrayList<Result> results = new ArrayList<>();
            // add result results array
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

            }
            return results;

        } catch (SQLException ex) {
            Logger.getLogger(TestDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    /**
     * To get all the result
     * 
     * @return arraylist of all the students
     * @throws RemoteException
     */
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

                // creating user
                User u = new User();
                u.setEmail(rs.getString("email"));
                u.setFirstName(rs.getString("firstname"));
                u.setLastName(rs.getString("lastname"));
                u.setLevel(rs.getInt("level"));
                u.setSemester(rs.getInt("semester"));
                u.setUserID(rs.getInt("UID"));
                u.setRole(rs.getString("role"));

                // creating test
                Test t = new Test();
                t.setId(rs.getInt("TID"));
                t.setTitle(rs.getString("title"));
                t.setDate(rs.getDate("date"));
                t.setStartTime(rs.getTime("start_time"));
                t.setEndTime(rs.getTime("end_time"));
                t.setLevel(rs.getInt("level"));
                t.setSemester(rs.getInt("semester"));
                t.setPassword(rs.getString("password"));
                t.setDuration(rs.getInt("duration"));
                t.setPassMarks(rs.getInt("pass_marks"));
                t.setFullMarks(rs.getInt("full_marks"));
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
