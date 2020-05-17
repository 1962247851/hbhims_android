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

import java.util.HashMap;

/**
 * 医疗意见和建议请求表(MedicalSuggestionRequest)实体类
 *
 * @author qq1962247851
 * @date 2020/5/10 12:53
 */
public class MedicalSuggestionRequest {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 请求者的id
     */
    private Long userId;
    /**
     * 被请求者的id
     */
    private Long professionalId;
    /**
     * 图片url
     */
    private String healthDataImageUrl;
    /**
     * 时间
     */
    private Long time;

    public MedicalSuggestionRequest() {
    }

    public MedicalSuggestionRequest(Long id, Long userId, Long professionalId, String healthDataImageUrl, Long time) {
        this.id = id;
        this.userId = userId;
        this.professionalId = professionalId;
        this.healthDataImageUrl = healthDataImageUrl;
        this.time = time;
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

    public Long getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(Long professionalId) {
        this.professionalId = professionalId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getHealthDataImageUrl() {
        return healthDataImageUrl;
    }

    public void setHealthDataImageUrl(String healthDataImageUrl) {
        this.healthDataImageUrl = healthDataImageUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public static void insert(@NotNull MedicalSuggestionRequest medicalSuggestionRequest, @NotNull RequestCallBack<Boolean> requestCallBack) {
        if (XNetworkUtils.isAvailable()) {
            Http.obtain().put(Constant.MEDICAL_SUGGESTION_REQUEST_INSERT, medicalSuggestionRequest.toString(), new HttpCallBack<JsonResult<JSONObject>>() {

                @Override
                public void onSuccess(@NotNull JsonResult<JSONObject> jsonObjectJsonResult) {
                    requestCallBack.onSuccess(true);
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

    public static void delete(@NotNull Long id, @NotNull Integer type, @NotNull RequestCallBack<Boolean> requestCallBack) {
        if (XNetworkUtils.isAvailable()) {
            HashMap<String, Object> params = new HashMap<>(2);
            params.put("id", id);
            params.put("type", type);
            Http.obtain().delete(Constant.MEDICAL_SUGGESTION_REQUEST_DELETE, params, new HttpCallBack<JsonResult<JSONObject>>() {
                @Override
                public void onSuccess(@NotNull JsonResult<JSONObject> jsonObjectJsonResult) {
                    requestCallBack.onSuccess(true);
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
