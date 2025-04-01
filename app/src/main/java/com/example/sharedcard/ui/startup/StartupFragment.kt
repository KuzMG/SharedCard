package com.example.sharedcard.ui.startup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentStartupBinding
import com.example.sharedcard.ui.startup.StartupViewModel.State.Authorization
import com.example.sharedcard.ui.startup.StartupViewModel.State.Registration
import com.example.sharedcard.util.appComponent

class StartupFragment : Fragment() {

    private val viewModel: StartupViewModel by viewModels({activity as StartupActivity}) {
        appComponent.multiViewModelFactory
    }
    private lateinit var binding: FragmentStartupBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartupBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.signUpButton.setOnClickListener {
            viewModel.setTransitionState(Registration)
        }
        binding.signInButton.setOnClickListener {
            viewModel.setTransitionState(Authorization)
        }
    }


}