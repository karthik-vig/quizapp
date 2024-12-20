package com.kvs.app.quizapp.controller;

import org.springframework.http.HttpStatus;
// import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.ResponseStatus;
// import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
// import java.util.Hashtable;
import java.util.HashMap;
import java.util.Random;
import java.util.Properties;
import java.util.Date;
import jakarta.mail.Session;
import jakarta.mail.Message;
import jakarta.mail.Transport;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.InternetAddress;
import io.github.cdimascio.dotenv.Dotenv;

import com.kvs.app.quizapp.dto.RequestBodies.Login;


class HandleEmail {
    public static String sendOTP(int otp, String receiverEmail) {
        // get values from the .env file
        Dotenv dotenv = Dotenv.load();
        String GSMTP_APP_PASSWORD = dotenv.get("GSMTP_APP_PASSWORD");
        // get the smtp server
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("sender.email.id", "karthik.v@swirepay.com");
        String sendEmailID = props.getProperty("sender.email.id");
        // set up authentication
        Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(sendEmailID, GSMTP_APP_PASSWORD);
			}
		};
        Session emailSession = Session.getInstance(props, auth);
        // create the email
        MimeMessage emailMessage = new MimeMessage(emailSession);
        try {
            emailMessage.addHeader("Content-type", "text/HTML; charset=UTF-8");
            emailMessage.addHeader("format", "flowed");
            emailMessage.addHeader("Content-Transfer-Encoding", "8bit");
            emailMessage.setFrom(new InternetAddress(sendEmailID, "NoReply-JD"));
            emailMessage.setReplyTo(InternetAddress.parse(sendEmailID, false));
	        emailMessage.setSubject("QuizAPP Login OTP", "UTF-8");
	        emailMessage.setText("The OTP is: " + otp, "UTF-8");
	        emailMessage.setSentDate(new Date());
            emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail, false));
    	    Transport.send(emailMessage); 
        } catch (Exception e) {
            System.out.println(e.toString());
            return "Fail";
        }
        return "Success";
    }
}

@RestController
@RequestMapping("/api")
public class QuizAppController {

    // need to clear the email key after some fixed time?
    private static volatile HashMap<String, Integer> emailOtp = new HashMap<>();

    @PostMapping("/login/request")
    public ResponseEntity<?> loginRequest(@RequestBody Login login) {
        // generate otp
        Random randomNumberGenerator = new Random();
        int otp = randomNumberGenerator.nextInt(100000, 999999);
        // send otp to email
        String result = HandleEmail.sendOTP(otp, login.getEmail());
        if (result.equals("Success")) {
            emailOtp.put(login.getEmail(), otp);
        }
        // return sucess or failure to frontend
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/login/verification")
    public ResponseEntity<?> startLogin(@RequestBody Login login, HttpSession session) {
        String email = login.getEmail();
        Integer userOtp = login.getOtp();
        Integer serverOtp = emailOtp.get(email);
        if (serverOtp != null && 
            userOtp != null && 
            userOtp.equals(serverOtp)) {
            session.setAttribute("username", email);
            emailOtp.remove(email);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }
        return new ResponseEntity<>("Authentication Failed", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/test")
    public ResponseEntity<String> testFn(HttpSession session) {
        return ResponseEntity.ok((String) session.getAttribute("username"));
    }
}
