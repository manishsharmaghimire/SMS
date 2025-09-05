package com.sms.dto;

import com.sms.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RegisterRequest {
    @NotBlank
    private String fullName;
    @NotBlank @Email
    private String email;
    @NotBlank
    private String password;
    @NotNull
    private Role role;

    // Student-only
    private String grade;
    private String rollNumber;

    // Teacher-only
    private String subject;
}
