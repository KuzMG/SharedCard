package com.example.sharedcard.ui.bottom_navigation.history.target

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
import com.example.sharedcard.database.entity.target.TargetHistory
import com.example.sharedcard.databinding.ListItemHistoryProductBinding
import com.example.sharedcard.databinding.ListItemHistoryTargetBinding
import com.example.sharedcard.ui.bottom_navigation.check.check_list.CheckListFragment


private const val DATE_FORMAT = "MM.dd HH:mm"

class HistoryTargetFragment : CheckListFragment() {
    companion object {
        private val diffUtilCheck = object : DiffUtil.ItemCallback<TargetHistory>() {
            override fun areItemsTheSame(oldItem: TargetHistory, newItem: TargetHistory): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: TargetHistory,
                newItem: TargetHistory
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    private lateinit var localDateFormat: String
    private val targetListAdapter = TargetListAdapter()
    private val viewModel by viewModels<HistoryTargetViewModel>()

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
        viewModel.historyItemLiveData.observe(viewLifecycleOwner) { targets ->
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
        popupMenu.inflate(R.menu.popup_menu_history)
        popupMenu
            .setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.sort_by_date -> {
                            targetListAdapter.sorting()
                        }
                    }
                    return true
                }

            })
        popupMenu.show()
    }


    private inner class TargetHolder(val binding: ListItemHistoryTargetBinding) :
        ViewHolder(binding.root), View.OnClickListener {
        lateinit var target: TargetHistory

        fun onBind(target: TargetHistory) {
            this.target = target
            binding.apply {
                this.target = target
                categoryTextView.text = getString(R.string.category, target.category)
                shopTextView.text = getString(R.string.shop, target.shop)
                if(target.priceFirst>0)
                    firstPriceTextView.text = getString(R.string.first_price,target.priceListFirst)
                if(target.priceLast>0)
                lastPriceTextView.text =  target.priceListLast
                dateTextView.text = DateFormat.format(localDateFormat, target.date)
                infoImageButton.setOnClickListener(this@TargetHolder)
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

    private inner class TargetListAdapter() : ListAdapter<TargetHistory, TargetHolder>(
        diffUtilCheck
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TargetHolder(
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.list_item_history_target,
                parent,
                false
            )
        )

        override fun onBindViewHolder(holder: TargetHolder, position: Int) {
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