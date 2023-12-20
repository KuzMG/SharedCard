package com.example.sharedcard.ui.bottom_navigation.check.target

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.target.Target
import com.example.sharedcard.databinding.ListItemTargetBinding
import com.example.sharedcard.ui.bottom_navigation.check.check_list.CheckListFragment
import com.example.sharedcard.ui.bottom_navigation.check.check_list.CustomSwipeCallback
import com.example.sharedcard.ui.bottom_navigation.check.delete_item.DeleteItemFragment
import com.example.sharedcard.ui.bottom_navigation.check.to_history.ToHistoryFragment

private const val DATE_FORMAT = "MM.dd HH:mm"

class TargetFragment : CheckListFragment() {
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
        viewModel.targetItemLiveData.observe(viewLifecycleOwner) { targets ->
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
        val itemTouchHelper = ItemTouchHelper(object : CustomSwipeCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val target = (viewHolder as TargetHolder).target
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        if(viewModel.quickDelete){
                            viewModel.deleteTarget(target.id)
                        } else {
                            DeleteItemFragment.apply {
                                newInstance(1, target.id, target.name)
                                    .show(childFragmentManager, DIALOG_DELETE)
                            }
                            targetListAdapter.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                        }
                    }

                    ItemTouchHelper.RIGHT -> {
                        ToHistoryFragment.apply {
                            newInstance(1, target.id, target.name)
                                .show(childFragmentManager, DIALOG_TO_HISTORY)
                        }
                        targetListAdapter.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                    }
                }
            }

        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun showPopupMenu(v: View) {
        val popupMenu = PopupMenu(requireContext(), v)
        popupMenu.inflate(R.menu.popup_menu_check)
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

    private inner class TargetHolder(private val binding: ListItemTargetBinding) :
        ViewHolder(binding.root),View.OnClickListener {
        lateinit var target: Target
        fun onBind(target: Target) {
            this.target = target
            binding.apply {
                this.target = target
                categoryTextView.text = getString(R.string.category, target.category)
                if(target.price>0)
                    priceTextView.text = target.priceList
                dateTextView.text = DateFormat.format(localDateFormat, target.date)
                infoImageButton.setOnClickListener(this@TargetHolder)
            }
        }

        override fun onClick(v: View?) {
            if(binding.info.visibility == View.GONE) {
                binding.info.visibility = View.VISIBLE
            } else {
                binding.info.visibility = View.GONE
            }
        }
    }

    private inner class TargetListAdapter() : ListAdapter<Target, TargetHolder>(
        diffUtilTarget
    ) {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ) = TargetHolder(
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.list_item_target,
                parent,
                false
            )
        )

        override fun onBindViewHolder(holder: TargetHolder, position: Int) {
            holder.onBind(getItem(position))
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
            submitList(list)
        }
    }
}