package com.example.hbhims.view.activity.bloodpressure

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
import com.example.hbhims.model.entity.HealthBloodPressure
import com.example.hbhims.model.eventbus.HealthDataChange
import com.example.hbhims.view.base.ContainerActivity
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.textfield.TextInputEditText
import com.youth.xframe.utils.XDateUtils
import com.youth.xframe.widget.XToast
import kotlinx.android.synthetic.main.activity_blood_pressure.*
import org.greenrobot.eventbus.EventBus

class BloodPressureActivity : ContainerActivity() {

    private lateinit var viewModel: BloodPressureViewModel

    override fun preFinish(): Boolean {
        return true
    }

    override fun getOptionsMenuId(menu: Menu?): Int {
        return 0
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_blood_pressure
    }

    override fun initData(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(BloodPressureViewModel::class.java)
        viewModel.mldBloodPressures.observe(this, Observer {
            updateBloodPressureChart(it)
        })
        smart_refresh_layout.setOnRefreshListener {
            getUserAllBloodPressureData(false)
        }
        getUserAllBloodPressureData(true)
    }

    private fun getUserAllBloodPressureData(needShowContent: Boolean) {
        viewModel.getUserAllBloodPressureData(userId,
            object : RequestCallBack<List<HealthBloodPressure>>() {
                override fun onSuccess(result: List<HealthBloodPressure>) {
                    viewModel.mldBloodPressures.value = result
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
            getUserAllBloodPressureData(true)
        }.showError()
    }

    private fun showNoNetWork() {
        loadingView.setOnRetryClickListener {
            loadingView.showLoading()
            getUserAllBloodPressureData(true)
        }.showNoNetwork()
    }

    private fun updateBloodPressureChart(bloodPressureList: List<HealthBloodPressure>) {
        val entriesList = ArrayList<List<Entry>>()
        val lowPressureEntries = ArrayList<Entry>()
        val highPressureEntries = ArrayList<Entry>()
        val xValues = ArrayList<String>()
        bloodPressureList.forEachIndexed { index, healthBloodPressure ->
            lowPressureEntries.add(
                Entry(
                    index.toFloat(),
                    healthBloodPressure.lowPressure.toFloat()
                )
            )
            highPressureEntries.add(
                Entry(
                    index.toFloat(),
                    healthBloodPressure.highPressure.toFloat()
                )
            )
            xValues.add(
                XDateUtils.millis2String(
                    healthBloodPressure.measureTime,
                    "yyyy/MM/dd HH:mm:ss"
                )
            )
        }
        entriesList.add(highPressureEntries)
        entriesList.add(lowPressureEntries)
        line_chart_blood_pressure.xAxis.labelRotationAngle = -10F
        MPAChartUtil.updateMultiLineChart(line_chart_blood_pressure,
            this,
            entriesList,
            xValues,
            listOf(getString(R.string.blood_pressure_high), getString(R.string.blood_pressure_low)),
            "",
            object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString() + getString(R.string.mmhg)
                }
            },
            object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString()
                }
            })
    }

    override fun initView() {
        if (userId == App.user.id) {
            image_view_add_pressure.setOnClickListener {
                var highPressure = 100
                var lowPressure = 100
                val inflate =
                    layoutInflater.inflate(R.layout.dialog_insert_blood_pressure, null, false)
                inflate.findViewById<TextInputEditText>(R.id.tiet_blood_pressure_high).apply {
                    setText(highPressure.toString())
                    setOnClickListener {
                        var selected = highPressure
                        AlertDialog.Builder(this@BloodPressureActivity).setCancelable(false)
                            .setTitle(R.string.blood_pressure_high_mmHg).setView(
                                NumberPicker(this@BloodPressureActivity).apply {
                                    minValue = 0
                                    maxValue = 200
                                    value = highPressure
                                    setOnValueChangedListener { _, _, newVal ->
                                        selected = newVal
                                    }
                                }
                            ).setPositiveButton(R.string.confirm) { _, _ ->
                                highPressure = selected
                                setText(selected.toString())

                            }.setNegativeButton(R.string.cancel, null).show()
                    }
                }
                inflate.findViewById<TextInputEditText>(R.id.tiet_blood_pressure_low).apply {
                    setText(lowPressure.toString())
                    setOnClickListener {
                        var selected = lowPressure
                        AlertDialog.Builder(this@BloodPressureActivity).setCancelable(false)
                            .setTitle(R.string.blood_pressure_low_mmHg).setView(
                                NumberPicker(this@BloodPressureActivity).apply {
                                    minValue = 0
                                    maxValue = 200
                                    value = lowPressure
                                    setOnValueChangedListener { _, _, newVal ->
                                        selected = newVal
                                        setText(selected.toString())
                                    }
                                }
                            ).setPositiveButton(R.string.confirm) { _, _ ->
                                lowPressure = selected
                            }.setNegativeButton(R.string.cancel, null).show()
                    }
                }
                AlertDialog.Builder(this).setTitle(R.string.insert_blood_pressure)
                    .setView(inflate).setPositiveButton(R.string.confirm) { _, _ ->
                        HealthBloodPressure.insert(HealthBloodPressure().apply {
                            userId = this@BloodPressureActivity.userId
                            this.highPressure = highPressure
                            this.lowPressure = lowPressure
                            this.measureTime = System.currentTimeMillis()
                        }, object : HttpCallBack<HealthBloodPressure>() {
                            override fun onSuccess(result: HealthBloodPressure) {
                                EventBus.getDefault().post(HealthDataChange(result))
                                XToast.success(getString(R.string.insert_success))
                            }

                            override fun onFailed(errorCode: Int, error: String) {
                                XToast.error("$errorCode\n$error")
                            }
                        })
                    }.setNegativeButton(R.string.cancel, null).show()
            }
        } else {
            image_view_add_pressure.visibility = View.GONE
        }
    }

}