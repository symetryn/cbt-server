/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbt.bll;

import com.google.gson.Gson;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Symetryn
 */
public class Result implements Serializable {

    private static final long serialVersionUID = 4L;
    private int id;
    private int testId;
    private int userId;
    private int marks = 0;
    private Boolean status;
    private ArrayList<ResultItem> resultItem;
    private Test test;

    /**
     *
     */
    public Result() {
        resultItem = new ArrayList<>();
    }

    /**
     *
     * @param id
     * @param testId
     * @param userId
     * @param marks
     * @param status
     * @param resultItem
     */
    public Result(int id, int testId, int userId, int marks, Boolean status, ArrayList<ResultItem> resultItem) {
        this.id = id;
        this.testId = testId;
        this.userId = userId;
        this.marks = marks;
        this.status = status;
        this.resultItem = resultItem;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public int getTestId() {
        return testId;
    }

    /**
     *
     * @param testId
     */
    public void setTestId(int testId) {
        this.testId = testId;
    }

    /**
     *
     * @return
     */
    public int getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     */
    public int getMarks() {
        return marks;
    }

    /**
     *
     * @param marks
     */
    public void setMarks(int marks) {
        this.marks = marks;
    }

    /**
     *
     * @return
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     *
     * @return
     */
    public ArrayList<ResultItem> getResultItem() {
        return resultItem;
    }

    /**
     *
     * @param resultItem
     */
    public void setResultItem(ArrayList<ResultItem> resultItem) {
        this.resultItem = resultItem;
    }

    /**
     *
     * @param r
     */
    public void pushResultItem(ResultItem r) {
        resultItem.add(r);
    }

    /**
     *
     * @param i
     */
    public void addMarks(Integer i) {
        this.marks += i;
    }

    /**
     *
     * @return
     */
    public Test getTest() {
        return test;
    }

    /**
     *
     * @param test
     */
    public void setTest(Test test) {
        this.test = test;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        Gson g = new Gson();
        return g.toJson(this);
    }

}
