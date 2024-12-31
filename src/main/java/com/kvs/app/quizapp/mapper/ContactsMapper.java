package com.kvs.app.quizapp.mapper;

import com.kvs.app.quizapp.dto.Contacts;
import com.kvs.app.quizapp.entity.ContactsEntity;

public class ContactsMapper {
    
    public static Contacts toDTO(ContactsEntity contactsEntity) {
        Contacts contacts = new Contacts();
        contacts.setId(contactsEntity.getId());
        return contacts;
    }
}
