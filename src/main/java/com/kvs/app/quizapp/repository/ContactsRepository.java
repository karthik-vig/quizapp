package com.kvs.app.quizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kvs.app.quizapp.dto.Contacts;
import com.kvs.app.quizapp.entity.ContactsEntity;

import jakarta.transaction.Transactional;

import java.util.List;

@Repository
public interface ContactsRepository extends JpaRepository<ContactsEntity, String> {

    @Query("SELECT c FROM ContactsEntity c WHERE c.userid = :userId")
    List<ContactsEntity> getAllContacts(@Param("userId") String userId);

    @Query("SELECT new com.kvs.app.quizapp.dto.Contacts(c.id, u.email) FROM ContactsEntity c JOIN UsersEntity u ON c.relateduserid = u.id WHERE c.userid = :userId")
    List<Contacts> getRelatedUserEmailById(@Param("userId") String userId);

    @Transactional
    @Modifying
    @Query("UPDATE ContactsEntity c SET c.relateduserid = :relatedUserId WHERE c.id = :contactId AND c.userid = :userId")
    int modifyContact(@Param("userId") String userId, @Param("contactId") String contactId, @Param("relatedUserId") String relatedUserId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ContactsEntity c WHERE c.id = :contactId AND c.userid = :userId")
    int deleteContactByIdAndUserId(@Param("contactId") String contactId, @Param("userId") String userId);

    @Query("SELECT c FROM ContactsEntity c WHERE c.relateduserid = :relatedUserId AND c.userid = :userId")
    ContactsEntity findByRelatedUserId(@Param("relatedUserId") String relatedUserId, @Param("userId") String userId);

}
