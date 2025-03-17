package com.kvs.app.quizapp.repository;

// import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kvs.app.quizapp.entity.QuizzesEntity;

import java.util.List;

@Repository
public interface QuizzesRepository extends JpaRepository<QuizzesEntity, String> {  

    @Query("SELECT q FROM QuizzesEntity q WHERE q.id = :id ORDER BY q.updatedat DESC")
    QuizzesEntity findByQuizid(@Param("id") String id);

    @Query("SELECT q FROM QuizzesEntity q WHERE q.userid = :userId ORDER BY q.updatedat DESC")
    List<QuizzesEntity> findByUserId(@Param("userId") String userId);
}
