package com.example.hbhims.view.activity.bmi

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hbhims.R
import com.example.hbhims.model.common.enums.ResultCode
import com.example.hbhims.model.common.util.MPAChartUtil
import com.example.hbhims.model.common.util.http.HttpCallBack
import com.example.hbhims.model.common.util.http.RequestCallBack
import com.example.hbhims.model.entity.HealthHeight
import com.example.hbhims.model.entity.HealthWeight
import com.example.hbhims.model.eventbus.HealthDataChange
import com.example.hbhims.view.base.ContainerActivity
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.textfield.TextInputEditText
import com.youth.xframe.utils.XDateUtils
import com.youth.xframe.utils.log.XLog
import com.youth.xframe.widget.XToast
import kotlinx.android.synthetic.main.activity_bmi.*
import org.greenrobot.eventbus.EventBus
import java.text.DecimalFormat

class BMIActivity : ContainerActivity() {

    private lateinit var viewModel: BMIViewModel

    override fun preFinish(): Boolean {
        return true
    }

    override fun getOptionsMenuId(menu: Menu?): Int {
        return 0
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_bmi
    }

    override fun initData(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(BMIViewModel::class.java)
        viewModel.initData()
        viewModel.mldHeight.observe(this, Observer {
            if (viewModel.mldWeight.value != null) {
                viewModel.calculateBMI()
            }
        })
        viewModel.mldWeight.observe(this, Observer {
            if (viewModel.mldHeight.value != null) {
                viewModel.calculateBMI()
            }
        })
        viewModel.mldBMI.observe(this, Observer {
            text_view_bmi.text = DecimalFormat("0.00").format(it)
            updateBMIBar(it)
        })
    }

