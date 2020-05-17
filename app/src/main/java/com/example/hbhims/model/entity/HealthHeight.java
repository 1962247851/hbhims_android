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

    @NonNull
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    /**
     * 更新身高
     *
     * @param healthHeight 身高实体类
     * @param callBack     回调
     */
    public static void insertOrReplace(@NotNull HealthHeight healthHeight, @NotNull HttpCallBack<HealthHeight> callBack) {
        Http.obtain().put(Constant.HEIGHT_INSERT_OR_REPLACE, healthHeight.toString(), new HttpCallBack<JsonResult<JSONObject>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONObject> stringJsonResult) {
                callBack.onSuccess(stringJsonResult.getData().toJavaObject(HealthHeight.class));
            }

            @Override
            public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                callBack.onFailed(errorCode, error);
            }
        });
    }

    /**
     * 根据userId查询最新的身高
     *
     * @param userId   userId
     * @param callBack 回调
     */
    public static void queryByUserId(@NotNull Long userId, @NotNull HttpCallBack<HealthHeight> callBack) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("userId", userId);
        Http.obtain().get(Constant.HEIGHT_QUERY, params, new HttpCallBack<JsonResult<JSONObject>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONObject> jsonObjectJsonResult) {
                if (jsonObjectJsonResult.getData() != null) {
                    callBack.onSuccess(jsonObjectJsonResult.getData().toJavaObject(HealthHeight.class));
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

}
