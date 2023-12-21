package com.example.sharedcard.ui.bottom_navigation.check

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentCheckBinding
import com.example.sharedcard.ui.bottom_navigation.check.add_check.AddCheckFragment
import com.example.sharedcard.ui.bottom_navigation.check.add_target.AddTargetFragment
import com.example.sharedcard.ui.bottom_navigation.check.product.ProductFragment
import com.example.sharedcard.ui.bottom_navigation.check.target.TargetFragment
import com.google.android.material.tabs.TabLayoutMediator



class CheckFragment : Fragment() {


    private lateinit var binding: FragmentCheckBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_check,
            container,
            false
        )
        binding.apply {
            checkPager.adapter =
                CheckListAdapter(
                    requireActivity()
                )
            checkPager.offscreenPageLimit = 2
            checkPager.isUserInputEnabled = false
            TabLayoutMediator(checkTab, checkPager) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.tab_product)
                    1 -> getString(R.string.tab_target)
                    else -> throw IndexOutOfBoundsException()
                }
            }.attach()

        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.searchButton.setOnClickListener {
            when (binding.checkPager.currentItem) {
                0 -> AddCheckFragment().show(
                        parentFragmentManager,
                        AddCheckFragment.DIALOG_ADD
                    )
                1 -> AddTargetFragment().show(
                    parentFragmentManager,
                    AddTargetFragment.DIALOG_ADD
                )
            }

        }
    }


    class CheckListAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ProductFragment()
                1 -> TargetFragment()
                else -> throw IndexOutOfBoundsException()
            }
        }

    }
}