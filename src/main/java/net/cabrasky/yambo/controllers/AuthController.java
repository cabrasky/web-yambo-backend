package net.cabrasky.yambo.controllers;

import net.cabrasky.yambo.models.User;
import net.cabrasky.yambo.payloads.request.AuthRequest;
import net.cabrasky.yambo.payloads.response.AuthResponse;
import net.cabrasky.yambo.payloads.response.MessageResponse;
import net.cabrasky.yambo.services.UserService;
import net.cabrasky.yambo.utils.JwtUtil;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public ResponseEntity<String> welcome() {
        return new ResponseEntity<>("Welcome, this endpoint is not secure", HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> addNewUser(@RequestBody User user) {
        User response = userService.registerUser(user.getUsername(), user.getPassword(), user.getEmail());
        return new ResponseEntity<>(
                new MessageResponse(String.format("User %s was created", response.getUsername())),
                HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        String token = jwtUtil.generateToken(authRequest.getUsername());
        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
    }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> userProfile() {
        return new ResponseEntity<>("Welcome to User Profile", HttpStatus.OK);
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminProfile() {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities();
        return new ResponseEntity<>(String.format("Welcome to Admin \n Authorities: %s", authorities), HttpStatus.OK);
    }
}
