package com.sms.mapper;

import com.sms.dto.TeacherDto;
import com.sms.entity.TeacherProfile;
import com.sms.entity.User;
import com.sms.exception.MappingException;
import com.sms.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between Teacher entity and DTO objects.
 */
@Slf4j
@Component
public class TeacherMapper {

    /**
     * Converts User and TeacherProfile entities to TeacherDto
     * @param user the User entity
     * @param teacherProfile the TeacherProfile entity
     * @return TeacherDto with mapped values
     * @throws ResourceNotFoundException if user or teacherProfile is null
     * @throws MappingException if there's an error during mapping
     */
    /**
     * Converts a TeacherProfile to TeacherDto
     * @param teacherProfile the TeacherProfile entity
     * @return TeacherDto with mapped values
     * @throws ResourceNotFoundException if teacherProfile or its user is null
     * @throws MappingException if there's an error during mapping
     */
    public TeacherDto toDto(TeacherProfile teacherProfile) {
        if (teacherProfile == null) {
            throw new ResourceNotFoundException("Teacher profile cannot be null");
        }
        if (teacherProfile.getUser() == null) {
            throw new ResourceNotFoundException("User associated with teacher profile cannot be null");
        }
        return toTeacherDto(teacherProfile.getUser(), teacherProfile);
    }
    
    /**
     * Converts User and TeacherProfile entities to TeacherDto
     * @param user the User entity
     * @param teacherProfile the TeacherProfile entity
     * @return TeacherDto with mapped values
     * @throws ResourceNotFoundException if user or teacherProfile is null
     * @throws MappingException if there's an error during mapping
     */
    public TeacherDto toTeacherDto(User user, TeacherProfile teacherProfile) {
        try {
            // Input validation
            if (user == null) {
                throw new ResourceNotFoundException("User cannot be null");
            }
            if (teacherProfile == null) {
                throw new ResourceNotFoundException("Teacher profile cannot be null");
            }

            return TeacherDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .contactNumber(user.getContactNumber())
                    .address(user.getAddress())
                    .dateOfBirth(user.getDateOfBirth())
                    .subject(teacherProfile.getSubject())
                    .salary(teacherProfile.getSalary())
                    .build();

        } catch (Exception e) {
            log.error("Error mapping Teacher to DTO: {}", e.getMessage(), e);
            throw new MappingException("Error mapping Teacher to DTO", e);
        }
    }
}
