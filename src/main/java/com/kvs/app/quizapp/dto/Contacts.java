package com.kvs.app.quizapp.dto;

public class Contacts {
    
    private String id;
    private String relatedUserEmail;

    public Contacts() {}

    public Contacts(
        String id,
        String relatedUserEmail
    ) {
        this.id = id;
        this.relatedUserEmail = relatedUserEmail;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getRelatedUserEmail() {
        return relatedUserEmail;
    }
    public void setRelatedUserEmail(String relatedUserEmail) {
        this.relatedUserEmail = relatedUserEmail;
    }
    
}
