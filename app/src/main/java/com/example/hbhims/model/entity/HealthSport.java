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
 * 运动记录表(HealthSport)实体类
 *
 * @author qq1962247851
 * @date 2020/4/20 10:45
 */
public class HealthSport {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 用户id
     **/
    private Long userId;
    /**
     * 运动日期
     */
    private Long date;
    /**
     * 步数
     */
    private Integer stepValue;
    /**
     * 距离
     */
    private Float distance;
    /**
     * 能量消耗
     */
    private Float calorie;

    public HealthSport() {
    }

    public HealthSport(Long id, Long userId, Long date, Integer stepValue, Float distance, Float calorie) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.stepValue = stepValue;
        this.distance = distance;
        this.calorie = calorie;
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

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Integer getStepValue() {
        return stepValue;
    }

    public void setStepValue(Integer stepValue) {
        this.stepValue = stepValue;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Float getCalorie() {
        return calorie;
    }

    public void setCalorie(Float calorie) {
        this.calorie = calorie;
    }

    /**
     * 查询用户Id的所有运动记录
     *
     * @param userId   userId
     * @param callBack 回调
     */
    public static void queryAllByUserId(@NotNull Long userId, @NotNull HttpCallBack<List<HealthSport>> callBack) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        Http.obtain().get(Constant.SPORT_QUERY, params, new HttpCallBack<JsonResult<JSONArray>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONArray> stringJsonResult) {
                callBack.onSuccess(stringJsonResult.getData().toJavaList(HealthSport.class));
            }

            @Override
            public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                callBack.onFailed(errorCode, error);
            }
        });
    }

    /**
         * 查询用户Id的所有今日运动记录或者指定时间段内的运动记录
     *
     * @param userId   userId
     * @param callBack 回调
     */
    public static void queryAllByUserIdBetween(@NotNull Long userId, @Nullable Long start, @Nullable Long end, @NotNull HttpCallBack<List<HealthSport>> callBack) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        if (start != null) {
            params.put("start", start);
        }
        if (end != null) {
            params.put("end", end);
        }
        Http.obtain().get(Constant.SPORT_QUERY_BETWEEN, params, new HttpCallBack<JsonResult<JSONArray>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONArray> stringJsonResult) {
                callBack.onSuccess(stringJsonResult.getData().toJavaList(HealthSport.class));
            }

            @Override
            public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                callBack.onFailed(errorCode, error);
            }
        });
    }

    /**
     * 新增一条运动记录
     *
     * @param healthSport 运动记录
     * @param callBack    回调
     */
    public static void insert(@NotNull HealthSport healthSport, @NotNull HttpCallBack<HealthSport> callBack) {
        Http.obtain().put(Constant.SPORT_INSERT, healthSport.toString(), new HttpCallBack<JsonResult<JSONObject>>() {
            @Override
            public void onSuccess(@NotNull JsonResult<JSONObject> stringJsonResult) {
                callBack.onSuccess(stringJsonResult.getData().toJavaObject(HealthSport.class));
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
