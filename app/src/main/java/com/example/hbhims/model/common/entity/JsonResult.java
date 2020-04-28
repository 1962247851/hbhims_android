package com.example.hbhims.model.common.entity;


import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 统一返回实体
 *
 * @param <T>
 * @author qq1962247851
 */
public class JsonResult<T> implements Serializable {

    private Boolean success;
    private Integer errorCode;
    private String errorMsg;
    private T data;

    public JsonResult() {
    }

    public static JsonResult<?> newInstance(String result) {
        return JSONObject.parseObject(result, JsonResult.class);
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @NonNull
    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
