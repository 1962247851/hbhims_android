package com.example.hbhims.model

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.alibaba.fastjson.JSONObject
import com.example.hbhims.view.activity.login.LoginActivity
import com.xiaomi.mipush.sdk.*
import com.youth.xframe.XFrame
import com.youth.xframe.utils.log.XLog

class BroadcastReceiver : PushMessageReceiver() {
    private var mRegId: String? = null
    private val mResultCode: Long = -1
    private val mReason: String? = null
    private val mCommand: String? = null
    private var mMessage: String? = null
    private var mTopic: String? = null
    private var mAlias: String? = null
    private var mUserAccount: String? = null
    private var mStartTime: String? = null
    private var mEndTime: String? = null

    /**
     * 透传消息到达手机端后，SDK会将消息通过广播方式传给AndroidManifest中注册的PushMessageReceiver的子类的[onReceivePassThroughMessage]
     *
     * @param context
     * @param message
     */
    override fun onReceivePassThroughMessage(
        context: Context,
        message: MiPushMessage
    ) {
        XLog.json(JSONObject.toJSONString(message))
        mMessage = message.content
        if (!TextUtils.isEmpty(message.topic)) {
            mTopic = message.topic
        } else if (!TextUtils.isEmpty(message.alias)) {
            mAlias = message.alias
        } else if (!TextUtils.isEmpty(message.userAccount)) {
            mUserAccount = message.userAccount
        }
    }

    /**
     * 用户点击之后再传给您的PushMessageReceiver的子类的[onNotificationMessageClicked]
     *
     * @param context
     * @param message
     */
    override fun onNotificationMessageClicked(
        context: Context,
        message: MiPushMessage
    ) {
        XLog.json(JSONObject.toJSONString(message))
        if (!TextUtils.isEmpty(message.topic)) {
            mTopic = message.topic
        } else if (!TextUtils.isEmpty(message.alias)) {
            mAlias = message.alias
        } else if (!TextUtils.isEmpty(message.userAccount)) {
            mUserAccount = message.userAccount
        }
        val currentContext: Context = try {
            XFrame.getContext()
        } catch (e: NullPointerException) {
            XLog.d("onNotificationMessageClicked XFrame.getContext() == null")
            context
        }
        currentContext.startActivity(
            Intent(currentContext, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    /**
     * 通知消息到达时会到达PushMessageReceiver子类的onNotificationMessageArrived方法
     * 对于应用在前台时不弹出通知的通知消息，SDK会将消息通过广播方式传给AndroidManifest中注册的PushMessageReceiver的子类的
     * [onNotificationMessageArrived]
     *
     * @param context
     * @param message
     */
    override fun onNotificationMessageArrived(
        context: Context,
        message: MiPushMessage
    ) {
        XLog.json(JSONObject.toJSONString(message))
        mMessage = message.content
        if (!TextUtils.isEmpty(message.topic)) {
            mTopic = message.topic
        } else if (!TextUtils.isEmpty(message.alias)) {
            mAlias = message.alias
        } else if (!TextUtils.isEmpty(message.userAccount)) {
            mUserAccount = message.userAccount
        }
    }

    /**
     * 用来接收客户端向服务器发送命令消息后返回的响应
     *
     * @param context
     * @param message
     */
    override fun onCommandResult(
        context: Context,
        message: MiPushCommandMessage
    ) {
        val command = message.command
        val arguments = message.commandArguments
        val cmdArg1 =
            if (arguments != null && arguments.size > 0) arguments[0] else null
        val cmdArg2 =
            if (arguments != null && arguments.size > 1) arguments[1] else null
        if (MiPushClient.COMMAND_REGISTER == command) {
            if (message.resultCode == ErrorCode.SUCCESS.toLong()) {
                mRegId = cmdArg1
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS == command) {
            if (message.resultCode == ErrorCode.SUCCESS.toLong()) {
                mAlias = cmdArg1
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS == command) {
            if (message.resultCode == ErrorCode.SUCCESS.toLong()) {
                mAlias = cmdArg1
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC == command) {
            if (message.resultCode == ErrorCode.SUCCESS.toLong()) {
                mTopic = cmdArg1
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC == command) {
            if (message.resultCode == ErrorCode.SUCCESS.toLong()) {
                mTopic = cmdArg1
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME == command) {
            if (message.resultCode == ErrorCode.SUCCESS.toLong()) {
                mStartTime = cmdArg1
                mEndTime = cmdArg2
            }
        }
    }

    /**
     * 用来接受客户端向服务器发送注册命令消息后返回的响应
     *
     * @param context
     * @param message
     */
    override fun onReceiveRegisterResult(
        context: Context,
        message: MiPushCommandMessage
    ) {
        val command = message.command
        val arguments = message.commandArguments
        val cmdArg1 =
            if (arguments != null && arguments.size > 0) arguments[0] else null
        val cmdArg2 =
            if (arguments != null && arguments.size > 1) arguments[1] else null
        if (MiPushClient.COMMAND_REGISTER == command) {
            if (message.resultCode == ErrorCode.SUCCESS.toLong()) {
                mRegId = cmdArg1
            }
        }
    }
}