package com.sms.repository;

import com.sms.entity.TeacherProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeacherProfileRepository extends JpaRepository<TeacherProfile, Long> {
    @Query("""
        SELECT t FROM TeacherProfile t 
        WHERE LOWER(t.user.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) 
           OR LOWER(t.user.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
           OR LOWER(COALESCE(t.department, '')) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
           OR LOWER(COALESCE(t.subjectSpecialization, '')) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
           OR (FUNCTION('REGEXP_LIKE', :searchTerm, '^\\d+$') = true AND t.id = CAST(:searchTerm AS long))
    """)
    Page<TeacherProfile> searchAllFields(@Param("searchTerm") String searchTerm, Pageable pageable);
}
