package com.example.sharedcard.ui.bottom_navigation.check.add_target


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.product.ProductEntity
import com.example.sharedcard.databinding.DialogFragmentAddCheckBinding
import com.example.sharedcard.databinding.DialogFragmentAddTargetBinding
import com.example.sharedcard.ui.bottom_navigation.check.array_adapter.CategoryArrayAdapter


class AddTargetFragment : DialogFragment() {
    private lateinit var binding: DialogFragmentAddTargetBinding
    private val viewModel: AddTargetViewModel by viewModels()




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_fragment_add_target,
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCurrency().observe(this) { currency ->
            binding.dialogCurrencySpinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                currency
            )
        }
        viewModel.getCategory().observe(this) {category ->
            binding.dialogCategorySpinner.adapter = CategoryArrayAdapter(category)
        }
    }

    override fun onStart() {
        super.onStart()

        binding.dialogNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.name = p0.toString()
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.dialogPriceEditView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.price = when(p0.toString()){
                    "" -> 0
                    else -> p0.toString().toInt()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.dialogCurrencySpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.currency = p3 + 1
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        binding.dialogCategorySpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.category = p3
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        binding.dialogAddButton.setOnClickListener {
            when(viewModel.check()){
                1 -> Toast.makeText(requireContext(),"Некорректное имя цели!",Toast.LENGTH_SHORT).show()
                2 -> {
                    viewModel.add()
                    dismiss()
                }
            }
        }
    }
}