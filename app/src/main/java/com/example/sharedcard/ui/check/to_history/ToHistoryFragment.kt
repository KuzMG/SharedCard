package com.example.sharedcard.ui.check.to_history

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.database.AppDatabase
import com.example.sharedcard.databinding.FragmentToHistoryBinding
import com.example.sharedcard.ui.check.array_adapter.ShopArrayAdapter
import com.example.sharedcard.util.appComponent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.UUID
import javax.inject.Inject

class ToHistoryFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentToHistoryBinding

    @Inject
    lateinit var factory: ToHistoryViewModel.FactoryHelper
    private val viewModel: ToHistoryViewModel by viewModels {
        factory.create(
            requireArguments().getInt(KEY_PAGE),
            requireArguments().getString(KEY_ID, AppDatabase.DEFAULT_UUID),
            requireArguments().getString(KEY_NAME, "")
        )
    }

    override fun onAttach(context: Context) {
        appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_to_history,
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
            binding.dialogToHistoryShopSpinner.setAdapter(ShopArrayAdapter(shops))

            binding.dialogToHistoryShopSpinner.setText(shops[0].name)
            viewModel.shop = shops[0].id
        }

        viewModel.getCurrency().observe(this) { currencies ->
            binding.dialogToHistoryCurrencySpinner.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    currencies
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()
        binding.run {
            dialogToHistoryAddButton.setOnClickListener {
                viewModel.toHistory()
                dismiss()
            }
            dialogToHistoryPriceEditView.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(p0.toString().isNotEmpty())
                        viewModel.price = p0.toString().toInt()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
            dialogToHistoryShopSpinner.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, _, id ->
                    viewModel.shop = id + 1
                }
            dialogToHistoryCurrencySpinner.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, _, id ->
                    viewModel.currency = id + 1
                }
        }
    }

    companion object {
        const val KEY_PAGE = "page"
        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val DIALOG_TO_HISTORY = "dialogToHistory"
        fun newInstance(page: Int, id: UUID, name: String): ToHistoryFragment {
            val dialog = ToHistoryFragment()
            val args = Bundle()
            args.putInt(KEY_PAGE, page)
            args.putString(KEY_ID, id.toString())
            args.putString(KEY_NAME, name)
            dialog.arguments = args
            return dialog
        }
    }

}