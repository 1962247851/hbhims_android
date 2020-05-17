package com.example.hbhims.view.fragment.login

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.fragment.app.Fragment
import com.alibaba.fastjson.JSONObject
import com.example.hbhims.App
import com.example.hbhims.R
import com.example.hbhims.model.common.Constant
import com.example.hbhims.model.common.entity.JsonResult
import com.example.hbhims.model.common.util.http.Http
import com.example.hbhims.model.common.util.http.HttpCallBack
import com.example.hbhims.model.entity.SysUser
import com.example.hbhims.view.activity.main.MainActivity
import com.example.hbhims.view.base.AbstractFragment
import com.example.hbhims.view.custom.LoadingDialog
import com.youth.xframe.utils.XPreferencesUtils
import com.youth.xframe.widget.XToast
import kotlinx.android.synthetic.main.fragment_login.*


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : AbstractFragment() {
    private var username: String? = null
    private var password: String? = null
    private var uuid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(ARG_PARAM_USERNAME)
            password = it.getString(ARG_PARAM_PASSWORD)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView() {
        tiet_username.setText(username)
        tiet_password.setText(password)
        getCaptchaImage()
        button_login.setOnClickListener {
            if (uuid != null) {
                LoadingDialog.with(requireContext()).setMessage(getString(R.string.loginning))
                    .show()
                val hashMap = HashMap<String, Any>(3)
                hashMap["username"] = tiet_username.text.toString()
                hashMap["password"] = tiet_password.text.toString()
                hashMap["code"] = tiet_code.text.toString()
                hashMap["uuid"] = uuid!!
                Http.obtain().post(Constant.USER_LOGIN, hashMap, object :
                    HttpCallBack<JsonResult<JSONObject>>() {

                    override fun onSuccess(result: JsonResult<JSONObject>) {
                        val sysUser =
                            JSONObject.parseObject(result.data.toString(), SysUser::class.java)
                        sysUser.password = tiet_password.text.toString()
                        LoadingDialog.with(requireContext()).cancel()
                        if (switch_remember_account.isChecked) {
                            XPreferencesUtils.put(
                                getString(R.string.key_user_json),
                                JSONObject.toJSONString(sysUser)
                            )
                        }
                        App.user = sysUser
                        requireActivity().finish()
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                    }

                    override fun onFailed(errorCode: Int, error: String) {
                        LoadingDialog.with(requireContext()).cancel()
                        getCaptchaImage()
                        XToast.error("$errorCode\n$error")
                    }

                })
            }
        }
    }

    private fun getCaptchaImage() {
        startGetCaptchaImage()
        Http.obtain().get(Constant.CAPTCHA_IMAGE, null, object :
            HttpCallBack<JsonResult<JSONObject>>() {
            override fun onSuccess(result: JsonResult<JSONObject>) {
                stopGetCaptchaImage()
                val data = result.data
                val decode = Base64.decode(data.getString("img"), Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.size)
                image_view_code?.setImageBitmap(bitmap)
                uuid = data.getString("uuid")
                image_view_code?.setOnClickListener {
                    getCaptchaImage()
                }
            }

            override fun onFailed(errorCode: Int, error: String) {
                failedGetCaptchaImage()
                XToast.error("$errorCode\n$error")
            }

        })
    }

    private fun stopGetCaptchaImage() {
        tiet_code?.text = null
        progress_bar?.visibility = View.GONE
        image_view_code?.visibility = View.VISIBLE
    }

    private fun failedGetCaptchaImage() {
        progress_bar?.visibility = View.GONE
        image_view_code?.visibility = View.VISIBLE
        image_view_code?.setImageResource(R.drawable.xloading_error)
        image_view_code?.setOnClickListener {
            getCaptchaImage()
        }
    }

    private fun startGetCaptchaImage() {
        progress_bar?.visibility = View.VISIBLE
        image_view_code?.visibility = View.INVISIBLE
    }

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        const val ARG_PARAM_USERNAME = "username"
        const val ARG_PARAM_PASSWORD = "password"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param username Parameter 1.
         * @param password Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        @JvmStatic
        fun newInstance(username: String, password: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_USERNAME, username)
                    putString(ARG_PARAM_PASSWORD, password)
                }
            }
    }
}
