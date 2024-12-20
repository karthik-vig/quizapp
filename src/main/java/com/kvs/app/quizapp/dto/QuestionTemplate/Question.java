package com.kvs.app.quizapp.dto.QuestionTemplate;

import java.util.Vector;
import javax.validation.constraints.NotNull;

public class Question {
    @NotNull
    private String questionType;
    @NotNull
    private String question;
    @NotNull
    private Vector<String> answerOptions;
    
    public String getQuestionType() {
        return questionType;
    }
    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public Vector<String> getAnswerOptions() {
        return answerOptions;
    }
    public void setAnswerOptions(Vector<String> answerOptions) {
        this.answerOptions = answerOptions;
    }

    
}
