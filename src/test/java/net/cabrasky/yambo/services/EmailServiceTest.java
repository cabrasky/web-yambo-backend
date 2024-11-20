package net.cabrasky.yambo.services;

import net.cabrasky.yambo.payloads.request.ContactForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendEmail_Success() {
        // Arrange
        ContactForm contactForm = new ContactForm();
        contactForm.setName("John Doe");
        contactForm.setEmail("john.doe@example.com");
        contactForm.setSubject("Test Subject");
        contactForm.setMessage("Test Message");

        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        // Act
        emailService.sendEmail(contactForm);

        // Assert
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
