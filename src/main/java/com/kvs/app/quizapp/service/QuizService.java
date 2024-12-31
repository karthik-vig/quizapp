package com.kvs.app.quizapp.service;

import java.util.Vector;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kvs.app.quizapp.dto.NewQuiz;
import com.kvs.app.quizapp.dto.QuizShortform;
import com.kvs.app.quizapp.entity.InvitesEntity;
import com.kvs.app.quizapp.entity.QuizTemplateEntity;
import com.kvs.app.quizapp.entity.QuizzesEntity;
import com.kvs.app.quizapp.entity.UsersEntity;
import com.kvs.app.quizapp.mapper.QuizRowAndQuizShortformMapper;
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

    public List<QuizShortform> getAllQuiz(String userEmail) {
        List<QuizShortform> quizShortforms = new Vector<>();
        UsersEntity usersRow = usersRepository.findByEmail(userEmail);
        if (usersRow == null) return quizShortforms;
        List<QuizzesEntity> quizzesEntities = quizzesRepository.findByUserId(usersRow.getId());
        for (QuizzesEntity quizzesEntity: quizzesEntities) {
            quizShortforms.add(QuizRowAndQuizShortformMapper.toDTO(quizzesEntity));
        }
        return quizShortforms;
    }

    public String createQuiz(
        String userEmail, 
        NewQuiz newQuiz
    ) {
        UsersEntity usersRow = usersRepository.findByEmail(userEmail);
        if (usersRow == null) return "Fail";
        // get the quiz data from the quiz template table
        QuizTemplateEntity quizTemplateEntity = quizTemplateRepository.findByQuizTemplateId(newQuiz.getQuizTemplateId());
        // put the quiz in the quizzes table
        QuizzesEntity quizzesEntity = new QuizzesEntity();
        String uuid = UUID.randomUUID().toString();
        quizzesEntity.setId(uuid);
        quizzesEntity.setQuiztitle(newQuiz.getQuizTitle());
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
        return "Success";
    }
}
