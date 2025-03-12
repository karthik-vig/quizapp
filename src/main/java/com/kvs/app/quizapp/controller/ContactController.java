package com.kvs.app.quizapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kvs.app.quizapp.dto.Contacts;
import com.kvs.app.quizapp.dto.NewContact;
import com.kvs.app.quizapp.service.ContactsService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    private ContactsService contactsService;

    @Autowired
    public ContactController(
        ContactsService contactsService
    ) {
        this.contactsService = contactsService;
    }
    
    // contacts api section
    @GetMapping("/")
    public ResponseEntity<?> getContacts(HttpSession session) {
        // get the user's email
        String userEmail = (String) session.getAttribute("username");
        // service to fetch all the contacts the user has
        // the service uses uses the useremail to get the userid
        // then uses the userid to fetch all the contacts and return it
        List<Contacts> contacts = this.contactsService.getAllContacts(userEmail);
        return ResponseEntity.ok(contacts);
    }

    @PostMapping("/")
    public ResponseEntity<?> createNewContact(
        @RequestBody NewContact newContact,
        HttpSession session
    ) {
        // get the user's email
        String userEmail = (String) session.getAttribute("username");
        // the service creates a new contacts entry in the contacts table for the user
        // return success or fail status
        String status = this.contactsService.createNewContact(userEmail, newContact);
        switch(status) {
            case "Duplicate":
                return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);
            case "Fail":
                return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Success");
    }

    @PutMapping("/{contactId}")
    public ResponseEntity<?> modifyContacts(
        @PathVariable String contactId,
        @RequestBody NewContact newContact,
        HttpSession session
    ) {
        // get the user's email
        String userEmail = (String) session.getAttribute("username");
        // the service needs to get the contactId and ensure it belongs to the 
        // user requesting it; then replace the existing one with one given by user
        // the process it similar to creating a new contact
        String status = contactsService.modifyContact(userEmail, newContact, contactId);
        if (status.equals("Fail")) {
            return new ResponseEntity<>("Fail", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Success");
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<?> deleteContact(
        @PathVariable String contactId,
        HttpSession session
    ) {
        // get the user's email
        String userEmail = (String) session.getAttribute("username");
        // service will check if the contact id belongs to the user
        // and then delete the entry from the table
        String status = contactsService.deleteContact(userEmail, contactId);
        if (status.equals("Fail")) {
            return new ResponseEntity<>("Fail", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Success");
    }
}
