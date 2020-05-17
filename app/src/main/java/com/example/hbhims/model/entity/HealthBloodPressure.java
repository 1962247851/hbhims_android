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

    @NonNull
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    /**
     * 根据userId查询最新的血压
     *
     * @param userId   userId
     * @param callBack 回调
     */
    public static void queryLatestByUserId(@NotNull Long userId, @NotNull HttpCallBack<HealthBloodPressure> callBack) {
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("userId", userId);
        params.put("isLatest", true);
        Http.obtain().get(Constant.BLOOD_PRESSURE_QUERY, params, new HttpCallBack<JsonResult<JSONObject>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONObject> jsonObjectJsonResult) {
                if (jsonObjectJsonResult.getData() != null) {
                    callBack.onSuccess(jsonObjectJsonResult.getData().toJavaObject(HealthBloodPressure.class));
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
     * 根据userId查询所有血压
     *
     * @param userId   userId
     * @param callBack 回调
     */
    public static void queryAllByUserId(@NotNull Long userId, @NotNull HttpCallBack<List<HealthBloodPressure>> callBack) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("userId", userId);
        Http.obtain().get(Constant.BLOOD_PRESSURE_QUERY, params, new HttpCallBack<JsonResult<JSONArray>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONArray> jsonArrayJsonResult) {
                callBack.onSuccess(jsonArrayJsonResult.getData().toJavaList(HealthBloodPressure.class));
            }

            @Override
            public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                callBack.onFailed(errorCode, error);
            }
        });
    }

    /**
     * 记录血压
     *
     * @param healthBloodPressure 血压实体类
     * @param callBack            回调
     */
    public static void insert(@NotNull HealthBloodPressure healthBloodPressure, @NotNull HttpCallBack<HealthBloodPressure> callBack) {
        Http.obtain().put(Constant.BLOOD_PRESSURE_INSERT, healthBloodPressure.toString(), new HttpCallBack<JsonResult<JSONObject>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONObject> stringJsonResult) {
                callBack.onSuccess(stringJsonResult.getData().toJavaObject(HealthBloodPressure.class));
            }

            @Override
            public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                callBack.onFailed(errorCode, error);
            }
        });
    }
}
