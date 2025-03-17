package com.kvs.app.quizapp.service;

import java.util.Vector;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kvs.app.quizapp.dto.NewQuiz;
import com.kvs.app.quizapp.dto.QuizDetail;
import com.kvs.app.quizapp.dto.QuizShortform;
import com.kvs.app.quizapp.entity.InvitesEntity;
import com.kvs.app.quizapp.entity.QuizTemplateEntity;
import com.kvs.app.quizapp.entity.QuizzesEntity;
import com.kvs.app.quizapp.entity.UsersEntity;
import com.kvs.app.quizapp.mapper.QuizRowAndQuizShortformMapper;
import com.kvs.app.quizapp.mapper.QuizRowToQuizDetailMapper;
import com.kvs.app.quizapp.repository.InvitesRepository;
import com.kvs.app.quizapp.repository.QuizTemplateRepository;
import com.kvs.app.quizapp.repository.QuizzesRepository;
import com.kvs.app.quizapp.repository.UsersRepository;

@Service
public class QuizService {
    
    private UsersRepository usersRepository;
    private QuizzesRepository quizzesRepository;
    private QuizTemplateRepository quizTemplateRepository;
    private InvitesRepository invitesRepository;

    @Autowired
    public QuizService(
        UsersRepository usersRepository,
        QuizzesRepository quizzesRepository,
        QuizTemplateRepository quizTemplateRepository,
        InvitesRepository invitesRepository
    ) {
        this.usersRepository = usersRepository;
        this.quizzesRepository = quizzesRepository;
        this.quizTemplateRepository = quizTemplateRepository;
        this.invitesRepository = invitesRepository;
    }

    public Map<String, Object> getAllQuiz(String userEmail) {
        HashMap<String, Object> response = new HashMap<>();
        List<QuizShortform> quizShortforms = new Vector<>();
        UsersEntity usersRow = usersRepository.findByEmail(userEmail);
        if (usersRow == null) {
            response.put("status", "Error");
            response.put("message", "Could not fetch the user details");
            response.put("statusCode", HttpStatus.UNAUTHORIZED);
            return response;
        }
        List<QuizzesEntity> quizzesEntities = quizzesRepository.findByUserId(usersRow.getId());
        for (QuizzesEntity quizzesEntity: quizzesEntities) {
            quizShortforms.add(QuizRowAndQuizShortformMapper.toDTO(quizzesEntity));
        }
        response.put("status", "Success");
        response.put("data", quizShortforms);
        response.put("statusCode", HttpStatus.OK);
        return response;
    }

    public Map<String, Object> createQuiz(
        String userEmail, 
        NewQuiz newQuiz
    ) {
        HashMap<String, Object> response = new HashMap<>();
        UsersEntity usersRow = usersRepository.findByEmail(userEmail);
        if (usersRow == null) {
            response.put("status", "Error");
            response.put("message", "Could not fetch the user details");
            response.put("statusCode", HttpStatus.UNAUTHORIZED);
            return response;
        }
        // get the quiz data from the quiz template table
        QuizTemplateEntity quizTemplateEntity = quizTemplateRepository.findByQuizTemplateId(usersRow.getId(), newQuiz.getQuizTemplateId());
        if (quizTemplateEntity == null) {
            response.put("status", "Error");
            response.put("message", "Could not fetch the quiz template");
            response.put("statusCode", HttpStatus.NOT_FOUND);
            return response;
        }
        // put the quiz in the quizzes table
        QuizzesEntity quizzesEntity = new QuizzesEntity();
        String uuid = UUID.randomUUID().toString();
        quizzesEntity.setId(uuid);
        quizzesEntity.setQuiztitle(newQuiz.getQuizTitle());
        quizzesEntity.setUserid(usersRow.getId());
        quizzesEntity.setQuizdata(quizTemplateEntity.getQuiztemplate());
        quizzesRepository.save(quizzesEntity);
        // put entry in the invites table too
        // but first get the userid from the emails
        for (String email: newQuiz.getEmails()) {
            UsersEntity contactUsersRow = usersRepository.findByEmail(email);
            InvitesEntity invitesEntity = new InvitesEntity();
            String invitesUUID = UUID.randomUUID().toString();
            invitesEntity.setId(invitesUUID);
            invitesEntity.setInvitestatus(true);
            invitesEntity.setQuizid(uuid);
            invitesEntity.setUserid(contactUsersRow.getId());
            invitesRepository.save(invitesEntity);
        }
        response.put("status", "Success");
        response.put("message", "Created the quiz");
        response.put("statusCode", HttpStatus.OK);
        return response;
    }

    public Map<String, Object> getQuizDetail(
        String userEmail,
        String quizId
    ) {
        HashMap<String, Object> response = new HashMap<>();
        UsersEntity usersRow = usersRepository.findByEmail(userEmail);
        if (usersRow == null) {
            response.put("status", "Error");
            response.put("message", "Could not fetch the user details");
            response.put("statusCode", HttpStatus.UNAUTHORIZED);
            return response;
        }
        // get the quiz detail
        QuizzesEntity quizRow = quizzesRepository.getQuizRowByQuizIdAndUserId(usersRow.getId(), quizId);
        if (quizRow == null) {
            response.put("status", "Error");
            response.put("message", "Could not fetch the quiz details");
            response.put("statusCode", HttpStatus.NOT_FOUND);
            return response;
        }
        // map the quiz details into an format appropriate for the front-end
        QuizDetail quizDetail = QuizRowToQuizDetailMapper.toDto(quizRow);
        response.put("status", "Success");
        response.put("data", quizDetail); // change the it actual data
        response.put("statusCode", HttpStatus.OK);
        return response;
        
    }

    public Map<String, Object> deleteQuiz(
        String userEmail,
        String quizId
    ) {
        HashMap<String, Object> response = new HashMap<>();
        UsersEntity usersRow = usersRepository.findByEmail(userEmail);
        if (usersRow == null) {
            response.put("status", "Error");
            response.put("message", "Could not fetch the user details");
            response.put("statusCode", HttpStatus.UNAUTHORIZED);
            return response;
        }
        // delete the quiz
        quizzesRepository.deleteRowByUserIdAndQuizId(usersRow.getId(), quizId);
        response.put("status", "Success");
        response.put("message", "Permanently remove the quiz");
        response.put("statusCode", HttpStatus.OK);
        return response;
    }
}
