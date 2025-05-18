package com.example.sharedcard.ui.purchase.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        private lateinit var adapter: PurchaseItemListAdapter
        private val itemTouchHelper = ItemTouchHelper(object : PurchaseSwipeCallback() {
            override fun swipeLeft(purchase: Purchase, position: Int) {
                viewModel.deletePurchase(purchase.group.id, purchase.id, position)
            }

            override fun swipeRight(purchase: Purchase, position: Int) {
                viewModel.purchaseToHistory(purchase.group.id, purchase.id, position)
            }
        })
        private var purchase:  LiveData<List<Purchase>>? = null
        private var purchaseObserve = { list: List<Purchase> ->
            adapter.submitList(list,group is GroupEntity)
        }

        private var purchaseSwipe: LiveData<Triple<UUID, Int, Throwable>?>? = null
        private var purchaseSwipeObserver = { triple: Triple<UUID, Int, Throwable>? ->
            triple?.let {
                val purchaseId = it.first
                val position = it.second
                val e = it.third
                if (position >= adapter.itemCount) return@let
                val purchase = adapter.currentList[position]
                if (purchase.id == purchaseId) {
                    adapter.notifyItemChanged(position)
                    Toast.makeText(binding.root.context,e.message ?: binding.root.context.getString(R.string.not_purchases_items), Toast.LENGTH_SHORT).show()
                }
            }
            Unit
        }


        fun unbind() {
            purchase?.removeObserver(purchaseObserve)
            purchaseSwipe?.removeObserver(purchaseSwipeObserver)
            purchaseSwipe = null
            purchase = null
        }

        lateinit var group: GroupItem
        fun onBind(item: GroupItem) {
            adapter = PurchaseItemListAdapter(viewModel, viewLifecycleOwner, fragmentManager)
            binding.recyclerView.adapter = adapter
            group = item
            binding.apply {
                if (item._name.isNotBlank()) {
                    Picasso.get().load(group._pic).into(picImageView)
                } else {
                    picImageView.setImageResource(R.drawable.ic_home)
                }
                itemFilterTextView.text = item._name
                purchase =  viewModel.getPurchases(group._id)
                purchase?.observe(viewLifecycleOwner, purchaseObserve)
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