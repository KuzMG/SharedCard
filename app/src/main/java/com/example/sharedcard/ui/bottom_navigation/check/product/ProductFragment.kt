package com.example.sharedcard.ui.bottom_navigation.check.product

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.check.Check
import com.example.sharedcard.ui.bottom_navigation.check.check_list.CheckListFragment
import com.example.sharedcard.ui.bottom_navigation.check.check_list.CustomSwipeCallback
import com.example.sharedcard.ui.bottom_navigation.check.check_list.ListAdapterGeneral
import com.example.sharedcard.ui.bottom_navigation.check.check_list.ViewHolderGeneral
import com.example.sharedcard.ui.bottom_navigation.check.dialog.DeleteItemFragment
import com.example.sharedcard.ui.bottom_navigation.check.dialog.ToHistoryFragment


private const val DATE_FORMAT = "MM.dd HH:mm"

class ProductFragment : CheckListFragment() {
    companion object {
        private val diffUtilCheck = object : DiffUtil.ItemCallback<Check>() {
            override fun areItemsTheSame(oldItem: Check, newItem: Check): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Check,
                newItem: Check
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    private lateinit var localDateFormat: String
    private val productListAdapter = ProductListAdapter()
    private val viewModel by viewModels<ProductViewModel>()

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
        viewModel.checkItemLiveData.observe(viewLifecycleOwner) { products ->
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
        val itemTouchHelper = ItemTouchHelper(object : CustomSwipeCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val product = (viewHolder as ProductHolder).item
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        DeleteItemFragment.apply {
                            newInstance(0, product.id, product.name)
                                .show(childFragmentManager, DIALOG_DELETE)
                        }

                    }

                    ItemTouchHelper.RIGHT -> {
                        ToHistoryFragment.apply {
                            newInstance(0, product.id, product.name)
                                .show(childFragmentManager, DIALOG_TO_HISTORY)
                        }
                    }
                }
                productListAdapter.notifyItemChanged(viewHolder.absoluteAdapterPosition)
            }

        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }


    private fun showPopupMenu(v: View) {
        val popupMenu = PopupMenu(requireContext(), v)
        popupMenu.inflate(R.menu.popup_menu)
        popupMenu
            .setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.sort_by_date -> {
                            productListAdapter.sorting(1)
                        }

                        R.id.sort_by_product -> {
                            productListAdapter.sorting(2)
                        }

                        R.id.sort_by_category -> {
                            productListAdapter.sorting(3)
                        }

                        R.id.sort_by_user -> {
                            productListAdapter.sorting(4)
                        }
                    }
                    return true
                }

            })
        popupMenu.show()
    }


    private inner class ProductHolder(itemView: View) : ViewHolderGeneral(itemView) {
        lateinit var item: Check

        fun onBind(item: Check) {
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
            userTextView.text = item.user
            dateTextView.text = DateFormat.format(localDateFormat, item.date)
        }
    }

    private inner class ProductListAdapter : ListAdapterGeneral<Check>(
        diffUtilCheck
    ) {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolderGeneral {
            val view = super.onCreateViewHolder(parent, viewType).itemView
            return ProductHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolderGeneral, position: Int) {
            (holder as ProductHolder).onBind(getItem(position))
        }

        private var flag1 = false
        private var flag2 = false
        private var flag3 = false
        private var flag4 = false
        fun sorting(fieldSorting: Int) {
            val list = when (fieldSorting) {
                1 -> {
                    flag1 = !flag1
                    if (flag1)
                        currentList.sortedBy { it.date }
                    else
                        currentList.sortedByDescending { it.date }
                }

                2 -> {
                    flag2 = !flag2
                    if (flag2)
                        currentList.sortedBy { it.name }
                    else
                        currentList.sortedByDescending { it.name }
                }

                3 -> {
                    flag3 = !flag3
                    if (flag3)
                        currentList.sortedBy { it.category }
                    else
                        currentList.sortedByDescending { it.category }
                }

                4 -> {
                    flag4 = !flag4
                    if (flag4)
                        currentList.sortedBy { it.user }
                    else
                        currentList.sortedByDescending { it.user }
                }

                else -> throw IndexOutOfBoundsException()
            }
            submitList(list.sortedBy { it.status })
        }
    }
}