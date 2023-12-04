package com.example.sharedcard.ui.check.target

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
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.target.Target
import com.example.sharedcard.ui.check.check_list.CheckListFragment
import com.example.sharedcard.ui.check.check_list.ListAdapterGeneral
import com.example.sharedcard.ui.check.check_list.ViewHolderGeneral

private const val DATE_FORMAT = "MM.dd HH:mm"
class TargetFragment: CheckListFragment() {
    companion object {
        private val diffUtilTarget = object : DiffUtil.ItemCallback<Target>() {
            override fun areItemsTheSame(oldItem: Target, newItem: Target): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Target,
                newItem: Target
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
    private lateinit var localDateFormat: String
    private val targetListAdapter = TargetListAdapter()
    private val viewModel by viewModels<TargetViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locale = requireContext().resources.configuration.locales[0]
        localDateFormat = DateFormat.getBestDateTimePattern(locale, DATE_FORMAT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            recyclerView.adapter = targetListAdapter
        }
        viewModel.targetItemLiveData.observe(viewLifecycleOwner) {targets ->
            targetListAdapter.submitList(targets)
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
        popupMenu.inflate(R.menu.popup_menu)
        popupMenu
            .setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.sort_by_date -> {
                            targetListAdapter.sorting(1)
                        }

                        R.id.sort_by_product -> {
                            targetListAdapter.sorting(2)
                        }

                        R.id.sort_by_category -> {
                            targetListAdapter.sorting(3)
                        }

                        R.id.sort_by_user -> {
                            targetListAdapter.sorting(4)
                        }
                    }
                    return true
                }

            })
        popupMenu.show()
    }

    private inner class TargetHolder(itemView: View) : ViewHolderGeneral<Target>(itemView) {
        private lateinit var item: Target
        fun onBind(item: Target) {
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
            quantityPriceTextView.text = item.priceList
            userTextView.text = item.creator
            dateTextView.text = DateFormat.format(localDateFormat, item.date)
        }
    }

    private inner class TargetListAdapter() : ListAdapterGeneral<Target>(
        diffUtilTarget
    ) {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolderGeneral<Target> {
            val view = super.onCreateViewHolder(parent, viewType).itemView
            return TargetHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolderGeneral<Target>, position: Int) {
            (holder as TargetHolder).onBind(getItem(position))
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
                        currentList.sortedBy { it.creator }
                    else
                        currentList.sortedByDescending { it.creator }
                }

                else -> throw IndexOutOfBoundsException()
            }
            submitList(list.sortedBy { it.status })
        }
    }
}