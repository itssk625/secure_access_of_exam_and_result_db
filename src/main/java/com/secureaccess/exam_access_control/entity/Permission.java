
package com.secureaccess.exam_access_control.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "permissions")
public class Permission {

    @Id
    @Column(name = "permission_id")
    private Long permissionId;

    @Column(name = "permission_name", nullable = false)
    private String permissionName;

    public Permission() {
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }
}
