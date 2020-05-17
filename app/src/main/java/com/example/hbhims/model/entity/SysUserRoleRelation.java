package com.example.hbhims.model.entity;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.hbhims.model.common.Constant;
import com.example.hbhims.model.common.entity.JsonResult;
import com.example.hbhims.model.common.util.http.Http;
import com.example.hbhims.model.common.util.http.HttpCallBack;
import com.example.hbhims.model.common.util.http.RequestCallBack;
import com.youth.xframe.utils.XNetworkUtils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

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

    @NonNull
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public static void queryByUserIdAndRoleId(@NotNull Long userId, @NotNull Long roleId, @NotNull RequestCallBack<SysUserRoleRelation> requestCallBack) {
        if (XNetworkUtils.isAvailable()) {
            HashMap<String, Object> params = new HashMap<>(2);
            params.put("userId", userId);
            params.put("roleId", roleId);
            Http.obtain().get(Constant.USER_ROLE_RELATION_QUERY_BY_USER_ID_AND_ROLE_ID, params, new HttpCallBack<JsonResult<JSONObject>>() {
                @Override
                public void onSuccess(@NotNull JsonResult<JSONObject> jsonObjectJsonResult) {
                    if (jsonObjectJsonResult.getData() != null) {
                        requestCallBack.onSuccess(jsonObjectJsonResult.getData().toJavaObject(SysUserRoleRelation.class));
                    } else {
                        requestCallBack.onFailed(jsonObjectJsonResult.getErrorCode(), "数据为空");
                    }
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

    public static void queryAllByUserId(@NotNull Long userId, @NotNull RequestCallBack<List<SysUserRoleRelation>> requestCallBack) {
        if (XNetworkUtils.isAvailable()) {
            HashMap<String, Object> params = new HashMap<>(1);
            params.put("userId", userId);
            Http.obtain().get(Constant.USER_ROLE_RELATION_QUERY_BY_USER_ID_AND_ROLE_ID, params, new HttpCallBack<JsonResult<JSONArray>>() {
                @Override
                public void onSuccess(@NotNull JsonResult<JSONArray> jsonArrayJsonResult) {
                    requestCallBack.onSuccess(jsonArrayJsonResult.getData().toJavaList(SysUserRoleRelation.class));
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

    public static void insert(@NotNull Long roleId, @NotNull Long userId, @NotNull HttpCallBack<Boolean> callBack) {
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("roleId", roleId);
        params.put("userId", userId);
        Http.obtain().put(Constant.USER_ROLE_RELATION_INSERT, params, new HttpCallBack<JsonResult<JSONObject>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONObject> jsonObjectJsonResult) {
                callBack.onSuccess(true);
            }

            @Override
            public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                callBack.onFailed(errorCode, error);
            }
        });
    }
}
