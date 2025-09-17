package com.sms.service;

import com.sms.dto.StudentDto;
import com.sms.dto.TeacherDto;
import com.sms.entity.StudentProfile;
import com.sms.entity.TeacherProfile;
import com.sms.mapper.StudentMapper;
import com.sms.mapper.TeacherMapper;
import com.sms.repository.StudentProfileRepository;
import com.sms.repository.TeacherProfileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final StudentProfileRepository studentProfileRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^\\d+$");

    public Page<StudentDto> searchStudents(String query, Pageable pageable) {
        String cleanedQuery = cleanAndNormalizeQuery(query);
        
        Page<StudentProfile> studentPage = studentProfileRepository
            .findByUserFullNameContainingIgnoreCaseOrUserEmailContainingIgnoreCaseOrGradeContainingIgnoreCaseOrRollNumberContainingIgnoreCaseOrRollNumberEqualsOrId(
                cleanedQuery, cleanedQuery, cleanedQuery, cleanedQuery, cleanedQuery, 
                isNumeric(cleanedQuery) ? Long.parseLong(cleanedQuery) : -1L, 
                pageable
            );
            
        return studentPage.map(student -> studentMapper.toStudentDto(student.getUser(), student));
    }

    public Page<TeacherDto> searchTeachers(String query, Pageable pageable) {
        String cleanedQuery = cleanAndNormalizeQuery(query);
        
        Page<TeacherProfile> teacherPage = teacherProfileRepository
            .findByUserFullNameContainingIgnoreCaseOrUserEmailContainingIgnoreCaseOrDepartmentContainingIgnoreCaseOrSubjectSpecializationContainingIgnoreCaseOrId(
                cleanedQuery, cleanedQuery, cleanedQuery, cleanedQuery, 
                isNumeric(cleanedQuery) ? Long.parseLong(cleanedQuery) : -1L,
                pageable
            );
            
        return teacherPage.map(teacherMapper::toDto);
    }
    
    public Page<Object> searchAll(String query, Pageable pageable) {
        String cleanedQuery = cleanAndNormalizeQuery(query);
        
        // Search students
        Page<StudentProfile> studentPage = studentProfileRepository
            .findByUserFullNameContainingIgnoreCaseOrUserEmailContainingIgnoreCaseOrGradeContainingIgnoreCaseOrRollNumberContainingIgnoreCaseOrRollNumberEqualsOrId(
                cleanedQuery, cleanedQuery, cleanedQuery, cleanedQuery, cleanedQuery, 
                isNumeric(cleanedQuery) ? Long.parseLong(cleanedQuery) : -1L, 
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() / 2, pageable.getSort())
            );
            
        // Search teachers
        Page<TeacherProfile> teacherPage = teacherProfileRepository
            .findByUserFullNameContainingIgnoreCaseOrUserEmailContainingIgnoreCaseOrDepartmentContainingIgnoreCaseOrSubjectSpecializationContainingIgnoreCaseOrId(
                cleanedQuery, cleanedQuery, cleanedQuery, cleanedQuery, 
                isNumeric(cleanedQuery) ? Long.parseLong(cleanedQuery) : -1L,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() / 2, pageable.getSort())
            );
            
        // Combine results
        List<Object> combinedResults = new ArrayList<>();
        
        // Map StudentProfile to StudentDto
        combinedResults.addAll(studentPage.getContent().stream()
            .map(studentProfile -> studentMapper.toStudentDto(studentProfile.getUser(), studentProfile))
            .collect(Collectors.toList()));
            
        // Map TeacherProfile to TeacherDto
        combinedResults.addAll(teacherPage.getContent().stream()
            .map(teacherMapper::toDto)
            .collect(Collectors.toList()));
            
        return new PageImpl<>(
            combinedResults,
            pageable,
            studentPage.getTotalElements() + teacherPage.getTotalElements()
        );
    }
    
    private String cleanAndNormalizeQuery(String query) {
        if (query == null) {
            return "";
        }
        return query.trim().replaceAll("\\s+", " ");
    }
    
    private boolean isNumeric(String str) {
        return str != null && NUMERIC_PATTERN.matcher(str).matches();
    }
}
