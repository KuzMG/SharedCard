package com.example.sharedcard.ui.check.target

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.target.Target
import com.example.sharedcard.databinding.ListItemTargetBinding
import com.example.sharedcard.databinding.ListItemTargetInformationBinding
import com.example.sharedcard.ui.check.check_list.CheckListFragment
import com.example.sharedcard.ui.check.check_list.CustomSwipeCallback
import com.example.sharedcard.ui.check.delete_item.DeleteItemFragment
import com.example.sharedcard.ui.check.to_history.ToHistoryFragment
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.util.isInternetConnection
import com.example.sharedcard.viewmodel.MultiViewModelFactory
import com.squareup.picasso.Picasso
import javax.inject.Inject

private const val DATE_FORMAT = "yyyy-MM.dd HH:mm"

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

    private val viewModel by viewModels<TargetViewModel> {
        appComponent.multiViewModelFactory
    }

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
        viewModel.groupChanged.observe(viewLifecycleOwner){
            viewModel.setQuery("")
        }

        viewModel.sendLiveData.observe(viewLifecycleOwner){
            it?.let {
                Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
            }
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
            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                val target = (viewHolder as TargetHolder).target
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        if (viewModel.quickDelete) {
                            viewModel.deleteTarget(isInternetConnection(requireContext()),target.id)
                        } else {
                            DeleteItemFragment.apply {
                                newInstance(1, target.id, target.name)
                                    .show(childFragmentManager, DIALOG_DELETE)
                            }
                            targetListAdapter.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                            viewHolder.close()
                        }
                    }

                    ItemTouchHelper.RIGHT -> {
                        ToHistoryFragment.apply {
                            newInstance(1, target.id, target.name)
                                .show(childFragmentManager, DIALOG_TO_HISTORY)
                        }
                        targetListAdapter.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                        viewHolder.close()
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
        ViewHolder(binding.root), View.OnClickListener {
        lateinit var target: Target
        private lateinit var bottomMenuBinding: ListItemTargetInformationBinding
        var pressed = false
        fun close() {
            if (binding.itemLayout.size == 3) {
                binding.border.visibility = View.INVISIBLE
                binding.itemLayout.removeViewAt(2)
                pressed = false
            }
        }

        fun onBind(target: Target) {
            this.target = target
            binding.apply {
                nameTextView.text = target.name
                Picasso.get()
                    .load(target.userPic)
                    .into(userImageView)
                if (target.price > 0) {
                    priceTextView.text = target.priceList
                } else{
                  priceTextView.visibility = View.GONE
                }
                binding.root.setOnClickListener(this@TargetHolder)
            }
        }

        override fun onClick(v: View?) {
            bottomMenuBinding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.list_item_target_information,
                binding.root as ViewGroup,
                false
            )
            if (!pressed) {
                binding.itemLayout.addView(
                    bottomMenuBinding.root,
                    LinearLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )
                )
                bottomMenuBinding.userTextView.text = target.user
                bottomMenuBinding.categoryTextView.text = target.category
                bottomMenuBinding.dateTextView.text =
                    DateFormat.format(localDateFormat, target.date)
                binding.border.visibility = View.VISIBLE
                pressed = true
            } else {
                close()
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
                        currentList.sortedBy { it.user }
                    else
                        currentList.sortedByDescending { it.user }
                }

                else -> throw IndexOutOfBoundsException()
            }
            submitList(list)
        }
    }
}