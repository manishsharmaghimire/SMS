package com.sms.repository;

import com.sms.entity.TeacherProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherProfileRepository extends JpaRepository<TeacherProfile, Long> {
    Page<TeacherProfile> findByUserFullNameContainingIgnoreCaseOrUserEmailContainingIgnoreCaseOrDepartmentContainingIgnoreCaseOrSubjectSpecializationContainingIgnoreCaseOrId(
        String fullName, String email, String department, String subjectSpecialization,
        Long id, Pageable pageable);
}
