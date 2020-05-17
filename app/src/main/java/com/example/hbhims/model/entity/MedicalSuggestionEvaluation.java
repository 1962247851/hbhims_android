package com.example.hbhims.model.entity;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.hbhims.model.common.Constant;
import com.example.hbhims.model.common.entity.JsonResult;
import com.example.hbhims.model.common.util.http.Http;
import com.example.hbhims.model.common.util.http.HttpCallBack;

import org.jetbrains.annotations.NotNull;

/**
 * 医疗意见和建议评价表(MedicalSuggestionEvaluation)实体类
 *
 * @author qq1962247851
 * @date 2020/4/25 17:20
 */
public class MedicalSuggestionEvaluation {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 建议id
     **/
    private Long suggestionId;
    /**
     * 评价分数0-5
     **/
    private Integer evaluationScore;
    /**
     * 时间
     */
    private Long time;

    public MedicalSuggestionEvaluation() {
    }

    public MedicalSuggestionEvaluation(Long id, Long suggestionId, Integer evaluationScore, Long time) {
        this.id = id;
        this.suggestionId = suggestionId;
        this.evaluationScore = evaluationScore;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSuggestionId() {
        return suggestionId;
    }

    public void setSuggestionId(Long suggestionId) {
        this.suggestionId = suggestionId;
    }

    public Integer getEvaluationScore() {
        return evaluationScore;
    }

    public void setEvaluationScore(Integer evaluationScore) {
        this.evaluationScore = evaluationScore;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @NonNull
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static void insert(@NotNull MedicalSuggestionEvaluation medicalSuggestionEvaluation, @NotNull HttpCallBack<MedicalSuggestionEvaluation> callBack) {
        Http.obtain().put(Constant.MEDICAL_SUGGESTION_EVALUATION_INSERT, medicalSuggestionEvaluation.toString(), new HttpCallBack<JsonResult<JSONObject>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONObject> jsonObjectJsonResult) {
                callBack.onSuccess(jsonObjectJsonResult.getData().toJavaObject(MedicalSuggestionEvaluation.class));
            }

            @Override
            public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                callBack.onFailed(errorCode, error);
            }
        });
    }
}
