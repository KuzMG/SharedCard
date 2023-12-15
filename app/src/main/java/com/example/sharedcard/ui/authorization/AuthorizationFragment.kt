package com.example.sharedcard.ui.authorization

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentAuthorizationBinding
import java.util.UUID

class AuthorizationFragment : Fragment() {
    interface Callbacks {
        fun onAuthorizationToRegistrationFragment()
        fun onAuthorizationToNavigationFragment()
        fun onAuthorizationToChangePasswordFragment()
    }
    private var callbacks: Callbacks? = null
    private val viewModel: AuthorizationViewModel by viewModels()
    private lateinit var binding: FragmentAuthorizationBinding

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
            R.layout.fragment_authorization,
            container,
            false
        )


        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.loginEditText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.login = p0.toString()
            }
            override fun afterTextChanged(p0: Editable?) {}
        } )

        binding.passwordEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.password = p0.toString()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.signUpButton.setOnClickListener {
            callbacks?.onAuthorizationToRegistrationFragment()
        }
        binding.changePasswordButton.setOnClickListener {
            callbacks?.onAuthorizationToChangePasswordFragment()
        }
        binding.continueButton.setOnClickListener {
            when(viewModel.check()){
                0 -> Toast.makeText(requireContext(),"Заполните поля!",Toast.LENGTH_SHORT).show()
                1 -> {
                    viewModel.signIn()
                    callbacks?.onAuthorizationToNavigationFragment()
                }

            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

}