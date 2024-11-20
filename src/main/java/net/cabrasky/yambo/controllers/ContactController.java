package net.cabrasky.yambo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import net.cabrasky.yambo.payloads.request.ContactForm;
import net.cabrasky.yambo.services.EmailService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendEmail(@RequestBody ContactForm contactForm) {
        Map<String, String> response = new HashMap<>();
        try {
            emailService.sendEmail(contactForm);
            response.put("message", "Email sent successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Failed to send email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
