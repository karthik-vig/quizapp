package com.kvs.app.quizapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

import com.kvs.app.quizapp.dto.Contacts;
import com.kvs.app.quizapp.dto.Login;
import com.kvs.app.quizapp.dto.NewContact;
import com.kvs.app.quizapp.dto.NewQuiz;
import com.kvs.app.quizapp.dto.QuizInvite;
import com.kvs.app.quizapp.dto.QuizSubmissionAnswer;
import com.kvs.app.quizapp.dto.QuizTemplate;
import com.kvs.app.quizapp.dto.QuestionTemplate.Question;
import com.kvs.app.quizapp.dto.QuestionTemplate.QuestionAndAnswer;
import com.kvs.app.quizapp.helpers.EmailSender;
import com.kvs.app.quizapp.service.ContactsService;
import com.kvs.app.quizapp.service.HandleUsers;
import com.kvs.app.quizapp.service.InvitesSerivce;
import com.kvs.app.quizapp.service.QuizService;
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
    private ContactsService contactsService;
    private QuizService quizService;

    @Autowired
    public QuizAppController(
        InvitesSerivce invitesSerivce,
        QuizTemplateService quizTemplateService,
        HandleUsers handleUsers,
        ContactsService contactsService,
        QuizService quizService
        ) {
        this.invitesSerivce = invitesSerivce;
        this.quizTemplateService = quizTemplateService;
        this.handleUsers = handleUsers;
        this.contactsService = contactsService;
        this.quizService = quizService;
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

    @GetMapping("/quiz-template/{quizTemplateId}")
    public ResponseEntity<?> getQuizTemplate(
        @PathVariable(name = "quizTemplateId") String quizTemplateId,
        HttpSession session
    ) {
        // get the user's email id
        String userEmail = (String) session.getAttribute("username");
        // service returns the dto of the request json of the respective quizTemplateId
        return ResponseEntity.ok(this.quizTemplateService.getQuizTemplate(userEmail, quizTemplateId));
    }

    @PutMapping("/quiz-template/{quizTemplateId}")
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

    @DeleteMapping("/quiz-template/{quizTemplateId}")
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

    // create quiz section
    @GetMapping("/quiz")
    public ResponseEntity<?> getAllQuiz(
        HttpSession session
    ) {
        // get the user's email from session
        String userEmail = (String) session.getAttribute("username");
        // the service fetches all the quiz's id and title and returns it
        return ResponseEntity.ok(quizService.getAllQuiz(userEmail));
    }

    @PostMapping("/quiz")
    public ResponseEntity<?> createQuiz(
        @RequestBody NewQuiz newQuiz,
        HttpSession session
    ) {
        // get the user's email from session
        String userEmail = (String) session.getAttribute("username");
        // the service needs to create the quiz in the quiz table, as well as put them in 
        // the invites table
        return ResponseEntity.ok(null);
    }

    // contacts api section
    @GetMapping("/contacts")
    public ResponseEntity<?> getContacts(HttpSession session) {
        // get the user's email
        String userEmail = (String) session.getAttribute("username");
        // service to fetch all the contacts the user has
        // the service uses uses the useremail to get the userid
        // then uses the userid to fetch all the contacts and return it
        List<Contacts> contacts = this.contactsService.getAllContacts(userEmail);
        return ResponseEntity.ok(contacts);
    }

    @PostMapping("/contacts")
    public ResponseEntity<?> createNewContact(
        @RequestBody NewContact newContact,
        HttpSession session
    ) {
        // get the user's email
        String userEmail = (String) session.getAttribute("username");
        // the service creates a new contacts entry in the contacts table for the user
        // return success or fail status
        String status = this.contactsService.createNewContact(userEmail, newContact);
        if (status == "Fail") {
            return new ResponseEntity<>("Fail", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Success");
    }

    @PutMapping("/contacts/{contactId}")
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

    @DeleteMapping("/contacts/{contactId}")
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
