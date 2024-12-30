package com.kvs.app.quizapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
// import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// import org.springframework.web.bind.annotation.ResponseStatus;
// import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Vector;

import javax.validation.Valid;

import java.util.Random;
import java.util.List;

import com.kvs.app.quizapp.dto.Login;
import com.kvs.app.quizapp.dto.QuizInvite;
import com.kvs.app.quizapp.dto.QuizSubmissionAnswer;
import com.kvs.app.quizapp.dto.QuizTemplate;
import com.kvs.app.quizapp.dto.QuestionTemplate.Question;
import com.kvs.app.quizapp.dto.QuestionTemplate.QuestionAndAnswer;
import com.kvs.app.quizapp.helpers.EmailSender;
import com.kvs.app.quizapp.service.HandleUsers;
import com.kvs.app.quizapp.service.InvitesSerivce;
// import com.kvs.app.quizapp.dto.QuestionTemplate.QuestionAndAnswer;
import com.kvs.app.quizapp.service.QuizTemplateService;


@RestController
@RequestMapping("/api")
public class QuizAppController {
    
    // login api section
    // need to clear the email key after some fixed time?
    private static volatile HashMap<String, Integer> emailOtp = new HashMap<>();

    private InvitesSerivce invitesSerivce;
    private QuizTemplateService quizTemplateService;
    private HandleUsers handleUsers;

    @Autowired
    public QuizAppController(
        InvitesSerivce invitesSerivce,
        QuizTemplateService quizTemplateService,
        HandleUsers handleUsers
        ) {
        this.invitesSerivce = invitesSerivce;
        this.quizTemplateService = quizTemplateService;
        this.handleUsers = handleUsers;
    }

    @PostMapping("/login/request")
    public ResponseEntity<?> loginRequest(@Valid @RequestBody Login login) {
        // generate otp
        Random randomNumberGenerator = new Random();
        int otp = randomNumberGenerator.nextInt(100000, 999999);
        // send otp to email
        String result = EmailSender.sendOTP(otp, login.getEmail());
        if (result.equals("Success")) {
            emailOtp.put(login.getEmail(), otp);
        }
        // return sucess or failure to frontend
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/login/verification")
    public ResponseEntity<?> startLogin(
        @Valid @RequestBody Login login, 
        HttpSession session
        ) {
        String email = login.getEmail();
        Integer userOtp = login.getOtp();
        Integer serverOtp = emailOtp.get(email);
        if (serverOtp != null && 
            userOtp != null && 
            userOtp.equals(serverOtp)) {
            session.setAttribute("username", email);
            emailOtp.remove(email);
            this.handleUsers.createUserIfNotExist(email);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }
        return new ResponseEntity<>("Authentication Failed", HttpStatus.UNAUTHORIZED);
    }

    // the quiz template api section
    @GetMapping("/quiz-template")
    public ResponseEntity<?> getQuizTemplates(HttpSession session) {
        // get the email of the user using the session info.
        String userEmail = (String) session.getAttribute("username");
        // service needs to query and get list of all quiz template id and name and return it as a dto
        // return the List<dto>
        return ResponseEntity.ok(quizTemplateService.getQuizTemplates(userEmail));
    }

    @PostMapping("/quiz-template")
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

    // the invite api section
    @GetMapping("/invites/active") 
    public ResponseEntity<?> getInvites(HttpSession session) {
        // get the user's email
        String userEmail = (String) session.getAttribute("username");
        List<QuizInvite> quizInvites = invitesSerivce.getActiveInvites(userEmail);
        return ResponseEntity.ok(quizInvites);
    } 

    @GetMapping("/invites/active/{inviteId}")
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
        QuizTemplate<Question> inviteeQuizTemplate = new QuizTemplate<Question>();
        Question question = new Question();
        question.setQuestionType("radio");
        question.setQuestion("what is your age?");
        Vector<String> answers = new Vector<>();
        answers.add("21");
        answers.add("22");
        answers.add("45");
        question.setAnswerOptions(answers);
        Vector<Question> questions = new Vector<Question>();
        questions.add(question);
        inviteeQuizTemplate.setTitle("First test quiz: " + inviteId + " " + userEmail);
        inviteeQuizTemplate.setQuestions(questions);
        return ResponseEntity.ok(inviteeQuizTemplate);
    }

    @PostMapping("/invites/active/{inviteId}")
    public ResponseEntity<?> getInviteDetails(
        @PathVariable String inviteId, 
        @RequestBody QuizSubmissionAnswer quizSubmissionAnswer, 
        HttpSession session) {
        // Get the user's email from session info.
        // Get the invite id
        // Get the request body as dto
        // give the dto to service (queries the userid from the users table using the email from the session info., then it converts the dto to string json and stores it in the submissions table)
        // return a sucess message and status code
        String userEmail = (String) session.getAttribute("username");
        return ResponseEntity.ok(quizSubmissionAnswer);
    }

    @GetMapping("/test")
    public ResponseEntity<String> testFn(HttpSession session) {
        return ResponseEntity.ok((String) session.getAttribute("username"));
    }
}
