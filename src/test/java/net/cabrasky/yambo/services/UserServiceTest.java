package net.cabrasky.yambo.services;

import net.cabrasky.yambo.exceptions.EmailAlreadyRegisteredException;
import net.cabrasky.yambo.exceptions.UserNotEnabledException;
import net.cabrasky.yambo.exceptions.UsernameAlreadyExistsException;
import net.cabrasky.yambo.models.Role;
import net.cabrasky.yambo.models.User;
import net.cabrasky.yambo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUserSuccess() {
        String username = "testuser";
        String password = "password123";
        String email = "test@example.com";

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setUsername(username);
        savedUser.setEmail(email);
        savedUser.setPassword("encodedPassword");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.registerUser(username, password, email);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(email, result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        assertFalse(result.isEnabled());
    }

    @Test
    public void testRegisterUserUsernameAlreadyExists() {
        String username = "testuser";
        String password = "password123";
        String email = "test@example.com";

        when(userRepository.existsByUsername(username)).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class, () -> userService.registerUser(username, password, email));
    }

    @Test
    public void testRegisterUserEmailAlreadyRegistered() {
        String username = "testuser";
        String password = "password123";
        String email = "test@example.com";

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(EmailAlreadyRegisteredException.class, () -> userService.registerUser(username, password, email));
    }

    @Test
    public void testLoadUserByUsernameNotEnabledThrowsException() {
        String username = "testuser";
        String email = "test@example.com";

        Role privilege = new Role();
        privilege.setId("ROLE_USER");

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("encodedPassword");
        user.setRoles(Set.of(privilege));
        user.setEnabled(false);

        when(userRepository.findByUsernameOrEmail(username, username)).thenReturn(Optional.of(user));
        when(userRepository.findByUsernameOrEmail(email, email)).thenReturn(Optional.of(user));

        assertThrows(UserNotEnabledException.class, () -> userService.loadUserByUsername(username));
        assertThrows(UserNotEnabledException.class, () -> userService.loadUserByUsername(email));
    }

    @Test
    public void testGetUnapprovedUsers() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEnabled(false);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setEnabled(false);

        when(userRepository.findByIsEnabledFalse()).thenReturn(Arrays.asList(user1, user2));

        List<User> unapprovedUsers = userService.getDisabledUsers();

        assertNotNull(unapprovedUsers);
        assertEquals(2, unapprovedUsers.size());
    }

    @Test
    public void testGetUsersByRole() {
        String roleName = "ADMIN";

        User user1 = new User();
        user1.setUsername("admin1");

        User user2 = new User();
        user2.setUsername("admin2");

        when(userRepository.findByRoles_id(roleName)).thenReturn(Arrays.asList(user1, user2));

        List<User> usersByRole = userService.getUsersByRole(roleName);

        assertNotNull(usersByRole);
        assertEquals(2, usersByRole.size());
        assertEquals("admin1", usersByRole.get(0).getUsername());
        assertEquals("admin2", usersByRole.get(1).getUsername());
    }

    @Test
    public void testLoadUserByUsernameSuccess() {
        String username = "testuser";
        String email = "test@example.com";

        Role privilege = new Role();
        privilege.setId("ROLE_USER");

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("encodedPassword");
        user.setEnabled(true);
        user.setRoles(Set.of(privilege));

        when(userRepository.findByUsernameOrEmail(username, username)).thenReturn(Optional.of(user));
        when(userRepository.findByUsernameOrEmail(email, email)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));

        userDetails = userService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    public void testLoadUserByUsernameUserNotFound() {
        String username = "nonexistentuser";
        String email = "nonexistent@example.com";

        when(userRepository.findByUsernameOrEmail(username, username)).thenReturn(Optional.empty());
        when(userRepository.findByUsernameOrEmail(email, email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(email));
    }
}