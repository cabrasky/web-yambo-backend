package net.cabrasky.yambo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import net.cabrasky.yambo.payloads.request.ContactForm;
import net.cabrasky.yambo.payloads.response.ErrorResponse;
import net.cabrasky.yambo.payloads.response.MessageResponse;
import net.cabrasky.yambo.services.EmailService;

@RestController
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody ContactForm contactForm) {
        try {
            emailService.sendEmail(contactForm);
            return new ResponseEntity<>(new MessageResponse("Email sent successfully."), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse("Failed to send email"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
