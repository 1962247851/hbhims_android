package com.example.hbhims.view.fragment.health

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hbhims.App
import com.example.hbhims.R
import com.example.hbhims.model.common.Constant
import com.example.hbhims.model.common.entity.JsonResult
import com.example.hbhims.model.common.enums.ResultCode
import com.example.hbhims.model.common.util.http.Http
import com.example.hbhims.model.common.util.http.RequestCallBack
import com.example.hbhims.model.entity.*
import com.example.hbhims.model.eventbus.EveryDayStepValueChange
import com.example.hbhims.model.eventbus.HealthDataChange
import com.example.hbhims.model.eventbus.StepValueChange
import com.example.hbhims.view.activity.bloodoxygen.BloodOxygenActivity
import com.example.hbhims.view.activity.bloodoxygen.BloodOxygenViewModel
import com.example.hbhims.view.activity.bloodpressure.BloodPressureActivity
import com.example.hbhims.view.activity.bloodpressure.BloodPressureViewModel
import com.example.hbhims.view.activity.bloodsugar.BloodSugarActivity
import com.example.hbhims.view.activity.bloodsugar.BloodSugarViewModel
import com.example.hbhims.view.activity.bmi.BMIActivity
import com.example.hbhims.view.activity.bmi.BMIViewModel
import com.example.hbhims.view.activity.selectprofessional.SelectProfessionalActivity
import com.example.hbhims.view.activity.sleep.SleepActivity
import com.example.hbhims.view.activity.sleep.SleepViewModel
import com.example.hbhims.view.activity.sport.SportActivity
import com.example.hbhims.view.activity.sport.SportViewModel
import com.example.hbhims.view.base.AbstractFragment
import com.example.hbhims.view.custom.LoadingDialog
import com.youth.xframe.utils.XDateUtils
import com.youth.xframe.utils.XFormatTimeUtils
import com.youth.xframe.utils.XPreferencesUtils
import com.youth.xframe.utils.log.XLog
import com.youth.xframe.widget.XToast
import kotlinx.android.synthetic.main.fragment_health.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat
import java.util.*


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USER_ID = "USER_ID"

