package com.example.hbhims.model.entity;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.hbhims.model.common.Constant;
import com.example.hbhims.model.common.entity.JsonResult;
import com.example.hbhims.model.common.util.http.Http;
import com.example.hbhims.model.common.util.http.HttpCallBack;
import com.example.hbhims.model.common.util.http.RequestCallBack;
import com.youth.xframe.utils.XNetworkUtils;

import org.jetbrains.annotations.NotNull;

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

    @NonNull
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    /**
     * 查询用户Id的所有运动记录
     *
     * @param userId          userId
     * @param requestCallBack 回调
     */
    public static void queryAllByUserId(@NotNull Long userId, @NotNull RequestCallBack<List<HealthSport>> requestCallBack) {
        if (XNetworkUtils.isAvailable()) {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            Http.obtain().get(Constant.SPORT_QUERY, params, new HttpCallBack<JsonResult<JSONArray>>() {
                @Override
                public void onSuccess(@NotNull JsonResult<JSONArray> stringJsonResult) {
                    requestCallBack.onSuccess(stringJsonResult.getData().toJavaList(HealthSport.class));
                }

                @Override
                public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                    requestCallBack.onFailed(errorCode, error);
                }
            });
        } else {
            requestCallBack.onNoNetWork();
        }
    }

    /**
     * 查询用户Id的今日运动记录或者指定时间的运动记录
     *
     * @param userId          userId
     * @param date            日期
     * @param requestCallBack 回调
     */
    public static void queryAllByUserIdAndDate(@NotNull Long userId, @NotNull Long date, @NotNull RequestCallBack<HealthSport> requestCallBack) {
        if (XNetworkUtils.isAvailable()) {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("date", date);
            Http.obtain().get(Constant.SPORT_QUERY_BY_DATE, params, new HttpCallBack<JsonResult<JSONObject>>() {
                @Override
                public void onSuccess(@NotNull JsonResult<JSONObject> jsonObjectJsonResult) {
                    if (jsonObjectJsonResult.getData() != null) {
                        requestCallBack.onSuccess(jsonObjectJsonResult.getData().toJavaObject(HealthSport.class));
                    } else {
                        requestCallBack.onFailed(jsonObjectJsonResult.getErrorCode(), "数据为空");
                    }
                }

                @Override
                public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                    requestCallBack.onFailed(errorCode, error);
                }
            });
        } else {
            requestCallBack.onNoNetWork();
        }
    }

    /**
     * 新增或更新一条运动记录
     *
     * @param healthSport 运动记录
     * @param callBack    回调
     */
    public static void insertOrReplace(@NotNull HealthSport healthSport, @NotNull HttpCallBack<HealthSport> callBack) {
        Http.obtain().put(Constant.SPORT_INSERT_OR_REPLACE, healthSport.toString(), new HttpCallBack<JsonResult<JSONObject>>() {
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

    public static void queryAll(@NotNull RequestCallBack<List<HealthSport>> requestCallBack) {
        if (XNetworkUtils.isAvailable()) {
            Http.obtain().get(Constant.SPORT_QUERY, null, new HttpCallBack<JsonResult<JSONArray>>() {
                @Override
                public void onSuccess(@NotNull JsonResult<JSONArray> jsonObjectJsonResult) {
                    if (jsonObjectJsonResult.getData() != null) {
                        requestCallBack.onSuccess(jsonObjectJsonResult.getData().toJavaList(HealthSport.class));
                    } else {
                        requestCallBack.onFailed(jsonObjectJsonResult.getErrorCode(), "数据为空");
                    }
                }

                @Override
                public void onFailed(@NotNull Integer errorCode, @NotNull String error) {
                    requestCallBack.onFailed(errorCode, error);
                }
            });
        } else {
            requestCallBack.onNoNetWork();
        }
    }
}
