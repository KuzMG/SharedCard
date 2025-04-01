package com.example.sharedcard.ui.settings

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RadioButton
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.sharedcard.databinding.BottomSheetCurrencyBinding
import com.example.sharedcard.databinding.BottomSheetFilterBinding
import com.example.sharedcard.ui.purchase.PurchaseViewModel
import com.example.sharedcard.ui.purchase.filter.enums.GROUPING_BY
import com.example.sharedcard.ui.purchase.filter.enums.SORTING_BY
import com.example.sharedcard.ui.purchase.filter.enums.SORT_MODE
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.util.createChipChose
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import java.util.UUID

class CurrencyBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetCurrencyBinding
    private lateinit var viewModel: SettingsViewModel
    private val currencyMap = hashMapOf<Int,Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireParentFragment() as SettingsFragment,appComponent.multiViewModelFactory)[SettingsViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetCurrencyBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       viewModel.getCurrencies().observe(viewLifecycleOwner){currencies ->
           currencies.forEach { c ->
               val radioButton = RadioButton(requireContext())
               radioButton.text = "${c.name} (${c.symbol})"
               binding.currencyRadioButton.addView(radioButton)
               currencyMap[radioButton.id] = c.id
               if(viewModel.currencyId==c.id){
                   binding.currencyRadioButton.check(radioButton.id)
               }
           }
       }
        viewModel.getCurrency().observe(viewLifecycleOwner){
            System.out.println(it)
        }
        binding.closeButton.setOnClickListener {
            dismiss()
        }
        binding.completeButton.setOnClickListener {
            val id = binding.currencyRadioButton.checkedRadioButtonId
            viewModel.currencyId = currencyMap[id] ?: 1
            dismiss()
        }


    }

    override fun onStart() {
        super.onStart()

    }

    companion object {
        const val TAG = "BottomSheetDialogFragment"
    }
}