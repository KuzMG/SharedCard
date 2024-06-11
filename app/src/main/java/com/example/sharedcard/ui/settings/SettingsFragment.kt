package com.example.sharedcard.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentSettingsBinding
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerActivity
import com.example.sharedcard.util.appComponent


class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels {
        appComponent.multiViewModelFactory
    }
    private lateinit var binding: FragmentSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().setTitle(R.string.settings)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_settings,
                container,
                false
            )
        binding.appBar.toolbar.setTitle(R.string.settings)
        (requireActivity() as NavigationDrawerActivity).setSupportActionBar(binding.appBar.toolbar)
        (requireActivity() as NavigationDrawerActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
            true
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            quickDeleteSwitch.setOnCheckedChangeListener { _, isChecked ->
                viewModel.quickDelete = isChecked
            }
        }

    }


}