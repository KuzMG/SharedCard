package com.example.sharedcard.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentSettingsBinding
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerActivity
import com.example.sharedcard.util.appComponent


class SettingsFragment : Fragment() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: FragmentSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,appComponent.multiViewModelFactory)[SettingsViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentSettingsBinding.inflate(
                layoutInflater,
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            currencyTextView.text
        }
        viewModel.getCurrency().observe(viewLifecycleOwner){
            binding.currencyTextView.text = getString(R.string.settings_currency,it.name)

        }
        binding.currencyTextView.setOnClickListener {
            CurrencyBottomSheet().show(childFragmentManager,CurrencyBottomSheet.TAG)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.appBar.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

}