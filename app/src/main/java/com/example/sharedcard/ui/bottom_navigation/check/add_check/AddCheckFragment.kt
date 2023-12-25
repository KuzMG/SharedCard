package com.example.sharedcard.ui.bottom_navigation.check.add_check


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentAddCheckBinding
import com.example.sharedcard.ui.bottom_navigation.check.array_adapter.ProductArrayAdapter


class AddCheckFragment : DialogFragment() {
    private lateinit var binding: FragmentAddCheckBinding
    private val viewModel: AddCheckViewModel by viewModels()

companion object{
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
            binding.dialogMetricSpinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                metric
            )
        }
    }

    override fun onStart() {
        super.onStart()
        binding.dialogNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.getProducts(p0.toString()).observe(viewLifecycleOwner) { products ->
                    if (products[0].name == p0.toString()) {
                        viewModel.product = products[0]
                    } else {
                        viewModel.product = null
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
        binding.dialogMetricSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.metric = p3 + 1
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
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