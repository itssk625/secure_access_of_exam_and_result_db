
package com.secureaccess.exam_access_control.repository;

import com.secureaccess.exam_access_control.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUserId(Long userId);
}
