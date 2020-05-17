package com.example.hbhims.model.entity;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
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
 * 医疗意见和建议表(MedicalSuggestion)实体类
 *
 * @author qq1962247851
 * @date 2020/4/25 17:20
 */
public class MedicalSuggestion {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 专业人员id
     **/
    private Long professionalId;
    /**
     * 用户id
     **/
    private Long userId;
    /**
     * 时间
     */
    private Long time;
    /**
     * 建议内容
     */
    private String content;

    public MedicalSuggestion() {
    }

    public MedicalSuggestion(Long id, Long professionalId, Long userId, Long time, String content) {
        this.id = id;
        this.professionalId = professionalId;
        this.userId = userId;
        this.time = time;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(Long professionalId) {
        this.professionalId = professionalId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @NonNull
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    /**
     * 根据userId分页查询收到的医疗意见和建议
     *
     * @param userId   userId
     * @param page     page
     * @param size     size
     * @param callBack callback
     */
    public static void queryAllByUserIdAndPage(@NotNull Long userId, @NotNull Integer page, @NotNull Integer size, HttpCallBack<List<CustomMedicalSuggestion>> callBack) {
        HashMap<String, Object> params = new HashMap<>(3);
        params.put("userId", userId);
        params.put("page", page);
        params.put("size", size);
        Http.obtain().get(Constant.MEDICAL_SUGGESTION_QUERY_RECEIVE, params, new HttpCallBack<JsonResult<JSONArray>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONArray> jsonArrayJsonResult) {
                callBack.onSuccess(jsonArrayJsonResult.getData().toJavaList(CustomMedicalSuggestion.class));
            }

            @Override
            public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                callBack.onFailed(errorCode, error);
            }
        });
    }

    /**
     * 根据userId分页查询发送的医疗意见和建议
     *
     * @param professionalId professionalId
     * @param page           page
     * @param size           size
     * @param callBack       callback
     */
    public static void queryAllByProfessionalIdAndPage(@NotNull Long professionalId, @NotNull Integer page, @NotNull Integer size, HttpCallBack<List<CustomMedicalSuggestion>> callBack) {
        HashMap<String, Object> params = new HashMap<>(3);
        params.put("professionalId", professionalId);
        params.put("page", page);
        params.put("size", size);
        Http.obtain().get(Constant.MEDICAL_SUGGESTION_QUERY_SEND, params, new HttpCallBack<JsonResult<JSONArray>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONArray> jsonArrayJsonResult) {
                callBack.onSuccess(jsonArrayJsonResult.getData().toJavaList(CustomMedicalSuggestion.class));
            }

            @Override
            public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                callBack.onFailed(errorCode, error);
            }
        });
    }

    public static void insert(@NotNull MedicalSuggestion medicalSuggestion, @NotNull RequestCallBack<MedicalSuggestion> requestCallBack) {
        if (XNetworkUtils.isAvailable()) {
            Http.obtain().put(Constant.MEDICAL_SUGGESTION_INSERT, medicalSuggestion.toString(), new HttpCallBack<JsonResult<JSONObject>>() {
                @Override
                public void onSuccess(@NotNull JsonResult<JSONObject> jsonObjectJsonResult) {
                    requestCallBack.onSuccess(jsonObjectJsonResult.getData().toJavaObject(MedicalSuggestion.class));
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
