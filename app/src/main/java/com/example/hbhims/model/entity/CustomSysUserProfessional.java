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

import java.util.List;

/**
 * 用于查询所有专业人员
 *
 * @author qq1962247851
 * @date 2020/5/10 14:24
 */
public class CustomSysUserProfessional {
    private Long id;
    private String username;
    private Double meanEvaluationScore;
    private Integer totalSuggestionCount;

    public CustomSysUserProfessional() {
    }

    public CustomSysUserProfessional(Long id, String username, Double meanEvaluationScore, Integer totalSuggestionCount) {
        this.id = id;
        this.username = username;
        this.meanEvaluationScore = meanEvaluationScore;
        this.totalSuggestionCount = totalSuggestionCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getMeanEvaluationScore() {
        return meanEvaluationScore;
    }

    public void setMeanEvaluationScore(Double meanEvaluationScore) {
        this.meanEvaluationScore = meanEvaluationScore;
    }

    public Integer getTotalSuggestionCount() {
        return totalSuggestionCount;
    }

    public void setTotalSuggestionCount(Integer totalSuggestionCount) {
        this.totalSuggestionCount = totalSuggestionCount;
    }

    @NonNull
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public static void queryAll(RequestCallBack<List<CustomSysUserProfessional>> requestCallBack) {
        if (XNetworkUtils.isAvailable()) {
            Http.obtain().get(Constant.USER_QUERY_ALL_PROFESSIONAL, null, new HttpCallBack<JsonResult<JSONArray>>() {
                @Override
                public void onSuccess(@NotNull JsonResult<JSONArray> jsonArrayJsonResult) {
                    requestCallBack.onSuccess(jsonArrayJsonResult.getData().toJavaList(CustomSysUserProfessional.class));
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
