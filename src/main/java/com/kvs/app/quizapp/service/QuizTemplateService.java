package com.kvs.app.quizapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kvs.app.quizapp.dto.QuizTemplate;
import com.kvs.app.quizapp.dto.QuestionTemplate.QuestionAndAnswer;
import com.kvs.app.quizapp.dto.QuizTemplateListDTO;
import com.kvs.app.quizapp.entity.QuizTemplateEntity;
import com.kvs.app.quizapp.entity.UsersEntity;
import com.kvs.app.quizapp.repository.UsersRepository;
import com.kvs.app.quizapp.repository.QuizTemplateRepository;
import com.kvs.app.quizapp.mapper.QuizTemplateListMapper;
import com.kvs.app.quizapp.mapper.QuizTemplateMapper;

import java.util.List;
import java.util.Vector;
import java.util.UUID;

import com.google.gson.Gson;

@Service
public class QuizTemplateService {

    private Gson gson = new Gson();
    private UsersRepository usersRepository;
    private QuizTemplateRepository quizTemplateRepository;

    @Autowired
    public QuizTemplateService(
        UsersRepository usersRepository,
        QuizTemplateRepository quizTemplateRepository
    ) {
        this.quizTemplateRepository = quizTemplateRepository;
        this.usersRepository = usersRepository;
    }

    public List<?> getQuizTemplates(String userEmail) {
        List<String> emptyList = new Vector<String>();
        // get the users table row using email to get the userid
        UsersEntity usersRow = this.usersRepository.findByEmail(userEmail);
        // get the list of quiz templates from the quiz template table for the given user id
        if (usersRow == null) return emptyList;
        List<QuizTemplateEntity> quizTemplateRows = this.quizTemplateRepository.findByUserId(usersRow.getId());
        // get the quiz template id and quiz template title and get dto using the mapper
        if (quizTemplateRows.isEmpty()) return emptyList;
        List<QuizTemplateListDTO> quizTemplateListDTOs = QuizTemplateListMapper.toDTO(quizTemplateRows);
        // return the list of a dto that contains the quiz template id and quiz template title
        return quizTemplateListDTOs;
    }

    public String createNewQuizTemplate(QuizTemplate<QuestionAndAnswer> quizTemplate, String userEmail) {
        // get the users table row using email to get the userid
        UsersEntity usersRow = this.usersRepository.findByEmail(userEmail);
        // parse the dto into a string
        if (usersRow == null) return "Fail";
        String quizTemplateJson = this.gson.toJson(quizTemplate);
        System.out.println("The quiz template json: " + quizTemplateJson);
        // map the dto to the entity
        QuizTemplateEntity quizTemplateEntity = QuizTemplateMapper.toEntity(quizTemplate);
        // generate random uuidv4 and insert the enitty into the table
        String uuid = UUID.randomUUID().toString();
        quizTemplateEntity.setId(uuid);
        quizTemplateEntity.setUserid(usersRow.getId());
        quizTemplateEntity.setQuiztemplate(quizTemplateJson);
        this.quizTemplateRepository.save(quizTemplateEntity);
        return "Success";
    }

}
