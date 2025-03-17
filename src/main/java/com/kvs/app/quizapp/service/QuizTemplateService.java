package com.kvs.app.quizapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.UUID;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

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

    public Map<String, Object> getQuizTemplates(String userEmail) {
        HashMap<String, Object> response = new HashMap<>();
        List<String> emptyList = new Vector<String>();
        // get the users table row using email to get the userid
        UsersEntity usersRow = this.usersRepository.findByEmail(userEmail);
        // get the list of quiz templates from the quiz template table for the given user id
        if (usersRow == null) {
            response.put("status", "Error");
            response.put("message", "Could not fetch the user details");
            response.put("statusCode", HttpStatus.UNAUTHORIZED);
            return response;
        };
        List<QuizTemplateEntity> quizTemplateRows = this.quizTemplateRepository.findByUserId(usersRow.getId());
        // get the quiz template id and quiz template title and get dto using the mapper
        if (quizTemplateRows.isEmpty()) {
            response.put("status", "Success");
            response.put("data", emptyList);
            response.put("statusCode", HttpStatus.OK);
            return response;
        }
        List<QuizTemplateListDTO> quizTemplateListDTOs = QuizTemplateListMapper.toDTO(quizTemplateRows);
        // return the list of a dto that contains the quiz template id and quiz template title
        response.put("status", "Success");
        response.put("data", quizTemplateListDTOs);
        response.put("statusCode", HttpStatus.OK);
        return response;
    }

    public Map<String, Object> createNewQuizTemplate(
        QuizTemplate<QuestionAndAnswer> quizTemplate, 
        String userEmail
        ) {
        HashMap<String, Object> response = new HashMap<>();
        // get the users table row using email to get the userid
        UsersEntity usersRow = this.usersRepository.findByEmail(userEmail);
        // parse the dto into a string
        if (usersRow == null) {
            response.put("status", "Error");
            response.put("message", "Could not fetch the user details");
            response.put("statusCode", HttpStatus.UNAUTHORIZED);
            return response;
        };
        String quizTemplateJson = this.gson.toJson(quizTemplate);
        // System.out.println("The quiz template json: " + quizTemplateJson);
        // map the dto to the entity
        QuizTemplateEntity quizTemplateEntity = QuizTemplateMapper.toEntity(quizTemplate);
        // generate random uuidv4 and insert the enitty into the table
        String uuid = UUID.randomUUID().toString();
        quizTemplateEntity.setId(uuid);
        quizTemplateEntity.setUserid(usersRow.getId());
        quizTemplateEntity.setQuiztemplate(quizTemplateJson);
        this.quizTemplateRepository.save(quizTemplateEntity);
        response.put("status", "Success");
        response.put("message", "Creaeted a quiz template with given infromation");
        response.put("statusCode", HttpStatus.OK);
        return response;
    }

    public Map<String, Object> getQuizTemplate(
        String userEmail, 
        String quizTemplateId
        ) {
        HashMap<String, Object> response = new HashMap<>();
        // get the users table row using email to get the userid
        UsersEntity usersRow = this.usersRepository.findByEmail(userEmail);
        if (usersRow == null) {
            response.put("status", "Error");
            response.put("message", "Could not fetch the user details");
            response.put("statusCode", HttpStatus.UNAUTHORIZED);
            return response;
        };
        // get the quiz json
        QuizTemplateEntity quizTemplateRow = this.quizTemplateRepository.findByQuizTemplateId(usersRow.getId(), quizTemplateId);
        if (quizTemplateRow == null) {
            response.put("status", "Error");
            response.put("message", "Could not fetch the quiz template");
            response.put("statusCode", HttpStatus.NOT_FOUND);
            return response;
        }
        // parse the json into dto
        Type type = new TypeToken<QuizTemplate<QuestionAndAnswer>>(){}.getType();
        QuizTemplate<QuestionAndAnswer> quizTemplate = this.gson.fromJson(quizTemplateRow.getQuiztemplate(), type);
        response.put("status", "Success");
        response.put("data", quizTemplate);
        response.put("statusCode", HttpStatus.OK);
        return response;
    }

    public Map<String, Object> modifyQuizTemplate(
        String userEmail,
        String quizTemplateId,
        QuizTemplate<QuestionAndAnswer> quizTemplate
    ) {
        HashMap<String, Object> response = new HashMap<>();
        // get the users table row using email to get the userid
        UsersEntity usersRow = this.usersRepository.findByEmail(userEmail);
        if (usersRow == null) {
            response.put("status", "Error");
            response.put("message", "Could not fetch the user details");
            response.put("statusCode", HttpStatus.UNAUTHORIZED);
            return response;
        };
        // convert quiz template into json
        String quizTemplateJson = this.gson.toJson(quizTemplate);
        // update the title
        int quizTitlesUpdated = this.quizTemplateRepository.updateQuizTemplateTitle(usersRow.getId(), quizTemplate.getTitle(), quizTemplateId);
        // update the quizTemplate json data
        int quizTemplateJsonUpdated = this.quizTemplateRepository.updateQuizTemplateJson(usersRow.getId(), quizTemplateJson, quizTemplateId);
        if (quizTitlesUpdated != 1 && quizTemplateJsonUpdated != 1) {
            response.put("status", "Error");
            response.put("message", "Could not complete the quiz template update operation");
            response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
        response.put("status", "Success");
        response.put("message", "Updated the select quiz template");
        response.put("statusCode", HttpStatus.OK);
        return response;
    }

    public Map<String, Object> deleteQuizTemplate(
        String userEmail,
        String quizTemplateId
    ) {
        HashMap<String, Object> response = new HashMap<>();
        // get the users table row using email to get the userid
        UsersEntity usersRow = this.usersRepository.findByEmail(userEmail);
        if (usersRow == null) {
            response.put("status", "Error");
            response.put("message", "Could not fetch the user details");
            response.put("statusCode", HttpStatus.UNAUTHORIZED);
            return response;
        };
        // delete the row associated with the id
        int rowsDeleted = this.quizTemplateRepository.deleteByQuizTemplateIdAndUserId(quizTemplateId, usersRow.getId());
        if (rowsDeleted != 1) {
            response.put("status", "Error");
            response.put("message", "Operation to delete row failed");
            response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
        response.put("status", "Success");
        response.put("message", "Deleted the quiz template");
        response.put("statusCode", HttpStatus.OK);
        return response;
    }

}
