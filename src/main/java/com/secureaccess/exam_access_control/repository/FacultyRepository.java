
package com.secureaccess.exam_access_control.repository;

import com.secureaccess.exam_access_control.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacultyRepository
        extends JpaRepository<Faculty, Long> {

    Optional<Faculty> findByUserId(Long userId);
}
