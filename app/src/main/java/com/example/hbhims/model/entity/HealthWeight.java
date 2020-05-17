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
 * 体重表(HealthWeight)实体类
 *
 * @author qq1962247851
 * @date 2020/4/20 10:43
 */
public class HealthWeight {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 用户id
     **/
    private Long userId;
    /**
     * 测量体重值
     */
    private Float weightData;
    /**
     * 测量时间
     */
    private Long measureTime;
    /**
     * 测量设备
     */
    private String measureDevice;

    public HealthWeight() {
    }

    public HealthWeight(Long id, Long userId, Float weightData, Long measureTime, String measureDevice) {
        this.id = id;
        this.userId = userId;
        this.weightData = weightData;
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

    public Float getWeightData() {
        return weightData;
    }

    public void setWeightData(Float weightData) {
        this.weightData = weightData;
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

    /**
     * 记录体重
     *
     * @param healthWeight 体重实体类
     * @param callBack     回调
     */
    public static void insert(@NotNull HealthWeight healthWeight, @NotNull HttpCallBack<HealthWeight> callBack) {
        Http.obtain().put(Constant.WEIGHT_INSERT, healthWeight.toString(), new HttpCallBack<JsonResult<JSONObject>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONObject> stringJsonResult) {
                callBack.onSuccess(stringJsonResult.getData().toJavaObject(HealthWeight.class));
            }

            @Override
            public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                callBack.onFailed(errorCode, error);
            }
        });
    }

    /**
     * 根据userId查询所有体重
     *
     * @param userId   userId
     * @param callBack 回调
     */
    public static void queryAllByUserId(@NotNull Long userId, @NotNull HttpCallBack<List<HealthWeight>> callBack) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("userId", userId);
        Http.obtain().get(Constant.WEIGHT_QUERY, params, new HttpCallBack<JsonResult<JSONArray>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONArray> jsonArrayJsonResult) {
                callBack.onSuccess(jsonArrayJsonResult.getData().toJavaList(HealthWeight.class));
            }

            @Override
            public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                callBack.onFailed(errorCode, error);
            }
        });
    }

    /**
     * 根据userId查询最新的体重
     *
     * @param userId   userId
     * @param callBack 回调
     */
    public static void queryLatestByUserId(@NotNull Long userId, @NotNull HttpCallBack<HealthWeight> callBack) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("userId", userId);
        params.put("isLatest", true);
        Http.obtain().get(Constant.WEIGHT_QUERY, params, new HttpCallBack<JsonResult<JSONObject>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONObject> jsonObjectJsonResult) {
                if (jsonObjectJsonResult.getData() != null) {
                    callBack.onSuccess(jsonObjectJsonResult.getData().toJavaObject(HealthWeight.class));
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

    @NonNull
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
