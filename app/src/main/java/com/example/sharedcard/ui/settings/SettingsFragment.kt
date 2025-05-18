package com.example.sharedcard.ui.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                binding.notificationSwitch.isChecked = isGranted
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
        binding.notificationSwitch.isChecked =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else{
            true
        }
        binding.notificationSwitch.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    openAppSettings()
                }

                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) ->{
                    openAppSettings()
                }

                else -> {
                    requestPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
    private fun openAppSettings() {
        val intent = android.content.Intent(
            android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            android.net.Uri.fromParts("package", requireContext().packageName, null)
        )
        startActivity(intent)
    }
}