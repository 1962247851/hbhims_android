package com.example.hbhims.view.custom

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

abstract class MyHealthAndroidViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * 指标是否正常
     */
    val mldIsNormal = MutableLiveData<Boolean>()
    abstract fun initData()

    fun setNotNormal() {
        if (mldIsNormal.value != false) {
            mldIsNormal.value = false
        }
    }

    fun setNormal() {
        if (mldIsNormal.value != true) {
            mldIsNormal.value = true
        }
    }

}