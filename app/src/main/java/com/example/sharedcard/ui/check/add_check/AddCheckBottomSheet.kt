package com.example.sharedcard.ui.check.add_check


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
import com.example.sharedcard.databinding.FragmentAddCheckBinding
import com.example.sharedcard.ui.check.array_adapter.MetricArrayAdapter
import com.example.sharedcard.ui.check.array_adapter.ProductArrayAdapter
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.viewmodel.MultiViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject


class AddCheckBottomSheet : BottomSheetDialogFragment() {


    private lateinit var binding: FragmentAddCheckBinding


    private val viewModel: AddCheckViewModel by viewModels {
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
                R.layout.fragment_add_check,
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMetric().observe(this) { metric ->
            binding.dialogMetricSpinner.setAdapter(
                MetricArrayAdapter(metric)
            )
            binding.dialogMetricSpinner.setText(metric[0])
            binding.dialogCountEditView.setText("1")
        }
    }

    override fun onStart() {
        super.onStart()
        binding.dialogNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.getProducts(p0.toString()).observe(viewLifecycleOwner) { products ->
                    if (products.isNotEmpty() && products[0].name == p0.toString()) {
                        viewModel.product = products[0]
                        binding.dialogAddButton.isEnabled = true
                    } else {
                        viewModel.product = null
                        binding.dialogAddButton.isEnabled = false
                    }
                    binding.dialogNameEditText.setAdapter(ProductArrayAdapter(products))
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.dialogCountEditView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.count = when (p0.toString()) {
                    "" -> 1
                    else -> p0.toString().toInt()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.dialogMetricSpinner.onItemClickListener =
            OnItemClickListener { _, _, _, id -> viewModel.metric = id + 1 }
        binding.dialogDescriptionEditView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.description = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.dialogAddButton.setOnClickListener {
            when (viewModel.check()) {
                1 -> Toast.makeText(
                    requireContext(),
                    "Некорректное имя продукта!",
                    Toast.LENGTH_SHORT
                ).show()

                2 -> {
                    viewModel.add()
                    dismiss()
                }
            }
        }
    }
}