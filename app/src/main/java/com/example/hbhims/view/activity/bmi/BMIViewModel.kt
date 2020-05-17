package com.example.hbhims.view.activity.bmi

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.hbhims.model.common.util.http.RequestCallBack
import com.example.hbhims.model.entity.HealthBmiStandards
import com.example.hbhims.model.entity.HealthHeight
import com.example.hbhims.model.entity.HealthWeight
import com.example.hbhims.view.custom.MyHealthAndroidViewModel
import com.youth.xframe.utils.XNetworkUtils
import com.youth.xframe.utils.log.XLog

class BMIViewModel(application: Application) : MyHealthAndroidViewModel(application) {

    val mldHeight = MutableLiveData<HealthHeight>()
    val mldWeight = MutableLiveData<HealthWeight>()
    val mldBMI = MutableLiveData<Float>()
    var bmiRealIndex = 0
    var bmiBefore = 0F
    var bmiAfter = 0F
    var bmiStandards: HealthBmiStandards? = null

    override fun initData() {
        bmiStandards = HealthBmiStandards().apply {
            bmiRange = "18.5,24.0,28.0"
        }
    }

    fun getBMIStandards(
        sex: Boolean, age: Int,
        requestCallBack: RequestCallBack<HealthBmiStandards>
    ) {
    }

    fun getUserLatestWeight(userId: Long, requestCallBack: RequestCallBack<HealthWeight>) {
        if (XNetworkUtils.isAvailable()) {
            HealthWeight.queryLatestByUserId(userId, requestCallBack)
        } else {
            requestCallBack.onNoNetWork()
        }
    }

    fun getAllUserWeight(userId: Long, requestCallBack: RequestCallBack<List<HealthWeight>>) {
        if (XNetworkUtils.isAvailable()) {
            HealthWeight.queryAllByUserId(userId, requestCallBack)
        } else {
            requestCallBack.onNoNetWork()
        }
    }

    fun getUserHeight(userId: Long, requestCallBack: RequestCallBack<HealthHeight>) {
        if (XNetworkUtils.isAvailable()) {
            HealthHeight.queryByUserId(userId, requestCallBack)
        } else {
            requestCallBack.onNoNetWork()
        }
    }

    fun calculateBMI() {
        val weight = mldWeight.value!!.weightData
        val height = mldHeight.value!!.heightData
        val bmi = weight / height / height

        val bmiRange = bmiStandards!!.bmiRange.split(",")
        bmiRange.forEachIndexed { index, s ->
            bmiRealIndex = index
            bmiBefore = if (index == 0) {
                0F
            } else {
                bmiRange[index - 1].toFloat()
            }
            bmiAfter = if (bmi > bmiRange[2].toFloat()) {
                bmiBefore = bmiRange[2].toFloat()
                bmiRealIndex = 3
                if (bmi > 50F) {
                    bmi
                } else {
                    50F
                }
            } else {
                s.toFloat()
            }
            if (bmi > bmiBefore && bmi <= bmiAfter) {
                XLog.d("BMI：bmi = $bmi")
                XLog.d("BMI：bmiBefore = $bmiBefore")
                XLog.d("BMI：bmiAfter = $bmiAfter")
                XLog.d("BMI：index = $index")
                XLog.d("BMI：bmiRealIndex = $bmiRealIndex")
                mldBMI.value = bmi
                if (bmiRealIndex != 1) {
                    setNotNormal()
                }
                return
            }
        }
    }

}