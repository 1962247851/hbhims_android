package com.example.hbhims.model.entity;


import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.example.hbhims.model.common.Constant;
import com.example.hbhims.model.common.entity.JsonResult;
import com.example.hbhims.model.common.util.http.Http;
import com.example.hbhims.model.common.util.http.HttpCallBack;
import com.example.hbhims.model.common.util.http.RequestCallBack;
import com.youth.xframe.utils.XNetworkUtils;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 用户表(SysUser)实体类
 */
public class SysUser implements Serializable {
    private static final long serialVersionUID = 915478504870211231L;
    private Long id;
    //用户名
    private String userName;
    //性别
    private Boolean sex;
    //出生日期
    private Long birthday;
    //邮箱
    private String email;
    //手机号码
    private String phone;
    //用户密码
    private String password;
    //个人介绍
    private String selfIntroduction;
    //上一次登录时间
    private Long lastLoginTime = System.currentTimeMillis();
    //账号是否可用。默认为1（可用）
    private Boolean enabled;
    //是否过期。默认为1（没有过期）
    private Boolean notExpired;
    //账号是否锁定。默认为1（没有锁定）
    private Boolean notLocked;
    //密码是否过期。默认为1（没有过期）
    private Boolean passwordNotExpired;
    //创建时间
    private Long createTime;
    //修改时间
    private Long updateTime;

    public SysUser() {
    }

    public SysUser(Long id, String userName, Boolean sex, Long birthday, String email, String phone, String password, String selfIntroduction, Long lastLoginTime, Boolean enabled, Boolean notExpired, Boolean notLocked, Boolean passwordNotExpired, Long createTime, Long updateTime) {
        this.id = id;
        this.userName = userName;
        this.sex = sex;
        this.birthday = birthday;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.selfIntroduction = selfIntroduction;
        this.lastLoginTime = lastLoginTime;
        this.enabled = enabled;
        this.notExpired = notExpired;
        this.notLocked = notLocked;
        this.passwordNotExpired = passwordNotExpired;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSelfIntroduction() {
        return selfIntroduction;
    }

    public void setSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }

    public Long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getNotExpired() {
        return notExpired;
    }

    public void setNotExpired(Boolean notExpired) {
        this.notExpired = notExpired;
    }

    public Boolean getNotLocked() {
        return notLocked;
    }

    public void setNotLocked(Boolean notLocked) {
        this.notLocked = notLocked;
    }

    public Boolean getPasswordNotExpired() {
        return passwordNotExpired;
    }

    public void setPasswordNotExpired(Boolean passwordNotExpired) {
        this.passwordNotExpired = passwordNotExpired;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @NonNull
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    /**
     * 根据id查询用户
     *
     * @param id              用户id
     * @param requestCallBack 回调
     */
    public static void query(@NotNull Long id, @NotNull RequestCallBack<SysUser> requestCallBack) {
        if (XNetworkUtils.isAvailable()) {
            HashMap<String, Object> params = new HashMap<>(1);
            params.put("id", id);
            Http.obtain().get(Constant.USER_QUERY, params, new HttpCallBack<JsonResult<JSONObject>>() {

                @Override
                public void onSuccess(@NotNull JsonResult<JSONObject> jsonObjectJsonResult) {
                    requestCallBack.onSuccess(jsonObjectJsonResult.getData().toJavaObject(SysUser.class));
                }

                @Override
                public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                    requestCallBack.onFailed(errorCode, error);
                }
            });
        } else {
            requestCallBack.onNoNetWork();
        }
    }

    public static void insert(@NotNull SysUser user, @NotNull RequestCallBack<SysUser> requestCallBack) {
        if (XNetworkUtils.isAvailable()) {
            Http.obtain().put(Constant.USER_REGISTER, user.toString(), new HttpCallBack<JsonResult<JSONObject>>() {
                @Override
                public void onSuccess(@NotNull JsonResult<JSONObject> jsonObjectJsonResult) {
                    requestCallBack.onSuccess(jsonObjectJsonResult.getData().toJavaObject(SysUser.class));
                }

                @Override
                public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                    requestCallBack.onFailed(errorCode, error);
                }
            });
        } else {
            requestCallBack.onNoNetWork();
        }
    }

    public static void update(@NotNull SysUser user, @NotNull RequestCallBack<SysUser> requestCallBack) {
        if (XNetworkUtils.isAvailable()) {
            Http.obtain().post(Constant.USER_UPDATE, user.toString(), new HttpCallBack<JsonResult<JSONObject>>() {
                @Override
                public void onSuccess(@NotNull JsonResult<JSONObject> jsonObjectJsonResult) {
                    requestCallBack.onSuccess(jsonObjectJsonResult.getData().toJavaObject(SysUser.class));
                }

                @Override
                public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                    requestCallBack.onFailed(errorCode, error);
                }
            });
        } else {
            requestCallBack.onNoNetWork();
        }
    }

}