package com.example.hbhims.model.entity;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;

public class CustomMedicalSuggestion {

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
    /**
     * 评价分数0-5
     **/
    private Integer evaluationScore;
    /**
     * 评价时间
     */
    private Long evaluationTime;

    public CustomMedicalSuggestion() {
    }

    public CustomMedicalSuggestion(Long id, Long professionalId, Long userId, Long time, String content, Integer evaluationScore, Long evaluationTime) {
        this.id = id;
        this.professionalId = professionalId;
        this.userId = userId;
        this.time = time;
        this.content = content;
        this.evaluationScore = evaluationScore;
        this.evaluationTime = evaluationTime;
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

    public Integer getEvaluationScore() {
        return evaluationScore;
    }

    public void setEvaluationScore(Integer evaluationScore) {
        this.evaluationScore = evaluationScore;
    }

    public Long getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(Long evaluationTime) {
        this.evaluationTime = evaluationTime;
    }

    @NonNull
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
