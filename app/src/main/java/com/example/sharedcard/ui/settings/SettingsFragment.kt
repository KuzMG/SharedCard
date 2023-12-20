package com.example.sharedcard.ui.settings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentSettingsBinding
import com.example.sharedcard.ui.MainActivity

class SettingsFragment : Fragment() {


    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_settings,
            container,
            false
        )
        (requireActivity() as MainActivity).setToolbar(R.string.settings)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUser().observe(viewLifecycleOwner){user ->
            binding.userNameTextView.text = user.name
        }
        binding.quickDeleteCheckBox.isChecked = viewModel.quickDelete
    }

    override fun onStart() {
        super.onStart()
        binding.quickDeleteCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.quickDelete = isChecked
        }
    }
}