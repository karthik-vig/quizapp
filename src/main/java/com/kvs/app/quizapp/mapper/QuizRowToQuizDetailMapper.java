package com.kvs.app.quizapp.mapper;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kvs.app.quizapp.dto.QuizDetail;
import com.kvs.app.quizapp.dto.QuizTemplate;
import com.kvs.app.quizapp.dto.QuestionTemplate.QuestionAndAnswer;
import com.kvs.app.quizapp.entity.QuizzesEntity;

public class QuizRowToQuizDetailMapper {

    private static Gson gson = new Gson();
    
    public static QuizDetail toDto(
        QuizzesEntity quizRow
    ) {
        QuizDetail quizDetail = new QuizDetail();
        quizDetail.setQuiztitle(quizRow.getQuiztitle());
        Type type = new TypeToken<QuizTemplate<QuestionAndAnswer>>(){}.getType();
        QuizTemplate<QuestionAndAnswer> quizData = gson.fromJson(quizRow.getQuizdata(), type);
        quizDetail.setQuizDetails(quizData);
        quizDetail.setExpiration(quizRow.getExpiration());
        return quizDetail;
    }
}
