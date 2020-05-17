package com.example.hbhims

import android.app.ActivityManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Rect
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Process
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.example.hbhims.model.common.util.OkHttpEngine
import com.example.hbhims.model.common.util.http.Http
import com.example.hbhims.model.entity.SysUser
import com.example.hbhims.model.eventbus.StepValueChange
import com.xiaomi.channel.commonutils.logger.LoggerInterface
import com.xiaomi.mipush.sdk.Logger
import com.xiaomi.mipush.sdk.MiPushClient
import com.youth.xframe.XFrame
import com.youth.xframe.base.XApplication
import com.youth.xframe.utils.XDateUtils
import com.youth.xframe.utils.XPreferencesUtils
import com.youth.xframe.widget.XToast
import org.greenrobot.eventbus.EventBus

class App : XApplication(), SensorEventListener, ViewModelStoreOwner {

    private lateinit var sensorManager: SensorManager

    override fun onTerminate() {
        super.onTerminate()
        sensorManager.unregisterListener(this)
    }

    override fun onCreate() {
        super.onCreate()
        XFrame.initXLog().isDebug = BuildConfig.DEBUG
        Http.init(OkHttpEngine())
        //初始化push推送服务
        if (shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY)
        }
        //打开Log
        val newLogger: LoggerInterface = object : LoggerInterface {
            override fun setTag(tag: String) {
                // ignore
            }

            override fun log(content: String, t: Throwable) {
                Log.d(TAG, content, t)
            }

            override fun log(content: String) {
                Log.d(TAG, content)
            }
        }
        Logger.setLogger(this, newLogger)
        initStepSensor()
    }

    private fun initStepSensor() {
        val sensorService = getSystemService(Context.SENSOR_SERVICE)
        sensorManager = sensorService as SensorManager
        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepCounterSensor != null) {
            sensorManager.registerListener(
                this,
                stepCounterSensor,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        } else {
            XToast.warning("该设备不支持步数传感器，部分功能可能无法正常使用", Toast.LENGTH_LONG)
        }
    }

    private fun shouldInit(): Boolean {
        val am =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processInfos =
            am.runningAppProcesses
        val mainProcessName = applicationInfo.processName
        val myPid = Process.myPid()
        for (info in processInfos) {
            if (info.pid == myPid && mainProcessName == info.processName) {
                return true
            }
        }
        return false
    }

    companion object {
        const val APP_ID = "2882303761518395774"
        const val APP_KEY = "5981839552774"
        const val APP_SECRET = "mm9vdHCYJLGfRHETrpwuvg=="
        const val TAG = "com.example.hbhims"

        @JvmStatic
        lateinit var user: SysUser

        /**
         * 文字复制到剪切板
         *
         * @param context context
         * @param text    要复制的文字
         */
        fun copyToClipboard(context: Context, text: String?) {
            val systemService =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            systemService.setPrimaryClip(ClipData.newPlainText("text", text))
            XToast.success(String.format(XFrame.getString(R.string.copy_to_clipboard), text))
        }

        /**
         * 获取状态栏高度
         *
         * @param activity activity
         * @return 高度
         */
        fun getStatusHeight(activity: AppCompatActivity): Int {
            var statusHeight: Int
            val rect = Rect()
            activity.window.decorView
                .getWindowVisibleDisplayFrame(rect)
            statusHeight = rect.top
            if (0 == statusHeight) {
                val localClass: Class<*>
                try {
                    localClass = Class.forName("com.android.internal.R\$dimen")
                    val `object` = localClass.newInstance()
                    val height = localClass
                        .getField("status_bar_height")[`object`]
                        .toString().toInt()
                    statusHeight = activity.resources
                        .getDimensionPixelSize(height)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return statusHeight
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //ignore
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            var stepValue = event.values[0].toInt()
            if (!XDateUtils.isSameDay(
                    XPreferencesUtils.get(
                        getString(R.string.key_health_step_value_update_time),
                        0L
                    ) as Long
                )
            ) {
                stepValue = 0
                XPreferencesUtils.put(
                    getString(R.string.key_health_step_value_update_time),
                    System.currentTimeMillis()
                )
            }
            EventBus.getDefault().post(StepValueChange(stepValue))
            XPreferencesUtils.put(getString(R.string.key_health_step_value), stepValue)
        }
    }

    private val appViewModelStore: ViewModelStore by lazy {
        ViewModelStore()
    }

    override fun getViewModelStore(): ViewModelStore {
        return appViewModelStore
    }
}