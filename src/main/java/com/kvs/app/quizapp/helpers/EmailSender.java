package com.kvs.app.quizapp.helpers;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.Properties;
import java.util.Date;

import jakarta.mail.Session;
import jakarta.mail.Message;
import jakarta.mail.Transport;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.InternetAddress;

public class EmailSender {
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
