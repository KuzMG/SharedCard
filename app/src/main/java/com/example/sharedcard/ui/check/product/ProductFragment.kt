package com.example.sharedcard.ui.check.product

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import com.example.sharedcard.database.entity.product.Product
import com.example.sharedcard.ui.check.check_list.CheckListFragment
import com.example.sharedcard.ui.check.check_list.ListAdapterGeneral
import com.example.sharedcard.ui.check.check_list.ViewHolderGeneral

private const val DATE_FORMAT = "MM.dd HH:mm"

class ProductFragment : CheckListFragment() {
    companion object {
        private val diffUtilProduct = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Product,
                newItem: Product
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    private lateinit var localDateFormat: String
    private val productListAdapter = ProductListAdapter()
    private val viewModel by viewModels<ProductViewModel> {
        ProductViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locale = requireContext().resources.configuration.locales[0]
        localDateFormat = DateFormat.getBestDateTimePattern(locale, DATE_FORMAT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            recyclerView.adapter = productListAdapter
        }
        viewModel.productItemLiveData.observe(viewLifecycleOwner) { products ->
            productListAdapter.submitList(products)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.searchBar.searchEditView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setQuery(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }


    private inner class ProductHolder(itemView: View) : ViewHolderGeneral<Product>(itemView) {
        private lateinit var item: Product

        fun onBind(item: Product) {
            this.item = item
            checkBox.apply {
                isChecked = when (item.status) {
                    0 -> false
                    1 -> true
                    else -> throw IndexOutOfBoundsException()
                }
                setOnClickListener {
                    viewModel.setCheckBox(item.id, isChecked)
                }
            }
            nameTextView.text = item.name
            categoryTextView.text = item.category
            quantityPriceTextView.text = item.quantity
            userTextView.text = item.creator
            dateTextView.text = DateFormat.format(localDateFormat, item.date)
        }
    }

    private inner class ProductListAdapter : ListAdapterGeneral<Product>(
        diffUtilProduct
    ) {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolderGeneral<Product> {
            val view = super.onCreateViewHolder(parent, viewType).itemView
            return ProductHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolderGeneral<Product>, position: Int) {
            (holder as ProductHolder).onBind(getItem(position))
        }
    }
}