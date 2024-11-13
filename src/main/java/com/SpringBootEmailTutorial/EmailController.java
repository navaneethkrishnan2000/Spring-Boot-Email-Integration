package com.SpringBootEmailTutorial;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RestController
public class EmailController {

    private final JavaMailSender javaMailSender;

    public EmailController(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

//    Send a simple email
    @RequestMapping("/send-email")
    public String sendEmail() {
        try {
            //To send an email we need an obj of SimpleMailMessage, then we have to configure the properties of the email
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("inavaneethkrishnan@gmail.com"); // Set From email
            message.setTo("inavaneethkrishnan@gmail.com"); // Set To email
            message.setSubject("Sample Email from Spring Boot"); // Set Subject
            message.setText("This is a sample email body for Sending emails using java spring boot"); // Set Body

            javaMailSender.send(message); // To send the message

            return "Email Send Successfully!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

//    Send an email with attachment
    @RequestMapping("/send-email-with-attachment")
    public String sendEmailWithAttachment() {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // if we didn't specify the 'true' here, then we can't send files through the mail

            helper.setFrom("inavaneethkrishnan@gmail.com"); // Set From email
            helper.setTo("inavaneethkrishnan@gmail.com"); // Set To email
            helper.setSubject("Java Email with attachment from Spring Boot"); // Set Subject
            helper.setText("Please find the attached documents below!"); // Set Body
            helper.addAttachment("angled-waves.png", new File("D:\\Spring Boot\\Email Sending Using Spring Boot\\angled-waves.png")); //Single slash is not supported here, so we have to use double slash
            helper.addAttachment("React Test.txt", new File("D:\\Spring Boot\\Email Sending Using Spring Boot\\React Test.txt"));

            javaMailSender.send(message); // To send the message
            return "Email With Attachment Send Successfully!";

        } catch (Exception e) {
            return e.getMessage();
        }
    }

//    Send an email with html body
@RequestMapping("/send-html-email")
public String sendHtmlEmail() {
    try {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // if we didn't specify the 'true' here, then we can't send files through the mail

        helper.setFrom("inavaneethkrishnan@gmail.com"); // Set From email
        helper.setTo("inavaneethkrishnan@gmail.com"); // Set To email
        helper.setSubject("Java Email with attachment from Spring Boot"); // Set Subject

        // To send the html page as a body
        // The setText method only accepts String, so we have to convert the html page into string i think, for that i use this code EmailController.class.getResourceAsStream("/templates/email-content.html")
        // The above code can throw null pointer exception, so use Objects.requireNonNull to avoid that
        // Then add the String and set html as true
        // We have a logo in the html body, we can't provide the image url in the html, we can't do that
        // So add 'cid' in-front of the file name inside the src attribute <img src="cid:angled-waves.png" width="100" height="100"/>
        // Then in the handler method use addInline method, then specify the name and url of the image
        try (var inputStream = Objects.requireNonNull(EmailController.class.getResourceAsStream("/templates/email-content.html"))) {
            helper.setText(
                    new String(inputStream.readAllBytes(), StandardCharsets.UTF_8),
                    true
            );
        }
        helper.addInline("angled-waves.png", new File("D:\\Spring Boot\\Email Sending Using Spring Boot\\angled-waves.png"));

        javaMailSender.send(message); // To send the message
        return "Email With Html Send Successfully!";

    } catch (Exception e) {
        return e.getMessage();
    }
}
}
