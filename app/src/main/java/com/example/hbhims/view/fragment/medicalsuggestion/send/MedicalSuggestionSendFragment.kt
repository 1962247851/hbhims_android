package com.example.hbhims.view.fragment.medicalsuggestion.send

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSONObject
import com.example.hbhims.R
import com.example.hbhims.model.common.util.http.RequestCallBack
import com.example.hbhims.model.entity.CustomMedicalSuggestion
import com.example.hbhims.model.entity.MedicalSuggestion
import com.example.hbhims.model.entity.SysUserRoleRelation
import com.example.hbhims.view.activity.medicalsuggestion.MedicalSuggestionActivity
import com.example.hbhims.view.adapter.recyclerview.MedicalSuggestionAdapter
import com.example.hbhims.view.base.ContainerFragment
import com.youth.xframe.utils.XPreferencesUtils
import com.youth.xframe.utils.log.XLog
import kotlinx.android.synthetic.main.fragment_medical_suggestion_send.*

class MedicalSuggestionSendFragment : ContainerFragment(),
    MedicalSuggestionAdapter.IMedicalSuggestion {

    private lateinit var viewModel: MedicalSuggestionSendViewModel
    private lateinit var medicalSuggestionAdapter: MedicalSuggestionAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_medical_suggestion_send
    }

    override fun initData(savedInstanceState: Bundle?) {
        viewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                .create(MedicalSuggestionSendViewModel::class.java)
        viewModel.isProfessional.observe(this, Observer {
            if (it) {
                smart_refresh_layout.visibility = View.VISIBLE
                text_view.visibility = View.GONE
            } else {
                smart_refresh_layout.visibility = View.GONE
                text_view.visibility = View.VISIBLE
            }
        })
    }

    private fun updateRoleInfo(needShowContent: Boolean) {
        SysUserRoleRelation.queryAllByUserId(userId,
            object : RequestCallBack<List<SysUserRoleRelation>>() {
                override fun onSuccess(result: List<SysUserRoleRelation>) {
                    XPreferencesUtils.put(
                        getString(R.string.key_user_role_relation_json),
                        JSONObject.toJSONString(result)
                    )
                    viewModel.updateRoleRelation()
                    showSuccess(needShowContent)
                }

                override fun onFailed(
                    errorCode: Int, error: String
                ) {
                    showFailed()
                }

                override fun onNoNetWork() {
                    showNoNetWork()
                }
            })
    }

    override fun initView() {
        smart_refresh_layout.setOnRefreshListener {
            updateRoleInfo(false)
        }
        updateRoleInfo(true)
        medicalSuggestionAdapter =
            MedicalSuggestionAdapter(viewModel.customMedicalSuggestionList, requireContext(), this)
        recycler_view.layoutManager = LinearLayoutManager(requireContext())
        recycler_view.adapter = medicalSuggestionAdapter
        smart_refresh_layout.setOnRefreshListener {
            refresh(false)
        }
        smart_refresh_layout.setOnLoadMoreListener {
            loadMore()
        }
        refresh(true)
    }

    private fun refresh(needShowContent: Boolean) {
        viewModel.resetPageAndSize()
        getData(needShowContent)
    }

    private fun getData(needShowContent: Boolean) {
        XLog.d("getData page=${viewModel.page},size=${viewModel.size}")
        MedicalSuggestion.queryAllByProfessionalIdAndPage(
            userId, viewModel.page, viewModel.size,
            object : RequestCallBack<List<CustomMedicalSuggestion>>() {
                override fun onSuccess(result: List<CustomMedicalSuggestion>) {
                    if (viewModel.page == 0) {
                        refreshSuccess(needShowContent, result)
                    } else {
                        loadMoreSuccess(result)
                    }
                }

                override fun onFailed(errorCode: Int, error: String) {
                    refreshFailed()
                }

                override fun onNoNetWork() {
                    showNoNetWork()
                }
            })
    }

    private fun loadMoreSuccess(result: List<CustomMedicalSuggestion>) {
        if (result.size < 10) {
            smart_refresh_layout.finishLoadMoreWithNoMoreData()
        } else {
            smart_refresh_layout.finishLoadMore()
        }
    }

    private fun refreshFailed() {
        loadingView.setOnRetryClickListener {
            loadingView.showLoading()
            refresh(true)
        }.showError()
        smart_refresh_layout.finishRefresh(false)
    }

    private fun refreshSuccess(needShowContent: Boolean, result: List<CustomMedicalSuggestion>) {
        val oldSize = viewModel.customMedicalSuggestionList.size
        viewModel.customMedicalSuggestionList.clear()
        medicalSuggestionAdapter.notifyItemRangeRemoved(0, oldSize)
        viewModel.customMedicalSuggestionList.addAll(result)
        medicalSuggestionAdapter.notifyItemRangeInserted(0, result.size)
        if (needShowContent) {
            loadingView.showContent()
        }
        if (result.isEmpty()) {
            loadingView.showEmpty()
        }
        smart_refresh_layout.finishRefresh()
        if (result.size < 10) {
            smart_refresh_layout.setNoMoreData(true)
        }
    }

    private fun loadMore() {
        viewModel.page++
        viewModel.size += 10
        getData(false)
    }

    override fun onClick(position: Int, medicalSuggestion: CustomMedicalSuggestion) {
        startActivity(
            Intent(requireContext(), MedicalSuggestionActivity::class.java).putExtra(
                MedicalSuggestionActivity.CUSTOM_MEDICAL_SUGGESTION,
                medicalSuggestion.toString()
            )
        )
    }

    private fun showSuccess(needShowContent: Boolean) {
        if (needShowContent) {
            loadingView.showContent()
        }
    }

    private fun showFailed() {
        smart_refresh_layout.finishRefresh(false)
        loadingView.setOnRetryClickListener {
            loadingView.showLoading()
            updateRoleInfo(true)
        }.showError()
    }

    private fun showNoNetWork() {
        loadingView.setOnRetryClickListener {
            loadingView.showLoading()
            refresh(true)
            updateRoleInfo(true)
        }.showNoNetwork()
    }
}