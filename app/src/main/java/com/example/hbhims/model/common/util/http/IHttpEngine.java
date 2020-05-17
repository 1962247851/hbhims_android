package com.example.hbhims.model.common.util.http;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Map;

public interface IHttpEngine {

    void get(@NotNull String url, @Nullable Map<String, Object> params, @NotNull HttpCallBack callBack);

    void post(@NotNull String url, @Nullable Map<String, Object> params, @NotNull HttpCallBack callBack);

    void post(@NotNull String url, @NotNull String requestBodyJSONString, @NotNull HttpCallBack callBack);

    void put(@NotNull String url, @Nullable Map<String, Object> params, @NotNull HttpCallBack callBack);

    void put(@NotNull String url, @NotNull String requestBodyJSONString, @NotNull HttpCallBack callBack);

    void delete(@NotNull String url, @Nullable Map<String, Object> params, @NotNull HttpCallBack callBack);

    void uploadFile(@NotNull String url, @NotNull String fileName, @NotNull String path, @NotNull File file, @NotNull HttpCallBack callBack);
}
