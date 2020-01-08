package com.cbt.model;

import java.io.Serializable;

/**
 * Model for storing each question and selected answer after exam
 * @author Symetryn
 */
public class ResultItem implements Serializable {

    private static final long serialVersionUID = 5L;
    private Integer questionId;
    private String correctAnswer;
    private String selectedAnswer;
    private Boolean correct;
    private Question question;

    public ResultItem() {
    }

    public ResultItem(Integer question, String correctAnswer, String selectedAnswer, Boolean correct) {
        this.questionId = question;
        this.correctAnswer = correctAnswer;
        this.selectedAnswer = selectedAnswer;
        this.correct = correct;
    }

    public ResultItem(Integer questionId, String correctAnswer, String selectedAnswer, Boolean correct, Question question) {
        this.questionId = questionId;
        this.correctAnswer = correctAnswer;
        this.selectedAnswer = selectedAnswer;
        this.correct = correct;
        this.question = question;
    }
//Getter and setter methods for result item model
    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

}
