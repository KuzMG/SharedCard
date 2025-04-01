package com.example.sharedcard.ui.startup.authentication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentAuthorizationBinding
import com.example.sharedcard.ui.startup.StartupActivity
import com.example.sharedcard.ui.startup.StartupViewModel
import com.example.sharedcard.ui.startup.StartupViewModel.State.Registration
import com.example.sharedcard.ui.startup.StartupViewModel.State.Startup
import com.example.sharedcard.ui.startup.StartupViewModel.State.Synchronization
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.util.isInternetConnection

class AuthenticationFragment : Fragment() {

    private val viewModel by viewModels<StartupViewModel>({ activity as StartupActivity }) {
        appComponent.multiViewModelFactory
    }
    private lateinit var binding: FragmentAuthorizationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthorizationBinding.inflate(
            layoutInflater,
            container,
            false
        )

        binding.run {
            loginEditText.setText(viewModel.authLogin)
            passwordEditText.setText(viewModel.authPassword)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.authResult.observe(viewLifecycleOwner) { result ->
            result ?: return@observe
            if (result.loading) {
                    loading(true)
            } else {
                loading(false)
                if (result.message != null) {
                    showLoginFailed(result.message,result.error)
                } else {
                    viewModel.setTransitionState(Synchronization)
                }
            }
        }

        viewModel.authFormState.observe(viewLifecycleOwner) { state ->
            state ?: return@observe
            binding.continueButton.isEnabled = state.isDataValid
            state.usernameError?.let {
                    binding.loginEditText.error = getString(it)
            }
            state.passwordError?.let {
                    binding.passwordEditText.error = getString(it)
            }
        }
    }
    private fun loading(flag:Boolean){
        if(flag) {
            binding.progressBar.visibility = View.VISIBLE
            binding.continueButton.isEnabled = false
        } else {
            binding.progressBar.visibility = View.GONE
            binding.continueButton.isEnabled = true
        }
    }

    override fun onStart() {
        super.onStart()
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                viewModel.authenticationUI(
                    binding.loginEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                )
            }
        }
        binding.loginEditText.addTextChangedListener(textWatcher)
        binding.passwordEditText.addTextChangedListener(textWatcher)

        binding.continueButton.setOnClickListener {
            viewModel.authentication(
                binding.loginEditText.text.toString(),
                binding.passwordEditText.text.toString(),
                isInternetConnection(requireContext())
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel
    }

    private fun showLoginFailed(@StringRes errorString: Int,e: Exception?) {
        val appContext = context?.applicationContext ?: return
        val message = getString(errorString)
        Toast.makeText(appContext, message + (e?.message ?: ""), Toast.LENGTH_LONG).show()
    }
}