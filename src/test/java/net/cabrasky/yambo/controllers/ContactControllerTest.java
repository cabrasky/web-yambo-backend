package net.cabrasky.yambo.controllers;

import net.cabrasky.yambo.payloads.request.ContactForm;
import net.cabrasky.yambo.services.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ContactControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ContactController contactController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(contactController).build();
    }

    @Test
    void testSendEmail_Success() throws Exception {
        // Arrange
        doNothing().when(emailService).sendEmail(any(ContactForm.class));

        String requestBody = """
            {
                "name": "John Doe",
                "email": "john.doe@example.com",
                "subject": "Test Subject",
                "message": "Test Message"
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/contact/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))  // Verifica que la respuesta es JSON
                .andExpect(jsonPath("$.message").value("Email sent successfully."));  // Verifica el campo "message" en el JSON de respuesta
    }

    @Test
    void testSendEmail_Failure() throws Exception {
        // Arrange
        doThrow(new RuntimeException("SMTP server error")).when(emailService).sendEmail(any(ContactForm.class));

        String requestBody = """
            {
                "name": "John Doe",
                "email": "john.doe@example.com",
                "subject": "Test Subject",
                "message": "Test Message"
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/contact/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Failed to send email"));
    }
}
