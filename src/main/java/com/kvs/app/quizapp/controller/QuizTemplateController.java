package com.kvs.app.quizapp.controller;

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
    public ResponseEntity<?> getQuizTemplates(HttpSession session) {
        // get the email of the user using the session info.
        String userEmail = (String) session.getAttribute("username");
        // service needs to query and get list of all quiz template id and name and return it as a dto
        // return the List<dto>
        return ResponseEntity.ok(quizTemplateService.getQuizTemplates(userEmail));
    }

    @PostMapping("/")
    public ResponseEntity<?> createNewQuizTemplate(
        @RequestBody QuizTemplate<QuestionAndAnswer> quizTemplate,
        HttpSession session
    ) {
        String userEmail = (String) session.getAttribute("username");
        System.out.println("the user's email when creating quiz template is: " + userEmail);
        String status = this.quizTemplateService.createNewQuizTemplate(quizTemplate, userEmail);
        if (status.equals("Fail")) {
            return new ResponseEntity<>(status, HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(status);
    }

    @GetMapping("/{quizTemplateId}")
    public ResponseEntity<?> getQuizTemplate(
        @PathVariable(name = "quizTemplateId") String quizTemplateId,
        HttpSession session
    ) {
        // get the user's email id
        String userEmail = (String) session.getAttribute("username");
        // service returns the dto of the request json of the respective quizTemplateId
        return ResponseEntity.ok(this.quizTemplateService.getQuizTemplate(userEmail, quizTemplateId));
    }

    @PutMapping("/{quizTemplateId}")
    public ResponseEntity<?> modifyQuizTemplate(
        @PathVariable(name = "quizTemplateId") String quizTemplateId,
        @RequestBody QuizTemplate<QuestionAndAnswer> quizTemplate,
        HttpSession session
    ) {
        // get the user's email from session
        String userEmail = (String) session.getAttribute("username");
        // use the quiz template service to replace the existing json data and quiz template title
        String status = this.quizTemplateService.modifyQuizTemplate(userEmail, quizTemplateId, quizTemplate);
        if (status.equals("Fail")) {
            return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(status);
    }

    @DeleteMapping("/{quizTemplateId}")
    public ResponseEntity<?> deleteQuizTemplate(
        @PathVariable(name = "quizTemplateId") String quizTemplateId,
        HttpSession session
    ) {
        // get the user's email from session
        String userEmail = (String) session.getAttribute("username");
        // the quiz template service needs to delete the row for the respective id
        String status = this.quizTemplateService.deleteQuizTemplate(userEmail, quizTemplateId);
        if (status.equals("Fail")) {
            return new ResponseEntity<>("Fail", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Success");
    }
    
}
