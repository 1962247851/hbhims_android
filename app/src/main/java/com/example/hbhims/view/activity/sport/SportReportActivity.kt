package com.example.hbhims.view.activity.sport

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
import com.example.hbhims.model.entity.HealthSport
import com.example.hbhims.view.base.ContainerActivity
import com.example.hbhims.view.custom.LoadingDialog
import com.example.hbhims.view.fragment.health.HealthFragment
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.youth.xframe.utils.XDateUtils
import com.youth.xframe.utils.XPreferencesUtils
import com.youth.xframe.widget.XToast
import kotlinx.android.synthetic.main.activity_sport_report.*
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class SportReportActivity : ContainerActivity() {

    override fun preFinish(): Boolean {
        return true
    }

    override fun getOptionsMenuId(menu: Menu?): Int {
        return R.menu.share
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_sport_report
    }

    override fun initData(savedInstanceState: Bundle?) {
        getData()
    }

    private fun getData() {
        HealthSport.queryAllByUserId(userId, object : RequestCallBack<List<HealthSport>>() {
            override fun onSuccess(result: List<HealthSport>) {
                if (result.isEmpty()) {
                    loadingView.showEmpty()
                } else {
                    HealthSport.queryAll(object : RequestCallBack<List<HealthSport>>() {
                        override fun onSuccess(resultAllUser: List<HealthSport>) {
                            loadingView.showContent()
                            updateUI(result, resultAllUser)
                        }

                        override fun onFailed(errorCode: Int, error: String) {
                            showError()
                        }

                        override fun onNoNetWork() {
                            showNoNetWork()
                        }
                    })
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

    private fun updateUI(result: List<HealthSport>, resultAllUser: List<HealthSport>) {
        maxStepValue(result, resultAllUser)
        //获取制定的标准
        val standard = XPreferencesUtils.get(
            getString(R.string.key_health_every_day_step_value),
            resources.getInteger(R.integer.default_every_day_step_value)
        ) as Int
        val endTimeMills = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -7)
        val startTimeMills = calendar.timeInMillis
        val latestSportList = ArrayList<HealthSport>()
        val entries = ArrayList<Entry>()
        val xValues = ArrayList<String>()
        var totalStepValue = 0
        var reachStandardDays = 0
        result.forEach {
            totalStepValue += it.stepValue
            if (it.stepValue >= standard) {
                reachStandardDays++
            }
            if (it.date in startTimeMills..endTimeMills) {
                latestSportList.add(it)
            }
        }
        latestSportList.sortedBy { it.date }.forEachIndexed { index, healthSport ->
            entries.add(Entry(index.toFloat(), healthSport.stepValue.toFloat()))
            xValues.add(XDateUtils.millis2String(healthSport.date, "MM/dd"))
        }
        MPAChartUtil.updateLineChart(
            line_chart_latest_seven_days,
            this,
            entries,
            xValues,
            "",
            listOf(getColor(android.R.color.white)),
            "",
            object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString() + getString(R.string.step)
                }
            },
            object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString() + getString(R.string.step)
                }
            })
        line_chart_latest_seven_days.xAxis.textColor = getColor(android.R.color.white)
        line_chart_latest_seven_days.axisLeft.textColor = getColor(android.R.color.white)
        line_chart_latest_seven_days.data.setValueTextColor(getColor(android.R.color.white))
        line_chart_latest_seven_days.setNoDataTextColor(getColor(android.R.color.white))
        line_chart_latest_seven_days.axisLeft.addLimitLine(
            LimitLine(standard.toFloat(), "每日目标").apply {
                textColor = getColor(android.R.color.white)
                lineColor = getColor(android.R.color.holo_orange_light)
            }
        )

        text_view_total_step_value.text = totalStepValue.toString()
        text_view_step_value_per_day.text = (totalStepValue / result.size).toString()
        text_view_number_of_reach_standard.text = reachStandardDays.toString()
    }

    private fun maxStepValue(
        result: List<HealthSport>,
        resultAllUser: List<HealthSport>
    ) {
        val highestStepValue = result.sortedByDescending { it.stepValue }[0].stepValue
        text_view_max_step_value.text = highestStepValue.toString()
        var defeatedPercentage = "100"
        //超过的数量
        var countOfOver = 0
        if (resultAllUser.isNotEmpty()) {
            val groupBy = resultAllUser.groupBy { it.userId }
            var needCountOne = false
            for (entry in groupBy) {
                val otherUserMaxStepValue = entry.value.sortedByDescending { it.stepValue }[0]
                if (otherUserMaxStepValue.userId != userId) {
                    if (highestStepValue > otherUserMaxStepValue.stepValue) {
                        countOfOver++
                    }
                } else {
                    needCountOne = true
                }
            }
            val total = if (needCountOne) {
                groupBy.size - 1
            } else {
                groupBy.size
            }
            defeatedPercentage = DecimalFormat("0.##").format(countOfOver * 100F / total)
        }
        text_view_percentage_of_users_defeated.text = defeatedPercentage
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