package com.example.hbhims.view.activity.selectprofessional

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hbhims.R
import com.example.hbhims.model.common.util.http.RequestCallBack
import com.example.hbhims.model.entity.CustomSysUserProfessional
import com.example.hbhims.view.adapter.recyclerview.SelectProfessionalAdapter
import com.example.hbhims.view.base.ContainerActivity
import com.youth.xframe.widget.XToast
import kotlinx.android.synthetic.main.activity_select_professional.*

class SelectProfessionalActivity : ContainerActivity(),
    SelectProfessionalAdapter.ISelectProfessional {

    private lateinit var viewModel: SelectProfessionalViewModel
    private lateinit var adapter: SelectProfessionalAdapter

    override fun preFinish(): Boolean {
        return true
    }

    override fun getOptionsMenuId(menu: Menu?): Int {
        return 0
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_select_professional
    }

    override fun initData(savedInstanceState: Bundle?) {
        viewModel =
            ViewModelProvider.NewInstanceFactory().create(SelectProfessionalViewModel::class.java)
        adapter = SelectProfessionalAdapter(viewModel.dataList, this, this)
    }

    override fun initView() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter
        smart_refresh_layout.setOnRefreshListener {
            refresh(false)
        }
        refresh(true)
    }

    private fun refresh(needShowContent: Boolean) {
        CustomSysUserProfessional.queryAll(object :
            RequestCallBack<List<CustomSysUserProfessional>>() {
            override fun onSuccess(result: List<CustomSysUserProfessional>) {
                adapter.notifyItemRangeRemoved(0, adapter.itemCount)
                viewModel.dataList.clear()
                viewModel.dataList.addAll(result.sortedByDescending { it.meanEvaluationScore })
                adapter.notifyItemRangeInserted(0, result.size)
                smart_refresh_layout.finishRefresh()
                if (result.isEmpty()) {
                    loadingView.showEmpty()
                } else {
                    if (needShowContent) {
                        loadingView.showContent()
                    }
                }
            }

            override fun onFailed(errorCode: Int, error: String) {
                showFailed()
            }

            override fun onNoNetWork() {
                showNoNetWork()
            }
        })
    }

    private fun showFailed() {
        loadingView.setOnRetryClickListener {
            refresh(true)
        }.showError()
    }

    private fun showNoNetWork() {
        loadingView.setOnRetryClickListener {
            refresh(true)
        }.showNoNetwork()
    }

    companion object {
        const val SELECTED_PROFESSIONAL_ID = "SELECTED_PROFESSIONAL_ID"
        const val REQUEST_CODE = 415
    }

    override fun onSelect(position: Int, sysUserProfessional: CustomSysUserProfessional) {
        if (sysUserProfessional.id == userId) {
            XToast.info("不能选择自己哦")
        } else {
            setResult(
                Activity.RESULT_OK,
                Intent().putExtra(SELECTED_PROFESSIONAL_ID, sysUserProfessional.id)
            )
            finish()
        }
    }

}