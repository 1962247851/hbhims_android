package com.example.hbhims.view.fragment.medicalsuggestion

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.hbhims.App
import com.example.hbhims.R
import com.example.hbhims.view.activity.medicalsuggestionrequest.MedicalSuggestionRequestActivity
import com.example.hbhims.view.adapter.viewpager2.MedicalSuggestionPagerAdapter
import com.example.hbhims.view.base.AbstractFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.dialog_date_and_time_picker.tab_layout
import kotlinx.android.synthetic.main.fragment_medical_suggestion.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USER_ID = "USER_ID"

/**
 * A simple [Fragment] subclass.
 * Use the [MedicalSuggestionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MedicalSuggestionFragment : AbstractFragment() {

    private var userId: Long = App.user.id
    private lateinit var medicalSuggestionPagerAdapter: MedicalSuggestionPagerAdapter
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getLong(ARG_USER_ID)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_medical_suggestion
    }

    override fun initData(savedInstanceState: Bundle?) {
        medicalSuggestionPagerAdapter =
            MedicalSuggestionPagerAdapter(childFragmentManager, lifecycle)
    }

    override fun initView() {
        view_pager2.offscreenPageLimit = 2
        view_pager2.adapter = medicalSuggestionPagerAdapter
        tabLayoutMediator = TabLayoutMediator(tab_layout, view_pager2,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = resources.getStringArray(R.array.entries_medical_suggestion)[position]
            })
        tabLayoutMediator.attach()
        fab.setOnClickListener {
            startActivity(Intent(requireContext(), MedicalSuggestionRequestActivity::class.java))
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param userId UserId.
         * @return A new instance of fragment MedicalSuggestionFragment.
         */
        @JvmStatic
        fun newInstance(userId: Long) =
            MedicalSuggestionFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_USER_ID, userId)
                }
            }
    }
}
