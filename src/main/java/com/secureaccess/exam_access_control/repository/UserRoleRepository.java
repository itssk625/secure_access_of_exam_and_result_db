package com.secureaccess.exam_access_control.repository;


import com.secureaccess.exam_access_control.entity.UserRoleMapping;
import com.secureaccess.exam_access_control.entity.UserRoleMapping.UserRoleId;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository
        extends JpaRepository<UserRoleMapping, UserRoleId> {

    List<UserRoleMapping> findByUserId(Long userId);
}