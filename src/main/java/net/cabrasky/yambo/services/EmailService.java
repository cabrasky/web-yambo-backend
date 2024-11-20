package net.cabrasky.yambo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import net.cabrasky.yambo.payloads.request.ContactForm;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${email.recipient}")
    private String recipientEmail;

    public void sendEmail(ContactForm contactForm) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject(contactForm.getSubject());
        message.setReplyTo(contactForm.getEmail());
        message.setText("Nombre: " + contactForm.getName() + "\n"
                      + "Email: " + contactForm.getEmail() + "\n"
                      + contactForm.getMessage());
        mailSender.send(message);
    }
}
