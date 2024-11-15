package com.example.sharedcard.ui.check.add_target


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentAddTargetBinding
import com.example.sharedcard.ui.adapter.CategoryArrayAdapter
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.util.isInternetConnection
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddTargetBottomSheet : BottomSheetDialogFragment() {


    private lateinit var binding: FragmentAddTargetBinding


    private val viewModel: AddTargetViewModel by viewModels {
        appComponent.multiViewModelFactory
    }

    companion object {
        const val DIALOG_ADD = "dialogAdd"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_target,
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCurrency().observe(this) { currency ->
            binding.dialogCurrencySpinner.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    currency
                )
            )
        }
        viewModel.getCategory().observe(this) { category ->
            binding.dialogCategorySpinner.setAdapter(CategoryArrayAdapter(category))
            binding.dialogCategorySpinner.setText(category[0].name)
            viewModel.category = category[0].id
        }
        viewModel.sendLiveData.observe(viewLifecycleOwner){
            it?.let {
                Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
            }
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()

        binding.dialogNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.dialogAddButton.isEnabled = p0.toString().isNotEmpty()
                viewModel.name = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.dialogPriceEditView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.price = when (p0.toString()) {
                    "" -> 0
                    else -> p0.toString().toInt()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.dialogCurrencySpinner.onItemClickListener =
            OnItemClickListener { _, _, _, id -> viewModel.currency = id.toInt() + 1 }
        binding.dialogCategorySpinner.onItemClickListener =
            OnItemClickListener { _, _, _, id -> viewModel.category = id.toInt() }
        binding.dialogAddButton.setOnClickListener {
            when (viewModel.check()) {
                1 -> Toast.makeText(requireContext(), "Некорректное имя цели!", Toast.LENGTH_SHORT)
                    .show()

                2 -> {
                    viewModel.add(isInternetConnection(requireContext()))
                }
            }
        }
    }
}