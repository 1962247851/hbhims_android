package com.example.hbhims.model.entity;

/**
 * 用户角色关联表(SysUserRoleRelation)实体类
 **/
public class SysUserRoleRelation {
    private Long id;
    private Long userId;
    private Long roleId;

    public SysUserRoleRelation() {
    }

    public SysUserRoleRelation(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
