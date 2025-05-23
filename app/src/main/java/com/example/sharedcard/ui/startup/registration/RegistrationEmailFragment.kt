package com.example.sharedcard.ui.startup.registration

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentRegistrationEmailBinding
import com.example.sharedcard.ui.startup.StartupActivity
import com.example.sharedcard.ui.startup.StartupViewModel
import com.example.sharedcard.util.appComponent


class RegistrationEmailFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationEmailBinding
    private val viewModel by viewModels<StartupViewModel>({ activity as StartupActivity }) {
        appComponent.multiViewModelFactory
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.registerFormState.observe(viewLifecycleOwner) { state ->
            state ?: return@observe
            state.emailError?.let {
                binding.emailEditText.error = getString(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.emailEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.regEmail = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        binding.emailEditText.setText(viewModel.regEmail)

    }

}