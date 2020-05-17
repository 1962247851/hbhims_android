package com.example.hbhims.view.activity.login

import android.Manifest
import android.os.Bundle
import android.view.Menu
import com.example.hbhims.R
import com.example.hbhims.view.base.AbstractActivity
import com.youth.xframe.utils.permission.XPermission

class LoginActivity : AbstractActivity() {

    override fun preFinish(): Boolean {
        return true
    }

    override fun getOptionsMenuId(menu: Menu?): Int {
        return 0
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initData(savedInstanceState: Bundle?) {
        XPermission.requestPermissions(
            this,
            1,
            arrayOf(
                Manifest.permission.BODY_SENSORS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET
            ),
            object : XPermission.OnPermissionListener {
                override fun onPermissionGranted() {
                    //ignore
                }

                override fun onPermissionDenied() {
                    XPermission.showTipsDialog(this@LoginActivity)
                }
            }
        )
    }

    override fun initView() {
    }

}