package com.kvs.app.quizapp.dto.QuestionTemplate;

import java.util.Vector;
import javax.validation.constraints.NotNull;

public class QuestionAndAnswer extends Question {
    @NotNull
    private Vector<Integer> correctOptions;

    public Vector<Integer> getCorrectOptions() {
        return correctOptions;
    }

    public void setCorrectOptions(Vector<Integer> correctOptions) {
        this.correctOptions = correctOptions;
    }

}
