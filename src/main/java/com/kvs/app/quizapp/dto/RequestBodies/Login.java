package com.kvs.app.quizapp.dto.RequestBodies;

import javax.validation.constraints.NotNull;

public class Login {
    @NotNull
    private String email;
    private int otp;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public int getOtp() {
        return this.otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }
}
