package com.kvs.app.quizapp.mapper;

import com.kvs.app.quizapp.dto.QuizShortform;
import com.kvs.app.quizapp.entity.QuizzesEntity;

public class QuizRowAndQuizShortformMapper {
    public static QuizShortform toDTO(QuizzesEntity quizzesEntity) {
        QuizShortform quizShortform = new QuizShortform();
        quizShortform.setQuizId(quizzesEntity.getId());
        quizShortform.setQuizTitle(quizzesEntity.getQuiztitle());
        return quizShortform;
    }
}
