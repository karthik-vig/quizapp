package com.kvs.app.quizapp.service;

import com.kvs.app.quizapp.repository.InvitesRepository;
import com.kvs.app.quizapp.repository.QuizzesRepository;
import com.kvs.app.quizapp.repository.SubmissionsRepository;
import com.kvs.app.quizapp.repository.UsersRepository;

import jakarta.persistence.Tuple;

import com.kvs.app.quizapp.entity.InvitesEntity;
import com.kvs.app.quizapp.entity.QuizzesEntity;
import com.kvs.app.quizapp.entity.SubmissionsEntity;
import com.kvs.app.quizapp.entity.UsersEntity;
import com.google.gson.Gson;
import com.kvs.app.quizapp.dto.CompletedQuizzes;
// import com.kvs.app.quizapp.mapper.ActiveInvitesMapper;
import com.kvs.app.quizapp.dto.QuizInvite;
import com.kvs.app.quizapp.dto.QuizSubmissionAnswer;
import com.kvs.app.quizapp.dto.QuizTemplate;
import com.kvs.app.quizapp.dto.QuestionTemplate.Question;
import com.kvs.app.quizapp.dto.QuestionTemplate.QuestionAndAnswer;
import com.google.gson.reflect.TypeToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
// import java.util.Optional;
import java.util.Vector;
import java.util.Map;

@Service
public class InvitesSerivce {

    private InvitesRepository invitesRepository;
    private QuizzesRepository quizzesRepository;
    private UsersRepository usersRepository;
    private SubmissionsRepository submissionsRepository;

    private Gson gson = new Gson();

    @Autowired
    public InvitesSerivce(
        InvitesRepository invitesRepository,
        QuizzesRepository quizzesRepository,
        UsersRepository usersRepository,
        SubmissionsRepository submissionsRepository
        ) {
        this.invitesRepository = invitesRepository;
        this.quizzesRepository = quizzesRepository;
        this.usersRepository = usersRepository;
        this.submissionsRepository = submissionsRepository;
    }

    public Map<String, Object> getActiveInvites(String userEmail) {
        HashMap<String, Object> response = new HashMap<>();
        Vector<QuizInvite> quizInvites = new Vector<>();
        // use the email to get the userid
        UsersEntity usersRow = this.usersRepository.findByEmail(userEmail);
        // user userid to get all quizids from invites table
        if (usersRow == null) {
            response.put("status", "Error");
            response.put("message", "Could not fetch the user details");
            response.put("statusCode", HttpStatus.UNAUTHORIZED);
            return response;
        }
        List<InvitesEntity> invitesRows = this.invitesRepository.findByUserid(usersRow.getId());
        // use the quizid to get the quiz titles from the quizzes table
        if (invitesRows.isEmpty()) {
            response.put("status", "Error");
            response.put("message", "Could not find invites for the user");
            response.put("statusCode", HttpStatus.NOT_FOUND);
            return response;
        }
        for (InvitesEntity invitesRow : invitesRows) {
           QuizzesEntity quizzesRow = this.quizzesRepository.findByQuizid(invitesRow.getQuizid());
           if (quizzesRow != null) {
                QuizInvite quizInvite = new QuizInvite();
                quizInvite.setId(invitesRow.getId());
                quizInvite.setQuizName(quizzesRow.getQuiztitle());
                quizInvites.add(quizInvite);
           }
        }
        response.put("status", "Success");
        response.put("data", quizInvites);
        response.put("statusCode", HttpStatus.OK);
        return response;
    }

