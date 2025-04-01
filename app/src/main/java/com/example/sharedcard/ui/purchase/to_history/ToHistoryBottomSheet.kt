package com.example.sharedcard.ui.purchase.to_history

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentToHistoryBinding
import com.example.sharedcard.ui.purchase.PurchaseViewModel
import com.example.sharedcard.ui.purchase.adapters.ShopArrayAdapter
import com.example.sharedcard.ui.purchase.add_purchase.AddPurchaseFragment
import com.example.sharedcard.ui.purchase.add_purchase.NumberTextBottomSheet
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.util.createChipChose
import com.example.sharedcard.util.toStringFormat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import java.util.UUID


class ToHistoryBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentToHistoryBinding
    private var shop = 1
    private var price = 0.0
    private lateinit var viewModel: PurchaseViewModel

    override fun onAttach(context: Context) {
        appComponent.inject(this)
        super.onAttach(context)
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.setOnShowListener { dialogInterface ->
//            val bottomSheetDialog = dialogInterface as BottomSheetDialog
//            setupFullHeight(bottomSheetDialog)
//        }
//        return dialog
//    }
//    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
//        val bottomSheet = binding.root
//        val behavior = bottomSheetDialog.behavior
//        val layoutParams = bottomSheet.layoutParams
//        val windowHeight = getWindowHeight()
//        if (layoutParams != null) {
//            layoutParams.height = windowHeight
//        }
//        bottomSheet.layoutParams = layoutParams
//        behavior.state = BottomSheetBehavior.STATE_EXPANDED
//        behavior.maxHeight = windowHeight
//        behavior.peekHeight = windowHeight
//    }
//
//    private fun getWindowHeight(): Int {
//        val displayMetrics = DisplayMetrics()
//        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
//        return displayMetrics.heightPixels
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[PurchaseViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentToHistoryBinding.inflate(
                inflater,
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getShops().observe(viewLifecycleOwner) { shops ->
            binding.shopSpinner.setAdapter(ShopArrayAdapter(shops))
            binding.shopSpinner.setText(shops[0].name)
        }
        val basketId = UUID.fromString(arguments?.getString(KEY_ID_BASKET, "") ?: "")
        viewModel.getCurrency(basketId).observe(viewLifecycleOwner){
            binding.currencyTextView.text = it.symbol
        }
        binding.priceChipGroup.check(binding.notPriceChip.id)
        binding.priceEditText.visibility = View.GONE
        binding.currencyTextView.visibility = View.GONE
        binding.notPriceChip.setOnClickListener {
            price = 0.0
            binding.priceEditText.visibility = View.GONE
            binding.currencyTextView.visibility = View.GONE
        }
        binding.editPriceChip.text = getString(R.string.chip_custom_default)
        binding.editPriceChip.setOnClickListener {
            binding.priceEditText.visibility = View.VISIBLE
            binding.currencyTextView.visibility = View.VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
        binding.run {
            cancelButton.setOnClickListener {
                dismiss()
            }
            okButton.setOnClickListener {
                val basketId = UUID.fromString(arguments?.getString(KEY_ID_BASKET, "") ?: "")
                val groupId = UUID.fromString(arguments?.getString(KEY_ID_GROUP, "") ?: "")
                val count = binding.priceEditText.text.toString().ifBlank { "0" }.toDouble()
                viewModel.toHistory(groupId, basketId, count, shop)
               dismiss()
            }

            shopSpinner.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, _, id ->
                    shop = id.toInt()
                }
        }
    }

    companion object {
        const val KEY_ID_BASKET = "idBasket"
        const val KEY_ID_GROUP = "idGroup"
        const val DIALOG_TO_HISTORY = "dialogToHistory"
        fun newInstance(basketId: UUID, groupId: UUID) = ToHistoryBottomSheet().apply {
            arguments = Bundle().apply {
                putString(KEY_ID_BASKET, basketId.toString())
                putString(KEY_ID_GROUP, groupId.toString())
            }
        }
    }

}