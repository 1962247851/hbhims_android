package com.example.hbhims.view.activity.medicalsuggestionrequest

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import com.alibaba.fastjson.JSONObject
import com.bumptech.glide.Glide
import com.example.hbhims.R
import com.example.hbhims.model.common.util.http.RequestCallBack
import com.example.hbhims.model.entity.CustomMedicalSuggestionRequest
import com.example.hbhims.model.entity.MedicalSuggestion
import com.example.hbhims.model.entity.MedicalSuggestionRequest
import com.example.hbhims.view.base.AbstractActivity
import com.youth.xframe.widget.XToast
import kotlinx.android.synthetic.main.activity_new_medical_suggestion.*

class NewMedicalSuggestionActivity : AbstractActivity() {

    private lateinit var customMedicalSuggestionRequest: CustomMedicalSuggestionRequest

    override fun preFinish(): Boolean {
        return true
    }

    override fun getOptionsMenuId(menu: Menu?): Int {
        return 0
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_new_medical_suggestion
    }

    override fun initData(savedInstanceState: Bundle?) {
        if (intent.hasExtra(CUSTOM_MEDICAL_SUGGESTION_REQUEST)) {
            customMedicalSuggestionRequest = JSONObject.parseObject(
                intent.getStringExtra(
                    CUSTOM_MEDICAL_SUGGESTION_REQUEST
                ), CustomMedicalSuggestionRequest::class.java
            )
        } else if (savedInstanceState != null && savedInstanceState.containsKey(
                CUSTOM_MEDICAL_SUGGESTION_REQUEST
            )
        ) {
            customMedicalSuggestionRequest = JSONObject.parseObject(
                savedInstanceState.getString(
                    CUSTOM_MEDICAL_SUGGESTION_REQUEST
                ), CustomMedicalSuggestionRequest::class.java
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(
            CUSTOM_MEDICAL_SUGGESTION_REQUEST,
            customMedicalSuggestionRequest.toString()
        )
        super.onSaveInstanceState(outState)
    }

    override fun initView() {
        if (!customMedicalSuggestionRequest.healthDataImageUrl.isNullOrEmpty()) {
            constraint_layout.setOnClickListener {
                if (expand_layout.isExpand) {
                    expand_layout.collapse()
                    image_view_arrow.setImageResource(R.drawable.ic_keyboard_arrow_down)
                } else {
                    expand_layout.expand()
                    image_view_arrow.setImageResource(R.drawable.ic_keyboard_arrow_up)
                }
            }
            Glide.with(this).load(customMedicalSuggestionRequest.healthDataImageUrl)
                .into(image_view)
        } else {
            image_view_arrow.setImageResource(R.drawable.ic_keyboard_arrow_down)
            constraint_layout.setOnClickListener {
                XToast.info("没有数据")
            }
        }
        button_finish.setOnClickListener {
            val content = tiet_content.text.toString()
            if (content.isNotEmpty() && content.isNotBlank()) {
                MedicalSuggestion.insert(MedicalSuggestion().apply {
                    professionalId = customMedicalSuggestionRequest.professionalId
                    userId = customMedicalSuggestionRequest.userId
                    time = System.currentTimeMillis()
                    this.content = content
                }, object : RequestCallBack<MedicalSuggestion>() {
                    override fun onSuccess(result: MedicalSuggestion) {
                        MedicalSuggestionRequest.delete(
                            customMedicalSuggestionRequest.id,
                            1,
                            object : RequestCallBack<Boolean>() {
                                override fun onSuccess(result: Boolean) {
                                    XToast.success("发表成功")
                                    setResult(Activity.RESULT_OK, intent)
                                    finish()
                                }

                                override fun onFailed(errorCode: Int, error: String) {
                                    showFailed(errorCode, error)
                                }

                                override fun onNoNetWork() {
                                    showNoNetWork()
                                }
                            })
                    }

                    override fun onFailed(errorCode: Int, error: String) {
                        showFailed(errorCode, error)
                    }

                    override fun onNoNetWork() {
                        showNoNetWork()
                    }
                })
            } else {
                XToast.error("请检查输入")
            }

        }
    }

    private fun showFailed(errorCode: Int, error: String) {
        XToast.error("$errorCode\n$error")
    }

    private fun showNoNetWork() {
        XToast.info(getString(R.string.xloading_no_network_text))
    }

    companion object {
        const val CUSTOM_MEDICAL_SUGGESTION_REQUEST = "CUSTOM_MEDICAL_SUGGESTION_REQUEST"
        const val REQUEST_CODE = 4161
    }
}