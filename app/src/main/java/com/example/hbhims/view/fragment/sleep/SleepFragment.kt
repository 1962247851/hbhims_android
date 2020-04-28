package com.example.hbhims.view.fragment.sleep

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hbhims.App
import com.example.hbhims.R
import com.example.hbhims.model.common.util.http.HttpCallBack
import com.example.hbhims.model.entity.HealthSleep
import com.example.hbhims.view.base.ContainerFragment
import com.example.hbhims.view.custom.DateAndTimePicker
import com.google.android.material.textfield.TextInputEditText
import com.youth.xframe.utils.XDateUtils
import com.youth.xframe.utils.XNetworkUtils
import com.youth.xframe.widget.XToast
import kotlinx.android.synthetic.main.fragment_sleep.*
import java.util.*

class SleepFragment : ContainerFragment() {

    private lateinit var sleepViewModel: SleepViewModel

    override fun getLayoutId(): Int {
        return R.layout.fragment_sleep
    }

    override fun initData(savedInstanceState: Bundle?) {
        sleepViewModel = ViewModelProvider.NewInstanceFactory().create(SleepViewModel::class.java)
        sleepViewModel.mldHealthSleep.observe(this, Observer {
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
        }
        image_view_add.setOnClickListener {
            val inflate = layoutInflater.inflate(R.layout.dialog_insert_sleep, null, false)
            var startTime = sleepViewModel.mldHealthSleep.value!!.startTime
            var endTime = sleepViewModel.mldHealthSleep.value!!.endTime
            val tietStartTime = inflate.findViewById<TextInputEditText>(R.id.tied_start_time)
            val tietEndTime = inflate.findViewById<TextInputEditText>(R.id.tied_end_time)
            tietStartTime.setText(XDateUtils.millis2String(startTime, "yyyy/MM/dd HH:mm"))
            tietEndTime.setText(XDateUtils.millis2String(endTime, "yyyy/MM/dd HH:mm"))
            tietStartTime.setOnClickListener {
                DateAndTimePicker(
                    requireContext(),
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
                    requireContext(),
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
            AlertDialog.Builder(requireContext())
                .setTitle("记录昨晚睡眠情况")
                .setView(inflate)
                .setNegativeButton("取消", null)
                .setPositiveButton(
                    "确定"
                ) { _, _ ->
                    if (endTime <= startTime) {
                        XToast.info("数据错误")
                    } else {
                        HealthSleep.insertOrReplace(
                            HealthSleep().apply {
                                val calender = Calendar.getInstance()
                                calender.roll(Calendar.DAY_OF_MONTH, -1)
                                userId = App.user.id
                                date = calender.timeInMillis
                                this.startTime = startTime
                                this.endTime = endTime
                            }, object : HttpCallBack<HealthSleep>() {
                                override fun onSuccess(result: HealthSleep) {
                                    sleepViewModel.mldHealthSleep.value = result
                                    XToast.success("更新成功")
                                }

                                override fun onFailed(errorCode: Int, error: String) {
                                    XToast.error("$errorCode,$error")
                                }
                            })
                    }
                }.show()
        }
        material_card_view_analyze_sleep.setOnClickListener {
            XToast.success("分析")
        }
        getUserSleepData(true)
    }

    private fun getUserSleepData(needShowContent: Boolean) {
        if (XNetworkUtils.isAvailable()) {
            HealthSleep.queryByUserIdAndDate(
                App.user.id,
                null,
                object : HttpCallBack<HealthSleep>() {
                    override fun onSuccess(result: HealthSleep) {
                        sleepViewModel.mldHealthSleep.value = result
                        smart_refresh_layout.finishRefresh()
                        if (needShowContent) {
                            loadingView.showContent()
                        }
                    }

                    override fun onFailed(errorCode: Int, error: String) {
                        smart_refresh_layout.finishRefresh(false)
                        loadingView.setOnRetryClickListener {
                            loadingView.showLoading()
                            getUserSleepData(true)
                        }.showError()
                    }
                })
        } else {
            loadingView.setOnRetryClickListener {
                loadingView.showLoading()
                getUserSleepData(true)
            }.showNoNetwork()
        }
    }
}