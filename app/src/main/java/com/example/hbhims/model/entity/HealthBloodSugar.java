package com.example.hbhims.model.entity;

/**
 * 血糖表(HealthBloodSugar)实体类
 *
 * @author qq1962247851
 * @date 2020/4/20 11:10
 */
public class HealthBloodSugar {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 用户id
     **/
    private Long userId;
    /**
     * 测量数据
     */
    private Float measureData;
    /**
     * 测量时间
     */
    private Long measureTime;
    /**
     * 测量设备
     */
    private String measureDevice;

    public HealthBloodSugar() {
    }

    public HealthBloodSugar(Long id, Long userId, Float measureData, Long measureTime, String measureDevice) {
        this.id = id;
        this.userId = userId;
        this.measureData = measureData;
        this.measureTime = measureTime;
        this.measureDevice = measureDevice;
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

    public Float getMeasureData() {
        return measureData;
    }

    public void setMeasureData(Float measureData) {
        this.measureData = measureData;
    }

    public Long getMeasureTime() {
        return measureTime;
    }

    public void setMeasureTime(Long measureTime) {
        this.measureTime = measureTime;
    }

    public String getMeasureDevice() {
        return measureDevice;
    }

    public void setMeasureDevice(String measureDevice) {
        this.measureDevice = measureDevice;
    }
}
