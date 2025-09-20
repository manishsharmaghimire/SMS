package com.sms.repository;

import com.sms.entity.StudentProfile;
import com.sms.entity.TeacherProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends JpaRepository<StudentProfile, Long> {
    
    @Query("""
        SELECT s FROM StudentProfile s 
        WHERE LOWER(s.user.fullName) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(s.user.email) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(s.grade) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(s.rollNumber) LIKE LOWER(CONCAT('%', :query, '%'))
           OR (FUNCTION('REGEXP_LIKE', :query, '^\\d+$') = true AND s.id = CAST(:query AS long))
           OR s.rollNumber = :query
    """)
    Page<StudentProfile> searchStudents(@Param("query") String query, Pageable pageable);
    
    @Query("""
        SELECT t FROM TeacherProfile t 
        WHERE LOWER(t.user.fullName) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(t.user.email) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(COALESCE(t.department, '')) LIKE LOWER(CONCAT('%', :query, '%'))
           OR LOWER(COALESCE(t.subjectSpecialization, '')) LIKE LOWER(CONCAT('%', :query, '%'))
           OR (FUNCTION('REGEXP_LIKE', :query, '^\\d+$') = true AND t.id = CAST(:query AS long))
    """)
    Page<TeacherProfile> searchTeachers(@Param("query") String query, Pageable pageable);
}
