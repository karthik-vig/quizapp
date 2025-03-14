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

import com.kvs.app.quizapp.dto.QuizTemplate;
import com.kvs.app.quizapp.dto.QuestionTemplate.QuestionAndAnswer;
import com.kvs.app.quizapp.service.QuizTemplateService;

import jakarta.servlet.http.HttpSession;

import java.util.Map;

@RestController
@RequestMapping("/api/quiz-template")
public class QuizTemplateController {

    private QuizTemplateService quizTemplateService;
    
    @Autowired
    public QuizTemplateController(
        QuizTemplateService quizTemplateService
    ) {
        this.quizTemplateService = quizTemplateService;
    }

    // the quiz template api section
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getQuizTemplates(HttpSession session) {
        HashMap<String, Object> response = new HashMap<>();
        // get the user's email
        String userEmail = (String) session.getAttribute("username");
        if (userEmail == null) {
            response.put("status", "error");
            response.put("message", "user is not logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        // service needs to query and get list of all quiz template id and name and return it as a dto
        // return the List<dto>
        response.put("status", "Success");
        response.put("data", quizTemplateService.getQuizTemplates(userEmail));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> createNewQuizTemplate(
        @RequestBody QuizTemplate<QuestionAndAnswer> quizTemplate,
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
        // System.out.println("the user's email when creating quiz template is: " + userEmail);
        String status = this.quizTemplateService.createNewQuizTemplate(quizTemplate, userEmail);
        if (status.equals("Fail")) {
            response.put("status", "Error");
            response.put("message", "Could not create the quiz template");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
        response.put("status", "Success");
        response.put("message", status);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{quizTemplateId}")
    public ResponseEntity<Map<String, Object>> getQuizTemplate(
        @PathVariable(name = "quizTemplateId") String quizTemplateId,
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
        // service returns the dto of the request json of the respective quizTemplateId
        response.put("status", "Success");
        response.put("data", this.quizTemplateService.getQuizTemplate(userEmail, quizTemplateId));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{quizTemplateId}")
    public ResponseEntity<Map<String, Object>> modifyQuizTemplate(
        @PathVariable(name = "quizTemplateId") String quizTemplateId,
        @RequestBody QuizTemplate<QuestionAndAnswer> quizTemplate,
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
        // use the quiz template service to replace the existing json data and quiz template title
        String status = this.quizTemplateService.modifyQuizTemplate(userEmail, quizTemplateId, quizTemplate);
        if (status.equals("Fail")) {
            response.put("status", "Error");
            response.put("message", "Could not modify the given quiz template");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response.put("status", "Success");
        response.put("message", "Modified the qiven quiz template");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{quizTemplateId}")
    public ResponseEntity<Map<String, Object>> deleteQuizTemplate(
        @PathVariable(name = "quizTemplateId") String quizTemplateId,
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
        // the quiz template service needs to delete the row for the respective id
        String status = this.quizTemplateService.deleteQuizTemplate(userEmail, quizTemplateId);
        if (status.equals("Fail")) {
            response.put("status", "Error");
            response.put("message", "Could not complete the delete operation");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        response.put("status", "Success");
        response.put("message", "Deleted the given quiz template");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
}
