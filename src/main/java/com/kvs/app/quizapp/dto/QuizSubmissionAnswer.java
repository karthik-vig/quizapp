package com.kvs.app.quizapp.dto;

import javax.validation.constraints.NotNull;
import java.util.Vector;

public class QuizSubmissionAnswer {
    @NotNull
    private Vector<Integer> answers;

    public Vector<Integer> getValue() {
        return this.answers;
    }

    public void setValue(Vector<Integer> answers) {
        this.answers = answers;
    }
    
}
