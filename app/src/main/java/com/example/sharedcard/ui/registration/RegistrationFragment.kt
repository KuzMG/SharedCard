package com.example.sharedcard.ui.registration

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentRegistrationBinding
import com.example.sharedcard.ui.authorization.AuthorizationFragment

class RegistrationFragment : Fragment() {
    interface Callbacks {
        fun onRegistrationToNavigationFragment()
    }
    private var callbacks: Callbacks? = null

    private val viewModel: RegistrationViewModel by viewModels()
    private lateinit var binding: FragmentRegistrationBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onStart() {
        super.onStart()
        binding.nameEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.name =p0.toString()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.loginEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.email = p0.toString()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.codeEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.code = p0.toString()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.passwordEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.password = p0.toString()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.repeatPasswordEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.repeatPassword = p0.toString()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.sendCodeButton.setOnClickListener {
            viewModel.sendMessage()
        }

        binding.continueButton.setOnClickListener {
            when(viewModel.check()){
                0 -> Toast.makeText(requireContext(),"Заполните поля!",Toast.LENGTH_SHORT).show()
                1 -> Toast.makeText(requireContext(),"Код подтверждения неверный!",Toast.LENGTH_SHORT).show()
                2 -> Toast.makeText(requireContext(),"Пароли не совпадают!",Toast.LENGTH_SHORT).show()
                3 ->{
                    viewModel.create()
                    callbacks?.onRegistrationToNavigationFragment()
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }
}