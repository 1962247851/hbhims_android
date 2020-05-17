package com.example.hbhims.view.activity.medicalsuggestion

import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.RatingBar
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.alibaba.fastjson.JSONObject
import com.example.hbhims.App
import com.example.hbhims.R
import com.example.hbhims.model.common.util.http.HttpCallBack
import com.example.hbhims.model.common.util.http.RequestCallBack
import com.example.hbhims.model.entity.CustomMedicalSuggestion
import com.example.hbhims.model.entity.MedicalSuggestionEvaluation
import com.example.hbhims.model.entity.SysUser
import com.example.hbhims.view.base.AbstractActivity
import com.youth.xframe.utils.XDateUtils
import com.youth.xframe.widget.XToast
import kotlinx.android.synthetic.main.activity_medical_suggestion.*
import kotlinx.android.synthetic.main.rating_bar_with_time.*
import java.util.*

class MedicalSuggestionActivity : AbstractActivity() {

    private lateinit var customMedicalSuggestion: CustomMedicalSuggestion
    private lateinit var professionalUser: SysUser

    override fun preFinish(): Boolean {
        return true
    }

    override fun getOptionsMenuId(menu: Menu?): Int {
        return 0
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_medical_suggestion
    }

    override fun initData(savedInstanceState: Bundle?) {
        val jsonString = if (intent.hasExtra(CUSTOM_MEDICAL_SUGGESTION)) {
            intent.getStringExtra(CUSTOM_MEDICAL_SUGGESTION)
        } else if (savedInstanceState != null &&
            savedInstanceState.containsKey(CUSTOM_MEDICAL_SUGGESTION)
        ) {
            savedInstanceState.getString(CUSTOM_MEDICAL_SUGGESTION)
        } else {
            throw RuntimeException("$CUSTOM_MEDICAL_SUGGESTION must be used")
        }
        customMedicalSuggestion =
            JSONObject.parseObject(jsonString, CustomMedicalSuggestion::class.java)
    }

    private fun updateUserInfo(userId: Long) {
        SysUser.query(userId, object : RequestCallBack<SysUser>() {
            override fun onSuccess(result: SysUser) {
                ctl.title = result.userName
                text_view_description.text = result.selfIntroduction
                text_view_sex.text = if (result.sex) {
                    getString(R.string.male)
                } else {
                    getString(R.string.female)
                }
                text_view_age.text =
                    (XDateUtils.getTwoDataDifference(Date(result.birthday)).day / 365).toString()
            }

            override fun onFailed(errorCode: Int, error: String) {
                XToast.error("$errorCode\n$error")
            }

            override fun onNoNetWork() {
                XToast.info("没有网络")
            }
        })
    }

    override fun initView() {
        ctl.setCollapsedTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        if (customMedicalSuggestion.professionalId != App.user.id) {
            //收到的，展示专业人员信息
            updateUserInfo(customMedicalSuggestion.professionalId)
        } else {
            //发送的，展示接收人的信息
            updateUserInfo(customMedicalSuggestion.userId)
        }
        text_view_content.text = customMedicalSuggestion.content
        if (customMedicalSuggestion.evaluationScore == null) {
            text_view_no_rating_data.visibility = View.VISIBLE
            rating_bar.visibility = View.GONE
            if (customMedicalSuggestion.professionalId != App.user.id) {
                text_view_no_rating_data.text = "给这个建议评个分"
                text_view_no_rating_data.setOnClickListener {
                    var ratingValue = 0
                    val show = AlertDialog.Builder(this).setTitle("评分").setCancelable(false)
                        .setView(layoutInflater.inflate(R.layout.rating_bar, null, false).apply {
                            findViewById<RatingBar>(R.id.rating_bar).apply {
                                setOnRatingBarChangeListener { _, rating, _ ->
                                    ratingValue = rating.toInt()
                                }
                            }
                        }).setPositiveButton(R.string.confirm, null)
                        .setNegativeButton(R.string.cancel, null).show()
                    show.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener {
                        MedicalSuggestionEvaluation.insert(MedicalSuggestionEvaluation().apply {
                            suggestionId = customMedicalSuggestion.id
                            evaluationScore = ratingValue
                            time = System.currentTimeMillis()
                        }, object : HttpCallBack<MedicalSuggestionEvaluation>() {
                            override fun onSuccess(result: MedicalSuggestionEvaluation) {
                                XToast.success("评分成功")
                                customMedicalSuggestion.evaluationScore = result.evaluationScore
                                customMedicalSuggestion.evaluationTime = result.time
                                rating_bar.rating =
                                    customMedicalSuggestion.evaluationScore.toFloat()
                                text_view_no_rating_data.visibility = View.GONE
                                rating_bar.visibility = View.VISIBLE
                                show.cancel()
                            }

                            override fun onFailed(errorCode: Int, error: String) {
                                XToast.error("$errorCode\n$error")
                            }
                        })
                    }
                }
            }
        } else {
            rating_bar.rating = customMedicalSuggestion.evaluationScore.toFloat()
            text_view_no_rating_data.visibility = View.GONE
            rating_bar.visibility = View.VISIBLE
        }
        text_view_time.text =
            XDateUtils.millis2String(customMedicalSuggestion.time, "yyyy/MM/dd HH:mm:ss")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(CUSTOM_MEDICAL_SUGGESTION, customMedicalSuggestion.toString())
        super.onSaveInstanceState(outState)
    }

    companion object {
        const val CUSTOM_MEDICAL_SUGGESTION = "CUSTOM_MEDICAL_SUGGESTION"
    }

}