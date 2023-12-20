package com.example.sharedcard.ui.bottom_navigation.history.product

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.check.CheckHistory
import com.example.sharedcard.databinding.ListItemCheckBinding
import com.example.sharedcard.databinding.ListItemHistoryProductBinding
import com.example.sharedcard.ui.bottom_navigation.check.check_list.CheckListFragment


private const val DATE_FORMAT = "MM.dd HH:mm"

class HistoryProductFragment : CheckListFragment() {
    companion object {
        private val diffUtilCheck = object : DiffUtil.ItemCallback<CheckHistory>() {
            override fun areItemsTheSame(oldItem: CheckHistory, newItem: CheckHistory): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CheckHistory,
                newItem: CheckHistory
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    private lateinit var localDateFormat: String
    private val productListAdapter = ProductListAdapter()
    private val viewModel by viewModels<HistoryProductViewModel>()

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
        viewModel.historyItemLiveData.observe(viewLifecycleOwner) { products ->
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

        binding.searchBar.buttonSort.setOnClickListener { v ->
            showPopupMenu(v)
        }
    }


    private fun showPopupMenu(v: View) {
        val popupMenu = PopupMenu(requireContext(), v)
        popupMenu.inflate(R.menu.popup_menu_history)
        popupMenu
            .setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.sort_by_date -> {
                            productListAdapter.sorting()
                        }
                    }
                    return true
                }

            })
        popupMenu.show()
    }


    private inner class ProductHolder(val binding: ListItemHistoryProductBinding) :
        ViewHolder(binding.root), View.OnClickListener {
        lateinit var check: CheckHistory

        fun onBind(check: CheckHistory) {
            this.check = check
            binding.apply {
                this.check = check
                categoryTextView.text = getString(R.string.category, check.category)
                shopTextView.text = getString(R.string.shop, check.shop)
                if(check.price>0)
                    priceTextView.text = check.priceList
                dateTextView.text = DateFormat.format(localDateFormat, check.date)
                infoImageButton.setOnClickListener(this@ProductHolder)
            }
        }

        override fun onClick(v: View?) {
            if (binding.info.visibility == View.GONE) {
                binding.info.visibility = View.VISIBLE
            } else {
                binding.info.visibility = View.GONE
            }
        }
    }

    private inner class ProductListAdapter() : ListAdapter<CheckHistory, ProductHolder>(
        diffUtilCheck
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductHolder(
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.list_item_history_product,
                parent,
                false
            )
        )

        override fun onBindViewHolder(holder: ProductHolder, position: Int) {
            holder.onBind(getItem(position))
        }

        private var flag1 = false
        fun sorting() {
            flag1 = !flag1
            val list = when (flag1) {
                true -> currentList.sortedBy { it.date }
                false -> currentList.sortedByDescending { it.date }
            }
            submitList(list)
        }
    }
}