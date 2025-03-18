package com.kvs.app.quizapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kvs.app.quizapp.service.SubmissionService;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private SubmissionService SubmissionService;

    @Autowired
    public SubmissionController(
        SubmissionService submissionService
    ) {
        this.SubmissionService = submissionService;
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getAllSubmissions(
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
        // get the list of submission done by this user
        response = (HashMap<String, Object>) this.SubmissionService.getAllSubmissions(userEmail);
        ResponseEntity.BodyBuilder responsBodyBuilder = ResponseEntity.status((HttpStatus) response.get("statusCode"));
        response.remove("statusCode");
        return responsBodyBuilder.body(response);
    }
    
}
