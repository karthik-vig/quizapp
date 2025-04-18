package com.kvs.app.quizapp.repository;

import com.kvs.app.quizapp.entity.UsersEntity;

// import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, String> {

    @Query("SELECT u FROM UsersEntity u WHERE u.email = :email ORDER BY u.updatedat DESC")
    UsersEntity findByEmail(@Param("email") String userEmail);

}
