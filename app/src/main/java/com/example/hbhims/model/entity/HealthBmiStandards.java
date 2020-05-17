package com.example.hbhims.model.entity;


import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;

/**
 * BMI健康指标表(HealthBMIStandards)实体类
 *
 * @author qq1962247851
 * @date 2020/5/1 18:58
 */
public class HealthBmiStandards {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 性别（男1，女0）
     */
    private Boolean sex;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * BMI健康范围（mmHg）
     * 18.5,24,27
     */
    private String bmiRange;

    public HealthBmiStandards() {
    }

    public HealthBmiStandards(Long id, Boolean sex, Integer age, String bmiRange) {
        this.id = id;
        this.sex = sex;
        this.age = age;
        this.bmiRange = bmiRange;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBmiRange() {
        return bmiRange;
    }

    public void setBmiRange(String bmiRange) {
        this.bmiRange = bmiRange;
    }

    @NonNull
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
