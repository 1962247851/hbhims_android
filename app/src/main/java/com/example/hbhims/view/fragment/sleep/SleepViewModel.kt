package com.example.hbhims.view.fragment.sleep

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hbhims.model.entity.HealthSleep

class SleepViewModel : ViewModel() {

    val mldHealthSleep = MutableLiveData<HealthSleep>()

}