package com.kvs.app.quizapp.dto;

import javax.validation.constraints.NotNull;

public class CompletedQuizzes {
    @NotNull
    String id;

    @NotNull
    String quizTitle;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }
}
