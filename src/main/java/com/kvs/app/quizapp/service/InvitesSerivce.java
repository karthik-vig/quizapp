package com.kvs.app.quizapp.service;

import com.kvs.app.quizapp.repository.InvitesRepository;
import com.kvs.app.quizapp.repository.QuizzesRepository;
import com.kvs.app.quizapp.repository.UsersRepository;
import com.kvs.app.quizapp.entity.InvitesEntity;
import com.kvs.app.quizapp.entity.QuizzesEntity;
import com.kvs.app.quizapp.entity.UsersEntity;
// import com.kvs.app.quizapp.mapper.ActiveInvitesMapper;
import com.kvs.app.quizapp.dto.QuizInvite;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
// import java.util.Optional;
import java.util.Vector;

@Service
public class InvitesSerivce {

    private InvitesRepository invitesRepository;
    private QuizzesRepository quizzesRepository;
    private UsersRepository usersRepository;

    @Autowired
    public InvitesSerivce(
        InvitesRepository invitesRepository,
        QuizzesRepository quizzesRepository,
        UsersRepository usersRepository
        ) {
        this.invitesRepository = invitesRepository;
        this.quizzesRepository = quizzesRepository;
        this.usersRepository = usersRepository;
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
}
