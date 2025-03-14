package com.kvs.app.quizapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kvs.app.quizapp.dto.NewQuiz;
import com.kvs.app.quizapp.service.QuizService;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    private QuizService quizService;

    @Autowired
    public QuizController(
        QuizService quizService
    ) {
        this.quizService = quizService;
    }
    
    // create quiz section
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getAllQuiz(
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
        // the service fetches all the quiz's id and title and returns it
        response.put("status", "Success");
        response.put("data", quizService.getAllQuiz(userEmail));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> createQuiz(
        @RequestBody NewQuiz newQuiz,
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
        // the service needs to create the quiz in the quiz table, as well as put them in 
        // the invites table
        quizService.createQuiz(userEmail, newQuiz);
        response.put("status", "Success");
        response.put("message", "Created the quiz");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
