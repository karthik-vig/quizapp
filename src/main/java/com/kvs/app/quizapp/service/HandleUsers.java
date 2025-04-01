package com.kvs.app.quizapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kvs.app.quizapp.entity.UsersEntity;
import com.kvs.app.quizapp.helpers.ULIDGenerator;
import com.kvs.app.quizapp.repository.UsersRepository;

// import java.util.UUID;

@Service
public class HandleUsers {
    
    private UsersRepository usersRepository;

    @Autowired
    public HandleUsers(
        UsersRepository usersRepository
    ) {
        this.usersRepository = usersRepository;
    }

    public String createUserIfNotExist(String userEmail) {
        // try to get the row from the users table for the given email
        UsersEntity usersRow = this.usersRepository.findByEmail(userEmail);
        // if the row is null, create a new entry in the users table
        if (usersRow == null) {
            // String uuid = UUID.randomUUID().toString();
            String uuid = ULIDGenerator.getULID();
            UsersEntity usersEntity = new UsersEntity();
            usersEntity.setId(uuid);
            usersEntity.setEmail(userEmail);
            this.usersRepository.save(usersEntity);
        }
        return "Success";
    }
}