/**
 * A simple [Fragment] subclass.
 * Use the [HealthFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HealthFragment : AbstractFragment(), View.OnClickListener {

    private lateinit var sportViewModel: SportViewModel
    private lateinit var sleepViewModel: SleepViewModel
    private lateinit var bmiViewModel: BMIViewModel
    private lateinit var bloodPressureViewModel: BloodPressureViewModel
    private lateinit var bloodSugarViewModel: BloodSugarViewModel
    private lateinit var bloodOxygenViewModel: BloodOxygenViewModel
    private var notNormalCount = 0
    private var userId: Long = App.user.id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getLong(ARG_USER_ID)
        }
    }

    override fun getLayoutId(): Int {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        return R.layout.fragment_health
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onHealthDataChange(healthDataChange: HealthDataChange) {
        when (val healthData = healthDataChange.healthData) {
            is HealthSport -> {

            }
            is HealthSleep -> {
                sleepViewModel.mldHealthSleep.value = healthData
            }
            is HealthWeight -> {
                bmiViewModel.mldWeight.value = healthData
            }
            is HealthBloodPressure -> {
                bloodPressureViewModel.mldBloodPressure.value = healthData
            }
            is HealthBloodSugar -> {
                bloodSugarViewModel.mldBloodSugar.value = healthData
            }
            is HealthBloodOxygen -> {
                bloodOxygenViewModel.mldBloodOxygen.value = healthData
            }
            else -> {
                //ignore
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onStepValueChange(stepValueChange: StepValueChange) {
        sportViewModel.todayStepValue.value = stepValueChange.stepValue
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEveryDayStepValueChange(everyDayStepValueChange: EveryDayStepValueChange) {
        val everyDayStepValue = everyDayStepValueChange.everyDayStepValue
        sportViewModel.everyDayStepValue = everyDayStepValue
        text_view_step_value_total.text = everyDayStepValue.toString()
        completed_view_sport.setmTotalProgress(everyDayStepValue)
        text_view_step_percent.text =
            (1.0 * sportViewModel.todayStepValue.value!! / everyDayStepValue * 100).toInt()
                .toString()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        initSport()
        initSleep()
        initBMI()
        initBloodPressure()
        initBloodSugar()
        initBloodOxygen()
        bmiViewModel.mldIsNormal.observe(this, Observer {
            XLog.d("bmiViewModel.mldIsNormal $it")
            updateNotNormalCount(it)
        })
        bloodPressureViewModel.mldIsNormal.observe(this, Observer {
            XLog.d("bloodPressureViewModel.mldIsNormal $it")
            updateNotNormalCount(it)
        })
        bloodSugarViewModel.mldIsNormal.observe(this, Observer {
            XLog.d("bloodSugarViewModel.mldIsNormal $it")
            updateNotNormalCount(it)
        })
        bloodOxygenViewModel.mldIsNormal.observe(this, Observer {
            XLog.d("bloodOxygenViewModel.mldIsNormal $it")
            updateNotNormalCount(it)
        })
    }

    private fun initBloodPressure() {
        bloodPressureViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(BloodPressureViewModel::class.java)
        bloodPressureViewModel.mldBloodPressure.observe(this, Observer {
            text_view_blood_pressure_high.text = it.highPressure.toString()
            text_view_blood_pressure_low.text = it.lowPressure.toString()
            text_view_blood_pressure_update_time.text =
                XFormatTimeUtils.getTimeSpanByNow1(it.measureTime)
            when {
                it.highPressure > 120 -> {
                    bloodPressureViewModel.setNotNormal()
                    text_view_blood_pressure_status.setText(R.string.status_health_blood_pressure_high_over)
                    text_view_blood_pressure_status.setBackgroundResource(R.drawable.background_status_over)
                }
                it.lowPressure < 60 -> {
                    bloodPressureViewModel.setNotNormal()
                    text_view_blood_pressure_status.setText(R.string.status_health_blood_pressure_low_less)
                    text_view_blood_pressure_status.setBackgroundResource(R.drawable.background_status_less)
                }
                else -> {
                    bloodPressureViewModel.setNormal()
                    text_view_blood_pressure_status.setText(R.string.normal)
                    text_view_blood_pressure_status.setBackgroundResource(R.drawable.background_status)
                }
            }
        })
        updateBloodPressureData()
    }

    private fun updateBloodPressureData() {
        bloodPressureViewModel.getUserLatestBloodPressureData(
            userId, object : RequestCallBack<HealthBloodPressure>() {
                override fun onSuccess(result: HealthBloodPressure) {
                    bloodPressureViewModel.mldBloodPressure.value = result
                    showSuccess()
                }

                override fun onFailed(errorCode: Int, error: String) {
                    showFailed(errorCode, error)
                }

                override fun onNoNetWork() {
                    showNoNetWork()
                }
            })
    }

    private fun initBloodSugar() {
        bloodSugarViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(BloodSugarViewModel::class.java)
        bloodSugarViewModel.mldBloodSugar.observe(this, Observer {
            text_view_blood_sugar_value.text = DecimalFormat("0.#").format(it.measureData)
            text_view_blood_sugar_update_time.text =
                XFormatTimeUtils.getTimeSpanByNow1(it.measureTime)
            when {
                it.measureData >= 7 -> {
                    bloodSugarViewModel.setNotNormal()
                    text_view_blood_sugar_status.setText(R.string.status_health_blood_sugar_over)
                    text_view_blood_sugar_status.setBackgroundResource(R.drawable.background_status_over)
                }
                it.measureData <= 3.9 -> {
                    bloodSugarViewModel.setNotNormal()
                    text_view_blood_sugar_status.setText(R.string.status_health_blood_sugar_less)
                    text_view_blood_sugar_status.setBackgroundResource(R.drawable.background_status_less)
                }
                else -> {
                    bloodSugarViewModel.setNormal()
                    text_view_blood_sugar_status.setText(R.string.normal)
                    text_view_blood_sugar_status.setBackgroundResource(R.drawable.background_status)
                }
            }
        })
        updateBloodSugarData()
    }

    private fun updateBloodSugarData() {
        bloodSugarViewModel.getUserLatestBloodSugarData(userId,
            object : RequestCallBack<HealthBloodSugar>() {
                override fun onSuccess(result: HealthBloodSugar) {
                    bloodSugarViewModel.mldBloodSugar.value = result
                    showSuccess()
                }

                override fun onFailed(errorCode: Int, error: String) {
                    showFailed(errorCode, error)
                }

                override fun onNoNetWork() {
                    showNoNetWork()
                }
            })
    }

    private fun initBloodOxygen() {
        bloodOxygenViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(BloodOxygenViewModel::class.java)
        completed_view_blood_oxygen.setmTotalProgress(100)
        bloodOxygenViewModel.mldBloodOxygen.observe(this, Observer {
            completed_view_blood_oxygen.setProgress(it.measureData)
            text_view_blood_oxygen_value.text = it.measureData.toString()
            text_view_blood_oxygen_update_time.text =
                XFormatTimeUtils.getTimeSpanByNow1(it.measureTime)
            if (it.measureData >= 95) {
                bloodOxygenViewModel.setNormal()
                text_view_blood_oxygen_status.setText(R.string.normal)
                text_view_blood_oxygen_status.setBackgroundResource(R.drawable.background_status)
            } else {
                bloodOxygenViewModel.setNotNormal()
                text_view_blood_oxygen_status.setText(R.string.status_health_blood_oxygen_less)
                text_view_blood_oxygen_status.setBackgroundResource(R.drawable.background_status_less)
            }
        })
        updateBloodOxygenData()
    }

    private fun updateBloodOxygenData() {
        bloodOxygenViewModel.getUserLatestBloodOxygenData(userId,
            object : RequestCallBack<HealthBloodOxygen>() {
                override fun onSuccess(result: HealthBloodOxygen) {
                    bloodOxygenViewModel.mldBloodOxygen.value = result
                    showSuccess()
                }

                override fun onFailed(errorCode: Int, error: String) {
                    showFailed(errorCode, error)
                }

                override fun onNoNetWork() {
                    showNoNetWork()
                }
            })
    }

    private fun initSport() {
        val everyDayStepValue = XPreferencesUtils.get(
            getString(R.string.key_health_every_day_step_value),
            resources.getInteger(R.integer.default_every_day_step_value)
        ) as Int
        text_view_step_value_total.text = everyDayStepValue.toString()
        completed_view_sport.setmTotalProgress(everyDayStepValue)
        sportViewModel = ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(SportViewModel::class.java)
        sportViewModel.everyDayStepValue = everyDayStepValue
        sportViewModel.todayStepValue.observe(this, Observer {
            text_view_step_value_today.text = it.toString()
            completed_view_sport.setProgress(it)
            text_view_step_percent.text =
                (1.0 * it / sportViewModel.everyDayStepValue * 100).toInt().toString()
        })
        updateSportData()
    }

    private fun updateSportData() {
        sportViewModel.todayStepValue.value = XPreferencesUtils.get(
            getString(R.string.key_health_step_value),
            resources.getInteger(R.integer.default_step_value)
        ) as Int
    }

    @SuppressLint("SetTextI18n")
    private fun initSleep() {
        sleepViewModel = ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(SleepViewModel::class.java)
        sleepViewModel.mldHealthSleep.observe(this, Observer {
            val twoDataDifference =
                XDateUtils.getTwoDataDifference(Date(it.endTime), Date(it.startTime))
            val totalHour = twoDataDifference.hour
            text_view_sleep_total_time_hour.text = totalHour.toString()
            text_view_sleep_total_time_minute.text =
                (twoDataDifference.minute - totalHour * 60).toString()
            text_view_sleep_during_time.text = XDateUtils.millis2String(
                it.startTime,
                "HH:mm"
            ) + "-" + XDateUtils.millis2String(it.endTime, "HH:mm")
            if (totalHour in 7..8) {
                text_view_sleep_status.text = getString(R.string.status_health_sleep_normal)
            } else {
                if (totalHour < 7) {
                    text_view_sleep_status.text = getString(R.string.status_health_sleep_less)
                } else {
                    text_view_sleep_status.text = getString(R.string.status_health_sleep_over)
                }
            }
        })
        updateSleepData()
    }

    private fun updateSleepData() {
        sleepViewModel.getUserSleepData(userId, object : RequestCallBack<HealthSleep>() {
            override fun onSuccess(result: HealthSleep) {
                sleepViewModel.mldHealthSleep.value = result
                showSuccess()
            }

            override fun onFailed(errorCode: Int, error: String) {
                showFailed(errorCode, error)
            }

            override fun onNoNetWork() {
                showNoNetWork()
            }
        })
    }

    private fun initBMI() {
        bmiViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(BMIViewModel::class.java)
        bmiViewModel.initData()
        bmiViewModel.mldHeight.observe(this, Observer {
            if (bmiViewModel.mldWeight.value != null) {
                bmiViewModel.calculateBMI()
            }
        })
        bmiViewModel.mldWeight.observe(this, Observer {
            if (bmiViewModel.mldHeight.value != null) {
                bmiViewModel.calculateBMI()
            }
            text_view_bmi_weight_value.text = DecimalFormat("0.#").format(it.weightData)
            text_view_bmi_weight_update_time.text =
                XFormatTimeUtils.getTimeSpanByNow1(it.measureTime)
        })
        bmiViewModel.mldBMI.observe(this, Observer {
            text_view_bmi_status.text = when (bmiViewModel.bmiRealIndex) {
                0 -> {
                    bmiViewModel.setNotNormal()
                    text_view_bmi_status.setBackgroundResource(R.drawable.background_status_bmi_thin)
                    getString(R.string.thin)
                }
                1 -> {
                    bmiViewModel.setNormal()
                    text_view_bmi_status.setBackgroundResource(R.drawable.background_status)
                    getString(R.string.normal)
                }
                2 -> {
                    bmiViewModel.setNotNormal()
                    text_view_bmi_status.setBackgroundResource(R.drawable.background_status_bmi_fatter)
                    getString(R.string.fatter)
                }
                else -> {
                    bmiViewModel.setNotNormal()
                    text_view_bmi_status.setBackgroundResource(R.drawable.background_status_bmi_over_wight)
                    getString(R.string.over_weight)
                }
            }
        })
        updateBMIData()
    }

    private fun updateNotNormalCount(boolean: Boolean) {
        if (!boolean) {
            notNormalCount++
            XLog.d("notNormalCount = $notNormalCount")
            if (notNormalCount >= 2) {
                notNormalCount = 0
                material_card_view_need_medical_suggestion.visibility = View.VISIBLE
            }
        }
    }

    private fun updateBMIData() {
        bmiViewModel.getUserHeight(userId, object : RequestCallBack<HealthHeight>() {
            override fun onSuccess(result: HealthHeight) {
                bmiViewModel.mldHeight.value = result
                showSuccess()
            }

            override fun onFailed(errorCode: Int, error: String) {
                showFailed(errorCode, error)
            }

            override fun onNoNetWork() {
                showNoNetWork()
            }
        })
        bmiViewModel.getUserLatestWeight(userId, object : RequestCallBack<HealthWeight>() {
            override fun onSuccess(result: HealthWeight) {
                bmiViewModel.mldWeight.value = result
                showSuccess()
            }

            override fun onFailed(errorCode: Int, error: String) {
                showFailed(errorCode, error)
            }

            override fun onNoNetWork() {
                showNoNetWork()
            }
        })
    }

    private fun showSuccess() {
        smart_refresh_layout?.finishRefresh()
    }

    private fun showFailed(errorCode: Int, error: String) {
        if (errorCode != ResultCode.SUCCESS.code) {
            smart_refresh_layout?.finishRefresh(false)
            XToast.error("$errorCode\n$error")
        } else {
            showSuccess()
        }
    }

    private fun showNoNetWork() {
        XToast.info(getString(R.string.xloading_no_network_text))
    }

    override fun initView() {
        material_card_view_sport.setOnClickListener(this)
        material_card_view_sleep.setOnClickListener(this)
        material_card_view_bmi.setOnClickListener(this)
        material_card_view_blood_pressure.setOnClickListener(this)
        material_card_view_blood_sugar.setOnClickListener(this)
        material_card_view_blood_oxygen.setOnClickListener(this)
        smart_refresh_layout.setOnRefreshListener {
            updateSportData()
            updateSleepData()
            updateBMIData()
            updateBloodPressureData()
            updateBloodSugarData()
            updateBloodOxygenData()
        }
        if (userId == App.user.id) {
            image_view_close.setOnClickListener {
                material_card_view_need_medical_suggestion.visibility = View.GONE
            }
            material_card_view_need_medical_suggestion.setOnClickListener {
                startActivityForResult(
                    Intent(requireContext(), SelectProfessionalActivity::class.java),
                    SelectProfessionalActivity.REQUEST_CODE
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SelectProfessionalActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                LoadingDialog.with(requireContext()).setMessage("请求发送中...").show()
                val selectedProfessionalId =
                    data.getLongExtra(SelectProfessionalActivity.SELECTED_PROFESSIONAL_ID, 0L)
                if (selectedProfessionalId != 0L) {
                    val picture = takeHealthDataPicture(linear_layout, requireContext())
                    if (picture != null) {
                        Http.obtain().uploadFile(
                            Constant.FILE_UPLOAD,
                            UUID.randomUUID().toString().replace("-", "") + ".jpg",
                            Constant.FILE_UPLOAD_PATH,
                            picture,
                            object : RequestCallBack<JsonResult<String>>() {
                                override fun onSuccess(result: JsonResult<String>) {
                                    MedicalSuggestionRequest.insert(MedicalSuggestionRequest().apply {
                                        userId = this@HealthFragment.userId
                                        professionalId = selectedProfessionalId
                                        time = System.currentTimeMillis()
                                        healthDataImageUrl = result.data
                                    }, object : RequestCallBack<Boolean>() {
                                        override fun onSuccess(result: Boolean) {
                                            LoadingDialog.with(requireContext()).cancel()
                                            material_card_view_need_medical_suggestion.visibility =
                                                View.GONE
                                            XToast.success("请求发送成功")
                                        }

                                        override fun onFailed(errorCode: Int, error: String) {
                                            LoadingDialog.with(requireContext()).cancel()
                                            XToast.error("$errorCode\n$error")
                                        }

                                        override fun onNoNetWork() {
                                            LoadingDialog.with(requireContext()).cancel()
                                            XToast.info(getString(R.string.xloading_no_network_text))
                                        }
                                    })
                                }

                                override fun onFailed(errorCode: Int, error: String) {
                                    LoadingDialog.with(requireContext()).cancel()
                                    XToast.error("$errorCode\n$error")
                                }

                                override fun onNoNetWork() {
                                    LoadingDialog.with(requireContext()).cancel()
                                    XToast.info(getString(R.string.xloading_no_network_text))
                                }
                            })
                    } else {
                        XToast.error("截图获取失败")
                    }
                } else {
                    XToast.error("回传参数错误")
                }
            } else {
                XToast.info("未选择")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param userId UserId.
         * @return A new instance of fragment HealthFragment.
         */
        @JvmStatic
        fun newInstance(userId: Long) =
            HealthFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_USER_ID, userId)
                }
            }

        @JvmStatic
        fun takeHealthDataPicture(view: View, context: Context): File? {
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            //使用Canvas，调用自定义view控件的onDraw方法，绘制图片
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            if (bitmap != null) {
                return try {
                    // 获取缓存卡路径
                    val sdCardPath: String = context.externalCacheDir!!.path
                    // 图片文件路径
                    val filePath =
                        sdCardPath + File.separator.toString() + UUID.randomUUID().toString()
                            .replace("-", "") + ".jpg"
                    val file = File(filePath)
                    val os = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                    os.flush()
                    os.close()
                    file
                } catch (e: Exception) {
                    null
                }
            } else {
                return null
            }
        }

        @JvmStatic
        fun takeHealthDataPictureBitmap(view: View): Bitmap? {
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            //使用Canvas，调用自定义view控件的onDraw方法，绘制图片
            val canvas = Canvas(bitmap)
            view.draw(canvas)
            return bitmap
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.material_card_view_sport -> {
                startActivity(
                    Intent(requireContext(), SportActivity::class.java).putExtra(
                        ARG_USER_ID,
                        userId
                    )
                )
            }
            R.id.material_card_view_sleep -> {
                startActivity(
                    Intent(requireContext(), SleepActivity::class.java).putExtra(
                        ARG_USER_ID, userId
                    )
                )
            }
            R.id.material_card_view_bmi -> {
                startActivity(
                    Intent(requireContext(), BMIActivity::class.java).putExtra(
                        ARG_USER_ID, userId
                    )
                )
            }
            R.id.material_card_view_blood_pressure -> {
                startActivity(
                    Intent(requireContext(), BloodPressureActivity::class.java).putExtra(
                        ARG_USER_ID, userId
                    )
                )
            }
            R.id.material_card_view_blood_sugar -> {
                startActivity(
                    Intent(requireContext(), BloodSugarActivity::class.java).putExtra(
                        ARG_USER_ID, userId
                    )
                )
            }
            R.id.material_card_view_blood_oxygen -> {
                startActivity(
                    Intent(requireContext(), BloodOxygenActivity::class.java).putExtra(
                        ARG_USER_ID, userId
                    )
                )
            }
            else -> {

            }
        }
    }

    override fun onPause() {
        super.onPause()
        sportViewModel.uploadTodayStepValue(userId)
    }

}
