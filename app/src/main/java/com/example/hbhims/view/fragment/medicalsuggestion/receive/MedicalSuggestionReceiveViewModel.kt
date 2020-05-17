package com.example.hbhims.view.fragment.medicalsuggestion.receive

import android.app.Application
import com.example.hbhims.model.entity.CustomMedicalSuggestion
import com.example.hbhims.view.custom.MyAndroidViewModel

class MedicalSuggestionReceiveViewModel(application: Application) :
    MyAndroidViewModel(application) {
    val customMedicalSuggestionList = ArrayList<CustomMedicalSuggestion>()
    var page = 0
    var size = 10

    override fun initData() {
    }

    fun resetPageAndSize() {
        page = 0
        size = 10
    }

}