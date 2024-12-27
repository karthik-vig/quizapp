package com.kvs.app.quizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kvs.app.quizapp.entity.ContactsEntity;

public interface ContactsRepository extends JpaRepository<ContactsEntity, String> {
}
