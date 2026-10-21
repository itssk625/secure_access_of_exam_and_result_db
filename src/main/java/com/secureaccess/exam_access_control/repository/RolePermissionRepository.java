
package com.secureaccess.exam_access_control.repository;

import com.secureaccess.exam_access_control.entity.RolePermissionMapping;
import com.secureaccess.exam_access_control.entity.RolePermissionMapping.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermissionRepository
        extends JpaRepository<RolePermissionMapping, RolePermissionId> {

    List<RolePermissionMapping> findByRoleId(Long roleId);

}
