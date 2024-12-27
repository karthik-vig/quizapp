package com.kvs.app.quizapp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table( name = "submissions" )
public class SubmissionsEntity {
    @Id
    @Column( name = "id")
    private String id;

    @Column( name = "userid")
    private String userid;

    @Column( name = "quizid")
    private String quizid;

    @Column( name = "quizanswer")
    private String quizanswer;

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

    public String getQuizanswer() {
        return quizanswer;
    }

    public void setQuizanswer(String quizanswer) {
        this.quizanswer = quizanswer;
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
