package com.kvs.app.quizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kvs.app.quizapp.entity.SubmissionsEntity;

@Repository
public interface SubmissionsRepository extends JpaRepository<SubmissionsEntity, String> {

}
