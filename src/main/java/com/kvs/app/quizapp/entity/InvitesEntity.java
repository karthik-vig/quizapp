package com.kvs.app.quizapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

@Entity
@Table( name = "invites" )
public class InvitesEntity {

    @Id
    @Column( name = "id")
    private String id;

    @Column( name = "userid")
    private String userid;

    @Column( name = "quizid")
    private String quizid;

    @Column( name = "invitestatus")
    private boolean invitestatus;

    @Column( name = "createdat")
    private LocalDateTime createdat;

    @Column( name = "updatedat")
    private LocalDateTime updatedat;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getQuizid() {
        return quizid;
    }

    public void setQuizid(String quizid) {
        this.quizid = quizid;
    }

    public boolean getInvitestatus() {
        return invitestatus;
    }

    public void setInvitestatus(boolean invitestatus) {
        this.invitestatus = invitestatus;
    }

    public LocalDateTime getCreatedat() {
        return createdat;
    }

    public void setCreatedat(LocalDateTime createdat) {
        this.createdat = createdat;
    }

    public LocalDateTime getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(LocalDateTime updatedat) {
        this.updatedat = updatedat;
    }

    
}
