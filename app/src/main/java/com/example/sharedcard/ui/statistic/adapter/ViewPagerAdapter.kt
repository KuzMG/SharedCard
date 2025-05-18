package com.example.sharedcard.ui.statistic.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sharedcard.ui.statistic.ChartDateFragment

class ViewPagerAdapter(fragment: Fragment,private val size: Int): FragmentStateAdapter(fragment) {
    override fun getItemCount() = size

    override fun createFragment(position: Int): Fragment {
        return ChartDateFragment.newInstance(position)
    }
}