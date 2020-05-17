package com.example.hbhims.view.fragment.register

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.example.hbhims.R
import com.example.hbhims.model.common.util.http.RequestCallBack
import com.example.hbhims.model.entity.SysUser
import com.example.hbhims.view.base.AbstractFragment
import com.example.hbhims.view.custom.LoadingDialog
import com.example.hbhims.view.fragment.login.LoginFragment
import com.youth.xframe.utils.XDateUtils
import com.youth.xframe.utils.XRegexUtils
import com.youth.xframe.widget.XToast
import kotlinx.android.synthetic.main.fragment_register.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : AbstractFragment() {

    private var userName = ""
    private var password = ""
    private var passwordAgain = ""
    private var sex = true
    private val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    private var birthday = calendar.timeInMillis

    override fun getLayoutId(): Int {
        return R.layout.fragment_register
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView() {
        spinner_sex.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //ignore
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                sex = position == 0
            }
        }
        button_register.setOnClickListener {
            if (checkValid()) {
                LoadingDialog.with(requireContext()).setMessage("正在注册...").show()
                SysUser.insert(SysUser().apply {
                    userName = this@RegisterFragment.userName
                    sex = this@RegisterFragment.sex
                    birthday = this@RegisterFragment.birthday
                    password = this@RegisterFragment.password
                }, object : RequestCallBack<SysUser>() {
                    override fun onSuccess(result: SysUser) {
                        LoadingDialog.with(requireContext()).cancel()
                        XToast.success("注册成功")
                        // 设置动画参数
                        val navOption = navOptions {
                            anim {
                                enter = R.anim.fragment_fade_enter
                                exit = R.anim.fragment_fade_exit
                                popEnter = R.anim.fragment_fade_enter
                                popExit = R.anim.fragment_fade_exit
                            }
                        }
                        val bundle = Bundle()
                        bundle.putString(LoginFragment.ARG_PARAM_USERNAME, userName)
                        bundle.putString(LoginFragment.ARG_PARAM_PASSWORD, password)
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                            .navigate(
                                R.id.action_registerFragment_to_loginFragment, bundle, navOption
                            )
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
                XToast.info("请检查输入")
            }
        }
        updateBirthdayEditText()
        tiet_birthday.setOnClickListener {
            AlertDialog.Builder(requireContext()).setTitle(R.string.select_date)
                .setCancelable(false)
                .setView(DatePicker(requireContext()).apply {
                    maxDate = System.currentTimeMillis()
                    init(
                        calendar[Calendar.YEAR],
                        calendar[Calendar.MONTH],
                        calendar[Calendar.DAY_OF_MONTH]
                    ) { _, year, monthOfYear, dayOfMonth ->
                        calendar[Calendar.YEAR] = year
                        calendar[Calendar.MONTH] = monthOfYear
                        calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.confirm) { _, _ ->
                    birthday = calendar.timeInMillis
                    updateBirthdayEditText()
                }.show()
        }
    }

    private fun updateBirthdayEditText() {
        tiet_birthday.setText(XDateUtils.millis2String(birthday, "yyyy/MM/dd"))
    }

    private fun checkValid(): Boolean {
        userName = tiet_username.text.toString()
        password = tiet_password.text.toString()
        passwordAgain = tiet_password_again.text.toString()
        return userName.length in 1..10 && XRegexUtils.checkNickName(userName) &&
                password == passwordAgain && password.length in 1..18 &&
                XRegexUtils.checkPassword(password)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment RegisterFragment.
         */
        @JvmStatic
        fun newInstance() = RegisterFragment()
    }
}
