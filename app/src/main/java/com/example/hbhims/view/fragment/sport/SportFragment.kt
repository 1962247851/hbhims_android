package com.example.hbhims.view.fragment.sport

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.example.hbhims.App
import com.example.hbhims.R
import com.example.hbhims.model.common.util.http.HttpCallBack
import com.example.hbhims.model.entity.HealthSport
import com.example.hbhims.view.base.ContainerFragment
import com.google.android.material.textfield.TextInputEditText
import com.youth.xframe.utils.XNetworkUtils
import com.youth.xframe.widget.XToast
import kotlinx.android.synthetic.main.fragment_sport.*

class SportFragment : ContainerFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_sport
    }

    override fun initData(savedInstanceState: Bundle?) {
        completed_view.setmTotalProgress(8000)
    }

    override fun initView() {
        smart_refresh_layout.setOnRefreshListener {
            getUserSportData(false)
        }
        image_view_add.setOnClickListener {
            val inflate = layoutInflater.inflate(R.layout.dialog_insert_sport, null, false)
            var stepValue = 0
            val tiet = inflate.findViewById<TextInputEditText>(R.id.tied_step_value)
            tiet.addTextChangedListener {
                val toString = it.toString()
                stepValue = if (toString.isNotEmpty()) {
                    toString.toInt()
                } else {
                    0
                }
            }
            AlertDialog.Builder(requireContext())
                .setTitle("记录今日步数")
                .setView(inflate)
                .setNegativeButton("取消", null)
                .setPositiveButton(
                    "确定"
                ) { _, _ ->
                    if (stepValue != 0) {
                        HealthSport.insert(HealthSport().apply {
                            userId = App.user.id
                            date = System.currentTimeMillis()
                            this.stepValue = stepValue
                            distance = stepValue * 0.7F / 1000
                            calorie = stepValue / 20F
                        }, object : HttpCallBack<HealthSport>() {
                            override fun onSuccess(result: HealthSport) {
                                XToast.success("新增成功")
                            }

                            override fun onFailed(errorCode: Int, error: String) {
                                XToast.error("$errorCode,$error")
                            }
                        })
                    }
                }.show()
        }
        material_card_view_analyze_sport.setOnClickListener {
            XToast.success("分析")
        }
        getUserSportData(true)
    }

    private fun getUserSportData(needShowContent: Boolean) {
        if (XNetworkUtils.isAvailable()) {
            HealthSport.queryAllByUserIdBetween(
                App.user.id,
                null,
                null,
                object : HttpCallBack<List<HealthSport>>() {
                    override fun onSuccess(result: List<HealthSport>) {

                        //更新UI
                        var totalStep = 0
                        var totalCal = 0F
                        var totalDistance = 0F
                        result.forEach {
                            totalStep += it.stepValue
                            totalCal += it.calorie
                            totalDistance += it.distance
                        }
                        completed_view.setProgress(totalStep)
                        text_view_step_value.text = totalStep.toString()
                        text_view_cal.text = String.format("%.2f", totalCal)
                        text_view_distance.text = String.format("%.2f", totalDistance)

                        smart_refresh_layout.finishRefresh()
                        if (needShowContent) {
                            loadingView.showContent()
                        }
                    }

                    override fun onFailed(errorCode: Int, error: String) {
                        smart_refresh_layout.finishRefresh(false)
                        loadingView.setOnRetryClickListener {
                            loadingView.showLoading()
                            getUserSportData(true)
                        }.showError()
                    }
                })
        } else {
            loadingView.setOnRetryClickListener {
                loadingView.showLoading()
                getUserSportData(true)
            }.showNoNetwork()
        }
    }

}
