package com.example.sharedcard.ui.check.product

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.size
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.check.Check
import com.example.sharedcard.databinding.ListItemProductBinding
import com.example.sharedcard.databinding.ListItemProductInformationBinding
import com.example.sharedcard.ui.check.check_list.CheckListFragment
import com.example.sharedcard.ui.check.check_list.CustomSwipeCallback
import com.example.sharedcard.ui.check.delete_item.DeleteItemFragment
import com.example.sharedcard.ui.check.to_history.ToHistoryFragment
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.viewmodel.MultiViewModelFactory
import javax.inject.Inject


private const val DATE_FORMAT = "yyyy-MM.dd HH:mm"

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

    private val viewModel by viewModels<ProductViewModel> {
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
            recyclerView.adapter = productListAdapter
        }
        viewModel.checkItemLiveData.observe(viewLifecycleOwner) { products ->
            productListAdapter.submitList(products)
        }
        viewModel.groupChanged.observe(viewLifecycleOwner){
            viewModel.setQuery("")
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
                val product = (viewHolder as ProductHolder).check
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        if (viewModel.quickDelete) {
                            viewModel.deleteCheck(product.id)
                        } else {
                            DeleteItemFragment.apply {
                                newInstance(0, product.id, product.name)
                                    .show(childFragmentManager, DIALOG_DELETE)
                            }
                            viewHolder.close()
                            productListAdapter.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                        }
                    }

                    ItemTouchHelper.RIGHT -> {
                        ToHistoryFragment.apply {
                            newInstance(0, product.id, product.name)
                                .show(childFragmentManager, DIALOG_TO_HISTORY)
                        }
                        viewHolder.close()
                        productListAdapter.notifyItemChanged(viewHolder.absoluteAdapterPosition)
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


    private inner class ProductHolder(private val binding: ListItemProductBinding) :
        ViewHolder(binding.root), View.OnClickListener {
        lateinit var check: Check
        lateinit var bottomMenuBinding: ListItemProductInformationBinding
        var pressed = false
        fun close() {
            if (binding.itemLayout.size == 3) {
                binding.border.visibility = View.INVISIBLE
                binding.itemLayout.removeViewAt(2)
                pressed = false
            }
        }

        fun onBind(check: Check) {
            this.check = check
            binding.apply {
                this.check = check
                checkBox.setOnClickListener {
                    if (pressed) {
                        if (binding.itemLayout.size == 3)
                            binding.itemLayout.removeViewAt(2)
                        pressed = false
                    }
                    viewModel.setCheckBox(check.id, checkBox.isChecked)
                }
                binding.root.setOnClickListener(this@ProductHolder)
            }
        }

        override fun onClick(v: View?) {
            bottomMenuBinding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.list_item_product_information,
                binding.root as ViewGroup,
                false
            )
            if (!pressed) {
                binding.itemLayout.addView(
                    bottomMenuBinding.root,
                    ConstraintLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                )
                bottomMenuBinding.userTextView.text = check.user
                bottomMenuBinding.pfcTextView.text = check.pfc
                bottomMenuBinding.descriptionTextView.text = check.description
                bottomMenuBinding.categoryTextView.text = check.category
                bottomMenuBinding.dateTextView.text = DateFormat.format(localDateFormat, check.date)
                binding.border.visibility = View.VISIBLE
                pressed = true
            } else {
                close()
            }
        }
    }

    private inner class ProductListAdapter() : ListAdapter<Check, ProductHolder>(
        diffUtilCheck
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductHolder(
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.list_item_product,
                parent,
                false
            )
        )

        override fun onBindViewHolder(holder: ProductHolder, position: Int) {
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
            submitList(list.sortedBy { it.status })
        }
    }
}