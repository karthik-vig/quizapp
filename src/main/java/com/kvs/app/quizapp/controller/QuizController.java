package com.kvs.app.quizapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kvs.app.quizapp.dto.NewQuiz;
import com.kvs.app.quizapp.service.QuizService;

import jakarta.servlet.http.HttpSession;

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
    public ResponseEntity<?> getAllQuiz(
        HttpSession session
    ) {
        // get the user's email from session
        String userEmail = (String) session.getAttribute("username");
        // the service fetches all the quiz's id and title and returns it
        return ResponseEntity.ok(quizService.getAllQuiz(userEmail));
    }

    @PostMapping("/")
    public ResponseEntity<?> createQuiz(
        @RequestBody NewQuiz newQuiz,
        HttpSession session
    ) {
        // get the user's email from session
        String userEmail = (String) session.getAttribute("username");
        // the service needs to create the quiz in the quiz table, as well as put them in 
        // the invites table
        quizService.createQuiz(userEmail, newQuiz);
        return ResponseEntity.ok(null);
    }
}
