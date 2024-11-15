package com.example.sharedcard.ui.history.target

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.target.TargetHistory
import com.example.sharedcard.databinding.ListItemHistoryTargetBinding
import com.example.sharedcard.databinding.ListItemHistoryTargetInformationBinding
import com.example.sharedcard.ui.check.check_list.CheckListFragment
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.viewmodel.MultiViewModelFactory
import com.squareup.picasso.Picasso
import javax.inject.Inject


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


    private val viewModel by viewModels<HistoryTargetViewModel>{
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
            .setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.sort_by_date -> {
                        targetListAdapter.sorting()
                    }
                }
                true
            }
        popupMenu.show()
    }


    private inner class TargetHolder(val binding: ListItemHistoryTargetBinding) :
        ViewHolder(binding.root), View.OnClickListener {
        lateinit var target: TargetHistory
        lateinit var bottomMenuBinding: ListItemHistoryTargetInformationBinding
        var pressed = false
        fun onBind(target: TargetHistory) {
            this.target = target
            binding.apply {
                Picasso.get()
                    .load(target.userFirstPic)
                    .into(firstUserImageView)
                Picasso.get()
                    .load(target.userLastPic)
                    .into(lastUserImageView)
                nameTextView.text = target.name
                root.setOnClickListener(this@TargetHolder)
            }
        }

        override fun onClick(v: View?) {
            bottomMenuBinding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.list_item_history_target_information,
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
                if(target.priceFirst>0) {
                    bottomMenuBinding.priceFirstTextView.text = target.priceListFirst
                } else{
                    bottomMenuBinding.priceFirstTextView.visibility= View.GONE
                    bottomMenuBinding.priceFirstLabel.visibility = View.GONE
                }
                if(target.priceLast>0) {
                    bottomMenuBinding.priceLastTextView.text = target.priceListLast
                } else{
                    bottomMenuBinding.priceLastTextView.visibility= View.GONE
                    bottomMenuBinding.priceLastLabel.visibility = View.GONE
                }
                bottomMenuBinding.categoryTextView.text = target.category
                bottomMenuBinding.shopTextView.text = target.shop
                bottomMenuBinding.firstUserTextView.text = target.userFirst
                bottomMenuBinding.lastUserTextView.text = target.userLast
                bottomMenuBinding.dateTextView.text = DateFormat.format(localDateFormat, target.date)
                binding.border.visibility = View.VISIBLE
                pressed = true
            } else {
                binding.itemLayout.removeViewAt(2)
                binding.border.visibility = View.INVISIBLE
                pressed = false
            }
        }
    }

    private inner class TargetListAdapter : ListAdapter<TargetHistory, TargetHolder>(
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