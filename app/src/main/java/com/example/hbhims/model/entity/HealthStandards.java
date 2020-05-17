package com.example.hbhims.model.entity;


import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;

/**
 * 健康指标表(HealthStandards)实体类
 *
 * @author qq1962247851
 * @date 2020/5/1 18:58
 */
public class HealthStandards {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 步数健康范围（步）
     * 6000,8000
     */
    private String stepRange;
    /**
     * 睡眠时长健康范围（Long）
     * 2000000,8000000
     */
    private String sleepRange;
    /**
     * 血压健康范围（mmHg）
     * 80,120
     */
    private String bloodPressureRange;
    /**
     * 血糖健康范围(mmol/L)
     * 3.0,4.0
     */
    private String bloodSugarRange;
    /**
     * 血氧健康范围（%）
     * 80,90
     */
    private String bloodOxygenRange;

    public HealthStandards() {
    }

    public HealthStandards(Long id, String stepRange, String sleepRange, String bloodPressureRange, String bloodSugarRange, String bloodOxygenRange) {
        this.id = id;
        this.stepRange = stepRange;
        this.sleepRange = sleepRange;
        this.bloodPressureRange = bloodPressureRange;
        this.bloodSugarRange = bloodSugarRange;
        this.bloodOxygenRange = bloodOxygenRange;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStepRange() {
        return stepRange;
    }

    public void setStepRange(String stepRange) {
        this.stepRange = stepRange;
    }

    public String getSleepRange() {
        return sleepRange;
    }

    public void setSleepRange(String sleepRange) {
        this.sleepRange = sleepRange;
    }

    public String getBloodPressureRange() {
        return bloodPressureRange;
    }

    public void setBloodPressureRange(String bloodPressureRange) {
        this.bloodPressureRange = bloodPressureRange;
    }

    public String getBloodSugarRange() {
        return bloodSugarRange;
    }

    public void setBloodSugarRange(String bloodSugarRange) {
        this.bloodSugarRange = bloodSugarRange;
    }

    public String getBloodOxygenRange() {
        return bloodOxygenRange;
    }

    public void setBloodOxygenRange(String bloodOxygenRange) {
        this.bloodOxygenRange = bloodOxygenRange;
    }

    @NonNull
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
