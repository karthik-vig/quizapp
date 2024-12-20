package com.kvs.app.quizapp.dto;

import javax.validation.constraints.NotNull;
import java.util.Vector;

public class QuizTemplate<T> {
    @NotNull
    private String title;
    @NotNull
    private Vector<T> questions;
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Vector<T> getQuestions() {
        return questions;
    }
    public void setQuestions(Vector<T> questions) {
        this.questions = questions;
    }
    
}
