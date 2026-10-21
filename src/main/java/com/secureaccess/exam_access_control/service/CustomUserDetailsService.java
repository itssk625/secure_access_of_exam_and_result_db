
package com.secureaccess.exam_access_control.service;

import com.secureaccess.exam_access_control.entity.Permission;
import com.secureaccess.exam_access_control.entity.Role;
import com.secureaccess.exam_access_control.entity.RolePermissionMapping;
import com.secureaccess.exam_access_control.entity.User;
import com.secureaccess.exam_access_control.entity.UserRoleMapping;
import com.secureaccess.exam_access_control.repository.PermissionRepository;
import com.secureaccess.exam_access_control.repository.RolePermissionRepository;
import com.secureaccess.exam_access_control.repository.RoleRepository;
import com.secureaccess.exam_access_control.repository.UserRepository;
import com.secureaccess.exam_access_control.repository.UserRoleRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;

    public CustomUserDetailsService(
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            RoleRepository roleRepository,
            RolePermissionRepository rolePermissionRepository,
            PermissionRepository permissionRepository) {

        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        List<UserRoleMapping> userRoleMappings =
                userRoleRepository.findByUserId(user.getUserId());

        List<Long> roleIds = userRoleMappings.stream()
                .map(UserRoleMapping::getRoleId)
                .distinct()
                .toList();

        if (roleIds.isEmpty()) {
            throw new UsernameNotFoundException(
                    "User has no valid role assignments");
        }

        List<Role> roles = roleRepository.findByRoleIdIn(roleIds);

        if (roles.size() != roleIds.size()) {
            throw new UsernameNotFoundException(
                    "User has invalid role assignments");
        }

        List<RolePermissionMapping> rolePermissionMappings =
                new ArrayList<>();

        for (Long roleId : roleIds) {
            rolePermissionMappings.addAll(
                    rolePermissionRepository.findByRoleId(roleId));
        }

        Set<Long> permissionIds = new HashSet<>();

        for (RolePermissionMapping mapping : rolePermissionMappings) {
            permissionIds.add(mapping.getPermissionId());
        }

        List<Permission> permissions = permissionIds.isEmpty()
                ? List.of()
                : permissionRepository.findByPermissionIdIn(permissionIds);

        if (permissions.size() != permissionIds.size()) {
            throw new UsernameNotFoundException(
                    "Invalid permission assignments");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(
                    "ROLE_" + role.getRoleName()));
        }

        for (Permission permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(
                    permission.getPermissionName()));
        }

        boolean active = "ACTIVE".equals(user.getAccountStatus());

        boolean locked = user.getLockedUntil() != null
                && user.getLockedUntil().isAfter(Instant.now());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .disabled(!active)
                .accountLocked(locked)
                .build();
    }
}
