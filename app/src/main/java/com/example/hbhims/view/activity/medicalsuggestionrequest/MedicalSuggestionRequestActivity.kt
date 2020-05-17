package com.example.hbhims.view.activity.medicalsuggestionrequest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hbhims.App
import com.example.hbhims.R
import com.example.hbhims.model.common.util.http.RequestCallBack
import com.example.hbhims.model.entity.CustomMedicalSuggestionRequest
import com.example.hbhims.model.entity.MedicalSuggestionRequest
import com.example.hbhims.view.adapter.recyclerview.MedicalSuggestionRequestAdapter
import com.example.hbhims.view.base.ContainerActivity
import com.youth.xframe.widget.XToast
import kotlinx.android.synthetic.main.activity_medical_suggestion_request.*

class MedicalSuggestionRequestActivity : ContainerActivity(),
    MedicalSuggestionRequestAdapter.IMedicalSuggestionRequest {

    private var isSend = true
    private lateinit var viewModel: MedicalSuggestionRequestViewModel
    private lateinit var adapter: MedicalSuggestionRequestAdapter

    override fun preFinish(): Boolean {
        return true
    }

    override fun getOptionsMenuId(menu: Menu?): Int {
        return R.menu.medical_suggestion_request
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_medical_suggestion_request
    }

    override fun initData(savedInstanceState: Bundle?) {
        viewModel =
            ViewModelProvider(application as App, ViewModelProvider.NewInstanceFactory()).get(
                MedicalSuggestionRequestViewModel::class.java
            )
        adapter = MedicalSuggestionRequestAdapter(viewModel.dataList, this, this)
    }

    override fun initView() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter
        smart_refresh_layout.setOnRefreshListener {
            refresh()
        }
        refreshSend(true)
    }

    private fun refresh() {
        if (isSend) {
            refreshSend(false)
        } else {
            refreshReceive(false)
        }
    }

    private fun refreshSend(needShowContent: Boolean) {
        CustomMedicalSuggestionRequest.querySend(userId, object :
            RequestCallBack<List<CustomMedicalSuggestionRequest>>() {
            override fun onSuccess(result: List<CustomMedicalSuggestionRequest>) {
                refreshSuccess(result, needShowContent)
            }

            override fun onFailed(errorCode: Int, error: String) {
                smart_refresh_layout.finishRefresh(false)
                loadingView.setOnRetryClickListener {
                    loadingView.showLoading()
                    refreshSend(true)
                }.showError()
            }

            override fun onNoNetWork() {
                smart_refresh_layout.finishRefresh(false)
                loadingView.setOnRetryClickListener {
                    loadingView.showLoading()
                    refreshSend(true)
                }.showNoNetwork()
            }
        })
    }

    private fun refreshSuccess(
        result: List<CustomMedicalSuggestionRequest>,
        needShowContent: Boolean
    ) {
        adapter.notifyItemRangeRemoved(0, adapter.itemCount)
        viewModel.dataList.clear()
        viewModel.dataList.addAll(result)
        adapter.notifyItemRangeInserted(0, result.size)
        if (needShowContent) {
            loadingView.showContent()
        }
        if (result.isEmpty()) {
            loadingView.showEmpty()
        } else {
            loadingView.showContent()
        }
        smart_refresh_layout.finishRefresh()
    }

    private fun refreshReceive(needShowContent: Boolean) {
        CustomMedicalSuggestionRequest.queryReceive(userId, object :
            RequestCallBack<List<CustomMedicalSuggestionRequest>>() {
            override fun onSuccess(result: List<CustomMedicalSuggestionRequest>) {
                refreshSuccess(result, needShowContent)
            }

            override fun onFailed(errorCode: Int, error: String) {
                smart_refresh_layout.finishRefresh(false)
                loadingView.setOnRetryClickListener {
                    loadingView.showLoading()
                    refreshReceive(true)
                }.showError()
            }

            override fun onNoNetWork() {
                smart_refresh_layout.finishRefresh(false)
                loadingView.setOnRetryClickListener {
                    loadingView.showLoading()
                    refreshReceive(true)
                }.showNoNetwork()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_switch_send -> {
                loadingView.showLoading()
                supportActionBar?.subtitle = item.title
                item.isChecked = true
                isSend = true
                refreshSend(true)
                true
            }
            R.id.item_switch_receive -> {
                loadingView.showLoading()
                supportActionBar?.subtitle = item.title
                item.isChecked = true
                isSend = false
                refreshReceive(true)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSelect(
        position: Int,
        customMedicalSuggestionRequest: CustomMedicalSuggestionRequest
    ) {
        startActivityForResult(
            Intent(this, NewMedicalSuggestionActivity::class.java).putExtra(
                NewMedicalSuggestionActivity.CUSTOM_MEDICAL_SUGGESTION_REQUEST,
                customMedicalSuggestionRequest.toString()
            ).putExtra(POSITION, position), NewMedicalSuggestionActivity.REQUEST_CODE
        )
    }

    override fun onUndo(
        position: Int,
        customMedicalSuggestionRequest: CustomMedicalSuggestionRequest
    ) {
        AlertDialog.Builder(this).setTitle(R.string.recall).setMessage("确定撤回这条请求吗，对方将受到一条撤回通知")
            .setPositiveButton(R.string.confirm) { _, _ ->
                MedicalSuggestionRequest.delete(
                    customMedicalSuggestionRequest.id, 0, object : RequestCallBack<Boolean>() {
                        override fun onSuccess(result: Boolean) {
                            XToast.success(getString(R.string.recall_success))
                            removeRequest(position)
                        }

                        override fun onFailed(errorCode: Int, error: String) {
                            XToast.success("$errorCode\n$error")
                        }

                        override fun onNoNetWork() {
                            XToast.info(getString(R.string.xloading_no_network_text))
                        }
                    })
            }.setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun removeRequest(position: Int) {
        viewModel.dataList.removeAt(position)
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position, adapter.itemCount - position)
        if (viewModel.dataList.isEmpty()) {
            loadingView.showEmpty()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == NewMedicalSuggestionActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val position = data.getIntExtra(POSITION, -1)
                if (position != -1) {
                    removeRequest(position)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        const val POSITION = "POSITION"
    }

}