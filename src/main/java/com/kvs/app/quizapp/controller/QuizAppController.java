package com.kvs.app.quizapp.controller;

import org.springframework.http.HttpStatus;
// import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.ResponseStatus;
// import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

import com.kvs.app.quizapp.dto.RequestBodies.Login;


@RestController
@RequestMapping("/api")
public class QuizAppController {

    @PostMapping("/login")
    public ResponseEntity<?> startLogin(@RequestBody Login login) {
        if (login.getEmail().equals("karthik.v@swirepay.com")) {
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed", HttpStatus.UNAUTHORIZED);
        }
        // TestDTO testDto = new TestDTO("123", "karthik.v@swirepay.com");
        // return ResponseEntity.ok(testDto);
    }
}
