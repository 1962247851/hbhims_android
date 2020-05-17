package com.example.hbhims.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import com.alibaba.fastjson.JSONObject
import com.example.hbhims.R
import com.example.hbhims.model.common.util.http.RequestCallBack
import com.example.hbhims.model.entity.SysUser
import com.example.hbhims.view.base.AbstractActivity
import com.example.hbhims.view.custom.LoadingDialog
import com.youth.xframe.utils.XDateUtils
import com.youth.xframe.utils.XPreferencesUtils
import com.youth.xframe.utils.XRegexUtils
import com.youth.xframe.utils.log.XLog
import com.youth.xframe.widget.XToast
import kotlinx.android.synthetic.main.activity_update_user_info.*
import java.util.*

class UpdateUserInfoActivity : AbstractActivity() {

    private lateinit var user: SysUser
    private lateinit var calendar: Calendar

    override fun preFinish(): Boolean {
        return true
    }

    override fun getOptionsMenuId(menu: Menu?): Int {
        return R.menu.update
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_update_user_info
    }

    override fun initData(savedInstanceState: Bundle?) {
        user = if (savedInstanceState != null && savedInstanceState.containsKey(KEY_USER_JSON)) {
            JSONObject.parseObject(
                savedInstanceState.getString(KEY_USER_JSON),
                SysUser::class.java
            )
        } else {
            JSONObject.parseObject(
                XPreferencesUtils.get(
                    getString(R.string.key_user_json),
                    "{}"
                ) as String, SysUser::class.java
            )
        }
        calendar = Calendar.getInstance().apply {
            timeInMillis = user.birthday
        }
    }

    override fun initView() {
        tiet_username.setText(user.userName)
        tiet_self_introduction.setText(user.selfIntroduction)
        tiet_email.setText(user.email)
        tiet_phone.setText(user.phone)
        if (user.sex) {
            spinner_sex.setSelection(0)
        } else {
            spinner_sex.setSelection(1)
        }
        spinner_sex.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //ignore
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                user.sex = position == 0
            }
        }
        updateBirthdayEditText()
        tiet_birthday.setOnClickListener {
            AlertDialog.Builder(this).setTitle(R.string.select_date)
                .setCancelable(false)
                .setView(DatePicker(this).apply {
                    maxDate = System.currentTimeMillis()
                    init(
                        calendar[Calendar.YEAR],
                        calendar[Calendar.MONTH],
                        calendar[Calendar.DAY_OF_MONTH]
                    ) { _, year, monthOfYear, dayOfMonth ->
                        calendar[Calendar.YEAR] = year
                        calendar[Calendar.MONTH] = monthOfYear
                        calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.confirm) { _, _ ->
                    user.birthday = calendar.timeInMillis
                    updateBirthdayEditText()
                }.show()
        }
    }

    private fun updateBirthdayEditText() {
        tiet_birthday.setText(XDateUtils.millis2String(user.birthday, "yyyy/MM/dd"))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_USER_JSON, user.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.item_finish) {
            if (checkValid()) {
                LoadingDialog.with(this).setMessage(R.string.updatin).show()
                SysUser.update(user, object : RequestCallBack<SysUser>() {
                    override fun onSuccess(result: SysUser) {
                        LoadingDialog.with(this@UpdateUserInfoActivity).cancel()
                        XPreferencesUtils.put(getString(R.string.key_user_json), result.toString())
                        setResult(
                            Activity.RESULT_OK, Intent().putExtra(KEY_USER_JSON, result.toString())
                        )
                        finish()
                        XToast.success(getString(R.string.update_success))
                    }

                    override fun onFailed(errorCode: Int, error: String) {
                        LoadingDialog.with(this@UpdateUserInfoActivity).cancel()
                        XToast.error("$errorCode\n$error")
                    }

                    override fun onNoNetWork() {
                        LoadingDialog.with(this@UpdateUserInfoActivity).cancel()
                        XToast.info(getString(R.string.xloading_no_network_text))
                    }
                })
            } else {
                XToast.info("请检查输入")
            }
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun checkValid(): Boolean {
        user.userName = tiet_username.text.toString()
        user.selfIntroduction = tiet_self_introduction.text.toString()
        user.email = tiet_email.text.toString()
        user.phone = tiet_phone.text.toString()
        XLog.d("checkValid user.userName.length in 1..10 ${user.userName.length in 1..10}")
        XLog.d("checkValid XRegexUtils.checkNickName(user.userName) ${XRegexUtils.checkNickName(user.userName)}")
        XLog.d("checkValid user.selfIntroduction.length <= 255 ${user.selfIntroduction.length <= 255}")
        XLog.d("checkValid user.email.isNullOrEmpty() ${user.email.isNullOrEmpty()}")
        XLog.d("checkValid XRegexUtils.checkEmail(user.email) ${XRegexUtils.checkEmail(user.email)}")
        return user.userName.length in 1..10 &&
                user.selfIntroduction.length <= 255 &&
                (user.email.isNullOrEmpty() || XRegexUtils.checkEmail(user.email))
    }

    companion object {
        const val KEY_USER_JSON = "KEY_USER_JSON"
    }

}