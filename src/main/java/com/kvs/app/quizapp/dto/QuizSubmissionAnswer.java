package com.kvs.app.quizapp.dto;

import javax.validation.constraints.NotNull;
import java.util.Vector;

public class QuizSubmissionAnswer {
    @NotNull
    private Vector<Vector<Integer>> answers;

    public Vector<Vector<Integer>> getValue() {
        return this.answers;
    }

    public void setValue(Vector<Vector<Integer>> answers) {
        this.answers = answers;
    }
    
}
