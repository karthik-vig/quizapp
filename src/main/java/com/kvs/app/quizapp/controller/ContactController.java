package com.kvs.app.quizapp.controller;

import java.util.HashMap;

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

import com.kvs.app.quizapp.dto.NewContact;
import com.kvs.app.quizapp.service.ContactsService;

import jakarta.servlet.http.HttpSession;

import java.util.Map;

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
        HashMap<String, Object> response = new HashMap<>();
        // get the user's email
        String userEmail = (String) session.getAttribute("username");
        if (userEmail == null) {
            response.put("status", "error");
            response.put("message", "user is not logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        // service to fetch all the contacts the user has
        // the service uses uses the useremail to get the userid
        // then uses the userid to fetch all the contacts and return it
        response = (HashMap<String, Object>) this.contactsService.getAllContacts(userEmail);
        ResponseEntity.BodyBuilder responseEntity = ResponseEntity.status((HttpStatus) response.get("statusCode"));
        response.remove("statusCode");
        return responseEntity.body(response);
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> createNewContact(
        @RequestBody NewContact newContact,
        HttpSession session
    ) {
        HashMap<String, Object> response = new HashMap<>();
        // get the user's email
        String userEmail = (String) session.getAttribute("username");
        if (userEmail == null) {
            response.put("status", "error");
            response.put("message", "user is not logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        // the service creates a new contacts entry in the contacts table for the user
        // return success or fail status
        String status = this.contactsService.createNewContact(userEmail, newContact);
        switch(status) {
            case "Duplicate":
                response.put("status", "Error");
                response.put("message", "Duplicate value, already exists");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            case "Fail":
                response.put("status", "Error");
                response.put("message", "Failed");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        response.put("status", "Success");
        response.put("message", "added the contact");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{contactId}")
    public ResponseEntity<Map<String, Object>> modifyContacts(
        @PathVariable String contactId,
        @RequestBody NewContact newContact,
        HttpSession session
    ) {
        HashMap<String, Object> response = new HashMap<>();
        // get the user's email
        String userEmail = (String) session.getAttribute("username");
        if (userEmail == null) {
            response.put("status", "error");
            response.put("message", "user is not logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        // the service needs to get the contactId and ensure it belongs to the 
        // user requesting it; then replace the existing one with one given by user
        // the process it similar to creating a new contact
        String status = contactsService.modifyContact(userEmail, newContact, contactId);
        if (status.equals("Fail")) {
            response.put("status", "Error");
            response.put("message", "Operation Failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response.put("status", "Succcess");
        response.put("message", "modified the contact");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<Map<String, Object>> deleteContact(
        @PathVariable String contactId,
        HttpSession session
    ) {
        HashMap<String, Object> response = new HashMap<>();
        // get the user's email
        String userEmail = (String) session.getAttribute("username");
        if (userEmail == null) {
            response.put("status", "error");
            response.put("message", "user is not logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        // service will check if the contact id belongs to the user
        // and then delete the entry from the table
        String status = contactsService.deleteContact(userEmail, contactId);
        if (status.equals("Fail")) {
            response.put("status", "Error");
            response.put("message", "Operation Failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response.put("status", "Succcess");
        response.put("message", "deleted the contact");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
