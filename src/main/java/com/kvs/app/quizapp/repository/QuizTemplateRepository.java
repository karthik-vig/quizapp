package com.kvs.app.quizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kvs.app.quizapp.entity.QuizTemplateEntity;

import jakarta.transaction.Transactional;

import java.util.List;

@Repository
public interface QuizTemplateRepository extends JpaRepository<QuizTemplateEntity, String> {

    @Query("SELECT q FROM QuizTemplateEntity q WHERE q.userid = :userid")
    List<QuizTemplateEntity> findByUserId(@Param("userid") String userId);

    @Query("SELECT q FROM QuizTemplateEntity q WHERE q.userid = :userid AND q.id = :quizTemplateId")
    QuizTemplateEntity findByQuizTemplateId(@Param("userid") String userId, @Param("quizTemplateId") String quizTemplateId);

    @Transactional
    @Modifying
    @Query("UPDATE QuizTemplateEntity q SET q.quiztemplatetitle = :quizTemplateTitle WHERE q.userid = :userid AND q.id = :quizTemplateId")
    int updateQuizTemplateTitle(@Param("userid") String userId, @Param("quizTemplateTitle") String quizTemplateTitle, @Param("quizTemplateId") String quizTemplateId);

    @Transactional
    @Modifying
    @Query("UPDATE QuizTemplateEntity q SET q.quiztemplate = :quizTemplateJson WHERE q.userid = :userid AND q.id = :quizTemplateId")
    int updateQuizTemplateJson(@Param("userid") String userId, @Param("quizTemplateJson") String quizTemplateJson, @Param("quizTemplateId") String quizTemplateId);

    @Transactional
    @Modifying
    @Query("DELETE FROM QuizTemplateEntity q WHERE q.id = :quizTemplateId AND q.userid = :userId")
    int deleteByQuizTemplateIdAndUserId(@Param("quizTemplateId") String quizTemplateId, @Param("userId") String userId);
}
