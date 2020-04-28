package com.example.hbhims.model.entity;

import java.io.Serializable;

/**
 * 权限表(SysPermission)实体类
 */
public class SysPermission implements Serializable {
    private static final long serialVersionUID = -71969734644822184L;
    //主键id
    private Long id;
    //权限code
    private String code;
    //权限名
    private String name;

    public SysPermission() {
    }

    public SysPermission(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}