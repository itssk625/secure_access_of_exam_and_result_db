package com.secureaccess.exam_access_control.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_roles")
@IdClass(UserRoleMapping.UserRoleId.class)
public class UserRoleMapping {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "role_id")
    private Long roleId;

    public UserRoleMapping() {
    }

    public Long getUserId() {
        return userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public static class UserRoleId implements Serializable {
        private Long userId;
        private Long roleId;

        public UserRoleId() {
        }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public Long getRoleId() { return roleId; }
        public void setRoleId(Long roleId) { this.roleId = roleId; }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof UserRoleId that)) return false;
            return Objects.equals(userId, that.userId)
                && Objects.equals(roleId, that.roleId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, roleId);
        }
    }
}