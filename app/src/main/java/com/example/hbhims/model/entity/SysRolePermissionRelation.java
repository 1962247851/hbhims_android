package com.example.hbhims.model.entity;

/**
 * 角色权限关联表(SysRolePermissionRelation)实体类
 **/
public class SysRolePermissionRelation {
    private Long id;
    //角色id
    private Long roleId;
    //权限id
    private Long permissionId;

    public SysRolePermissionRelation() {
    }

    public SysRolePermissionRelation(Long roleId, Long permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }
}
