package com.sms.service;

import com.sms.dto.AuthResponse;
import com.sms.dto.LoginRequest;
import com.sms.dto.RegisterRequest;
import com.sms.entity.Role;
import com.sms.entity.StudentProfile;
import com.sms.entity.TeacherProfile;
import com.sms.entity.User;
import com.sms.exception.InvalidRequestException;
import com.sms.exception.ResourceAlreadyExistsException;
import com.sms.exception.ResourceNotFoundException;
import com.sms.repository.StudentProfileRepository;
import com.sms.repository.TeacherProfileRepository;
import com.sms.repository.UserRepository;
import com.sms.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    
    // Error messages
    private static final String INVALID_CREDENTIALS = "Invalid email or password";
    private static final String EMAIL_EXISTS = "Email %s is already registered";
    private static final String INVALID_ROLE = "Unsupported role: %s";
    private static final String LOGIN_ERROR = "An error occurred during login. Please try again.";

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    /**
     * Registers a new user with the provided registration details.
     *
     * @param req the registration request containing user details
     * @throws ResourceAlreadyExistsException if the email is already registered
     * @throws InvalidRequestException if the request is invalid
     */
    @Transactional
    public void register(RegisterRequest req) {
        if (req == null) {
            throw new InvalidRequestException("Registration request cannot be null");
        }
        
        log.info("Attempting to register user with email: {}", req.getEmail());
        
        // Basic validation
        if (req.getRole() == null) {
            throw new InvalidRequestException("Role is required for registration");
        }
        
        // Check if email already exists
        String email = StringUtils.trimToNull(req.getEmail());
        if (email == null) {
            throw new InvalidRequestException("Email cannot be empty");
        }
        
        if (userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException(String.format(EMAIL_EXISTS, email));
        }

        // Role-specific validation
        if (req.getRole() == Role.STUDENT) {
            validateStudentRegistration(req);
        } else if (req.getRole() == Role.TEACHER) {
            validateTeacherRegistration(req);
        } else {
            throw new InvalidRequestException(String.format(INVALID_ROLE, req.getRole()));
        }

        try {
            // Create and save user
            User user = User.builder()
                    .fullName(StringUtils.trimToEmpty(req.getFullName()))
                    .email(email.toLowerCase())
                    .password(passwordEncoder.encode(req.getPassword()))
                    .role(req.getRole())
                    .build();

            user = userRepository.save(user);
            log.info("User registered successfully with ID: {} and role: {}", user.getId(), user.getRole());

            createRoleSpecificProfile(user, req);
        } catch (Exception e) {
            log.error("Error during user registration: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void validateStudentRegistration(RegisterRequest req) {
        if (StringUtils.isBlank(req.getGrade())) {
            throw new InvalidRequestException("Grade is required for student registration");
        }
        if (StringUtils.isBlank(req.getRollNumber())) {
            throw new InvalidRequestException("Roll number is required for student registration");
        }
    }

    private void validateTeacherRegistration(RegisterRequest req) {
        if (StringUtils.isBlank(req.getSubject())) {
            throw new InvalidRequestException("Subject is required for teacher registration");
        }
    }

    private void createRoleSpecificProfile(User user, RegisterRequest req) {
        if (user.getRole() == Role.STUDENT) {
            StudentProfile studentProfile = StudentProfile.builder()
                    .grade(StringUtils.trimToEmpty(req.getGrade()))
                    .rollNumber(StringUtils.trimToEmpty(req.getRollNumber()))
                    .user(user)
                    .build();
            studentProfileRepository.save(studentProfile);
        } else if (user.getRole() == Role.TEACHER) {
            TeacherProfile teacherProfile = TeacherProfile.builder()
                    .subject(StringUtils.trimToEmpty(req.getSubject()))
                    .user(user)
                    .build();
            teacherProfileRepository.save(teacherProfile);
        }
    }

    /**
     * Authenticates a user and generates a JWT token upon successful authentication.
     *
     * @param req the login request containing credentials
     * @return AuthResponse containing the JWT token and user details
     * @throws BadCredentialsException if authentication fails
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        log.info("Login attempt for user: {}", req.getEmail());
        
        Objects.requireNonNull(req, "LoginRequest cannot be null");
        
        try {
            // Authenticate user
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            StringUtils.trimToEmpty(req.getEmail()),
                            req.getPassword()
                    )
            );

            // Get user details
            String email = ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

            // Generate JWT token with claims
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole().name());
            claims.put("name", user.getFullName());
            claims.put("email", user.getEmail());

            String token = jwtUtils.generateToken((UserDetails) auth.getPrincipal(), claims);
            log.info("User {} logged in successfully", email);
            
            return buildAuthResponse(token, user);
                    
        } catch (AuthenticationException e) {
            log.warn("Authentication failed for user: {}", req.getEmail());
            throw new BadCredentialsException(INVALID_CREDENTIALS);
        } catch (Exception e) {
            log.error("Error during login for user {}: {}", req.getEmail(), e.getMessage(), e);
            throw new RuntimeException(LOGIN_ERROR, e);
        }
    }
    
    /**
     * Builds the authentication response with user details.
     *
     * @param token the JWT token
     * @param user the authenticated user
     * @return AuthResponse with token and user details
     */
    private AuthResponse buildAuthResponse(String token, User user) {
        return AuthResponse.builder()
                .token(token)
                .role(user.getRole().name())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .build();
    }
}
