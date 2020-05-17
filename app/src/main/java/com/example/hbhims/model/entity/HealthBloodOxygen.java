package com.example.hbhims.model.entity;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.hbhims.model.common.Constant;
import com.example.hbhims.model.common.entity.JsonResult;
import com.example.hbhims.model.common.util.http.Http;
import com.example.hbhims.model.common.util.http.HttpCallBack;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

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

    @NonNull
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    /**
     * 根据userId查询最新的血氧
     *
     * @param userId   userId
     * @param callBack 回调
     */
    public static void queryLatestByUserId(@NotNull Long userId, @NotNull HttpCallBack<HealthBloodOxygen> callBack) {
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("userId", userId);
        params.put("isLatest", true);
        Http.obtain().get(Constant.BLOOD_OXYGEN_QUERY, params, new HttpCallBack<JsonResult<JSONObject>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONObject> jsonObjectJsonResult) {
                if (jsonObjectJsonResult.getData() != null) {
                    callBack.onSuccess(jsonObjectJsonResult.getData().toJavaObject(HealthBloodOxygen.class));
                } else {
                    callBack.onFailed(jsonObjectJsonResult.getErrorCode(), "数据为空");
                }
            }

            @Override
            public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                callBack.onFailed(errorCode, error);
            }
        });
    }

    /**
     * 根据userId查询所有血氧
     *
     * @param userId   userId
     * @param callBack 回调
     */
    public static void queryAllByUserId(@NotNull Long userId, @NotNull HttpCallBack<List<HealthBloodOxygen>> callBack) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("userId", userId);
        Http.obtain().get(Constant.BLOOD_OXYGEN_QUERY, params, new HttpCallBack<JsonResult<JSONArray>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONArray> jsonArrayJsonResult) {
                callBack.onSuccess(jsonArrayJsonResult.getData().toJavaList(HealthBloodOxygen.class));
            }

            @Override
            public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                callBack.onFailed(errorCode, error);
            }
        });
    }

    /**
     * 记录血氧
     *
     * @param healthBloodOxygen 血氧实体类
     * @param callBack          回调
     */
    public static void insert(@NotNull HealthBloodOxygen healthBloodOxygen, @NotNull HttpCallBack<HealthBloodOxygen> callBack) {
        Http.obtain().put(Constant.BLOOD_OXYGEN_INSERT, healthBloodOxygen.toString(), new HttpCallBack<JsonResult<JSONObject>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONObject> stringJsonResult) {
                callBack.onSuccess(stringJsonResult.getData().toJavaObject(HealthBloodOxygen.class));
            }

            @Override
            public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                callBack.onFailed(errorCode, error);
            }
        });
    }
}
