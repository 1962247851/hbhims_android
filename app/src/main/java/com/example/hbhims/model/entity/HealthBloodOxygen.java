package com.example.hbhims.model.entity;

/**
 * 血氧表(HealthBloodOxygen)实体类
 *
 * @author qq1962247851
 * @date 2020/4/20 11:10
 */
public class HealthBloodOxygen {
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
    private Integer measureData;
    /**
     * 测量时间
     */
    private Long measureTime;
    /**
     * 测量设备
     */
    private String measureDevice;

    public HealthBloodOxygen() {
    }

    public HealthBloodOxygen(Long id, Long userId, Integer measureData, Long measureTime, String measureDevice) {
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

    public Integer getMeasureData() {
        return measureData;
    }

    public void setMeasureData(Integer measureData) {
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
