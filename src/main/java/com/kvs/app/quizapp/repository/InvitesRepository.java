package com.kvs.app.quizapp.repository;

import com.kvs.app.quizapp.entity.InvitesEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InvitesRepository extends JpaRepository<InvitesEntity, String> {

    @Query("SELECT i FROM InvitesEntity i WHERE i.userid = :userid AND i.invitestatus = true ORDER BY i.updatedat DESC")
    List<InvitesEntity> findByUserid(@Param("userid") String userid);

    @Query("SELECT i.quizid FROM InvitesEntity i WHERE i.id = :inviteid AND i.userid = :userid ORDER BY i.updatedat DESC")
    String getQuizIdByInviteId(@Param("inviteid") String inviteId, @Param("userid") String userId);

    @Query("SELECT i.quizid FROM InvitesEntity i WHERE i.id = :inviteid AND i.invitestatus = true AND i.userid = :userid ORDER BY i.updatedat DESC")
    String getQuizIdByInviteIdIfActive(@Param("userid") String userId, @Param("inviteid") String inviteId);

    @Query("SELECT i.quizid FROM InvitesEntity i WHERE i.userid = :userid AND i.quizid = :quizid ORDER BY i.updatedat DESC")
    String getQuizIdIfUniqueForUser(@Param("userid") String userId, @Param("quizid") String quizId);

    @Transactional
    @Modifying
    @Query("UPDATE InvitesEntity i SET i.invitestatus = false WHERE i.id = :inviteid AND i.userid = :userid")
    void setAsCompleted(@Param("inviteid") String inviteId, @Param("userid") String userId);
}
