package com.example.hbhims.view.activity.sleep

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hbhims.App
import com.example.hbhims.R
import com.example.hbhims.model.common.enums.ResultCode
import com.example.hbhims.model.common.util.MPAChartUtil
import com.example.hbhims.model.common.util.http.HttpCallBack
import com.example.hbhims.model.common.util.http.RequestCallBack
import com.example.hbhims.model.entity.HealthSleep
import com.example.hbhims.model.eventbus.HealthDataChange
import com.example.hbhims.view.base.ContainerActivity
import com.example.hbhims.view.custom.DateAndTimePicker
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.textfield.TextInputEditText
import com.youth.xframe.utils.XDateUtils
import com.youth.xframe.widget.XToast
import kotlinx.android.synthetic.main.activity_sleep.*
import org.greenrobot.eventbus.EventBus
import java.util.*

class SleepActivity : ContainerActivity() {

    private lateinit var viewModel: SleepViewModel

    override fun getLayoutId(): Int {
        return R.layout.activity_sleep
    }

    override fun initData(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(SleepViewModel::class.java)
        viewModel.mldHealthSleep.observe(this, Observer {
            //更新UI
            val startHour = XDateUtils.millis2String(it.startTime, "HH")
            val startMinute = XDateUtils.millis2String(it.startTime, "mm")
            val endHour = XDateUtils.millis2String(it.endTime, "HH")
            val endMinute = XDateUtils.millis2String(it.endTime, "mm")
            text_view_start_time_hour.text = startHour
            text_view_start_time_minute.text = startMinute
            text_view_end_time_hour.text = endHour
            text_view_end_time_minute.text = endMinute
            val twoDataDifference = XDateUtils.getTwoDataDifference(
                Date(it.endTime),
                Date(it.startTime)
            )
            text_view_total_time_hour.text = twoDataDifference.hour.toString()
            text_view_total_time_minute.text =
                (twoDataDifference.minute - twoDataDifference.hour * 60).toString()
        })
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        smart_refresh_layout.setOnRefreshListener {
            getUserSleepData(false)
            getUserAllSleepData(false)
        }
        if (userId == App.user.id) {
            image_view_add.setOnClickListener {
                val inflate = layoutInflater.inflate(R.layout.dialog_insert_sleep, null, false)
                var startTime =
                    viewModel.mldHealthSleep.value?.startTime ?: System.currentTimeMillis()
                var endTime = viewModel.mldHealthSleep.value?.endTime ?: System.currentTimeMillis()
                val tietStartTime = inflate.findViewById<TextInputEditText>(R.id.tiet_start_time)
                val tietEndTime = inflate.findViewById<TextInputEditText>(R.id.tiet_end_time)
                tietStartTime.setText(XDateUtils.millis2String(startTime, "yyyy/MM/dd HH:mm"))
                tietEndTime.setText(XDateUtils.millis2String(endTime, "yyyy/MM/dd HH:mm"))
                tietStartTime.setOnClickListener {
                    DateAndTimePicker(
                        this,
                        object : DateAndTimePicker.IDateAndTimePickerListener {
                            override fun onConfirm(timeMills: Long) {
                                startTime = timeMills
                                val millis2String =
                                    XDateUtils.millis2String(timeMills, "yyyy/MM/dd HH:mm")
                                tietStartTime.setText(millis2String)
                            }
                        },
                        startTime
                    ).show()
                }
                tietEndTime.setOnClickListener {
                    DateAndTimePicker(
                        this,
                        object : DateAndTimePicker.IDateAndTimePickerListener {
                            override fun onConfirm(timeMills: Long) {
                                endTime = timeMills
                                val millis2String =
                                    XDateUtils.millis2String(timeMills, "yyyy/MM/dd HH:mm")
                                tietEndTime.setText(millis2String)
                            }
                        },
                        endTime
                    ).show()
                }
                AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle(getString(R.string.insert_yesterday_sleep))
                    .setView(inflate)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(
                        R.string.confirm
                    ) { _, _ ->
                        if (endTime <= startTime) {
                            XToast.info("数据错误")
                        } else {
                            HealthSleep.insertOrReplace(
                                HealthSleep().apply {
                                    val calender = Calendar.getInstance()
                                    calender.add(Calendar.DATE, -1)
                                    userId = this@SleepActivity.userId
                                    date = calender.timeInMillis
                                    this.startTime = startTime
                                    this.endTime = endTime
                                }, object : HttpCallBack<HealthSleep>() {
                                    override fun onSuccess(result: HealthSleep) {
                                        viewModel.mldHealthSleep.value = result
                                        EventBus.getDefault().post(HealthDataChange(result))
                                        XToast.success(getString(R.string.update_success))
                                    }

                                    override fun onFailed(errorCode: Int, error: String) {
                                        XToast.error("$errorCode,$error")
                                    }
                                })
                        }
                    }.show()
            }
            material_card_view_analyze_sleep.setOnClickListener {
                startActivity(Intent(this, SleepStatisticActivity::class.java))
            }
        } else {
            image_view_add.visibility = View.GONE
            material_card_view_analyze_sleep.visibility = View.GONE
        }
        getUserSleepData(true)
        getUserAllSleepData(true)
    }

