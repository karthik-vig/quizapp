package com.kvs.app.quizapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kvs.app.quizapp.entity.SubmissionsEntity;

import jakarta.persistence.Tuple;

@Repository
public interface SubmissionsRepository extends JpaRepository<SubmissionsEntity, String> {

    @Query("SELECT s.id AS id, q.quiztitle AS quizTitle FROM SubmissionsEntity s JOIN QuizzesEntity q ON s.quizid = q.id WHERE s.userid = :userid ORDER BY s.updatedat DESC")
    List<Tuple> getQuizIdByUserId(@Param("userid") String userId);

    @Query("SELECT s, q FROM SubmissionsEntity s JOIN QuizzesEntity q ON s.quizid = q.id WHERE q.userid = :userid ORDER BY s.updatedat DESC")
    List<Tuple> getSubmissionsForQuizOwner(@Param("userid") String userId);

}
