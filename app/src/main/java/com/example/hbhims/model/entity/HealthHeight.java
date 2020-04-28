package com.example.hbhims.model.entity;

/**
 * 身高表(HealthHeight)实体类
 *
 * @author qq1962247851
 * @date 2020/4/20 10:36
 */
public class HealthHeight {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 用户id
     **/
    private Long userId;
    /**
     * 测量身高值
     */
    private Float heightData;
    /**
     * 测量时间
     */
    private Long measureTime;
    /**
     * 测量设备
     */
    private String measureDevice;

    public HealthHeight() {
    }

    public HealthHeight(Long id, Long userId, Float heightData, Long measureTime, String measureDevice) {
        this.id = id;
        this.userId = userId;
        this.heightData = heightData;
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

    public Float getHeightData() {
        return heightData;
    }

    public void setHeightData(Float heightData) {
        this.heightData = heightData;
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
