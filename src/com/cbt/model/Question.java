/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbt.model;

import java.io.Serializable;

import java.util.ArrayList;

/**
 *
 * @author Symetryn
 */
public class Question implements Serializable {

    private static final long serialVersionUID = 3L;

    private Integer id;
    private String title;
    private int marks;
    private ArrayList<Answer> answers;

    /**
     *
     */
    public Question() {
        answers = new ArrayList();
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    /**
     *
     * @param answers
     */
    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }

    /**
     *
     * @param a
     */
    public void addAnswer(Answer a) {
        this.answers.add(a);
    }

    /**
     *
     * @param index
     */
    public void removeAnswer(int index) {
        this.answers.remove(index);
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
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

}
