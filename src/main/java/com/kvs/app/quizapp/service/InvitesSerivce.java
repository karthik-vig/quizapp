package com.kvs.app.quizapp.service;

import com.kvs.app.quizapp.repository.InvitesRepository;
import com.kvs.app.quizapp.repository.QuizzesRepository;
import com.kvs.app.quizapp.repository.SubmissionsRepository;
import com.kvs.app.quizapp.repository.UsersRepository;
import com.kvs.app.quizapp.entity.InvitesEntity;
import com.kvs.app.quizapp.entity.QuizzesEntity;
import com.kvs.app.quizapp.entity.SubmissionsEntity;
import com.kvs.app.quizapp.entity.UsersEntity;
import com.google.gson.Gson;
// import com.kvs.app.quizapp.mapper.ActiveInvitesMapper;
import com.kvs.app.quizapp.dto.QuizInvite;
import com.kvs.app.quizapp.dto.QuizSubmissionAnswer;
import com.kvs.app.quizapp.dto.QuizTemplate;
import com.kvs.app.quizapp.dto.QuestionTemplate.Question;
import com.kvs.app.quizapp.dto.QuestionTemplate.QuestionAndAnswer;
import com.google.gson.reflect.TypeToken;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;
// import java.util.Optional;
import java.util.Vector;

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

    public List<QuizInvite> getActiveInvites(String userEmail) {
        Vector<QuizInvite> quizInvites = new Vector<>();
        // use the email to get the userid
        UsersEntity usersRow = this.usersRepository.findByEmail(userEmail);
        // user userid to get all quizids from invites table
        if (usersRow == null) return quizInvites;
        List<InvitesEntity> invitesRows = this.invitesRepository.findByUserid(usersRow.getId());
        // use the quizid to get the quiz titles from the quizzes table
        if (invitesRows.isEmpty()) return quizInvites;
        for (InvitesEntity invitesRow : invitesRows) {
           QuizzesEntity quizzesRow = this.quizzesRepository.findByQuizid(invitesRow.getQuizid());
           if (quizzesRow != null) {
                QuizInvite quizInvite = new QuizInvite();
                quizInvite.setId(invitesRow.getId());
                quizInvite.setQuizName(quizzesRow.getQuiztitle());
                quizInvites.add(quizInvite);
           }
        }
        return quizInvites;
    }

    public QuizTemplate<Question> getInviteDetails(
        String inviteId,
        String userEmail
    ) {
        QuizTemplate<Question> inviteeQuizTemplate = new QuizTemplate<Question>();
        Vector<Question> questions = new Vector<Question>();
        // get the user's id
        UsersEntity userRow = this.usersRepository.findByEmail(userEmail);
        if (userRow == null) {
            return null;
        }
        // convert the invite id into quiz id
        String quizId = this.invitesRepository.getQuizIdByInviteId(inviteId);
        if (quizId == null) {
            return null;
        }
        // get the json data of the quiz
        QuizzesEntity quizRow = this.quizzesRepository.findByQuizid(quizId);
        if (quizRow == null) {
            return null;
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
        return inviteeQuizTemplate;
    }

    public String submiteQuizAnswers(
        String userEmail,
        String inviteId,
        QuizSubmissionAnswer quizSubmissionAnswer
    ) {
        // get the user's id
        UsersEntity userRow = this.usersRepository.findByEmail(userEmail);
        if (userRow == null) {
            return "Error - user does not Exist";
        }
        // save the quiz answers in the submissions table
        // convert the invite id into quiz id
        String quizId = this.invitesRepository.getQuizIdByInviteId(inviteId);
        if (quizId == null) {
            return "Error - could not fetch quiz id";
        }
        // create the entity
        SubmissionsEntity submissionsEntity = new SubmissionsEntity();
        submissionsEntity.setId(UUID.randomUUID().toString());
        submissionsEntity.setUserid(userRow.getId());
        submissionsEntity.setQuizid(quizId);
        submissionsEntity.setQuizanswer(this.gson.toJson(quizSubmissionAnswer));
        // save in the table
        this.submissionsRepository.save(submissionsEntity);
        return "temp";
    }
}
