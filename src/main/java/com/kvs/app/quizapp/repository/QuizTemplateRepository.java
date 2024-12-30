package com.kvs.app.quizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kvs.app.quizapp.entity.QuizTemplateEntity;

import java.util.List;

@Repository
public interface QuizTemplateRepository extends JpaRepository<QuizTemplateEntity, String> {

    @Query("SELECT q FROM QuizTemplateEntity q WHERE q.userid = :userid")
    List<QuizTemplateEntity> findByUserId(@Param("userid") String userId);

    @Query("SELECT q FROM QuizTemplateEntity q WHERE q.id = :quizTemplateId")
    QuizTemplateEntity findByQuizTemplateId(@Param("quizTemplateId") String quizTemplateId);

    @Query("UPDATE QuizTemplateEntity q SET q.quiztemplatetitle = :quizTemplateTitle WHERE q.id = :quizTemplateId")
    void updateQuizTemplateTitle(@Param("quizTemplateTitle") String quizTemplateTitle, @Param("quizTemplateId") String quizTemplateId);

    @Query("UPDATE QuizTemplateEntity q SET q.quiztemplate = :quizTemplateJson WHERE q.id = :quizTemplateId")
    void updateQuizTemplateJson(@Param("quizTemplateJson") String quizTemplateJson, @Param("quizTemplateId") String quizTemplateId);
}
