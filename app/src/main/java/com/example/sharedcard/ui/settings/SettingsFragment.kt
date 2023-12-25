package com.example.sharedcard.ui.settings

import android.content.Context
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
import com.example.sharedcard.ui.authorization.AuthorizationFragment

class SettingsFragment : Fragment() {
    interface Callbacks {
        fun onSettingsToAuthorizationFragment()
    }
    private var callbacks: Callbacks? = null
    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var binding: FragmentSettingsBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }
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
            binding.apply {
                userNameTextView.text = getString(R.string.name,user.name)
                userEmailTextView.text =getString(R.string.email,user.email)
                userWeightTextView.text = getString(R.string.weight,user.weight.toString())
                userHeightTextView.text = getString(R.string.height,user.height.toString())
                userAgeTextView.text = getString(R.string.age,user.age.toString())
            }
        }
        binding.quickDeleteCheckBox.isChecked = viewModel.quickDelete
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            quickDeleteCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                viewModel.quickDelete = isChecked
            }
            userChangeButton.setOnClickListener{
                callbacks?.onSettingsToAuthorizationFragment()
            }
        }
    }
    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }
}