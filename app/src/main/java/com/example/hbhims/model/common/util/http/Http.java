package com.example.hbhims.model.common.util.http;


import com.youth.xframe.utils.handler.XHandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class Http implements IHttpEngine {

    private static IHttpEngine httpEngine;
    private static Http http;
    public static XHandler handler = new XHandler();

    public static void init(IHttpEngine engine) {
        httpEngine = engine;
    }

    public static Http obtain() {
        if (httpEngine == null) {
            throw new NullPointerException("Call Http.init(IHttpEngine httpEngine) within your Application onCreate() method.");
        }
        if (http == null) {
            http = new Http();
        }
        return http;
    }

    /**
     * 获取实体类的类型
     *
     * @param obj
     * @return
     */
    public static Class<?> analysisClassInfo(Object obj) {
        Type genType = obj.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }

    @Override
    public void get(@NotNull String url, @Nullable Map<String, Object> params, @NotNull HttpCallBack callBack) {
        httpEngine.get(url, params, callBack);
    }

    @Override
    public void post(@NotNull String url, @Nullable Map<String, Object> params, @NotNull HttpCallBack callBack) {
        httpEngine.post(url, params, callBack);
    }

    @Override
    public void post(@NotNull String url, @NotNull String requestBodyJSONString, @NotNull HttpCallBack callBack) {
        httpEngine.post(url, requestBodyJSONString, callBack);
    }

    @Override
    public void put(@NotNull String url, @Nullable Map<String, Object> params, @NotNull HttpCallBack callBack) {
        httpEngine.put(url, params, callBack);
    }

    @Override
    public void put(@NotNull String url, @NotNull String requestBodyJSONString, @NotNull HttpCallBack callBack) {
        httpEngine.put(url, requestBodyJSONString, callBack);
    }

    @Override
    public void delete(@NotNull String url, @Nullable Map<String, Object> params, @NotNull HttpCallBack callBack) {
        httpEngine.delete(url, params, callBack);
    }

    @Override
    public void uploadFile(@NotNull String url, @NotNull String fileName, @NotNull String path, @NotNull File file, @NotNull HttpCallBack callBack) {
        httpEngine.uploadFile(url, fileName, path, file, callBack);
    }
}
