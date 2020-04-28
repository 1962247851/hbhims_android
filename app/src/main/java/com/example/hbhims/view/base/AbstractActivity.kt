package com.example.hbhims.view.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.hbhims.App
import com.example.hbhims.R
import com.youth.xframe.base.ICallback
import com.youth.xframe.common.XActivityStack
import com.youth.xframe.utils.permission.XPermission
import com.youth.xframe.utils.statusbar.XStatusBar

/**
 * @author qq1962247851
 * @date 2020/1/29 18:22
 */
abstract class AbstractActivity : AppCompatActivity(), ICallback {

    private var drawerLayout: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this)
//        }
        super.onCreate(savedInstanceState)
        XActivityStack.getInstance().addActivity(this)
        if (layoutId > 0) {
            setContentView(layoutId)
            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)
            drawerLayout = findViewById(R.id.drawer_layout)
            if (drawerLayout == null) {
                XStatusBar.setColorNoTranslucent(
                    this,
                    ContextCompat.getColor(this, R.color.colorPrimary)
                )
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            } else {
                if (toolbar != null) {
                    val toggle = ActionBarDrawerToggle(
                        this,
                        drawerLayout,
                        toolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close
                    )
                    drawerLayout?.addDrawerListener(toggle)
                    toggle.syncState()
                    toolbar.setPadding(
                        0,
                        App.getStatusHeight(this),
                        0,
                        0
                    )
                }
                XStatusBar.setTransparentForDrawerLayout(this, drawerLayout)
            }
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
//        if (EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this)
//        }
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
        if (drawerLayout != null) {
            when {
                drawerLayout!!.isDrawerOpen(GravityCompat.START) -> {
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                drawerLayout!!.isDrawerOpen(GravityCompat.END) -> {
                    drawerLayout!!.closeDrawer(GravityCompat.END)
                }
                preFinish() -> {
                    super.onBackPressed()
                }
            }
        } else if (preFinish()) {
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

}