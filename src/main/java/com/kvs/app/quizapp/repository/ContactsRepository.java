package com.kvs.app.quizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kvs.app.quizapp.entity.ContactsEntity;

@Repository
public interface ContactsRepository extends JpaRepository<ContactsEntity, String> {
}
