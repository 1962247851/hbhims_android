package com.example.hbhims.view.activity.login

import android.os.Bundle
import android.view.Menu
import com.example.hbhims.R
import com.example.hbhims.view.base.AbstractActivity

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
    }

    override fun initView() {
    }

}