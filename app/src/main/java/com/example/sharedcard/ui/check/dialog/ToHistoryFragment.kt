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
import com.example.sharedcard.databinding.DialogFragmentToHistoryBinding

class ToHistoryFragment : DialogFragment() {

    private lateinit var binding: DialogFragmentToHistoryBinding
    private val viewModel: ToHistoryViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.page = arguments?.getInt(KEY_PAGE)!!
        viewModel.name = arguments?.getString(KEY_NAME)!!
        viewModel.id = arguments?.getLong(KEY_ID)!!
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_fragment_to_history,
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dialogToHistoryTextView.text = when (viewModel.page) {
            0 -> getString(R.string.dialog_to_history_product, viewModel.name)
            1 -> getString(R.string.dialog_to_history_target, viewModel.name)
            else -> throw IndexOutOfBoundsException()
        }

        viewModel.getShop().observe(this) { shops ->
            binding.dialogToHistoryShopSpinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                shops
            )
        }

        viewModel.getCurrency().observe(this) { currencies ->
            binding.dialogToHistoryCurrencySpinner.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                currencies
            )
        }
    }

    override fun onStart() {
        super.onStart()

        binding.dialogToHistoryEditView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.price = p0.toString().toInt()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.dialogToHistoryShopSpinner.onItemSelectedListener =
            object : OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    viewModel.shop = p3 + 1
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        binding.dialogToHistoryCurrencySpinner.onItemSelectedListener =
            object : OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    viewModel.currency = p3 + 1
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        binding.dialogToHistoryAddButton.setOnClickListener {
            viewModel.toHistory()
            dismiss()
        }
    }

    companion object {
        const val KEY_PAGE = "page"
        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val DIALOG_TO_HISTORY = "dialogToHistory"
        fun newInstance(page: Int, id: Long, name: String): ToHistoryFragment {
            val dialog = ToHistoryFragment()
            val args = Bundle()
            args.putInt(KEY_PAGE, page)
            args.putLong(KEY_ID, id)
            args.putString(KEY_NAME, name)
            dialog.arguments = args
            return dialog
        }
    }
}