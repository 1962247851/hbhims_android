package com.example.hbhims.view.activity.bloodsugar

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
import com.example.hbhims.model.entity.HealthBloodSugar
import com.example.hbhims.model.eventbus.HealthDataChange
import com.example.hbhims.view.base.ContainerActivity
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.youth.xframe.utils.XDateUtils
import com.youth.xframe.widget.XToast
import kotlinx.android.synthetic.main.activity_blood_sugar.*
import org.greenrobot.eventbus.EventBus
import java.text.DecimalFormat

class BloodSugarActivity : ContainerActivity() {

    private lateinit var viewModel: BloodSugarViewModel

    override fun preFinish(): Boolean {
        return true
    }

    override fun getOptionsMenuId(menu: Menu?): Int {
        return 0
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_blood_sugar
    }

    override fun initData(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(BloodSugarViewModel::class.java)
        viewModel.mldBloodSugars.observe(this, Observer {
            updateBloodSugarChart(it)
        })
        smart_refresh_layout.setOnRefreshListener {
            getUserAllBloodSugarData(false)
        }
        getUserAllBloodSugarData(true)
    }

    private fun getUserAllBloodSugarData(needShowContent: Boolean) {
        viewModel.getUserAllBloodSugarData(userId,
            object : RequestCallBack<List<HealthBloodSugar>>() {
                override fun onSuccess(result: List<HealthBloodSugar>) {
                    viewModel.mldBloodSugars.value = result
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
            getUserAllBloodSugarData(true)
        }.showError()
    }

    private fun showNoNetWork() {
        loadingView.setOnRetryClickListener {
            loadingView.showLoading()
            getUserAllBloodSugarData(true)
        }.showNoNetwork()
    }

    private fun updateBloodSugarChart(bloodSugarList: List<HealthBloodSugar>) {
        val entries = ArrayList<Entry>()
        val xValues = ArrayList<String>()
        bloodSugarList.forEachIndexed { index, healthBloodSugar ->
            entries.add(Entry(index.toFloat(), healthBloodSugar.measureData))
            xValues.add(
                XDateUtils.millis2String(healthBloodSugar.measureTime, "yyyy/MM/dd HH:mm:ss")
            )
        }
        line_chart_blood_sugar.xAxis.labelRotationAngle = -10F
        MPAChartUtil.updateLineChart(line_chart_blood_sugar,
            this,
            entries,
            xValues,
            "",
            MPAChartUtil.defaultChartColors,
            "",
            object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString() + getString(R.string.mmol_per_L)
                }
            },
            object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return DecimalFormat("0.#").format(value)
                }
            })
    }

    override fun initView() {
        if (userId == App.user.id) {
            image_view_add_blood_sugar.setOnClickListener {
                var bloodSugarNumber: Int = (viewModel.mldBloodSugar.value?.measureData
                    ?: resources.getInteger(R.integer.default_blood_sugar_number).toFloat()).toInt()
                var bloodSugarDecimal: Int =
                    ((viewModel.mldBloodSugar.value?.measureData
                        ?: resources.getInteger(R.integer.default_blood_sugar_decimal)
                            .toFloat()) * 10 % 10).toInt()
                val inflate =
                    layoutInflater.inflate(R.layout.dialog_weight_picker, null, false)
                inflate.findViewById<NumberPicker>(R.id.number_picker_weight_number)
                    .apply {
                        minValue = 0
                        maxValue = 20
                        value = bloodSugarNumber
                        setOnValueChangedListener { _, _, newVal ->
                            bloodSugarNumber = newVal
                        }
                    }
                inflate.findViewById<NumberPicker>(R.id.number_picker_weight_decimal)
                    .apply {
                        minValue = 0
                        maxValue = 9
                        value = bloodSugarDecimal
                        setOnValueChangedListener { _, _, newVal ->
                            bloodSugarDecimal = newVal
                        }
                    }
                AlertDialog.Builder(this).setCancelable(false)
                    .setTitle(getString(R.string.insert_blood_sugar)).setView(inflate)
                    .setPositiveButton(R.string.confirm) { _, _ ->
                        HealthBloodSugar.insert(HealthBloodSugar().apply {
                            userId = this@BloodSugarActivity.userId
                            measureData = bloodSugarNumber + bloodSugarDecimal.toFloat() / 10
                            measureTime = System.currentTimeMillis()
                        }, object : HttpCallBack<HealthBloodSugar>() {
                            override fun onSuccess(result: HealthBloodSugar) {
                                viewModel.mldBloodSugar.value = result
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
            image_view_add_blood_sugar.visibility = View.GONE
        }
    }
}