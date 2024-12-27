package com.kvs.app.quizapp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "contacts")
public class ContactsEntity {
    @Id
    @Column( name = "id")
    private String id;

    @Column( name = "userid")
    private String userid;

    @Column( name = "relateduserid")
    private String relateduserid;

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

    public String getRelateduserid() {
        return relateduserid;
    }

    public void setRelateduserid(String relateduserid) {
        this.relateduserid = relateduserid;
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
