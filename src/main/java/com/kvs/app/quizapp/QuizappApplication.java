package com.kvs.app.quizapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.kvs.app.quizapp")
public class QuizappApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizappApplication.class, args);
	}

}
