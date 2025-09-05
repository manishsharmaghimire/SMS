package com.sms.controller;

import com.sms.entity.Role;
import com.sms.entity.StudentProfile;
import com.sms.entity.TeacherProfile;
import com.sms.entity.User;
import com.sms.repository.StudentProfileRepository;
import com.sms.repository.TeacherProfileRepository;
import com.sms.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:5175"}, 
           allowedHeaders = "*", 
           allowCredentials = "true")
public class AdminController {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final TeacherProfileRepository teacherProfileRepository;

    public AdminController(UserRepository userRepository,
                           StudentProfileRepository studentProfileRepository,
                           TeacherProfileRepository teacherProfileRepository) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.teacherProfileRepository = teacherProfileRepository;
    }

    @GetMapping("/students")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> allStudents() {
        return ResponseEntity.ok(
                userRepository.findAll().stream()
                        .filter(u -> u.getRole() == Role.STUDENT)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> allTeachers() {
        return ResponseEntity.ok(
                userRepository.findAll().stream()
                        .filter(u -> u.getRole() == Role.TEACHER)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/student-profiles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StudentProfile>> studentProfiles() {
        return ResponseEntity.ok(studentProfileRepository.findAll());
    }

    @GetMapping("/teacher-profiles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TeacherProfile>> teacherProfiles() {
        return ResponseEntity.ok(teacherProfileRepository.findAll());
    }
}
