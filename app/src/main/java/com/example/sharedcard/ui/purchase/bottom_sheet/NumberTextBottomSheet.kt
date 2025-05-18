package com.example.sharedcard.ui.purchase.bottom_sheet

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sharedcard.databinding.BottomSheetNumberTextBinding
import com.example.sharedcard.ui.purchase.add_purchase.AddPurchaseFragment
import com.example.sharedcard.util.getSerializableCompat
import com.example.sharedcard.util.toStringFormat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NumberTextBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetNumberTextBinding
    var count = 0.0
    private lateinit var connect: AddPurchaseFragment.Connect
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        count = arguments?.getDouble(ARG_COUNT, 0.0) ?: 0.0
        connect = arguments?.getSerializableCompat(ARG_CONNECT)!!
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetNumberTextBinding.inflate(
            layoutInflater,
            container,
            false
        )
        binding.editText.inputType = when (arguments?.getBoolean(ARG_IS_DOUBLE) ?: true) {
            true -> InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
            false -> InputType.TYPE_CLASS_NUMBER
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (count != 0.0) {
            binding.editText.setText(count.toStringFormat())
        }
        binding.profileOkButton.setOnClickListener {
            if (
                binding.editText.text.toString().isNotBlank() &&
                binding.editText.text.toString().toDouble() != 0.0
            ) {
                val count = binding.editText.text.toString().toDouble()
                connect.ok(count)
                dismiss()
            }
        }
        binding.profileCancelButton.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        const val TAG = "EditTextBottomSheet"
        private const val ARG_COUNT = "count"
        private const val ARG_CONNECT = "connect"
        private const val ARG_IS_DOUBLE = "isDouble"

        fun newInstance(count: Double, isDouble: Boolean, function: AddPurchaseFragment.Connect) =
            NumberTextBottomSheet().apply {
                val bundle = Bundle()
                bundle.putDouble(ARG_COUNT, count)
                bundle.putBoolean(ARG_IS_DOUBLE, isDouble)

                bundle.putSerializable(ARG_CONNECT, function)
                arguments = bundle
            }

    }
}