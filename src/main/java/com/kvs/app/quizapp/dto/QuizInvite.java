package com.kvs.app.quizapp.dto;

import javax.validation.constraints.NotNull;

public class QuizInvite {
    @NotNull
    private String id;

    @NotNull
    private String quizName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    
}
