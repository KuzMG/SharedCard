package com.example.sharedcard.ui.bottom_navigation.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentGroupBinding

class GroupFragment : Fragment() {
    private lateinit var binding: FragmentGroupBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_group, container, false)
        binding.groupViewPager2.apply {
            adapter = GroupStateAdapter(requireActivity())
            isUserInputEnabled = false
        }
        return binding.root
    }

    class GroupStateAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int = 1

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> GroupStateFragment()
                else -> throw IndexOutOfBoundsException()
            }
        }

    }
}