    public Map<String, Object> getInviteDetails(
        String inviteId,
        String userEmail
    ) {
        HashMap<String, Object> response = new HashMap<>();
        QuizTemplate<Question> inviteeQuizTemplate = new QuizTemplate<Question>();
        Vector<Question> questions = new Vector<Question>();
        // get the user's id
        UsersEntity userRow = this.usersRepository.findByEmail(userEmail);
        if (userRow == null) {
            response.put("status", "Error");
            response.put("message", "Could not fetch the user details");
            response.put("statusCode", HttpStatus.UNAUTHORIZED);
            return response;
        }
        // convert the invite id into quiz id
        String quizId = this.invitesRepository.getQuizIdByInviteIdIfActive(userRow.getId(), inviteId);
        if (quizId == null) {
            response.put("status", "Error");
            response.put("message", "Could not get the quiz invite details");
            response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
        // get the json data of the quiz
        QuizzesEntity quizRow = this.quizzesRepository.findByQuizid(quizId);
        if (quizRow == null) {
            response.put("status", "Error");
            response.put("message", "Could not get the quiz details");
            response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
        Type type = new TypeToken<QuizTemplate<QuestionAndAnswer>>(){}.getType();
        QuizTemplate<QuestionAndAnswer> quizTemplate = gson.fromJson(quizRow.getQuizdata(), type);
        // setup the values for quiz template
        inviteeQuizTemplate.setTitle(quizTemplate.getTitle());
        for (QuestionAndAnswer questionAndAnswer : quizTemplate.getQuestions()) {
            Question question = new Question();
            question.setQuestionType(questionAndAnswer.getQuestionType());
            question.setQuestion(questionAndAnswer.getQuestion());
            question.setAnswerOptions(questionAndAnswer.getAnswerOptions());
            questions.add(question);
        }        
        inviteeQuizTemplate.setQuestions(questions);
        response.put("status", "Success");
        response.put("data", inviteeQuizTemplate);
        response.put("statusCode", HttpStatus.OK);
        return response;
    }

    public Map<String, Object> submiteQuizAnswers(
        String userEmail,
        String inviteId,
        QuizSubmissionAnswer quizSubmissionAnswer
    ) {
        HashMap<String, Object> response = new HashMap<>();
        // get the user's id
        UsersEntity userRow = this.usersRepository.findByEmail(userEmail);
        if (userRow == null) {
            response.put("status", "Error");
            response.put("message", "Could not fetch the user details");
            response.put("statusCode", HttpStatus.UNAUTHORIZED);
            return response;
        }
        // save the quiz answers in the submissions table
        // convert the invite id into quiz id
        String quizId = this.invitesRepository.getQuizIdByInviteId(inviteId, userRow.getId());
        if (quizId == null) {
            response.put("status", "Error");
            response.put("message", "Could not fetch the quiz from the invite");
            return response;
        }
        // create the entity
        SubmissionsEntity submissionsEntity = new SubmissionsEntity();
        submissionsEntity.setId(UUID.randomUUID().toString());
        submissionsEntity.setUserid(userRow.getId());
        submissionsEntity.setQuizid(quizId);
        submissionsEntity.setQuizanswers(this.gson.toJson(quizSubmissionAnswer));
        // check if quizId is unique for the given user id
        String quizIdExistingCheck = this.invitesRepository.getQuizIdIfUniqueForUser(userRow.getId(), quizId);
        // save in the table
        if (quizIdExistingCheck == null) {
            this.submissionsRepository.save(submissionsEntity);
            // change invite status to 0 to indicate that is not active anymore
            this.invitesRepository.setAsCompleted(inviteId, userRow.getId());
            response.put("status", "Success");
            response.put("message", "Recorded quiz answer");
            response.put("statusCode", HttpStatus.OK);
            return response;
        }
        response.put("status", "Error");
        response.put("message", "Already submitted");
        response.put("statusCode", HttpStatus.CONFLICT);
        return response;
    }

    public Map<String, Object> getCompletedQuizzes(
        String userEmail
    ) {
        HashMap<String, Object> response = new HashMap<>();
        List<CompletedQuizzes> completedQuizzes = new Vector<CompletedQuizzes>();
        UsersEntity userRow = this.usersRepository.findByEmail(userEmail);
        if (userRow == null) {
            response.put("status", "Error");
            response.put("message", "Could not fetch the user details");
            response.put("statusCode", HttpStatus.UNAUTHORIZED);
            return response;
        }
        // get the quiz id from the submissions
        List<Tuple> results = this.submissionsRepository.getQuizIdByUserId(userRow.getId());
        for (Tuple result : results) {
            CompletedQuizzes completedQuiz = new CompletedQuizzes();
            completedQuiz.setId((String) result.get(0));
            completedQuiz.setQuizTitle((String) result.get(1));
            completedQuizzes.add(completedQuiz);
        }
        // use the quiz id and get the quiz title from quizzes table
        response.put("status", "Success");
        response.put("statusCode", HttpStatus.OK);
        response.put("data", completedQuizzes);
        return response;
    }
}
