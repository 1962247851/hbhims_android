package com.example.hbhims.view.fragment.myself

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.fastjson.JSONObject
import com.example.hbhims.App
import com.example.hbhims.R
import com.example.hbhims.model.common.Constant
import com.example.hbhims.model.common.entity.JsonResult
import com.example.hbhims.model.common.enums.ResultCode
import com.example.hbhims.model.common.util.http.Http
import com.example.hbhims.model.common.util.http.HttpCallBack
import com.example.hbhims.model.common.util.http.RequestCallBack
import com.example.hbhims.model.entity.SysRole
import com.example.hbhims.model.entity.SysUser
import com.example.hbhims.model.entity.SysUserRoleRelation
import com.example.hbhims.view.activity.UpdateUserInfoActivity
import com.example.hbhims.view.activity.login.LoginActivity
import com.example.hbhims.view.base.AbstractFragment
import com.example.hbhims.view.custom.LoadingDialog
import com.youth.xframe.utils.XDateUtils
import com.youth.xframe.utils.XPreferencesUtils
import com.youth.xframe.widget.XToast
import kotlinx.android.synthetic.main.fragment_my_self.*
import java.util.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USER_ID = "USER_ID"

/**
 * A simple [Fragment] subclass.
 * Use the [MyselfFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyselfFragment : AbstractFragment() {

    private lateinit var viewModel: MyselfViewModel
    private var userId: Long = App.user.id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getLong(ARG_USER_ID)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_my_self
    }

    @SuppressLint("SetTextI18n")
    override fun initData(savedInstanceState: Bundle?) {
        viewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(MyselfViewModel::class.java)
        viewModel.mldSysUser.observe(this, Observer {
            text_view_user_name.text = it.userName
            text_view_sex.text = if (it.sex) {
                getString(R.string.male)
            } else {
                getString(R.string.female)
            }
            text_view_age.text =
                (XDateUtils.getTwoDataDifference(Date(it.birthday)).day / 365).toString()
            text_view_description.text = "个人介绍：${it.selfIntroduction}"
        })
        viewModel.initData()
        updateUserRoleInfo()
    }

    override fun initView() {
        text_view_logout.setOnClickListener {
            AlertDialog.Builder(requireContext()).setTitle(getString(R.string.logout_title))
                .setPositiveButton(R.string.cancel, null)
                .setNegativeButton(R.string.confirm) { _, _ ->
                    LoadingDialog.with(requireContext()).setMessage(getString(R.string.logouting))
                        .show()
                    Http.obtain().get(
                        Constant.USER_LOGOUT,
                        null,
                        object : HttpCallBack<JsonResult<JSONObject>>() {
                            override fun onSuccess(result: JsonResult<JSONObject>) {
                                LoadingDialog.with(requireContext()).cancel()
                                XPreferencesUtils.remove(getString(R.string.key_user_json))
                                requireActivity().finish()
                                startActivity(Intent(requireContext(), LoginActivity::class.java))
                            }

                            override fun onFailed(errorCode: Int, error: String) {
                                LoadingDialog.with(requireContext()).cancel()
                                showError(errorCode, error)
                            }
                        })
                }.show()
        }
        material_card_view_application_professional_role.setOnClickListener {
            SysUserRoleRelation.queryByUserIdAndRoleId(
                userId, 2L, object : RequestCallBack<SysUserRoleRelation>() {
                    override fun onSuccess(result: SysUserRoleRelation) {
                        XToast.info("已经拥有该角色")
                    }

                    override fun onFailed(errorCode: Int, error: String) {
                        if (errorCode == ResultCode.SUCCESS.code) {
                            viewModel.applicationToBeAProfessional(userId,
                                object : RequestCallBack<Boolean>() {
                                    override fun onSuccess(result: Boolean) {
                                        updateUserRoleInfo()
                                        material_card_view_application_professional_role.visibility =
                                            View.GONE
                                        XToast.success("申请成功")
                                    }

                                    override fun onFailed(errorCode: Int, error: String) {
                                        showError(errorCode, error)
                                    }

                                    override fun onNoNetWork() {
                                        showNoNetWork()
                                    }
                                })
                        } else {
                            showError(errorCode, error)
                        }
                    }

                    override fun onNoNetWork() {
                        showNoNetWork()
                    }
                })
        }
        image_view_edit.setOnClickListener {
            startActivityForResult(
                Intent(requireContext(), UpdateUserInfoActivity::class.java),
                REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                viewModel.mldSysUser.value = JSONObject.parseObject(
                    data.getStringExtra(UpdateUserInfoActivity.KEY_USER_JSON),
                    SysUser::class.java
                )
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun updateUserRoleInfo() {
        SysUserRoleRelation.queryAllByUserId(userId,
            object : RequestCallBack<List<SysUserRoleRelation>>() {
                override fun onSuccess(result: List<SysUserRoleRelation>) {
                    XPreferencesUtils.put(
                        getString(R.string.key_user_role_relation_json),
                        JSONObject.toJSONString(result)
                    )
                    text_view_user_role.text = null
                    result.forEach {
                        SysRole.queryById(it.roleId,
                            object : RequestCallBack<SysRole>() {
                                @SuppressLint("SetTextI18n")
                                override fun onSuccess(result: SysRole) {
                                    if (result.name == "专业人员") {
                                        material_card_view_application_professional_role.visibility =
                                            View.GONE
                                    }
                                    val text = text_view_user_role.text
                                    if (text.isNotEmpty()) {
                                        text_view_user_role.text =
                                            text.toString() + "、" + result.name
                                    } else {
                                        text_view_user_role.text = result.name
                                    }
                                }

                                override fun onFailed(
                                    errorCode: Int,
                                    error: String
                                ) {
                                    showError(errorCode, error)
                                }

                                override fun onNoNetWork() {
                                    showNoNetWork()
                                }
                            })
                    }
                }

                override fun onFailed(
                    errorCode: Int, error: String
                ) {
                    showError(errorCode, error)
                }

                override fun onNoNetWork() {
                    showNoNetWork()
                }
            })
    }

    private fun showError(errorCode: Int, error: String) {
        XToast.error("$errorCode\n$error")
    }

    private fun showNoNetWork() {
        XToast.info(getString(R.string.xloading_no_network_text))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param userId UserId.
         * @return A new instance of fragment MyselfFragment.
         */
        @JvmStatic
        fun newInstance(userId: Long) =
            MyselfFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_USER_ID, userId)
                }
            }

        const val REQUEST_CODE = 41577
    }
}
