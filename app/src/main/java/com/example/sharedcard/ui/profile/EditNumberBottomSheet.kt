package com.example.sharedcard.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.BottomSheetNumberBinding
import com.example.sharedcard.util.appComponent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditNumberBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetNumberBinding
    private var type = 0

    private val viewModel by viewModels<ProfileViewModel>{
        appComponent.multiViewModelFactory
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments?.getInt(ARG_TYPE, 0)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.bottom_sheet_number,
            container,
            false
        )
        binding.profileNumberPicker.run {
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            wrapSelectorWheel = false
            minValue= 0
            maxValue= 300
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUser().observe(viewLifecycleOwner){user ->
            binding.profileNumberPicker.value = when(type){
                WEIGHT -> user.weight
                HEIGHT -> user.height
                AGE -> user.age
                else -> throw IndexOutOfBoundsException()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.profileCancelButton.setOnClickListener {
            dismiss()
        }
        binding.profileOkButton.setOnClickListener {
            when(type){
                WEIGHT -> {
                    viewModel.setWeight(binding.profileNumberPicker.value)
                }
                HEIGHT -> {
                    viewModel.setHeight(binding.profileNumberPicker.value)
                }
                AGE -> {
                    viewModel.setAge(binding.profileNumberPicker.value)
                }
            }
            dismiss()
        }
    }

    companion object {
        const val TAG = "EditNumberBottomSheet"
        private const val ARG_TYPE = "TYPE"
        const val AGE = 0
        const val HEIGHT = 1
        const val WEIGHT = 2
        fun newInstance(type: Int) = EditNumberBottomSheet().apply {
            arguments = Bundle().apply {
                putInt(ARG_TYPE, type)
            }
        }
    }
}