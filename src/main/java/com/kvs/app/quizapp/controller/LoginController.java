package com.kvs.app.quizapp.controller;

import java.util.HashMap;
import java.util.Random;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kvs.app.quizapp.dto.Login;
import com.kvs.app.quizapp.helpers.EmailSender;
import com.kvs.app.quizapp.service.HandleUsers;

import jakarta.servlet.http.HttpSession;

import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    // login api section
    // need to clear the email key after some fixed time?
    private static volatile HashMap<String, Integer> emailOtp = new HashMap<>();

    private HandleUsers handleUsers;

    @Autowired
    public LoginController(
        HandleUsers handleUsers
    ) {
        this.handleUsers = handleUsers;
    }

    @PostMapping("/request")
    public ResponseEntity<Map<String, Object>> loginRequest(@Valid @RequestBody Login login) {
        HashMap<String, Object> response = new HashMap<>();
        // generate otp
        Random randomNumberGenerator = new Random();
        int otp = randomNumberGenerator.nextInt(100000, 999999);
        // send otp to email
        String result = EmailSender.sendOTP(otp, login.getEmail());
        if (result.equals("Success")) {
            emailOtp.put(login.getEmail(), otp);
        }
        // return sucess or failure to frontend
        response.put("status", "Success");
        response.put("message", "The OTP has been sent to the email");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/verification")
    public ResponseEntity<Map<String, Object>> startLogin(
        @Valid @RequestBody Login login, 
        HttpSession session
        ) {
        HashMap<String, Object> response = new HashMap<>();
        String email = login.getEmail();
        Integer userOtp = login.getOtp();
        Integer serverOtp = emailOtp.get(email);
        if (serverOtp != null && 
            userOtp != null && 
            userOtp.equals(serverOtp)) {
            session.setAttribute("username", email);
            emailOtp.remove(email);
            this.handleUsers.createUserIfNotExist(email);
            response.put("status", "Success");
            response.put("message", "The user has been verified");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("status", "Error");
        response.put("message", "Could not verify the user");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
