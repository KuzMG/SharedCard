package com.example.sharedcard.ui.purchase.add_purchase


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.product.Product
import com.example.sharedcard.databinding.FragmentAddPurchaseBinding
import com.example.sharedcard.ui.purchase.adapters.ProductSelectAdapter
import com.example.sharedcard.ui.purchase.bottom_sheet.NumberTextBottomSheet
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.util.createChipChose
import com.example.sharedcard.util.toStringFormat
import com.google.android.material.chip.Chip
import java.io.Serializable
import javax.inject.Inject


class AddPurchaseFragment : Fragment() {
    interface Connect : Serializable {
        fun ok(count: Double)
    }

    companion object {
        private const val ARG_QUERY = "query"
        fun newInstance(query: String) = AddPurchaseFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_QUERY, query)
            }
        }

    }

    private lateinit var binding: FragmentAddPurchaseBinding

    @Inject
    lateinit var factory: AddPurchaseViewModel.FactoryHelper

    private val viewModel: AddPurchaseViewModel by viewModels({ this }) {
        val query = arguments?.getString(ARG_QUERY) ?: ""
        factory.create(query)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentAddPurchaseBinding.inflate(
                inflater,
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.appBar.toolbar.setNavigationIcon(R.drawable.ic_arrow_left)
        viewModel.productsLiveData.observe(viewLifecycleOwner) {
            binding.recyclerView.adapter = ProductSelectAdapter(it, viewModel)
        }
        viewModel.getCurrency().observe(viewLifecycleOwner) {
            viewModel.currency = it.symbol
        }
        viewModel.newProductLiveData.observe(viewLifecycleOwner) {
            generateCountProductChips(it)
        }
        viewModel.resultLiveData.observe(viewLifecycleOwner) {
            if (it.isFailure) {
                Toast.makeText(
                    requireContext(),
                    it.exceptionOrNull().toString(),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (it.getOrDefault(false)) {
                    parentFragmentManager.popBackStack()
                }
            }


        }
        generatePriceProductChips()
        generateGroupChips()
    }

    private fun generateGroupChips() {
        viewModel.getGroups().observe(viewLifecycleOwner) { groups ->
            groups.forEachIndexed { index, g ->
                val chip = createChipChose(binding.countChipGroup, g.name.ifBlank { "Локальная" }) {
                    viewModel.group = g.id
                }
                binding.groupChipGroup.addView(chip)
                if (g.id == viewModel.group) binding.groupChipGroup.check(chip.id)
            }
        }
    }

    private fun generatePriceProductChips() {
        val chipNotPrice = createChipChose(binding.countChipGroup, getString(R.string.chip_custom_not)) {
            viewModel.price = 0.0
            viewModel.selectablePriceChipIndex = false
        }
        val chipPrice =
            createChipChose(binding.countChipGroup, getString(R.string.chip_custom_default)) {
                val chip = (it as Chip)
                val splitText = chip.text.split(" ")
                val count = when (splitText.size) {
                    1 -> 0.0
                    else -> splitText[1].toDouble()
                }
                NumberTextBottomSheet
                    .newInstance(count, true,object : Connect {
                        override fun ok(count: Double) {
                            viewModel.price = count
                            chip.text = getString(
                                R.string.chip_custom,
                                "${count.toStringFormat()} ${viewModel.currency}"
                            )
                        }
                    })
                    .show(childFragmentManager, NumberTextBottomSheet.TAG)
                viewModel.selectablePriceChipIndex = true
            }
        binding.priceChipGroup.run {
            addView(chipNotPrice)
            addView(chipPrice)
        }
        binding.priceChipGroup.check(
            when (viewModel.selectablePriceChipIndex) {
                false -> chipNotPrice.id
                true -> {
                    val p = viewModel.price.toStringFormat()
                    val c = viewModel.currency
                    chipPrice.text = getString(R.string.chip_custom, "$p $c")
                    chipPrice.id
                }
            }
        )


    }


    private fun clickCustomChipCount(v: View) {
        val chip = (v as Chip)
        val splitText = chip.text.split(" ")
        val count = if (splitText.size == 1) {
            0.0
        } else {
            splitText[1].toDouble()
        }
        val connect = object : Connect {
            override fun ok(count: Double) {
                viewModel.count = count
                val m = viewModel.product?.metric?.name
                chip.text = getString(R.string.chip_custom, "${count.toStringFormat()} $m")
            }
        }
        viewModel.selectableCountChipIndex = viewModel.selectableListMetric.size
        val isDouble = when(viewModel.product?.metric?.name){
            "шт" -> false
            else -> true
        }
        NumberTextBottomSheet
            .newInstance(count, isDouble,connect)
            .show(childFragmentManager, NumberTextBottomSheet.TAG)
    }

    private fun clickChipCount(v: View) {
        val chip = (v as Chip)
        val count = chip.text.split(" ")[0].toDouble()
        viewModel.count = count
        viewModel.selectableCountChipIndex =
            viewModel.selectableListMetric.indexOf(count.toStringFormat())
    }

    private fun generateCountProductChips(product: Product) {
        binding.countChipGroup.removeAllViews()
        viewModel.selectableListMetric = when (product.productEntity.quantityMultiplier) {
            1 -> viewModel.listMetricOne
            2 -> viewModel.listMetricTwo
            3 -> viewModel.listMetricThree
            else -> throw IndexOutOfBoundsException()
        }
        viewModel.selectableListMetric.forEachIndexed { index, s ->
            val chip = createChipChose(
                binding.countChipGroup,
                "$s ${product.metric.name}",
                ::clickChipCount
            )
            binding.countChipGroup.addView(chip)
            if (index == viewModel.selectableCountChipIndex) {
                binding.countChipGroup.check(chip.id)
                viewModel.count = s.toDouble()
            }
        }
        val chip = createChipChose(
            binding.countChipGroup,
            getString(R.string.chip_custom_default),
            ::clickCustomChipCount
        )
        binding.countChipGroup.addView(chip)
        if (viewModel.selectableCountChipIndex == viewModel.selectableListMetric.size) {
            binding.countChipGroup.check(chip.id)
            val c = viewModel.count.toStringFormat()
            val m = viewModel.product?.metric?.name ?: ""
            chip.text = getString(R.string.chip_custom, "$c $m")
        }

    }


    override fun onStart() {
        super.onStart()

        binding.appBar.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setQuery(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        binding.appBar.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.addButton.setOnClickListener {
            viewModel.add()
        }
        binding.descriptionEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.description = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

}