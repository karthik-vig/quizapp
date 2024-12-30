package com.kvs.app.quizapp.mapper;

import com.kvs.app.quizapp.dto.QuizTemplateListDTO;
import com.kvs.app.quizapp.entity.QuizTemplateEntity;

import java.util.List;
import java.util.Vector;

// maps the quiz template table list of rows to 
// create a list of quiz template List dto that
// only contains the quiz template id and 
// quiz template title
public class QuizTemplateListMapper {
    public static List<QuizTemplateListDTO> toDTO(List<QuizTemplateEntity> quizTemplateRows) {
        List<QuizTemplateListDTO> quizTemplateListDTOs = new Vector<>();
        for (QuizTemplateEntity quizTemplateRow: quizTemplateRows) {
            QuizTemplateListDTO quizTemplateListDTO = new QuizTemplateListDTO();
            quizTemplateListDTO.setQuizTemplateId(quizTemplateRow.getId());
            quizTemplateListDTO.setQuizTemplateTitle(quizTemplateRow.getQuiztemplatetitle());
            quizTemplateListDTOs.add(quizTemplateListDTO);
        }
        return quizTemplateListDTOs;
    }
}
