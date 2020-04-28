package com.example.hbhims.model.entity;

import java.io.Serializable;

/**
 * 请求路径(SysRequestPath)实体类
 */
public class SysRequestPath implements Serializable {
    private static final long serialVersionUID = -38398465698914714L;
    //主键id
    private Long id;
    //请求路径
    private String url;
    //路径描述
    private String description;

    public SysRequestPath() {
    }

    public SysRequestPath(String url, String description) {
        this.url = url;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}