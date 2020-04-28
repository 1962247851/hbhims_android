package com.example.hbhims.view.adapter.viewpager2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hbhims.view.fragment.blood.BloodFragment
import com.example.hbhims.view.fragment.bmi.BMIFragment
import com.example.hbhims.view.fragment.sleep.SleepFragment
import com.example.hbhims.view.fragment.sport.SportFragment

class HealthPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> SportFragment()
            1 -> SleepFragment()
            2 -> BMIFragment()
            else -> BloodFragment()
        }
    }

}