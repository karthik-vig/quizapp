package com.kvs.app.quizapp.dto;

import java.time.LocalDateTime;

import com.kvs.app.quizapp.dto.QuestionTemplate.QuestionAndAnswer;

public class QuizDetail {
    
    private String quiztitle;
    private QuizTemplate<QuestionAndAnswer> quizdetails;
    private LocalDateTime expiration;

    public String getQuiztitle() {
        return quiztitle;
    }
    public void setQuiztitle(String quiztitle) {
        this.quiztitle = quiztitle;
    }
    public QuizTemplate<QuestionAndAnswer> getQuizDetails() {
        return quizdetails;
    }
    public void setQuizDetails(QuizTemplate<QuestionAndAnswer> quizdetails) {
        this.quizdetails = quizdetails;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }    

    public LocalDateTime getExpiration() {
        return this.expiration;
    }
}