    private fun getUserSleepData(needShowContent: Boolean) {
        viewModel.getUserSleepData(userId, object : RequestCallBack<HealthSleep>() {
            override fun onSuccess(result: HealthSleep) {
                viewModel.mldHealthSleep.value = result
                showSuccess(needShowContent)
            }

            override fun onFailed(errorCode: Int, error: String) {
                if (errorCode == ResultCode.SUCCESS.code) {
                    showSuccess(needShowContent)
                } else {
                    showFailed()
                }
            }

            override fun onNoNetWork() {
                showNoNetWork()
            }
        })
    }

    private fun getUserAllSleepData(needShowContent: Boolean) {
        viewModel.getUserAllSleepData(userId, object : RequestCallBack<List<HealthSleep>>() {
            override fun onSuccess(result: List<HealthSleep>) {
                updateLineCharts(result)
                showSuccess(needShowContent)
            }

            override fun onFailed(errorCode: Int, error: String) {
                showFailed()
            }

            override fun onNoNetWork() {
                showNoNetWork()
            }
        })
    }

    private fun showSuccess(needShowContent: Boolean) {
        smart_refresh_layout.finishRefresh()
        if (needShowContent) {
            loadingView.showContent()
        }
    }

    private fun updateLineCharts(result: List<HealthSleep>) {
        val entriesStartTime = ArrayList<Entry>()
        val entriesEndTime = ArrayList<Entry>()
        val entriesTotalTime = ArrayList<BarEntry>()
        val xValuesDate = ArrayList<String>()
        result.forEachIndexed { index, healthSleep ->
            entriesStartTime.add(
                Entry(index.toFloat(), (healthSleep.startTime % 86400000).toFloat())
            )
            val endTime = healthSleep.endTime
            val endTimeHour = XDateUtils.millis2String(endTime, "HH").toFloat()
            val endTimeMinute = XDateUtils.millis2String(endTime, "mm").toFloat()
            entriesEndTime.add(
                Entry(index.toFloat(), endTimeHour * 3600000F + endTimeMinute * 60000F)
            )
            entriesTotalTime.add(BarEntry(index.toFloat(), healthSleep.totalTime.toFloat()))
            xValuesDate.add(XDateUtils.millis2String(healthSleep.date, "yyyy/MM/dd"))
        }
        MPAChartUtil.updateLineChart(
            line_chart_start_time,
            this,
            entriesStartTime,
            xValuesDate,
            "",
            MPAChartUtil.defaultChartColors,
            "",
            object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return XDateUtils.millis2String(value.toLong(), "HH:mm")
                }
            },
            object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return XDateUtils.millis2String(value.toLong(), "HH:mm")
                }
            }
        )
        line_chart_start_time.axisLeft.axisMinimum = 28800000F
        line_chart_start_time.axisLeft.axisMaximum = 86400000F + 28800000F
        MPAChartUtil.updateLineChart(
            line_chart_end_time, this, entriesEndTime, xValuesDate,
            "", MPAChartUtil.defaultChartColors, "",
            object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return XDateUtils.millis2String(value.toLong() + 57600000L, "HH:mm")
                }
            },
            object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return XDateUtils.millis2String(value.toLong() + 57600000L, "HH:mm")
                }
            }
        )
        line_chart_end_time.axisLeft.axisMaximum = 86400000F

        bar_chart_total_time.xAxis.labelRotationAngle = -10F
        MPAChartUtil.updateBarChart(
            bar_chart_total_time,
            this,
            entriesTotalTime,
            xValuesDate,
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
        bar_chart_total_time.setVisibleXRangeMaximum(6F)
    }

    private fun showNoNetWork() {
        loadingView.setOnRetryClickListener {
            loadingView.showLoading()
            getUserSleepData(true)
            getUserAllSleepData(true)
        }.showNoNetwork()
    }

    private fun showFailed() {
        smart_refresh_layout.finishRefresh(false)
        loadingView.setOnRetryClickListener {
            loadingView.showLoading()
            getUserAllSleepData(true)
            getUserAllSleepData(true)
        }.showError()
    }

    override fun preFinish(): Boolean {
        return true
    }

    override fun getOptionsMenuId(menu: Menu?): Int {
        return 0
    }

}