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
import com.example.sharedcard.databinding.FragmentRegistrationNameBinding
import com.example.sharedcard.ui.startup.StartupActivity
import com.example.sharedcard.ui.startup.StartupViewModel
import com.example.sharedcard.util.appComponent
import java.util.Map

class RegistrationNameFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationNameBinding
    private val viewModel by viewModels<StartupViewModel>({ activity as StartupActivity }) {
        appComponent.multiViewModelFactory
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationNameBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.registerFormState.observe(viewLifecycleOwner) { state ->
            state ?: return@observe
            state.usernameError?.let {
                binding.nameEditText.error = getString(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.nameEditText.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.regName = p0.toString().trim()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.nameEditText.setText(viewModel.regName)
    }
}