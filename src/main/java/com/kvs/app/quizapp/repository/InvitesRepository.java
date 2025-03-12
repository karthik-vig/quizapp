package com.kvs.app.quizapp.repository;

import com.kvs.app.quizapp.entity.InvitesEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitesRepository extends JpaRepository<InvitesEntity, String> {

    @Query("SELECT i FROM InvitesEntity i WHERE i.userid = :userid")
    List<InvitesEntity> findByUserid(@Param("userid") String userid);

    @Query("SELECT i.quizid FROM InvitesEntity i WHERE i.id = :inviteid")
    String getQuizIdByInviteId(@Param("inviteid") String inviteId);
}
