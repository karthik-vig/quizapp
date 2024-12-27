package com.kvs.app.quizapp.mapper;

import com.kvs.app.quizapp.dto.QuizInvite;
import com.kvs.app.quizapp.entity.InvitesEntity;

public class ActiveInvitesMapper {
    public static QuizInvite toDTO(InvitesEntity invitesEntity) {
        QuizInvite quizInvite = new QuizInvite();
        quizInvite.setId(invitesEntity.getId());
        return quizInvite;
    }
}
