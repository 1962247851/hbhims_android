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

public class CustomMedicalSuggestionRequest {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 请求者的id
     */
    private Long userId;
    /**
     * 请求者的用户名
     */
    private String userUsername;
    /**
     * 被被请求者的id
     */
    private Long professionalId;
    /**
     * 请求者的用户名
     */
    private String professionalUsername;
    /**
     * 图片url
     */
    private String healthDataImageUrl;
    /**
     * 时间
     */
    private Long time;

    public CustomMedicalSuggestionRequest() {
    }

    public CustomMedicalSuggestionRequest(Long id, Long userId, String userUsername, Long professionalId, String professionalUsername, String healthDataImageUrl, Long time) {
        this.id = id;
        this.userId = userId;
        this.userUsername = userUsername;
        this.professionalId = professionalId;
        this.professionalUsername = professionalUsername;
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

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public String getProfessionalUsername() {
        return professionalUsername;
    }

    public void setProfessionalUsername(String professionalUsername) {
        this.professionalUsername = professionalUsername;
    }

    @NonNull
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public static void querySend(@NotNull Long userId, @NotNull RequestCallBack<List<CustomMedicalSuggestionRequest>> resultRequestCallBack) {
        if (XNetworkUtils.isAvailable()) {
            HashMap<String, Object> params = new HashMap<>(1);
            params.put("userId", userId);
            Http.obtain().get(Constant.MEDICAL_SUGGESTION_REQUEST_QUERY_SEND, params, new HttpCallBack<JsonResult<JSONArray>>() {
                @Override
                public void onSuccess(@NotNull JsonResult<JSONArray> jsonArrayJsonResult) {
                    resultRequestCallBack.onSuccess(jsonArrayJsonResult.getData().toJavaList(CustomMedicalSuggestionRequest.class));
                }

                @Override
                public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                    resultRequestCallBack.onFailed(errorCode, error);
                }
            });
        } else {
            resultRequestCallBack.onNoNetWork();
        }
    }

    public static void queryReceive(@NotNull Long professionalId, @NotNull RequestCallBack<List<CustomMedicalSuggestionRequest>> resultRequestCallBack) {
        if (XNetworkUtils.isAvailable()) {
            HashMap<String, Object> params = new HashMap<>(1);
            params.put("professionalId", professionalId);
            Http.obtain().get(Constant.MEDICAL_SUGGESTION_REQUEST_QUERY_RECEIVE, params, new HttpCallBack<JsonResult<JSONArray>>() {
                @Override
                public void onSuccess(@NotNull JsonResult<JSONArray> jsonArrayJsonResult) {
                    resultRequestCallBack.onSuccess(jsonArrayJsonResult.getData().toJavaList(CustomMedicalSuggestionRequest.class));
                }

                @Override
                public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                    resultRequestCallBack.onFailed(errorCode, error);
                }
            });
        } else {
            resultRequestCallBack.onNoNetWork();
        }
    }

    public String getHealthDataImageUrl() {
        return healthDataImageUrl;
    }

    public void setHealthDataImageUrl(String healthDataImageUrl) {
        this.healthDataImageUrl = healthDataImageUrl;
    }
}
