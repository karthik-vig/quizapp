package com.kvs.app.quizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kvs.app.quizapp.entity.QuizTemplateEntity;

public interface QuizTemplateRepository extends JpaRepository<QuizTemplateEntity, String> {
}
