package com.example.hbhims.model.entity;

import java.io.Serializable;

/**
 * 路径权限关联表(SysRequestPathPermissionRelation)实体类
 */
public class SysRequestPathPermissionRelation implements Serializable {
    private static final long serialVersionUID = -59197738311147860L;
    //主键id
    private Long id;
    //请求路径id
    private Long requestPathId;
    //权限id
    private Long permissionId;

    public SysRequestPathPermissionRelation() {
    }

    public SysRequestPathPermissionRelation(Long requestPathId, Long permissionId) {
        this.requestPathId = requestPathId;
        this.permissionId = permissionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequestPathId() {
        return requestPathId;
    }

    public void setRequestPathId(Long requestPathId) {
        this.requestPathId = requestPathId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

}