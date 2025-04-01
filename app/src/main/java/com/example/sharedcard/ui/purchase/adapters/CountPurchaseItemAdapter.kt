package com.example.sharedcard.ui.purchase.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.basket.Basket
import com.example.sharedcard.database.entity.purchase.Purchase
import com.example.sharedcard.databinding.ListItemCountPurchaseBinding
import com.example.sharedcard.ui.purchase.PurchaseViewModel
import com.example.sharedcard.ui.purchase.to_history.ToHistoryBottomSheet
import com.example.sharedcard.util.toStringFormat
import java.util.UUID


class CountPurchaseItemAdapter(
    private val fragmentManager: FragmentManager,
    private val groupId: UUID
) : ListAdapter<Basket, CountPurchaseItemAdapter.CountPurchaseItemHolder>(
    object : DiffUtil.ItemCallback<Basket>() {
        override fun areItemsTheSame(oldItem: Basket, newItem: Basket): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Basket,
            newItem: Basket
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    var metric = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CountPurchaseItemHolder(
        ListItemCountPurchaseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: CountPurchaseItemHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    inner class CountPurchaseItemHolder(private val binding: ListItemCountPurchaseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        lateinit var basket: Basket
        fun onBind(basket: Basket) {
            Log.d("TAG", "onBind basket count = ${basket.count}")
            this.basket = basket
            binding.apply {
                binding.historyView.visibility = when(basket.history){
                    null -> {
                        purchaseButton.visibility = View.VISIBLE
                        View.GONE
                    }
                    else ->{
                        purchaseButton.visibility = View.GONE
                        View.VISIBLE
                    }
                }
                quantityTextView.text = "${basket.count.toStringFormat()} $metric"
                purchaseButton.setOnClickListener {
                    ToHistoryBottomSheet.newInstance(basket.id, groupId).show(fragmentManager,ToHistoryBottomSheet.DIALOG_TO_HISTORY)
                }
            }
        }
    }
}