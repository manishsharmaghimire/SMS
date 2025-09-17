package com.sms.service;

import com.sms.dto.AuthResponse;
import com.sms.dto.LoginRequest;
import com.sms.dto.RegisterRequest;
import com.sms.exception.InvalidRequestException;
import com.sms.exception.ResourceAlreadyExistsException;

/**
 * Service interface for handling authentication related operations.
 */
public interface AuthServiceI {

    /**
     * Registers a new user with the provided registration details.
     *
     * @param req the registration request containing user details
     * @throws ResourceAlreadyExistsException if the email is already registered
     * @throws InvalidRequestException if the request is invalid or missing required fields
     */
    void register(RegisterRequest req);

    /**
     * Authenticates a user and generates a JWT token upon successful authentication.
     *
     * @param req the login request containing credentials
     * @return AuthResponse containing the JWT token and user details
     * @throws org.springframework.security.authentication.BadCredentialsException if authentication fails
     */
    AuthResponse login(LoginRequest req);
}
