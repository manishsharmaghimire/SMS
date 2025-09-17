package com.sms.controller;

import com.sms.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    
    private final SearchService searchService;

    @GetMapping("/all")
    public ResponseEntity<?> searchAll(
            @RequestParam String query,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(searchService.searchAll(query, pageable));
    }

    @GetMapping("/students")
    public ResponseEntity<?> searchStudents(
            @RequestParam String query,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(searchService.searchStudents(query, pageable));
    }

    @GetMapping("/teachers")
    public ResponseEntity<?> searchTeachers(
            @RequestParam String query,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(searchService.searchTeachers(query, pageable));
    }
}
