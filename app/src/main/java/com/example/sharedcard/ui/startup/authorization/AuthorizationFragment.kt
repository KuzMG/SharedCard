package com.example.sharedcard.ui.startup.authorization

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
import com.example.sharedcard.databinding.FragmentAuthorizationBinding
import com.example.sharedcard.ui.startup.StartupActivity
import com.example.sharedcard.ui.startup.StartupViewModel
import com.example.sharedcard.ui.startup.StartupViewModel.State.Continue
import com.example.sharedcard.util.appComponent

class AuthorizationFragment : Fragment() {

    private val viewModel by viewModels<StartupViewModel>({ activity as StartupActivity }) {
        appComponent.multiViewModelFactory
    }
    private lateinit var binding: FragmentAuthorizationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_authorization,
            container,
            false
        )


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            result ?: return@observe
            if (result.error == null) {
                viewModel.setTransitionState(Continue)
            } else {
                showLoginFailed(result.error!!)
            }
        }

        viewModel.loginFormState.observe(viewLifecycleOwner) { state ->
            if (state == null) {
                return@observe
            }
            binding.continueButton.isEnabled = state.isDataValid
            state.usernameError?.let {
                if (it != 0)
                    binding.loginEditText.error = getString(it)
            }
            state.passwordError?.let {
                if (it != 0)
                    binding.passwordEditText.error = getString(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                viewModel.authorization(
                    binding.loginEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                )
            }
        }
        binding.loginEditText.addTextChangedListener(textWatcher)
        binding.passwordEditText.addTextChangedListener(textWatcher)

        binding.continueButton.setOnClickListener {
            viewModel.signIn(
                binding.loginEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }
}