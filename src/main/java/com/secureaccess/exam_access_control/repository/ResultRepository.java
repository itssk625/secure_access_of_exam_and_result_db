package com.secureaccess.exam_access_control.repository;

import com.secureaccess.exam_access_control.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {

    List<Result> findByStudentId(Long studentId);
}