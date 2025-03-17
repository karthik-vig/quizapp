package com.kvs.app.quizapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map;

import com.kvs.app.quizapp.repository.ContactsRepository;
import com.kvs.app.quizapp.repository.UsersRepository;
import com.kvs.app.quizapp.dto.Contacts;
import com.kvs.app.quizapp.dto.NewContact;
import com.kvs.app.quizapp.entity.ContactsEntity;
import com.kvs.app.quizapp.entity.UsersEntity;
// import com.kvs.app.quizapp.mapper.ContactsMapper;

@Service
public class ContactsService {
    
    private UsersRepository usersRepository;
    private ContactsRepository contactsRepository;

    @Autowired
    public ContactsService(
        UsersRepository usersRepository,
        ContactsRepository contactsRepository
    ) {
        this.usersRepository = usersRepository;
        this.contactsRepository = contactsRepository;
    }

    public Map<String, Object> getAllContacts(String userEmail) {
        HashMap<String, Object> response = new HashMap<>();
        UsersEntity usersRow = this.usersRepository.findByEmail(userEmail);
        if ( usersRow == null ) {
            response.put("status", "Error");
            response.put("message", "Could not fetch the user details");
            response.put("statusCode", HttpStatus.UNAUTHORIZED);
            return response;
        }
        List<Contacts> contacts = this.contactsRepository.getRelatedUserEmailById(usersRow.getId());
        response.put("status", "Success");
        response.put("data", contacts);
        response.put("statusCode", HttpStatus.OK);
        return response;
    }

    private String createUserIfNotExist(NewContact newContact) {
        String relatedUserId = null;
        UsersEntity relatedUsersRow = this.usersRepository.findByEmail(newContact.getEmail());
        if ( relatedUsersRow == null ) {
            UsersEntity newUsersEntity = new UsersEntity();
            newUsersEntity.setEmail(newContact.getEmail());
            String uuid = UUID.randomUUID().toString();
            newUsersEntity.setId(uuid);
            UsersEntity newUsersRow = this.usersRepository.save(newUsersEntity);
            relatedUserId = newUsersRow.getId();
        } else {
            relatedUserId = relatedUsersRow.getId();
        }
        return relatedUserId;
    }

    public String createNewContact(
        String userEmail, 
        NewContact newContact
        ) {
        UsersEntity usersRow = this.usersRepository.findByEmail(userEmail);
        if ( usersRow == null ) return "Fail";
        // check if the new contact already exist in users table
        // if yes then fetch the id; if not insert the contact into
        // the table and get the id
        String relatedUserId = this.createUserIfNotExist(newContact);
        // check if this contact would be a duplicate
        ContactsEntity existingContactsRow = contactsRepository.findByRelatedUserId(relatedUserId, usersRow.getId());
        if (existingContactsRow != null) return "Duplicate";
        ContactsEntity contactsEntity = new ContactsEntity();
        String uuid = UUID.randomUUID().toString();
        contactsEntity.setId(uuid);
        contactsEntity.setUserid(usersRow.getId());
        contactsEntity.setRelateduserid(relatedUserId);
        contactsRepository.save(contactsEntity);
        return "Success";
    }

    public String modifyContact(
        String userEmail,
        NewContact newContact,
        String contactId
        ) {
        UsersEntity usersRow = usersRepository.findByEmail(userEmail);
        if ( usersRow == null ) return "Fail";
        String relatedUserId = createUserIfNotExist(newContact);
        int rowsModified = contactsRepository.modifyContact(usersRow.getId(), contactId, relatedUserId);
        if (rowsModified != 1) {
            return "Fail";
        }
        return "Success";
    }

    public String deleteContact(
        String userEmail, 
        String ContactId
    ) {
        UsersEntity usersRow = usersRepository.findByEmail(userEmail);
        if ( usersRow == null ) return "Fail";
        int rowsDeleted = contactsRepository.deleteContactByIdAndUserId(ContactId, usersRow.getId());
        if (rowsDeleted != 1) {
            return "Fail";
        }
        return "Success";
    }
}
