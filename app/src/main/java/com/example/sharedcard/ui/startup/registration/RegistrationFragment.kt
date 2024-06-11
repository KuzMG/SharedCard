package com.example.sharedcard.ui.startup.registration

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentRegistrationBinding
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerActivity
import com.example.sharedcard.ui.startup.StartupActivity
import com.example.sharedcard.ui.startup.StartupViewModel
import com.example.sharedcard.util.appComponent

class RegistrationFragment : Fragment() {


    private val viewModel by viewModels<StartupViewModel>({ activity as StartupActivity }){
        appComponent.multiViewModelFactory
    }
    private lateinit var binding: FragmentRegistrationBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            result ?: return@observe
            if (result.error == null) {
                startActivity(
                    Intent(requireActivity(), NavigationDrawerActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            } else {
                showLoginFailed(result.error!!)
            }
        }

        viewModel.loginFormState.observe(viewLifecycleOwner) { state ->
            if (state == null) {
                return@observe
            }
            binding.continueButton.isEnabled = state.isDataValid
            binding.sendCodeButton.isEnabled = when (state.usernameError) {
                null -> true
                0 -> false
                else -> {
                    binding.loginEditText.error = getString(state.usernameError)
                    false
                }
            }
            state.passwordError?.let {
                if(it!=0)
                    binding.passwordEditText.error = getString(it)
            }
            state.repeatPasswordError?.let {
                binding.repeatPasswordEditText.error = getString(it)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                viewModel.registration(
                    binding.loginEditText.text.toString(),
                    binding.passwordEditText.text.toString(),
                    binding.repeatPasswordEditText.text.toString(),
                    binding.codeEditText.text.toString()
                )
            }
        }
        binding.loginEditText.addTextChangedListener(textWatcher)
        binding.codeEditText.addTextChangedListener(textWatcher)
        binding.passwordEditText.addTextChangedListener(textWatcher)
        binding.repeatPasswordEditText.addTextChangedListener(textWatcher)

        binding.sendCodeButton.setOnClickListener {
            viewModel.sendMessage(binding.loginEditText.text.toString())
        }

        binding.continueButton.setOnClickListener {
            viewModel.signUp(
                binding.loginEditText.text.toString(),
                binding.passwordEditText.text.toString(),
                binding.codeEditText.text.toString()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clear()
    }
    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }
}