package com.example.sharedcard.ui.check.product

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.product.Product
import com.example.sharedcard.ui.check.check_list.CheckListFragment
import com.example.sharedcard.ui.check.check_list.ListAdapterGeneral
import com.example.sharedcard.ui.check.check_list.ViewHolderGeneral
import com.project.shared_card.database.dao.product.ProductEntity

private const val DATE_FORMAT = "MMM.dd HH:mm"

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
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setQuery(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }
        binding.searchBar.searchEditView.addTextChangedListener(textWatcher)
    }


    private inner class ProductHolder(itemView: View) : ViewHolderGeneral<Product>(itemView) {
        private lateinit var item: Product
        override fun onBind(item: Product) {
            super.onBind(item)
            this.item = item
            checkBox.isChecked = when (item.status) {
                0 -> false
                1 -> true
                else -> throw IndexOutOfBoundsException()
            }
            nameTextView.text = item.name
            categoryTextView.text = item.category
            quantityPriceTextView.text = item.quantity
            userTextView.text = item.creator
            dateTextView.text = DateFormat.format(localDateFormat, item.date)
        }
    }

    private inner class ProductListAdapter() : ListAdapterGeneral<Product, ProductHolder>(
        diffUtilProduct
    )
}