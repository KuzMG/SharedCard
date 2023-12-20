package com.example.sharedcard.ui.bottom_navigation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentHistoryBinding
import com.example.sharedcard.ui.bottom_navigation.check.product.ProductFragment
import com.example.sharedcard.ui.bottom_navigation.check.target.TargetFragment
import com.example.sharedcard.ui.bottom_navigation.history.product.HistoryProductFragment
import com.example.sharedcard.ui.bottom_navigation.history.target.HistoryTargetFragment
import com.google.android.material.tabs.TabLayoutMediator


class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_history,
            container,
            false
        )
        binding.apply {
            historyPager.adapter =
                HistoryListAdapter(
                    requireActivity()
                )
            historyPager.offscreenPageLimit = 2
            TabLayoutMediator(historyTab, historyPager) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.tab_product)
                    1 -> getString(R.string.tab_target)
                    else -> throw IndexOutOfBoundsException()
                }
            }.attach()

        }
        return binding.root
    }

    class HistoryListAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HistoryProductFragment()
                1 -> HistoryTargetFragment()
                else -> throw IndexOutOfBoundsException()
            }
        }

    }
}