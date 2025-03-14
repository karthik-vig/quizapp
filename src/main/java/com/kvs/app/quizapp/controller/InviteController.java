package com.kvs.app.quizapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kvs.app.quizapp.dto.CompletedQuizzes;
import com.kvs.app.quizapp.dto.QuizInvite;
import com.kvs.app.quizapp.dto.QuizSubmissionAnswer;
import com.kvs.app.quizapp.dto.QuizTemplate;
import com.kvs.app.quizapp.dto.QuestionTemplate.Question;
import com.kvs.app.quizapp.service.InvitesSerivce;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/invites")
public class InviteController {

    private InvitesSerivce invitesSerivce;

    @Autowired
    public InviteController(
        InvitesSerivce invitesSerivce
    ) {
        this.invitesSerivce = invitesSerivce;
    }

    // the invite api section
    @GetMapping("/active") 
    public ResponseEntity<?> getInvites(HttpSession session) {
        // get the user's email
        String userEmail = (String) session.getAttribute("username");
        List<QuizInvite> quizInvites = invitesSerivce.getActiveInvites(userEmail);
        return ResponseEntity.ok(quizInvites);
    }

    @GetMapping("/active/{inviteId}")
    public ResponseEntity<?> getInviteDetails(
        @PathVariable String inviteId, 
        HttpSession session
    ) {
        // get the user's email using the session info.
        // get the inviteid of the active invite from the url path
        // get the dto for the quiz (service needs to get the string json from database and parse it into dto)
        // return the dto
        // Mock up
        String userEmail = (String) session.getAttribute("username");
        QuizTemplate<Question> inviteeQuizTemplate = this.invitesSerivce.getInviteDetails(inviteId, userEmail);
        if (inviteeQuizTemplate == null) {
            return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(inviteeQuizTemplate);
    }

    @PostMapping("/active/{inviteId}")
    public ResponseEntity<?> getInviteDetails(
        @PathVariable(name = "inviteId") String inviteId, 
        @RequestBody QuizSubmissionAnswer quizSubmissionAnswer, 
        HttpSession session
    ) {
        // Get the user's email from session info.
        // Get the invite id
        // Get the request body as dto
        // give the dto to service (queries the userid from the users table using the email from the session info., then it converts the dto to string json and stores it in the submissions table)
        // return a sucess message and status code
        String userEmail = (String) session.getAttribute("username");
        String status = this.invitesSerivce.submiteQuizAnswers(
                                                                userEmail, 
                                                                inviteId, 
                                                                quizSubmissionAnswer
                                                            );
        return ResponseEntity.ok(status);
    }

    @GetMapping("/completed")
    public ResponseEntity<?> getCompletedQuizzes(
        HttpSession session
    ) {
        // get the user email id
        String userEmail = (String) session.getAttribute("username");
        // use the service to get the list of submissions from the submissions table
        // TODO: need to implement pagination
        List<CompletedQuizzes> completedQuizzes = this.invitesSerivce.getCompletedQuizzes(
            userEmail
        );
        return ResponseEntity.ok(completedQuizzes);
    }
    
}
