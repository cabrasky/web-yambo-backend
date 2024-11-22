package net.cabrasky.yambo.exceptions;

import net.cabrasky.yambo.payloads.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotEnabledException.class)
    public ResponseEntity<ErrorResponse> handleUserNotApprovedException(UserNotEnabledException e) {
        logger.error("UserNotApprovedException: {}", e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse("User not approved. Access denied.");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error("IllegalArgumentException: {}", e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse("Invalid input. Please check the data.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException e) {
        logger.error("NoResourceFoundException: {}", e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse("Requested resource was not found.");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        logger.error("AccessDeniedException: {}", e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse("Access denied. You do not have permission.");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        logger.error("BadCredentialsException: {}", e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse("Bad credentials. Please try again.");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        logger.error("AuthenticationException: {}", e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse("Authentication failed.");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<ErrorResponse> handleMailException(MailException e) {
        logger.error("MailException: {}", e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse("Error sending email.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException e) {
        logger.error("UsernameAlreadyExistsException: {}", e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse("Username already exists. Please try again.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyRegisteredException(EmailAlreadyRegisteredException e) {
        logger.error("EmailAlreadyRegisteredException: {}", e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse("Email already registered. Please try again.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        logger.error("Unexpected exception: {}", e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse("An unexpected error occurred.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error("Validation failed: {}", e.getBindingResult().getAllErrors());

        ErrorResponse errorResponse = new ErrorResponse("Validation failed. Check your input.");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

