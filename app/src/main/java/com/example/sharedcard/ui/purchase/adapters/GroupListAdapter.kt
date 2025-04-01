package com.example.sharedcard.ui.purchase.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.database.entity.purchase.Purchase
import com.example.sharedcard.databinding.ListPurchaseBinding
import com.example.sharedcard.ui.purchase.PurchaseViewModel
import com.example.sharedcard.util.dpToPx
import com.squareup.picasso.Picasso
import java.util.UUID


class GroupListAdapter(
    private val viewModel: PurchaseViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    private val fragmentManager: FragmentManager
) :
    ListAdapter<GroupListAdapter.GroupItem, GroupListAdapter.PurchaseHolder>(
        object : DiffUtil.ItemCallback<GroupItem>() {
            override fun areItemsTheSame(oldItem: GroupItem, newItem: GroupItem): Boolean {
                return oldItem._id == newItem._id

            }

            override fun areContentsTheSame(
                oldItem: GroupItem,
                newItem: GroupItem
            ): Boolean {
                return oldItem._name == newItem._name
            }
        }
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseHolder {
        return PurchaseHolder(
            ListPurchaseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PurchaseHolder, position: Int) {
        holder.onBind(getItem(position))
    }
    override fun onViewRecycled(holder: PurchaseHolder) {
        holder.unbind()
        super.onViewRecycled(holder)
    }

    inner class PurchaseHolder(private val binding: ListPurchaseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val adapter =
            PurchaseItemListAdapter(viewModel, viewLifecycleOwner, fragmentManager)
        private val itemTouchHelper = ItemTouchHelper(object : PurchaseSwipeCallback() {
            override fun swipeLeft(purchase: Purchase, position: Int) {
                viewModel.deletePurchase(purchase.idGroup, purchase.id, position)
            }

            override fun swipeRight(purchase: Purchase, position: Int) {
                viewModel.purchaseToHistory(purchase.idGroup, purchase.id, position)
            }

        })

        private var filter: LiveData<Boolean>? = null
        private val filterObserver = { b: Boolean ->
            if (b) {
                viewModel.getPurchases(group._id).observe(viewLifecycleOwner) { list ->
                    binding.root.visibility = when (list.isEmpty()) {
                        true -> View.GONE
                        false -> View.VISIBLE
                    }
                    adapter.submitList(list)
                }
            }
        }
        private var purchaseSwipe: LiveData<Pair<Int, Throwable>?>? = null
        private var purchaseSwipeObserver = { pair: Pair<Int, Throwable>? ->
            pair?.let {
                binding.recyclerView.adapter?.notifyItemChanged(it.first)
            }
            Unit
        }

        init {
            binding.recyclerView.adapter = adapter
        }

        fun unbind() {
            filter?.removeObserver(filterObserver)
            purchaseSwipe?.removeObserver(purchaseSwipeObserver)
            filter = null
            purchaseSwipe = null
        }

        lateinit var group: GroupItem
        fun onBind(item: GroupItem) {
            group = item
            binding.apply {
                if (item._name.isNotBlank()) {
                    Picasso.get().load(item._pic).into(picImageView)
                } else {
                    root.strokeColor = root.context.getColor(R.color.colorPrimary)
                    root.strokeWidth = root.context.dpToPx(1).toInt()
                }
                itemFilterTextView.text = item._name

                val filter = viewModel.filterLiveData
                filter.observe(viewLifecycleOwner, filterObserver)
                purchaseSwipe = viewModel.purchaseSwipeLiveData
                purchaseSwipe?.observe(viewLifecycleOwner, purchaseSwipeObserver)
                itemTouchHelper.attachToRecyclerView(binding.recyclerView)
            }
        }
    }

    interface GroupItem {
        val _id: UUID
        val _name: String
        val _pic: String
    }
}