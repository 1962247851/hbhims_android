package com.example.hbhims.model.entity;

/**
 * 血压表(HealthBloodPressure)实体类
 *
 * @author qq1962247851
 * @date 2020/4/20 10:54
 */
public class HealthBloodPressure {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 用户id
     **/
    private Long userId;
    /**
     * 高压
     */
    private Integer highPressure;
    /**
     * 低压
     */
    private Integer lowPressure;
    /**
     * 测量时间
     */
    private Long measureTime;
    /**
     * 测量设备
     */
    private String measureDevice;

    public HealthBloodPressure() {
    }

    public HealthBloodPressure(Long id, Long userId, Integer highPressure, Integer lowPressure, Long measureTime, String measureDevice) {
        this.id = id;
        this.userId = userId;
        this.highPressure = highPressure;
        this.lowPressure = lowPressure;
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

    public Integer getHighPressure() {
        return highPressure;
    }

    public void setHighPressure(Integer highPressure) {
        this.highPressure = highPressure;
    }

    public Integer getLowPressure() {
        return lowPressure;
    }

    public void setLowPressure(Integer lowPressure) {
        this.lowPressure = lowPressure;
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
