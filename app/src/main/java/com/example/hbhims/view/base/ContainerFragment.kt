package com.example.hbhims.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hbhims.view.custom.LoadingView
import com.youth.xframe.base.ICallback

/**
 * @author qq1962247851
 * @date 2020/1/29 19:08
 */
abstract class ContainerFragment : Fragment(), ICallback {

    public lateinit var loadingView: LoadingView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (layoutId > 0) {
            inflater.inflate(layoutId, container, false)
        } else {
            super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadingView = LoadingView.wrap(this)
        loadingView.showLoading()
        initData(savedInstanceState)
        initView()
    }
}