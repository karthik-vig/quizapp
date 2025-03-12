package com.kvs.app.quizapp.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("/api")
public class QuizAppController {
    
    @GetMapping("/test")
    public ResponseEntity<String> testFn(HttpSession session) {
        return ResponseEntity.ok((String) session.getAttribute("username"));
    }
}
