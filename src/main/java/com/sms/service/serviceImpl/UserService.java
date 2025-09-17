package com.sms.service.serviceImpl;

import com.sms.dto.StudentDto;
import com.sms.entity.StudentProfile;
import com.sms.entity.User;
import com.sms.exception.ResourceNotFoundException;
import com.sms.mapper.StudentMapper;
import com.sms.repository.StudentProfileRepository;
import com.sms.service.UserServiceI;
import com.sms.dto.PageResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceI {

    private final StudentProfileRepository studentProfileRepository;
    private final StudentMapper studentMapper;
    // Removed Elasticsearch dependency, using JPA search instead
    @Override
    public StudentDto findStudentById(Long id) {
        StudentProfile studentProfile=studentProfileRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(STR."Student not found with id \{id}"));
        
        // Get the associated user from the student profile
        User user = studentProfile.getUser();
        if (user == null) {
            throw new ResourceNotFoundException(STR."User not found for student with id \{id}");
        }
        
        return studentMapper.toStudentDto(user, studentProfile);

    }


    @Override
    public PageResponse<StudentDto> findAllStudents(int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        String sortBy = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<StudentProfile> studentPage = studentProfileRepository.findAll(pageable);
        
        return mapStudentPageToDtoPage(studentPage);
    }
    


    private PageResponse<StudentDto> mapStudentPageToDtoPage(Page<StudentProfile> studentPage) {
        Page<StudentDto> dtoPage = studentPage.map(studentProfile -> {
            User user = studentProfile.getUser();
            if (user == null) {
                throw new ResourceNotFoundException("User not found for student with id {studentProfile.getId()}");
            }
            return studentMapper.toStudentDto(user, studentProfile);
        });
        
        // Create and return response using the builder pattern
        return PageResponse.of(dtoPage);
    }

}