    private fun updateBMIBar(bmi: Float) {
        when (viewModel.bmiRealIndex) {
            0 -> {
                bubble_layout.setmBackGroundColor(getColor(R.color.weight_thin))
            }
            1 -> {
                bubble_layout.setmBackGroundColor(getColor(R.color.weight_normal))
            }
            2 -> {
                bubble_layout.setmBackGroundColor(getColor(R.color.weight_fatter))
            }
            else -> {
                bubble_layout.setmBackGroundColor(getColor(R.color.weight_over_weight))
            }
        }
        linear_layout.setPadding(
            ((viewModel.bmiRealIndex + (bmi - viewModel.bmiBefore) / (viewModel.bmiAfter - viewModel.bmiBefore)) * view_bmi_bar_one.width).toInt(),
            0,
            0,
            0
        )
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        val bmiRange = viewModel.bmiStandards!!.bmiRange.split(",")
        text_view_bmi_1.text = bmiRange[0]
        text_view_bmi_2.text = bmiRange[1]
        text_view_bmi_3.text = bmiRange[2]
        image_view_add.setOnClickListener {
            val inflate =
                layoutInflater.inflate(R.layout.dialog_update_height_weight, null, false)
            val heightTextInputEditText =
                inflate.findViewById<TextInputEditText>(R.id.tiet_height).apply {
                    setText(
                        (viewModel.mldHeight.value?.heightData
                            ?: resources.getInteger(R.integer.default_height) / 100F).toString()
                    )
                }
            val weightTextInputEditText =
                inflate.findViewById<TextInputEditText>(R.id.tiet_weight).apply {
                    if (viewModel.mldWeight.value == null) {
                        setText(
                            resources.getInteger(R.integer.default_weight_number)
                                .toString() + "." + resources.getInteger(R.integer.default_weight_decimal)
                                .toString()
                        )
                    } else {
                        setText(viewModel.mldWeight.value?.weightData.toString())
                    }
                }
            heightTextInputEditText.setOnClickListener {
                var height: Float = viewModel.mldHeight.value?.heightData
                    ?: resources.getInteger(R.integer.default_height) / 100
                        .toFloat()
                AlertDialog.Builder(this).setCancelable(false)
                    .setTitle(getString(R.string.height_m)).setView(
                        NumberPicker(this).apply {
                            minValue = 50
                            maxValue = 250
                            setFormatter {
                                return@setFormatter String.format("%.2f", it.toFloat() / 100)
                            }
                            value = (height * 100).toInt()
                            setOnValueChangedListener { _, _, newVal ->
                                height = newVal.toFloat() / 100
                            }
                        }
                    ).setPositiveButton(R.string.confirm) { _, _ ->
                        HealthHeight.insertOrReplace(HealthHeight().apply {
                            userId = this@BMIActivity.userId
                            heightData = height
                            measureTime = System.currentTimeMillis()
                        }, object : HttpCallBack<HealthHeight>() {
                            override fun onSuccess(result: HealthHeight) {
                                viewModel.mldHeight.value = result
                                heightTextInputEditText.setText(
                                    String.format("%.2f", result.heightData)
                                )
                                XToast.success(getString(R.string.update_success))
                            }

                            override fun onFailed(errorCode: Int, error: String) {
                                XToast.error("$errorCode\n$error")
                            }
                        })
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            }
            weightTextInputEditText.setOnClickListener {
                var weightNumber: Int = (viewModel.mldWeight.value?.weightData
                    ?: resources.getInteger(R.integer.default_weight_number).toFloat()).toInt()
                var weightDecimal: Int =
                    ((viewModel.mldWeight.value?.weightData
                        ?: resources.getInteger(R.integer.default_weight_decimal)
                            .toFloat()) * 10 % 10).toInt()
                val weightInflate =
                    layoutInflater.inflate(R.layout.dialog_weight_picker, null, false)
                weightInflate.findViewById<NumberPicker>(R.id.number_picker_weight_number)
                    .apply {
                        minValue = 20
                        maxValue = 250
                        value = weightNumber
                        setOnValueChangedListener { _, _, newVal ->
                            weightNumber = newVal
                            XLog.d("weightNumber $newVal")
                            XLog.d("weight ${weightNumber + weightDecimal.toFloat() / 10}")
                        }
                    }
                weightInflate.findViewById<NumberPicker>(R.id.number_picker_weight_decimal)
                    .apply {
                        minValue = 0
                        maxValue = 9
                        value = weightDecimal
                        setOnValueChangedListener { _, _, newVal ->
                            weightDecimal = newVal
                            XLog.d("weightDecimal $newVal")
                            XLog.d("weight ${weightNumber + weightDecimal.toFloat() / 10}")
                        }
                    }
                AlertDialog.Builder(this).setCancelable(false)
                    .setTitle(getString(R.string.weight_kg)).setView(weightInflate)
                    .setPositiveButton(R.string.confirm) { _, _ ->
                        HealthWeight.insert(HealthWeight().apply {
                            userId = this@BMIActivity.userId
                            weightData = weightNumber + weightDecimal.toFloat() / 10
                            measureTime = System.currentTimeMillis()
                        }, object : HttpCallBack<HealthWeight>() {
                            override fun onSuccess(result: HealthWeight) {
                                viewModel.mldWeight.value = result
                                weightTextInputEditText.setText(
                                    String.format("%.1f", result.weightData)
                                )
                                XToast.success(getString(R.string.update_success))
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
            AlertDialog.Builder(this).setTitle(getString(R.string.update_height_and_weight))
                .setView(inflate)
                .setPositiveButton(R.string.back, null).show()
        }
        smart_refresh_layout.setOnRefreshListener {
            getUserHeight(false)
            getUserWeight(false)
            getAllUserWeight(false)
        }
        getUserHeight(true)
        getUserWeight(true)
        getAllUserWeight(true)
    }

    private fun getUserHeight(needShowContent: Boolean) {
        viewModel.getUserHeight(userId, object : RequestCallBack<HealthHeight>() {
            override fun onSuccess(result: HealthHeight) {
                viewModel.mldHeight.value = result
                showSuccess(needShowContent)
            }

            override fun onFailed(errorCode: Int, error: String) {
                if (errorCode == ResultCode.SUCCESS.code) {
                    showSuccess(needShowContent)
                } else {
                    showError()
                }
            }

            override fun onNoNetWork() {
                showNoNetWork()
            }
        })
    }

    private fun getUserWeight(needShowContent: Boolean) {
        viewModel.getUserLatestWeight(userId, object : RequestCallBack<HealthWeight>() {
            override fun onSuccess(result: HealthWeight) {
                viewModel.mldWeight.value = result
                showSuccess(needShowContent)
            }

            override fun onFailed(errorCode: Int, error: String) {
                if (errorCode == ResultCode.SUCCESS.code) {
                    showSuccess(needShowContent)
                } else {
                    showError()
                }
            }

            override fun onNoNetWork() {
                showNoNetWork()
            }
        })
    }

    private fun getAllUserWeight(needShowContent: Boolean) {
        viewModel.getAllUserWeight(userId, object : RequestCallBack<List<HealthWeight>>() {
            override fun onSuccess(result: List<HealthWeight>) {
                updateWeightChart(result)
                showSuccess(needShowContent)
            }

            override fun onFailed(errorCode: Int, error: String) {
                showError()
            }

            override fun onNoNetWork() {
                showNoNetWork()
            }
        })
    }

    private fun updateWeightChart(weightList: List<HealthWeight>) {
        val entries = ArrayList<Entry>()
        val xValues = ArrayList<String>()
        weightList.forEachIndexed { index, healthWeight ->
            entries.add(Entry(index.toFloat(), healthWeight.weightData))
            xValues.add(XDateUtils.millis2String(healthWeight.measureTime, "yyyy/MM/dd HH:mm:ss"))
        }
        line_chart_weight.xAxis.labelRotationAngle = -10F
        MPAChartUtil.updateLineChart(
            line_chart_weight,
            this,
            entries,
            xValues,
            "",
            MPAChartUtil.defaultChartColors,
            "",
            object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString() + getString(R.string.kg)
                }
            },
            object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return DecimalFormat("0.#").format(value) + getString(R.string.kg)
                }
            }
        )
    }

    private fun showSuccess(needShowContent: Boolean) {
        smart_refresh_layout.finishRefresh()
        if (needShowContent) {
            loadingView.showContent()
        }
    }

    private fun showError() {
        smart_refresh_layout.finishRefresh(false)
        loadingView.setOnRetryClickListener {
            loadingView.showLoading()
            getUserWeight(true)
            getUserHeight(true)
            getAllUserWeight(true)
        }.showError()
    }

    private fun showNoNetWork() {
        loadingView.setOnRetryClickListener {
            loadingView.showLoading()
            getUserHeight(true)
            getUserWeight(true)
            getAllUserWeight(true)
        }.showNoNetwork()
    }

}