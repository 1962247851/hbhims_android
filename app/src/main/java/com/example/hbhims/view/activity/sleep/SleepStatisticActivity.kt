package com.example.hbhims.view.activity.sleep

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import com.example.hbhims.R
import com.example.hbhims.model.common.util.MPAChartUtil
import com.example.hbhims.model.common.util.http.RequestCallBack
import com.example.hbhims.model.entity.HealthSleep
import com.example.hbhims.view.base.ContainerActivity
import com.example.hbhims.view.custom.LoadingDialog
import com.example.hbhims.view.fragment.health.HealthFragment
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.youth.xframe.utils.XDateUtils
import com.youth.xframe.widget.XToast
import kotlinx.android.synthetic.main.activity_sleep_statistic.*
import java.util.*
import kotlin.collections.ArrayList

class SleepStatisticActivity : ContainerActivity() {

    override fun preFinish(): Boolean {
        return true
    }

    override fun getOptionsMenuId(menu: Menu?): Int {
        return R.menu.share
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_sleep_statistic
    }

    override fun initData(savedInstanceState: Bundle?) {
        getData()
    }

    private fun getData() {
        HealthSleep.queryAllByUserId(userId, object : RequestCallBack<List<HealthSleep>>() {
            override fun onSuccess(result: List<HealthSleep>) {
                if (result.isEmpty()) {
                    loadingView.showEmpty()
                } else {
                    updateUI(result)
                    loadingView.showContent()
                }
            }

            override fun onFailed(errorCode: Int, error: String) {
                showError()
            }

            override fun onNoNetWork() {
                showNoNetWork()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(result: List<HealthSleep>) {
        var totalSleepTime = 0L

        var normalDay = 0
        var lessDay = 0
        var overDay = 0

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val endTimeMills = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_MONTH, -7)
        val startTimeMills = calendar.timeInMillis
        val entries = ArrayList<BarEntry>()
        val xValues = ArrayList<String>()
        val sleepList = ArrayList<HealthSleep>()
        var indexTemp = 0
        result.sortedBy { it.date }.forEach {
            val totalTime = it.totalTime
            totalSleepTime += totalTime
            if (totalTime in 7 * 3600000..8 * 3600000) {
                normalDay++
            } else {
                if (totalTime < 7 * 3600000) {
                    lessDay++
                } else {
                    overDay++
                }
            }
            if (it.date in startTimeMills..endTimeMills) {
                sleepList.add(it)
                entries.add(BarEntry((indexTemp++).toFloat(), totalTime.toFloat()))
                xValues.add(XDateUtils.millis2String(it.date, "MM/dd"))
            }
        }
        MPAChartUtil.updateBarChart(
            bar_chart_latest_seven_days,
            this,
            entries,
            xValues,
            "",
            MPAChartUtil.defaultChartColors,
            "",
            object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val hour = value.toLong() / 3600000
                    val minute = (value.toLong() - hour * 3600000) / 60000
                    return hour.toInt().toString() + getString(R.string.hour) + minute.toInt()
                        .toString() + getString(R.string.minute)
                }
            },
            object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val hour = value.toLong() / 3600000
                    val minute = (value.toLong() - hour * 3600000) / 60000
                    return hour.toInt().toString() + getString(R.string.hour) + minute.toInt()
                        .toString() + getString(R.string.minute)
                }
            }
        )
        bar_chart_latest_seven_days.marker = SleepMarkerView(this, sleepList)

        val perDayTime = totalSleepTime / result.size
        val hour = perDayTime / 3600000
        val minute = (perDayTime - hour * 3600000) / 60000
        text_view_sleep_total_time_hour.text = hour.toInt().toString()
        text_view_sleep_total_time_minute.text = minute.toInt().toString()

        text_view_normal_count.text = normalDay.toString() + getString(R.string.day)
        text_view_less_count.text = lessDay.toString() + getString(R.string.day)
        text_view_over_count.text = overDay.toString() + getString(R.string.day)

    }

    private fun showError() {
        loadingView.setOnRetryClickListener {
            loadingView.showLoading()
            getData()
        }.showError()
    }

    private fun showNoNetWork() {
        loadingView.setOnRetryClickListener {
            loadingView.showLoading()
            getData()
        }.showNoNetwork()
    }

    override fun initView() {
        loadingView.showContent()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.item_share) {
            LoadingDialog.with(this).setMessage("正在生成图片...").show()
            val pictureBitmap = HealthFragment.takeHealthDataPictureBitmap(linear_layout)
            if (pictureBitmap != null) {
                shareImage(this, pictureBitmap)
            } else {
                XToast.error(getString(R.string.share_failed))
            }
            LoadingDialog.with(this).cancel()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    companion object {
        @JvmStatic
        fun shareImage(context: Context, pictureBitmap: Bitmap?) {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/*"
            val uriToImage = Uri.parse(
                MediaStore.Images.Media.insertImage(
                    context.contentResolver,
                    pictureBitmap,
                    null,
                    null
                )
            )
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            share.putExtra(Intent.EXTRA_STREAM, uriToImage)
            context.startActivity(Intent.createChooser(share, "运动报告"))
        }
    }

}