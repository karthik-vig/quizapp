package com.kvs.app.quizapp.mapper;

import com.kvs.app.quizapp.dto.QuestionTemplate.QuestionAndAnswer;
import com.kvs.app.quizapp.entity.QuizTemplateEntity;
import com.kvs.app.quizapp.dto.QuizTemplate;

public class QuizTemplateMapper {
    public static QuizTemplateEntity toEntity(
        QuizTemplate<QuestionAndAnswer> quizTemplate) {
        QuizTemplateEntity quizTemplateEntity = new QuizTemplateEntity();
        quizTemplateEntity.setQuiztemplatetitle(quizTemplate.getTitle());
        return quizTemplateEntity;
    }
}
