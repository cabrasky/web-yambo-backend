package net.cabrasky.yambo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.cabrasky.yambo.config.SecurityConfig;
import net.cabrasky.yambo.models.User;
import net.cabrasky.yambo.payloads.request.AuthRequest;
import net.cabrasky.yambo.services.UserService;
import net.cabrasky.yambo.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private UserService userService;

        @MockBean
        private JwtUtil jwtUtil;

        @MockBean
        private AuthenticationManager authenticationManager;

        @InjectMocks
        private AuthController authController;

        private final ObjectMapper objectMapper = new ObjectMapper();

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
        }

        @Test
        @WithMockUser(username = "user", roles = { "USER" })
        public void testUserProfileAccess_WithUserRole() throws Exception {
                mockMvc.perform(get("/auth/user/userProfile"))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        public void testAdminProfileAccess_WithAdminRole() throws Exception {
                mockMvc.perform(get("/auth/admin/adminProfile"))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "user", roles = { "USER" })
        public void testAdminProfileAccess_WithUserRole() throws Exception {
                mockMvc.perform(get("/auth/admin/adminProfile"))
                                .andExpect(status().isForbidden())
                                .andExpect(result -> assertTrue(
                                                result.getResolvedException() instanceof AccessDeniedException));
        }

        @Test
        @WithMockUser(username = "admin", roles = { "ADMIN" })
        public void testUserProfileAccess_WithAdminRole() throws Exception {
                mockMvc.perform(get("/auth/user/userProfile"))
                                .andExpect(status().isForbidden())
                                .andExpect(result -> assertTrue(
                                                result.getResolvedException() instanceof AccessDeniedException));
        }

        @Test
        @WithAnonymousUser
        public void testUserProfileAccess_WithoutAnyRole() throws Exception {
                mockMvc.perform(get("/auth/user/userProfile"))
                                .andExpect(status().isForbidden());
        }

        @Test
        public void testWelcome() throws Exception {
                mockMvc.perform(get("/auth/welcome"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Welcome, this endpoint is not secure"));
        }

        @Test
        public void testAddNewUserSuccess() throws Exception {
                User user = new User();
                user.setUsername("testuser");
                user.setPassword("password123");
                user.setEmail("test@example.com");

                when(userService.registerUser(user.getUsername(), user.getPassword(), user.getEmail()))
                                .thenReturn(user);

                mockMvc.perform(post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.message").value("User testuser was created"));
        }

        @Test
        public void testAuthenticateAndGetTokenSuccess() throws Exception {
                AuthRequest authRequest = new AuthRequest("testuser", "password123");

                Authentication mockAuthentication = mock(Authentication.class);
                when(mockAuthentication.isAuthenticated()).thenReturn(true);

                when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                                .thenReturn(mockAuthentication);
                when(jwtUtil.generateToken(authRequest.getUsername())).thenReturn("mock-jwt-token");

                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(authRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
        }

        @Test
        public void testAuthenticateAndGetTokenInvalidCredentials() throws Exception {
                AuthRequest authRequest = new AuthRequest("testuser", "wrongpassword");

                when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                                .thenThrow(new BadCredentialsException("Invalid credentials"));

                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(authRequest)))
                                .andExpect(status().isUnauthorized())
                                .andExpect(result -> assertTrue(
                                                result.getResolvedException() instanceof BadCredentialsException));
        }
}
