package net.cabrasky.yambo.utils;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String secret = "705efc4df44917cbb5195f5f2e1bb5d3e2a6fa5318def25b0b07da4e97726f51662606934c6ba1059530160060ab0073c576e225397d737e89a75b69a3adeb7827c20c634e198b8c9c8fe2b71f3ccec7e8ad8c22d502b35a245a41e1e26e72eb60b64778030fba84e301e5396c887a7675d8ee1e4853cf216e6477daec0d3a51";
    private final long expiration = 3600000;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        jwtUtil.setSecret(secret);
        jwtUtil.setExpiration(expiration);
    }

    @Test
    void testGenerateToken() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        assertNotNull(token);
        assertEquals(username, jwtUtil.extractUsername(token));
    }

    @Test
    void testExtractUsername() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        String extractedUsername = jwtUtil.extractUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void testExtractExpiration() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        Date expirationDate = jwtUtil.extractExpiration(token);

        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));
    }

    @Test
    void testValidateToken_Valid() {
        String username = "testUser";
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);

        String token = jwtUtil.generateToken(username);
        boolean isValid = jwtUtil.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidUsername() {
        String username = "testUser";
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("otherUser");

        String token = jwtUtil.generateToken(username);
        boolean isValid = jwtUtil.validateToken(token, userDetails);

        assertFalse(isValid);
    }

    @Test
    void testValidateToken_Expired() {
        String username = "testUser";
        jwtUtil.setExpiration(1);

        String token = jwtUtil.generateToken(username);

        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean isExpired = jwtUtil.isTokenExpired(token);

        assertTrue(isExpired);
    }

    @Test
    void testIsTokenExpired_True() {
        String username = "testUser";
        jwtUtil.setExpiration(1);

        String token = jwtUtil.generateToken(username);

        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertTrue(jwtUtil.isTokenExpired(token));
    }

    @Test
    void testIsTokenExpired_False() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        assertFalse(jwtUtil.isTokenExpired(token));
    }

    @Test
    void testExtractClaim() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        String extractedSubject = jwtUtil.extractClaim(token, claims -> claims.getSubject());
        assertEquals(username, extractedSubject);
    }

    @Test
    void testHandleExpiredJwtException() {
        String username = "testUser";
        jwtUtil.setExpiration(1);

        String token = jwtUtil.generateToken(username);

        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertThrows(ExpiredJwtException.class, () -> jwtUtil.extractAllClaims(token));
    }
}