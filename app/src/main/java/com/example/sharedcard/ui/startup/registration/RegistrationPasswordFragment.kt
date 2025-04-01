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
import com.example.sharedcard.databinding.FragmentRegistrationPasswordBinding
import com.example.sharedcard.ui.startup.StartupActivity
import com.example.sharedcard.ui.startup.StartupViewModel
import com.example.sharedcard.util.appComponent


class RegistrationPasswordFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationPasswordBinding
    private val viewModel by viewModels<StartupViewModel>({ activity as StartupActivity }) {
        appComponent.multiViewModelFactory
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationPasswordBinding.inflate(inflater, container, false)
        binding.passwordEditText.setText(viewModel.regPassword)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.registerFormState.observe(viewLifecycleOwner) { state ->
            state ?: return@observe
            state.passwordError?.let {
                binding.passwordEditText.error = getString(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.passwordEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.regPassword = p0.toString().trim()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        binding.passwordEditText.setText(viewModel.regPassword)
    }
}