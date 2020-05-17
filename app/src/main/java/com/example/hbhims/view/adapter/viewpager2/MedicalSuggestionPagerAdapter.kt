package com.example.hbhims.view.adapter.viewpager2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hbhims.view.fragment.medicalsuggestion.receive.MedicalSuggestionReceiveFragment
import com.example.hbhims.view.fragment.medicalsuggestion.send.MedicalSuggestionSendFragment

class MedicalSuggestionPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            MedicalSuggestionReceiveFragment()
        } else {
            MedicalSuggestionSendFragment()
        }
    }

}