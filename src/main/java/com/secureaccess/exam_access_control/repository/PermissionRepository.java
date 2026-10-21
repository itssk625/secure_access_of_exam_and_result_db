
package com.secureaccess.exam_access_control.repository;

import com.secureaccess.exam_access_control.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PermissionRepository
        extends JpaRepository<Permission, Long> {

    List<Permission> findByPermissionIdIn(
            Collection<Long> permissionIds
    );
}
