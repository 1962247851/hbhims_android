package com.example.hbhims

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.hbhims.model.common.util.OkHttpEngine
import com.example.hbhims.model.common.util.http.Http
import com.example.hbhims.model.entity.SysUser
import com.xiaomi.mipush.sdk.MiPushClient
import com.youth.xframe.XFrame
import com.youth.xframe.base.XApplication
import com.youth.xframe.widget.XToast
import org.jetbrains.annotations.NotNull

class App : XApplication() {

    override fun onCreate() {
        super.onCreate()
        initGreenDao()
        XFrame.initXLog().isDebug = DEBUG_MODE
        Http.init(OkHttpEngine())
        //初始化push推送服务
        if (shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY)
//            MiPushClient.setUserAccount(this, CURRENT_NAME, null)
        }
        //打开Log
//        val newLogger: LoggerInterface = object : LoggerInterface {
//            override fun setTag(tag: String) {
//                // ignore
//            }
//
//            override fun log(content: String, t: Throwable) {
//                Log.d(TAG, content, t)
//            }
//
//            override fun log(content: String) {
//                Log.d(TAG, content)
//            }
//        }
//        Logger.setLogger(this, newLogger)
    }

    private fun shouldInit(): Boolean {
//        val am =
//            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        val processInfos =
//            am.runningAppProcesses
//        val mainProcessName = applicationInfo.processName
//        val myPid = Process.myPid()
//        for (info in processInfos) {
//            if (info.pid == myPid && mainProcessName == info.processName) {
//                return true
//            }
//        }
        return false
    }

    private fun initGreenDao() {
////        val helper =
////            DaoMaster.DevOpenHelper(
////                this,
////                DB_NAME
////            )
//        val helper =
//            MyOpenHelper(
//                this,
//                DB_NAME
//            )
//        val sqLiteDatabase = helper.writableDatabase
//        daoSession =
//            DaoMaster(sqLiteDatabase).newSession()
    }


    companion object {
        const val DB_NAME = "HBHIMS.db"
        const val DEBUG_MODE = true
        const val APP_ID = "2882303761518314078"
        const val APP_KEY = "5701831481078"
        const val APP_SECRET = "NUAC1gJCghBpgb21ADLTXA=="
        const val TAG = "com.example.hbhims"
        const val NEW_PLAN = 0
        const val EDIT_PLAN = 1
        const val NEW_COMMEMORATION = 2
        const val EDIT_COMMEMORATION = 3
        const val SELECT_PICTURE = 4
        const val EDIT_COMMEMORATION_LIST = 5
        const val GALLERY_PHOTO_CURRENT_ITEM = 6
        const val NEW_LETTER = 7
        const val EDIT_LETTER = 8
        const val REQUEST_EXTERNAL_STORAGE = 9
        const val REQUEST_RECORD = 10
        const val REQUEST_CODE_CREATE_FILE = 11
        const val REQUEST_CODE_OPEN_FILE = 12
        const val REQUEST_CODE_READ_EXTERNAL_STORAGE = 13

        @JvmStatic
        lateinit var user: SysUser

//        @JvmStatic
//        lateinit var daoSession: DaoSession

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

        /**
         * 获取状态栏高度
         *
         * @param decorView decorView
         * @return 高度
         */
        fun getStatusHeight(fragmentActivity: FragmentActivity): Int {
            var statusHeight: Int
            val rect = Rect()
            fragmentActivity.window.decorView.getWindowVisibleDisplayFrame(rect)
            statusHeight = rect.top
            if (0 == statusHeight) {
                val localClass: Class<*>
                try {
                    localClass = Class.forName("com.android.internal.R\$dimen")
                    val `object` = localClass.newInstance()
                    val height = localClass
                        .getField("status_bar_height")[`object`]
                        .toString().toInt()
                    statusHeight = fragmentActivity.resources.getDimensionPixelSize(height)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return statusHeight
        }
    }

    @FunctionalInterface
    interface IAppToolbarSubTitleListener {
        fun onResponse(@NotNull result: String)
    }
}