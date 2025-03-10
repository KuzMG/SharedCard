package com.example.sharedcard.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentHistoryBinding
import com.example.sharedcard.ui.history.product.HistoryProductFragment
import com.example.sharedcard.ui.history.target.HistoryTargetFragment
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerActivity
import com.google.android.material.tabs.TabLayoutMediator


class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private val historyProductFragment= HistoryProductFragment()
    private val historyTargetFragment= HistoryTargetFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().setTitle(R.string.history)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        historyProductFragment.onResume()
        historyTargetFragment.onResume()
        binding.apply {

            appBar.toolbar.setTitle(R.string.history)
            (requireActivity() as NavigationDrawerActivity).setSupportActionBar(appBar.toolbar)
            (requireActivity() as NavigationDrawerActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            historyPager.adapter = HistoryListAdapter(this@HistoryFragment)
            historyPager.offscreenPageLimit = 2
            historyPager.isSaveEnabled = false
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

    class HistoryListAdapter(fragment: Fragment) :
        FragmentStateAdapter(fragment) {
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