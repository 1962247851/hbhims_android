package com.example.hbhims.model.common.util

import com.example.hbhims.model.common.entity.JsonResult
import com.example.hbhims.model.common.util.http.Http
import com.example.hbhims.model.common.util.http.HttpCallBack
import com.example.hbhims.model.common.util.http.IHttpEngine
import com.youth.xframe.XFrame
import com.youth.xframe.utils.log.XLog
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_REQUEST
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * @author 19622
 */
class OkHttpEngine : IHttpEngine {
    private val client: OkHttpClient
    override fun get(
        url: String,
        params: Map<String, Any>?,
        callBack: HttpCallBack<Any>
    ) {
        val request =
            Request.Builder().url(url + getUrlParamsByMap(params)).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Http.handler.post {
                    XLog.e(e.toString())
                    callBack.onFailed(999, e.message ?: "失败")
                }
            }

            @Throws(IOException::class)
            override fun onResponse(
                call: Call,
                response: Response
            ) {
                val body = response.body
                if (response.isSuccessful && body != null) {
                    val result = body.string()
                    XLog.json(result)
                    Http.handler.post {
                        try {
                            val jsonResult = JsonResult.newInstance(result)
                            if (jsonResult.success) {
                                callBack.onSuccess(jsonResult)
                            } else {
                                callBack.onFailed(
                                    jsonResult.errorCode,
                                    jsonResult.errorMsg
                                )
                            }
                        } catch (e: Exception) {
                            XLog.e(e.message)
                            callBack.onFailed(999, e.message ?: "失败")
                        }
                    }
                } else {
                    Http.handler.post {
                        XLog.e(response.message)
                        callBack.onFailed(response.code, response.message)
                    }
                }
            }
        })
    }

    override fun post(
        url: String,
        params: Map<String, Any>?,
        callBack: HttpCallBack<Any>
    ) {
        var body: RequestBody = EMPTY_REQUEST
        if (!params.isNullOrEmpty()) {
            val builder = FormBody.Builder()
            for (key in params.keys) {
                val o = params[key]
                if (o != null) {
                    builder.add(key, o.toString())
                }
            }
            body = builder.build()
        }
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Http.handler.post {
                    XLog.e(e.toString())
                    callBack.onFailed(999, e.message ?: "失败")
                }
            }

            @Throws(IOException::class)
            override fun onResponse(
                call: Call,
                response: Response
            ) {
                val responseBody = response.body
                if (response.isSuccessful && responseBody != null) {
                    val result = responseBody.string()
                    XLog.json(result)
                    Http.handler.post {
                        try {
                            val jsonResult = JsonResult.newInstance(result)
                            if (jsonResult.success) {
                                callBack.onSuccess(jsonResult)
                            } else {
                                callBack.onFailed(
                                    jsonResult.errorCode,
                                    jsonResult.errorMsg
                                )
                            }
                        } catch (e: Exception) {
                            XLog.e(e.message)
                            callBack.onFailed(999, e.message ?: "失败")
                        }
                    }
                } else {
                    Http.handler.post {
                        XLog.e(responseBody?.string())
                        callBack.onFailed(response.code, responseBody?.string() ?: "失败")
                    }
                }
            }
        })
    }

    override fun post(url: String, requestBodyJSONString: String, callBack: HttpCallBack<Any>) {
        val body = requestBodyJSONString.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Http.handler.post {
                    XLog.e(e.toString())
                    callBack.onFailed(999, e.message ?: "失败")
                }
            }

            @Throws(IOException::class)
            override fun onResponse(
                call: Call,
                response: Response
            ) {
                val responseBody = response.body
                if (response.isSuccessful && responseBody != null) {
                    val result = responseBody.string()
                    XLog.json(result)
                    Http.handler.post {
                        try {
                            val jsonResult = JsonResult.newInstance(result)
                            if (jsonResult.success) {
                                callBack.onSuccess(jsonResult)
                            } else {
                                callBack.onFailed(
                                    jsonResult.errorCode,
                                    jsonResult.errorMsg
                                )
                            }
                        } catch (e: Exception) {
                            XLog.e(e.message)
                            callBack.onFailed(999, e.message ?: "失败")
                        }
                    }
                } else {
                    Http.handler.post {
                        XLog.e(responseBody?.string())
                        callBack.onFailed(response.code, responseBody?.string() ?: "失败")
                    }
                }
            }
        })
    }

    override fun put(
        url: String,
        params: Map<String, Any>?,
        callBack: HttpCallBack<Any>
    ) {
        var body: RequestBody = EMPTY_REQUEST
        if (!params.isNullOrEmpty()) {
            val builder = FormBody.Builder()
            for (key in params.keys) {
                val o = params[key]
                if (o != null) {
                    builder.add(key, o.toString())
                }
            }
            body = builder.build()
        }
        val request = Request.Builder()
            .url(url)
            .put(body)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Http.handler.post {
                    XLog.e(e.toString())
                    callBack.onFailed(999, e.message ?: "失败")
                }
            }

            @Throws(IOException::class)
            override fun onResponse(
                call: Call,
                response: Response
            ) {
                val responseBody = response.body
                if (response.isSuccessful && responseBody != null) {
                    val result = responseBody.string()
                    XLog.json(result)
                    Http.handler.post {
                        try {
                            val jsonResult = JsonResult.newInstance(result)
                            if (jsonResult.success) {
                                callBack.onSuccess(jsonResult)
                            } else {
                                callBack.onFailed(
                                    jsonResult.errorCode,
                                    jsonResult.errorMsg
                                )
                            }
                        } catch (e: Exception) {
                            XLog.e(e.message)
                            callBack.onFailed(999, e.message ?: "失败")
                        }
                    }
                } else {
                    Http.handler.post {
                        XLog.e(responseBody?.string())
                        callBack.onFailed(response.code, responseBody?.string() ?: "失败")
                    }
                }
            }
        })
    }

    override fun put(url: String, requestBodyJSONString: String, callBack: HttpCallBack<Any>) {
        val body = requestBodyJSONString.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(url)
            .put(body)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Http.handler.post {
                    XLog.e(e.toString())
                    callBack.onFailed(999, e.message ?: "失败")
                }
            }

            @Throws(IOException::class)
            override fun onResponse(
                call: Call,
                response: Response
            ) {
                val responseBody = response.body
                if (response.isSuccessful && responseBody != null) {
                    val result = responseBody.string()
                    XLog.json(result)
                    Http.handler.post {
                        try {
                            val jsonResult = JsonResult.newInstance(result)
                            if (jsonResult.success) {
                                callBack.onSuccess(jsonResult)
                            } else {
                                callBack.onFailed(
                                    jsonResult.errorCode,
                                    jsonResult.errorMsg
                                )
                            }
                        } catch (e: Exception) {
                            XLog.e(e.message)
                            callBack.onFailed(999, e.message ?: "失败")
                        }
                    }
                } else {
                    Http.handler.post {
                        XLog.e(responseBody?.string())
                        callBack.onFailed(response.code, responseBody?.string() ?: "失败")
                    }
                }
            }
        })
    }

    override fun delete(
        url: String,
        params: Map<String, Any>?,
        callBack: HttpCallBack<Any>
    ) {
        var body: RequestBody = EMPTY_REQUEST
        if (!params.isNullOrEmpty()) {
            val builder = FormBody.Builder()
            for (key in params.keys) {
                val o = params[key]
                if (o != null) {
                    builder.add(key, o.toString())
                }
            }
            body = builder.build()
        }
        val request = Request.Builder()
            .url(url)
            .delete(body)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Http.handler.post {
                    XLog.e(e.toString())
                    callBack.onFailed(999, e.message ?: "失败")
                }
            }

            @Throws(IOException::class)
            override fun onResponse(
                call: Call,
                response: Response
            ) {
                val responseBody = response.body
                if (response.isSuccessful && responseBody != null) {
                    val result = responseBody.string()
                    XLog.json(result)
                    Http.handler.post {
                        try {
                            val jsonResult = JsonResult.newInstance(result)
                            if (jsonResult.success) {
                                callBack.onSuccess(jsonResult)
                            } else {
                                callBack.onFailed(
                                    jsonResult.errorCode,
                                    jsonResult.errorMsg
                                )
                            }
                        } catch (e: Exception) {
                            XLog.e(e.message)
                            callBack.onFailed(999, e.message ?: "失败")
                        }
                    }
                } else {
                    Http.handler.post {
                        XLog.e(responseBody?.string())
                        callBack.onFailed(response.code, responseBody?.string() ?: "失败")
                    }
                }
            }
        })
    }

    private fun getUrlParamsByMap(map: Map<String, Any>?): String {
        if (map == null || map.isEmpty()) {
            return ""
        }
        val params = StringBuilder("?")
        for ((key, value) in map) {
            params.append(key)
            params.append("=")
            params.append(value)
            params.append("&")
        }
        val str = params.toString()
        return str.substring(0, str.length - 1)
    }

    companion object {
        private const val CACHE_SIZE = 10 * 1024 * 1024
    }

    init {
        client = OkHttpClient().newBuilder()
            .retryOnConnectionFailure(true)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .cache(
                Cache(
                    XFrame.getContext().cacheDir,
                    CACHE_SIZE.toLong()
                )
            )
            .cookieJar(CookieJarImpl())
            .build()
    }

}