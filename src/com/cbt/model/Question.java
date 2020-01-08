package com.cbt.model;

import java.io.Serializable;

import java.util.ArrayList;

/**
 * Model for questions of test
 * @author Symetryn
 */
public class Question implements Serializable {

    private static final long serialVersionUID = 3L;

    private Integer id;
    private String title;
    private int marks;
    private ArrayList<Answer> answers;

    public Question() {
        answers = new ArrayList();
    }

    
    //Getters and setters methods for question model
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }

    public void addAnswer(Answer a) {
        this.answers.add(a);
    }

    public void removeAnswer(int index) {
        this.answers.remove(index);
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
