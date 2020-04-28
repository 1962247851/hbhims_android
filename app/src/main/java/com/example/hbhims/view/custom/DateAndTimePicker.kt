package com.example.hbhims.view.custom

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.viewpager.widget.PagerAdapter
import com.example.hbhims.R
import kotlinx.android.synthetic.main.dialog_date_and_time_picker.*
import java.util.*

/**
 * @author  qq1962247851
 * @date  2019/12/29 9:23
 *
 */
class DateAndTimePicker(
    context: Context,
    private var i: IDateAndTimePickerListener,
    private var timeMills: Long
) : AlertDialog(context), View.OnClickListener {

    private var layoutResID: Int = R.layout.dialog_date_and_time_picker
    private lateinit var adapter: Adapter

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_cancel -> {
                cancel()
            }
            R.id.button_confirm -> {
                adapter.getCalendar()[Calendar.SECOND] = 0
                i.onConfirm(adapter.getCalendar().timeInMillis)
                cancel()
            }
            R.id.button_today -> {
                adapter.getCalendar().timeInMillis = System.currentTimeMillis()
                adapter.getCalendar()[Calendar.SECOND] = 0
                i.onConfirm(adapter.getCalendar().timeInMillis)
                cancel()
            }
            else -> {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(layoutResID)
        adapter =
            Adapter(
                context,
                timeMills
            )
        view_pager.adapter = adapter
        initListener()
    }

    private fun initListener() {
        button_today.setOnClickListener(this)
        button_confirm.setOnClickListener(this)
        button_cancel.setOnClickListener(this)
    }

    class Adapter() : PagerAdapter() {

        private lateinit var context: Context
        private lateinit var datePicker: DatePicker
        private lateinit var timePicker: TimePicker
        private var calendar = Calendar.getInstance()

        constructor(context: Context, timeMills: Long) : this() {
            this.context = context
            datePicker = DatePicker(context)
            timePicker = TimePicker(context)
            initCalendar(timeMills)
            initDateAndTimePicker()
        }

        private fun initDateAndTimePicker() {
            datePicker.init(
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            ) { _, year, monthOfYear, dayOfMonth ->
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = monthOfYear
                calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            }
            timePicker.hour = calendar[Calendar.HOUR_OF_DAY]
            timePicker.minute = calendar[Calendar.MINUTE]
            timePicker.setIs24HourView(true)
            timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                calendar[Calendar.HOUR_OF_DAY] = hourOfDay
                calendar[Calendar.MINUTE] = minute
            }
        }

        private fun initCalendar(timeMills: Long) {
            calendar.timeInMillis = timeMills
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return if (position == 0) {
                context.getString(R.string.date)
            } else {
                context.getString(R.string.time)
            }
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            return if (position == 0) {
                container.addView(datePicker)
                datePicker
            } else {
                container.addView(timePicker)
                timePicker
            }
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return 2
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        }

        fun getCalendar(): Calendar {
            return calendar
        }
    }

    interface IDateAndTimePickerListener {
        fun onConfirm(timeMills: Long)
    }
}