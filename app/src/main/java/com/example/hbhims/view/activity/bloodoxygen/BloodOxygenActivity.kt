package com.example.hbhims.view.activity.bloodoxygen

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hbhims.App
import com.example.hbhims.R
import com.example.hbhims.model.common.util.MPAChartUtil
import com.example.hbhims.model.common.util.http.HttpCallBack
import com.example.hbhims.model.common.util.http.RequestCallBack
import com.example.hbhims.model.entity.HealthBloodOxygen
import com.example.hbhims.model.eventbus.HealthDataChange
import com.example.hbhims.view.base.ContainerActivity
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.youth.xframe.utils.XDateUtils
import com.youth.xframe.widget.XToast
import kotlinx.android.synthetic.main.activity_blood_oxygen.*
import org.greenrobot.eventbus.EventBus

class BloodOxygenActivity : ContainerActivity() {
    private lateinit var viewModel: BloodOxygenViewModel

    override fun preFinish(): Boolean {
        return true
    }

    override fun getOptionsMenuId(menu: Menu?): Int {
        return 0
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_blood_oxygen
    }

    override fun initData(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(BloodOxygenViewModel::class.java)
        viewModel.mldBloodOxygens.observe(this, Observer {
            updateBloodOxygenChart(it)
        })
        smart_refresh_layout.setOnRefreshListener {
            getUserAllBloodOxygenData(false)
        }
        getUserAllBloodOxygenData(true)
    }

    private fun getUserAllBloodOxygenData(needShowContent: Boolean) {
        viewModel.getUserAllBloodOxygenData(userId,
            object : RequestCallBack<List<HealthBloodOxygen>>() {
                override fun onSuccess(result: List<HealthBloodOxygen>) {
                    viewModel.mldBloodOxygens.value = result
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

    private fun showFailed() {
        smart_refresh_layout.finishRefresh(false)
        loadingView.setOnRetryClickListener {
            loadingView.showLoading()
            getUserAllBloodOxygenData(true)
        }.showError()
    }

    private fun showNoNetWork() {
        loadingView.setOnRetryClickListener {
            loadingView.showLoading()
            getUserAllBloodOxygenData(true)
        }.showNoNetwork()
    }

    private fun updateBloodOxygenChart(bloodOxygenList: List<HealthBloodOxygen>) {
        val entries = ArrayList<Entry>()
        val xValues = ArrayList<String>()
        bloodOxygenList.forEachIndexed { index, healthBloodOxygen ->
            entries.add(Entry(index.toFloat(), healthBloodOxygen.measureData.toFloat()))
            xValues.add(
                XDateUtils.millis2String(healthBloodOxygen.measureTime, "yyyy/MM/dd HH:mm:ss")
            )
        }
        line_chart_blood_oxygen.xAxis.labelRotationAngle = -10F
        MPAChartUtil.updateLineChart(line_chart_blood_oxygen,
            this,
            entries,
            xValues,
            "",
            MPAChartUtil.defaultChartColors,
            "",
            object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString() + getString(R.string.percent)
                }
            },
            object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString() + getString(R.string.percent)
                }
            })
    }

    override fun initView() {
        if (userId == App.user.id) {
            image_view_add_blood_oxygen.setOnClickListener {
                var bloodOxygenValue = viewModel.mldBloodOxygen.value?.measureData
                    ?: resources.getInteger(R.integer.default_blood_oxygen)
                AlertDialog.Builder(this).setCancelable(false)
                    .setTitle(getString(R.string.insert_blood_oxygen)).setView(
                        NumberPicker(this).apply {
                            setFormatter {
                                return@setFormatter "$it%"
                            }
                            minValue = 0
                            maxValue = 100
                            value = bloodOxygenValue
                            setOnValueChangedListener { _, _, newVal ->
                                bloodOxygenValue = newVal
                            }
                        }
                    )
                    .setPositiveButton(R.string.confirm) { _, _ ->
                        HealthBloodOxygen.insert(HealthBloodOxygen().apply {
                            userId = this@BloodOxygenActivity.userId
                            measureData = bloodOxygenValue
                            measureTime = System.currentTimeMillis()
                        }, object : HttpCallBack<HealthBloodOxygen>() {
                            override fun onSuccess(result: HealthBloodOxygen) {
                                viewModel.mldBloodOxygen.value = result
                                XToast.success(getString(R.string.insert_success))
                                EventBus.getDefault().post(HealthDataChange(result))
                            }

                            override fun onFailed(errorCode: Int, error: String) {
                                XToast.error("$errorCode\n$error")
                            }
                        })
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            }
        } else {
            image_view_add_blood_oxygen.visibility = View.GONE
        }
    }
}