package com.example.sharedcard.ui.check

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentCheckBinding
import com.example.sharedcard.ui.check.dialog.AddProductFragment
import com.example.sharedcard.ui.check.product.ProductFragment
import com.example.sharedcard.ui.check.target.TargetFragment
import com.google.android.material.tabs.TabLayoutMediator

private const val DIALOG_ADD = "dialogAdd"

class CheckFragment : Fragment() {

    private val viewModel: CheckViewModel by viewModels()
    private lateinit var binding: FragmentCheckBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_check,
            container,
            false
        )
        binding.apply {
            checkPager.adapter = CheckListAdapter(this@CheckFragment)

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
        binding.newItemButton.setOnClickListener {
            AddProductFragment.newInstance(binding.checkPager.currentItem)
                .show(parentFragmentManager, DIALOG_ADD)
        }
    }

    class CheckListAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
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