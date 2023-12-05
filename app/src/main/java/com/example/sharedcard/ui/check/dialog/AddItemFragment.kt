package com.example.sharedcard.ui.check.dialog



import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.DialogFragmentAddItemBinding


class AddItemFragment : DialogFragment() {
    private lateinit var binding: DialogFragmentAddItemBinding
    private val viewModel: AddItemViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.page = requireArguments().getInt(KEY_PAGE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_fragment_add_item,
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (viewModel.page) {
            0 -> {
                binding.dialogAddTextView.text = getString(R.string.dialog_add_product)
                binding.dialogNameEditText.hint = getString(R.string.dialog_add_text_hint_porduct)
                binding.dialogCountEditView.hint =
                    getString(R.string.dialog_add_text_hint_count)
            }

            1 -> {
                binding.dialogAddTextView.text = getString(R.string.dialog_add_target)
                binding.dialogNameEditText.hint = getString(R.string.dialog_add_text_hint_target)
                binding.dialogCountEditView.hint =
                    getString(R.string.dialog_add_text_hint_price)
            }
        }
        viewModel.getCategory().observe(this) { categories ->
            binding.dialogCategorySpinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                categories
            )
        }
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
                viewModel.name = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.dialogCountEditView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.count = p0.toString().toInt()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.dialogCategorySpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.category = p3+1
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        binding.dialogMetricSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.metric = p3+1
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        binding.dialogAddButton.setOnClickListener {
            viewModel.add()
            dismiss()
        }
    }

    companion object {
        const val KEY_PAGE = "page"
        fun newInstance(page: Int): AddItemFragment {
            val dialog = AddItemFragment()
            val args = Bundle()
            args.putInt(KEY_PAGE, page)
            dialog.arguments = args
            return dialog
        }
    }
}