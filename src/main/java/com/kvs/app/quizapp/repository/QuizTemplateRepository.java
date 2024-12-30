package com.kvs.app.quizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kvs.app.quizapp.entity.QuizTemplateEntity;

import java.util.List;

public interface QuizTemplateRepository extends JpaRepository<QuizTemplateEntity, String> {

    @Query("SELECT q FROM QuizTemplateEntity q where q.userid = :userid")
    List<QuizTemplateEntity> findByUserId(@Param("userid") String userId);
}
