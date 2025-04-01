package com.example.sharedcard.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.BottomSheetTextBinding
import com.example.sharedcard.util.appComponent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditTextBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetTextBinding


    private val viewModel by viewModels<ProfileViewModel> {
        appComponent.multiViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetTextBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUser().observe(viewLifecycleOwner) { user ->
            binding.editText.setText(user.name)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.profileCancelButton.setOnClickListener {
            dismiss()
        }
        binding.profileOkButton.setOnClickListener {
            if (binding.editText.text.toString().isNotEmpty())
                viewModel.setName(binding.editText.text.toString())
            dismiss()
        }
    }

    companion object {
        const val TAG = "EditTextBottomSheet"
    }
}