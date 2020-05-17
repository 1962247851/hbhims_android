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

    @NonNull
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    /**
     * 查询所有角色
     *
     * @param callBack 回调
     */
    public static void queryAllRole(HttpCallBack<List<SysRole>> callBack) {
        Http.obtain().get(Constant.ROLE_QUERY, null, new HttpCallBack<JsonResult<JSONArray>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONArray> jsonArrayJsonResult) {
                callBack.onSuccess(jsonArrayJsonResult.getData().toJavaList(SysRole.class));
            }

            @Override
            public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                callBack.onFailed(errorCode, error);
            }
        });
    }

    /**
     * 根据id查询角色
     *
     * @param requestCallBack 回调
     */
    public static void queryById(Long id, RequestCallBack<SysRole> requestCallBack) {
        if (XNetworkUtils.isAvailable()) {
            HashMap<String, Object> params = new HashMap<>(1);
            params.put("id", id);
            Http.obtain().get(Constant.ROLE_QUERY, params, new HttpCallBack<JsonResult<JSONObject>>() {
                @Override
                public void onSuccess(@NotNull JsonResult<JSONObject> jsonObjectJsonResult) {
                    if (jsonObjectJsonResult.getData() != null) {
                        requestCallBack.onSuccess(jsonObjectJsonResult.getData().toJavaObject(SysRole.class));
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
}
