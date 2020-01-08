/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cbt.model;

import java.io.Serializable;

/**
 * Overloaded constructor showing polymorphism
 */
public class Answer implements Serializable {

    private static final long serialVersionUID = 2L;

    private String title;
    private Boolean correctAnswer;
    private Integer id;

    /**
     * Overloaded constructor showing polymorphism
     */
    public Answer() {
    }

    /**
     * Overloaded constructor showing polymorphism
     */
    public Answer(String title, boolean correctAnswer) {
        this.title = title;
        this.correctAnswer = correctAnswer;

    }

    /**
     * Overloaded constructor showing polymorphism
     */
    public Answer(int id, String title, boolean correctAnswer) {
        this.title = title;
        this.correctAnswer = correctAnswer;
        this.id = id;

    }

// Getters and setters method for Answer model
    public String getTitle() {
        return title;
    }

    public Boolean getCorrectAnswer() {
        return correctAnswer;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCorrectAnswer(Boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
