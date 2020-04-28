package com.example.hbhims.view.activity.main

import android.os.Bundle
import android.view.Menu
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hbhims.R
import com.example.hbhims.view.base.AbstractActivity
import com.youth.xframe.widget.XToast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AbstractActivity() {
    private var backTime = 0L

    override fun preFinish(): Boolean {
        return if (System.currentTimeMillis() - backTime < 2000) {
            true
        } else {
            backTime = System.currentTimeMillis()
            XToast.info(getString(R.string.operate_again_to_exit))
            false
        }
    }

    override fun getOptionsMenuId(menu: Menu?): Int {
        return 0
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData(savedInstanceState: Bundle?) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.healthFragment,
                R.id.medicalSuggestionFragment,
                R.id.myselfFragment
            ), drawer_layout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navigation_view.setupWithNavController(navController)
    }

    override fun initView() {
    }

}
