package com.example.hbhims.view.fragment.welcome

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.navOptions
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
import com.example.hbhims.view.fragment.login.LoginFragment
import com.youth.xframe.utils.XPreferencesUtils
import com.youth.xframe.widget.XToast
import kotlinx.android.synthetic.main.fragment_welcome.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WelcomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WelcomeFragment : AbstractFragment() {

    private lateinit var navController: NavController

    override fun getLayoutId(): Int {
        return R.layout.fragment_welcome
    }

    override fun initData(savedInstanceState: Bundle?) {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        loadUserFromLocal()
    }

    private fun loadUserFromLocal() {
        val userJson = XPreferencesUtils.get(
            getString(R.string.key_user_json),
            "{}"
        ) as String
        if (userJson == "{}") {
            App.user = SysUser()
            button_switch_login.visibility = View.VISIBLE
            button_switch_register.visibility = View.VISIBLE
        } else {
            App.user = JSONObject.parseObject(userJson, SysUser::class.java)
            val hashMap = HashMap<String, Any>(3)
            hashMap["username"] = App.user.userName
            hashMap["password"] = App.user.password
            Http.obtain().post(Constant.USER_LOGIN, hashMap, object :
                HttpCallBack<JsonResult<JSONObject>>() {

                override fun onSuccess(result: JsonResult<JSONObject>) {
                    val sysUser =
                        JSONObject.parseObject(result.data.toString(), SysUser::class.java)
                    App.user = sysUser
                    App.user.password = hashMap["password"] as String
                    XPreferencesUtils.put(
                        getString(R.string.key_user_json),
                        JSONObject.toJSONString(App.user)
                    )
                    requireActivity().finish()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                }

                override fun onFailed(errorCode: Int, error: String) {
                    button_switch_login.visibility = View.VISIBLE
                    button_switch_register.visibility = View.VISIBLE
                    XToast.error("$errorCode\n$error")
                }
            })
        }
    }

    override fun initView() {
        button_switch_login.setOnClickListener {
            // 设置动画参数
            val navOption = navOptions {
                anim {
                    enter = R.anim.fragment_fade_enter
                    exit = R.anim.fragment_fade_exit
                    popEnter = R.anim.fragment_fade_enter
                    popExit = R.anim.fragment_fade_exit
                }
            }
            // 参数设置
            val bundle = Bundle()
            bundle.putString(LoginFragment.ARG_PARAM_USERNAME, App.user.userName)
            bundle.putString(LoginFragment.ARG_PARAM_PASSWORD, App.user.password)
            navController.navigate(R.id.action_welcomeFragment_to_loginFragment, bundle, navOption)
        }
        button_switch_register.setOnClickListener {
            // 设置动画参数
            val navOption = navOptions {
                anim {
                    enter = R.anim.fragment_fade_enter
                    exit = R.anim.fragment_fade_exit
                    popEnter = R.anim.fragment_fade_enter
                    popExit = R.anim.fragment_fade_exit
                }
            }
            navController.navigate(R.id.action_welcomeFragment_to_registerFragment, null, navOption)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment WelcomeFragment.
         */
        @JvmStatic
        fun newInstance() = WelcomeFragment()
    }
}
