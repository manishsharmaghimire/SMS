package com.sms.service;

import com.sms.dto.PageRequestDto;
import com.sms.dto.PageResponse;
import com.sms.dto.StudentDto;

/**
 * Service interface for user-related operations.
 */
public interface UserServiceI {
    
    /**
     * Retrieves a student by their unique identifier.
     *
     * @param id the unique identifier of the student
     * @return the StudentDto containing student details
     * @throws com.sms.exception.ResourceNotFoundException if no student is found with the given ID
     */
    StudentDto findStudentById(Long id);
    
    /**
     * Retrieves a paginated and sorted list of all students.
     *
     * @param page the page number (0-based)
     * @param size the number of items per page
     * @param sort the sort criteria in the format 'property,direction' (e.g., 'id,asc')
     * @return PageResponse containing paginated student details
     */
    PageResponse<StudentDto> findAllStudents(int page, int size, String sort);
    

}
