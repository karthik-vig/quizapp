package com.kvs.app.quizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kvs.app.quizapp.entity.SubmissionsEntity;

public interface SubmissionsRepository extends JpaRepository<SubmissionsEntity, String> {
}
