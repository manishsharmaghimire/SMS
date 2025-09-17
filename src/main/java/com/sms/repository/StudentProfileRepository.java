package com.sms.repository;

import com.sms.entity.StudentProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    
    /**
     * Searches for students where name, email, grade, or roll number matches the query
     * @param query The search term (case-insensitive, partial match)
     * @param pageable Pagination information
     * @return Page of matching StudentProfile entities
     */
    @Query("SELECT sp FROM StudentProfile sp " +
           "JOIN sp.user u " +
           "WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(sp.grade) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(sp.rollNumber) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<StudentProfile> searchStudents(@Param("query") String query, Pageable pageable);
    
    Page<StudentProfile> findByUserFullNameContainingIgnoreCaseOrUserEmailContainingIgnoreCaseOrGradeContainingIgnoreCaseOrRollNumberContainingIgnoreCaseOrRollNumberEqualsOrId(
        String fullName, String email, String grade, String rollNumber, String rollNumberExact, 
        Long id, Pageable pageable);
}
