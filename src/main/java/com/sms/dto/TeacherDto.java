package com.sms.dto;

import com.sms.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Teacher information.
 * Combines common user fields with teacher-specific fields.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDto {
    // Common fields from User entity
    private Long id;
    private String fullName;
    private String email;
    private Role role;
    private String contactNumber;
    private String address;
    private LocalDate dateOfBirth;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Teacher-specific fields from TeacherProfile
    private String subject;
    private Double salary;
}
