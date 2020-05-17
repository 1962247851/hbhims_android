package com.example.hbhims.view.activity.sleep

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.example.hbhims.R
import com.example.hbhims.model.entity.HealthSleep
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.youth.xframe.utils.XDateUtils

@SuppressLint("ViewConstructor")
class SleepMarkerView(context: Context, private val sleepList: List<HealthSleep>) :
    MarkerView(context, R.layout.marker_view_sleep) {

    private var textViewDuring = findViewById<TextView>(R.id.text_view_during_time)
    private var textViewTotal = findViewById<TextView>(R.id.text_view_total_time)

    @SuppressLint("SetTextI18n")
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        if (e != null) {
            val healthSleep = sleepList[e.x.toInt()]
            textViewDuring.text = XDateUtils.millis2String(healthSleep.startTime, "HH:mm") +
                    "-" + XDateUtils.millis2String(healthSleep.endTime, "HH:mm")
            val totalTime = healthSleep.totalTime
            val hour = totalTime.toLong() / 3600000
            val minute = (totalTime.toLong() - hour * 3600000) / 60000
            textViewTotal.text =
                hour.toInt().toString() + context.getString(R.string.hour) + minute.toInt()
                    .toString() + context.getString(R.string.minute)
        }
        super.refreshContent(e, highlight)
    }
}