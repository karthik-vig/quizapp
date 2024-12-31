package com.kvs.app.quizapp.dto;

import java.util.List;

public class NewQuiz {
    
    private String quizTemplateId;
    private String quizTitle;
    private List<String> emails;

    public String getQuizTemplateId() {
        return quizTemplateId;
    }
    public void setQuizTemplateId(String quizTemplateId) {
        this.quizTemplateId = quizTemplateId;
    }
    public String getQuizTitle() {
        return quizTitle;
    }
    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }
    public List<String> getEmails() {
        return emails;
    }
    public void setEmails(List<String> emails) {
        this.emails = emails;
    }
    
}
