package com.example.hbhims.model.entity;

/**
 * 角色表(SysRole)实体类
 **/
public class SysRole {
    private Long id;
    //角色code
    private String code;
    //角色名
    private String name;
    //角色说明
    private String description;

    public SysRole() {
    }

    public SysRole(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
