package com.example.sharedcard.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.example.sharedcard.databinding.BottomSheetNumberBinding
import com.example.sharedcard.util.appComponent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class EditNumberBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetNumberBinding
    private var type = 0

    private val viewModel by viewModels<ProfileViewModel> {
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
        binding = BottomSheetNumberBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUser().observe(viewLifecycleOwner) { user ->
            when (type) {
                HEIGHT -> showNumberPickerDialog(Array(200) {
                    (50 + it).toString()
                }, user.height.toString())

                WEIGHT -> showNumberPickerDialog(Array(320) {
                    (40F + (it.toFloat()) / 2).toString()
                }, user.weight.toString())

                else -> throw IndexOutOfBoundsException()
            }
        }
    }

    private fun showNumberPickerDialog(
        array: Array<String>,
        value: String
    ) {
        binding.profileNumberPicker.displayedValues = array
        binding.profileNumberPicker.wrapSelectorWheel = false
        binding.profileNumberPicker.minValue = 0
        binding.profileNumberPicker.maxValue = array.size
        binding.profileNumberPicker.value = array.indexOf(value)
        val editText = binding.profileNumberPicker.getChildAt(0) as EditText
        editText.width = 20
    }

    override fun onStart() {
        super.onStart()
        binding.profileCancelButton.setOnClickListener {
            dismiss()
        }
        binding.profileOkButton.setOnClickListener {
            val value = binding.profileNumberPicker.displayedValues[binding.profileNumberPicker.value]
            when (type) {
                WEIGHT -> {
                    viewModel.setWeight(value.toDouble())
                }

                HEIGHT -> {
                    viewModel.setHeight(value.toInt())
                }

            }
            dismiss()
        }
    }

    companion object {
        const val TAG = "EditNumberBottomSheet"
        private const val ARG_TYPE = "TYPE"
        const val HEIGHT = 1
        const val WEIGHT = 2
        fun newInstance(type: Int) = EditNumberBottomSheet().apply {
            arguments = Bundle().apply {
                putInt(ARG_TYPE, type)
            }
        }
    }
}