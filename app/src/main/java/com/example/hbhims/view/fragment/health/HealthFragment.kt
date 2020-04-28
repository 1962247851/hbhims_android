package com.example.hbhims.view.fragment.health

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.hbhims.R
import com.example.hbhims.view.adapter.viewpager2.HealthPagerAdapter
import com.example.hbhims.view.base.AbstractFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_health.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HealthFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HealthFragment : AbstractFragment() {

    private lateinit var healthPagerAdapter: HealthPagerAdapter

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_health
    }

    override fun initData(savedInstanceState: Bundle?) {
        healthPagerAdapter = HealthPagerAdapter(childFragmentManager, lifecycle)
    }

    override fun initView() {
        view_pager2.adapter = healthPagerAdapter
        view_pager2.offscreenPageLimit = 3
        val healthEntries = resources.getStringArray(R.array.entries_health)
        TabLayoutMediator(tab_layout, view_pager2) { tab, position ->
            tab.text = healthEntries[position]
        }.attach()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyHealthFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HealthFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
