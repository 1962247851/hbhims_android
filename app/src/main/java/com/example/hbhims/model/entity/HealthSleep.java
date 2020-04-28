package com.example.hbhims.model.entity;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.hbhims.model.common.Constant;
import com.example.hbhims.model.common.entity.JsonResult;
import com.example.hbhims.model.common.util.http.Http;
import com.example.hbhims.model.common.util.http.HttpCallBack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 睡眠质量表(HealthSleep)实体类
 *
 * @author qq1962247851
 * @date 2020/4/20 10:48
 */
public class HealthSleep {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 用户id
     **/
    private Long userId;
    /**
     * 上床时间
     */
    private Long startTime;
    /**
     * 起床时间
     */
    private Long endTime;
    /**
     * 总时间
     */
    private Long totalTime;
    /**
     * 日期
     */
    private Long date;
    /**
     * 测量设备
     */
    private String measureDevice;

    public HealthSleep() {
    }

    public HealthSleep(Long id, Long userId, Long startTime, Long endTime, Long totalTime, Long date, String measureDevice) {
        this.id = id;
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalTime = totalTime;
        this.date = date;
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

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    public String getMeasureDevice() {
        return measureDevice;
    }

    public void setMeasureDevice(String measureDevice) {
        this.measureDevice = measureDevice;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    @NonNull
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    /**
     * 查询用户Id的所有睡眠质量
     *
     * @param userId   userId
     * @param callBack 回调
     */
    public static void queryAllByUserId(@NotNull Long userId, @NotNull HttpCallBack<List<HealthSleep>> callBack) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        Http.obtain().get(Constant.SLEEP_QUERY, params, new HttpCallBack<JsonResult<JSONArray>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONArray> stringJsonResult) {
                callBack.onSuccess(stringJsonResult.getData().toJavaList(HealthSleep.class));
            }

            @Override
            public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                callBack.onFailed(errorCode, error);
            }
        });
    }

    /**
     * 查询用户Id的所有昨晚睡眠质量或指定日期的睡眠质量
     *
     * @param userId   userId
     * @param date     日期
     * @param callBack 回调
     */
    public static void queryByUserIdAndDate(@NotNull Long userId, @Nullable Long date, @NotNull HttpCallBack<HealthSleep> callBack) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        if (date != null) {
            params.put("date", date);
        }
        Http.obtain().get(Constant.SLEEP_QUERY_BY_DATE, params, new HttpCallBack<JsonResult<JSONObject>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONObject> jsonObjectJsonResult) {
                callBack.onSuccess(jsonObjectJsonResult.getData().toJavaObject(HealthSleep.class));
            }

            @Override
            public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                callBack.onFailed(errorCode, error);
            }
        });
    }

    /**
     * 新增或更新一条睡眠质量
     *
     * @param healthSleep 睡眠质量
     * @param callBack    回调
     */
    public static void insertOrReplace(@NotNull HealthSleep healthSleep, @NotNull HttpCallBack<HealthSleep> callBack) {
        Http.obtain().post(Constant.SLEEP_INSERT_OR_REPLACE, healthSleep.toString(), new HttpCallBack<JsonResult<JSONObject>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONObject> stringJsonResult) {
                callBack.onSuccess(stringJsonResult.getData().toJavaObject(HealthSleep.class));
            }

            @Override
            public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                callBack.onFailed(errorCode, error);
            }
        });
    }

}
