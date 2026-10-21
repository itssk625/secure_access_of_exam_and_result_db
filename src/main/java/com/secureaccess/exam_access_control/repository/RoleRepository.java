package com.secureaccess.exam_access_control.repository;
import com.secureaccess.exam_access_control.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findByRoleIdIn(Collection<Long> roleIds);
}