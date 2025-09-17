package com.sms.controller;

import com.sms.dto.PageRequestDto;
import com.sms.dto.PageResponse;
import com.sms.dto.StudentDto;
import com.sms.service.UserServiceI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Tag(name = "Student Management", description = "APIs for managing students")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "http://localhost:5175"}, 
           allowedHeaders = "*", allowCredentials = "true")
public class StudentController {

    private final UserServiceI userService;

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable("id") Long id) {
        System.out.println("GET /api/students/" + id + " endpoint hit!");
        try {
            StudentDto student = userService.findStudentById(id);

            System.out.println("Found student: " + student);
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            System.err.println("Error in getStudentById: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping
    @Operation(summary = "Get all students with pagination",
        parameters = {
            @Parameter(name = "page", description = "Page number (0-based)", example = "0"),
            @Parameter(name = "size", description = "Number of items per page", example = "10"),
            @Parameter(name = "sort", description = "Sorting criteria in the format: property,direction. Example: id,asc", 
                      example = "id,asc")
        })
    public ResponseEntity<PageResponse<StudentDto>> getAllStudents(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "id,asc") String sort) {
        
        return ResponseEntity.ok(userService.findAllStudents(page, size, sort));
    }


}
