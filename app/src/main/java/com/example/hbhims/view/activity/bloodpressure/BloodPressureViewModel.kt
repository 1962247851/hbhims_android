package com.example.hbhims.view.activity.bloodpressure

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.hbhims.model.common.util.http.RequestCallBack
import com.example.hbhims.model.entity.HealthBloodPressure
import com.example.hbhims.view.custom.MyHealthAndroidViewModel
import com.youth.xframe.utils.XNetworkUtils

class BloodPressureViewModel(application: Application) : MyHealthAndroidViewModel(application) {

    val mldBloodPressures = MutableLiveData<List<HealthBloodPressure>>()
    val mldBloodPressure = MutableLiveData<HealthBloodPressure>()

    override fun initData() {

    }

    fun getUserAllBloodPressureData(
        userId: Long, requestCallBack: RequestCallBack<List<HealthBloodPressure>>
    ) {
        if (XNetworkUtils.isAvailable()) {
            HealthBloodPressure.queryAllByUserId(userId, requestCallBack)
        } else {
            requestCallBack.onNoNetWork()
        }
    }

    fun getUserLatestBloodPressureData(
        userId: Long, requestCallBack: RequestCallBack<HealthBloodPressure>
    ) {
        if (XNetworkUtils.isAvailable()) {
            HealthBloodPressure.queryLatestByUserId(userId, requestCallBack)
        } else {
            requestCallBack.onNoNetWork()
        }
    }

}