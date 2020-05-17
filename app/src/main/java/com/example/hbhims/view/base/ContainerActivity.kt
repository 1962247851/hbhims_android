package com.example.hbhims.view.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.hbhims.App
import com.example.hbhims.R
import com.example.hbhims.view.custom.LoadingView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.youth.xframe.base.ICallback
import com.youth.xframe.common.XActivityStack
import com.youth.xframe.utils.permission.XPermission
import com.youth.xframe.utils.statusbar.XStatusBar

/**
 * @author qq1962247851
 * @date 2020/1/29 18:22
 */
abstract class ContainerActivity : AppCompatActivity(), ICallback {

    var userId = 0L
    lateinit var loadingView: LoadingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        XActivityStack.getInstance().addActivity(this)
        if (layoutId > 0) {
            setContentView(layoutId)
            loadingView =
                LoadingView.wrap(findViewById<SmartRefreshLayout>(R.id.smart_refresh_layout))
            loadingView.showLoading()
            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)
            XStatusBar.setColorNoTranslucent(
                this,
                ContextCompat.getColor(this, R.color.colorPrimary)
            )
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        if (intent.hasExtra(KEY_USER_ID)) {
            userId = intent.getLongExtra(KEY_USER_ID, userId)
        } else if (savedInstanceState != null) {
            userId = savedInstanceState.getLong(KEY_USER_ID, userId)
        } else {
            userId = App.user.id
        }
        initData(savedInstanceState)
        initView()
    }

    /**
     * Android M 全局权限申请回调
     *
     * @param requestCode  requestCode
     * @param permissions  permissions
     * @param grantResults grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        XPermission.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        XActivityStack.getInstance().finishActivity()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            if (preFinish()) {
                super.onBackPressed()
            }
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (preFinish()) {
            super.onBackPressed()
        }
    }

    /**
     * @return 是否完成所有操作，退出界面
     */
    abstract fun preFinish(): Boolean

    /**
     * @return 是否有右上角菜单
     */
    abstract fun getOptionsMenuId(menu: Menu?): Int

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (getOptionsMenuId(menu) > 0) {
            menuInflater.inflate(getOptionsMenuId(menu), menu)
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong(KEY_USER_ID, userId)
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val KEY_USER_ID = "USER_ID"
    }

}