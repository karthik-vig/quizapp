package com.kvs.app.quizapp.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kvs.app.quizapp.dto.CompletedQuizzes;
import com.kvs.app.quizapp.entity.QuizzesEntity;
import com.kvs.app.quizapp.entity.SubmissionsEntity;
import com.kvs.app.quizapp.entity.UsersEntity;
import com.kvs.app.quizapp.repository.SubmissionsRepository;
import com.kvs.app.quizapp.repository.UsersRepository;

import jakarta.persistence.Tuple;


@Service
public class SubmissionService {
    
    private UsersRepository usersRepository;
    private SubmissionsRepository submissionsRepository;

    @Autowired
    public SubmissionService(
        UsersRepository usersRepository,
        SubmissionsRepository submissionsRepository
    ) {
        this.usersRepository = usersRepository;
        this.submissionsRepository = submissionsRepository;
    }

    public Map<String, Object> getAllSubmissions(
        String userEmail
    ) {
        HashMap<String, Object> response = new HashMap<>();
        List<CompletedQuizzes> completedQuizzesList = new Vector<CompletedQuizzes>();
        // get the users table row using email to get the userid
        UsersEntity usersRow = this.usersRepository.findByEmail(userEmail);
        if (usersRow == null) {
            response.put("status", "Error");
            response.put("message", "Could not fetch the user details");
            response.put("statusCode", HttpStatus.UNAUTHORIZED);
            return response;
        };
        // join the submissions table with the quizzes table on quizid where the user id in the quizzes table 
        // is that of the current logged in user.
        List<Tuple> quizDataAndAnswers = submissionsRepository.getSubmissionsForQuizOwner(usersRow.getId());
        for (Tuple quizDataAndAnswer : quizDataAndAnswers) {
            CompletedQuizzes completedQuizzes = new CompletedQuizzes();
            SubmissionsEntity submissionsEntity = (SubmissionsEntity) quizDataAndAnswer.get(0);
            QuizzesEntity quizzesEntity = (QuizzesEntity) quizDataAndAnswer.get(1);
            completedQuizzes.setId(submissionsEntity.getId());
            completedQuizzes.setQuizTitle(quizzesEntity.getQuiztitle());
            completedQuizzesList.add(completedQuizzes);
        }
        response.put("status", "Success");
        response.put("statusCode", HttpStatus.OK);
        response.put("data", completedQuizzesList);
        return response;
    }

    public Map<String, Object> getAllSubmissionsOnQuizId(
        String userEmail,
        String quizId
    ) {
        HashMap<String, Object> response = new HashMap<>();
        List<CompletedQuizzes> completedQuizzesList = new Vector<CompletedQuizzes>();
        // get the users table row using email to get the userid
        UsersEntity usersRow = this.usersRepository.findByEmail(userEmail);
        if (usersRow == null) {
            response.put("status", "Error");
            response.put("message", "Could not fetch the user details");
            response.put("statusCode", HttpStatus.UNAUTHORIZED);
            return response;
        };
        // join the submissions table with the quizzes table on quizid where the user id in the quizzes table 
        // is that of the current logged in user and quiz id is also the one passed in by the user in the url
        List<Tuple> quizDataAndAnswers = submissionsRepository.getSubmissionsForQuizOwnerOnQuizId(usersRow.getId(), quizId);
        for (Tuple quizDataAndAnswer : quizDataAndAnswers) {
            CompletedQuizzes completedQuizzes = new CompletedQuizzes();
            SubmissionsEntity submissionsEntity = (SubmissionsEntity) quizDataAndAnswer.get(0);
            QuizzesEntity quizzesEntity = (QuizzesEntity) quizDataAndAnswer.get(1);
            completedQuizzes.setId(submissionsEntity.getId());
            completedQuizzes.setQuizTitle(quizzesEntity.getQuiztitle());
            completedQuizzesList.add(completedQuizzes);
        }
        response.put("status", "Success");
        response.put("statusCode", HttpStatus.OK);
        response.put("data", completedQuizzesList);
        return response;
    }

    

}
