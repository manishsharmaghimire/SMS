package com.sms.mapper;

import com.sms.dto.StudentDto;
import com.sms.entity.StudentProfile;
import com.sms.entity.User;
import com.sms.exception.MappingException;
import com.sms.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class StudentMapper {

    /**
     * Converts User and StudentProfile entities to StudentDto
     * @param user the User entity
     * @param studentProfile the StudentProfile entity
     * @return StudentDto with mapped values
     * @throws ResourceNotFoundException if user or studentProfile is null
     * @throws MappingException if there's an error during mapping
     */
    public StudentDto toStudentDto(User user, StudentProfile studentProfile) {
        try {
            // Input validation
            if (user == null) {
                throw new ResourceNotFoundException("User cannot be null");
            }
            if (studentProfile == null) {
                throw new ResourceNotFoundException("Student profile cannot be null");
            }

            return StudentDto.builder()
                    // User details
                    .id(user.getId())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .role(user.getRole() != null ? user.getRole().name() : null)
                    // Student profile details
                    .grade(studentProfile.getGrade())
                    .rollNumber(studentProfile.getRollNumber())
                    // Contact and address details
                    .contactNumber(user.getContactNumber())
                    .address(user.getAddress())
                    .dateOfBirth(user.getDateOfBirth())
                    // Timestamps
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .build();
                    
        } catch (ResourceNotFoundException e) {
            log.error("Resource not found while mapping StudentProfile to DTO: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            String errorMsg = String.format("Error mapping StudentProfile (ID: %s) to StudentDto: %s", 
                studentProfile != null ? studentProfile.getId() : "null", 
                e.getMessage());
            log.error(errorMsg, e);
            throw new MappingException(errorMsg, e);
        }
    }

    /**
     * Converts StudentDto to StudentProfile entity
     * @param studentDto the DTO to convert
     * @param user the associated User entity
     * @return new StudentProfile instance
     * @throws IllegalArgumentException if either parameter is null
     */
    public StudentProfile toStudentProfile(StudentDto studentDto, User user) {
        if (studentDto == null || user == null) {
            throw new IllegalArgumentException("StudentDto and User cannot be null");
        }

        try {
            return StudentProfile.builder()
                .user(user)
                .grade(studentDto.getGrade())
                .rollNumber(studentDto.getRollNumber())
                .build();
        } catch (Exception e) {
            String errorMsg = String.format("Error mapping StudentDto (ID: %s) to StudentProfile: %s",
                    studentDto.getId(),
                e.getMessage());
            log.error(errorMsg, e);
            throw new MappingException(errorMsg, e);
        }
    }
}