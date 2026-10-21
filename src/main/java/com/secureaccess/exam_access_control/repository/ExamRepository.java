
package com.secureaccess.exam_access_control.repository;

import com.secureaccess.exam_access_control.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Long> {
}
