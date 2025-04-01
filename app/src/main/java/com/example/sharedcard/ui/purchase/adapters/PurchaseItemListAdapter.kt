package com.example.sharedcard.ui.purchase.adapters

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.basket.Basket
import com.example.sharedcard.database.entity.purchase.Purchase
import com.example.sharedcard.databinding.ListItemPurchaseBinding
import com.example.sharedcard.databinding.ListItemPurchaseInformationBinding
import com.example.sharedcard.ui.purchase.PurchaseViewModel
import com.example.sharedcard.util.createChipAction
import com.example.sharedcard.util.toStringFormat
import com.google.android.material.chip.Chip
import com.squareup.picasso.Picasso
import java.util.UUID


class PurchaseItemListAdapter(
    private val viewModel: PurchaseViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    private val fragmentManager: FragmentManager
) : ListAdapter<Purchase, PurchaseItemListAdapter.PurchaseItemHolder>(
    object : DiffUtil.ItemCallback<Purchase>() {
        override fun areItemsTheSame(oldItem: Purchase, newItem: Purchase): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Purchase,
            newItem: Purchase
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    private lateinit var groupId: UUID

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PurchaseItemHolder(
        ListItemPurchaseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: PurchaseItemHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onViewRecycled(holder: PurchaseItemHolder) {
        holder.unbind()
        super.onViewRecycled(holder)
    }

    inner class PurchaseItemHolder(private val binding: ListItemPurchaseBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        lateinit var purchase: Purchase
        private val informationBinding: ListItemPurchaseInformationBinding by lazy {
            ListItemPurchaseInformationBinding.inflate(
                LayoutInflater.from(binding.root.context),
                binding.root as ViewGroup,
                false
            )
        }
        private lateinit var adapter: CountPurchaseItemAdapter
        private var press = false
        private var countBasket: LiveData<Double>? = null


        private val countBasketObserver = { c: Double? ->
            val m = purchase.product.metric.name
            binding.purchaseConstraintLayout.visibility = when {
                c != null -> {
                    binding.countPurchaseTextView.text = "${c.toStringFormat()} $m"
                    if (purchase.price > 0.0) {
                        val factor = c / purchase.count
                        val newPrice = factor * purchase.price
                        val cur = purchase.currency.symbol
                        binding.pricePurchaseTextView.text = "${newPrice.toStringFormat()} $cur"
                    }
                    View.VISIBLE
                }

                else -> View.GONE
            }
        }
        private var baskets: LiveData<List<Basket>>? = null
        private var basketsObserver = { list: List<Basket> ->
            adapter.submitList(list)
        }
        private val itemTouchHelper = ItemTouchHelper(object : BasketSwipeCallback() {
            override fun action(basket: Basket, position: Int) {
                viewModel.deleteBasket(basket.id, groupId, position)
            }
        })

        init {
            informationBinding.recyclerView.setOnTouchListener { v, event ->
                if (event.action === MotionEvent.ACTION_DOWN) {
                    v.parent.requestDisallowInterceptTouchEvent(true)
                }
                false
            }
            binding.root.setOnClickListener(this@PurchaseItemHolder)
            itemTouchHelper.attachToRecyclerView(informationBinding.recyclerView)

        }
        fun unbind() {
            removeObservers()
            if (press) {
                binding.itemLayout.removeView(informationBinding.root)
                press = false
            }
        }
        fun onBind(purchase: Purchase) {
            this.purchase = purchase
            groupId = purchase.idGroup
            adapter = CountPurchaseItemAdapter(fragmentManager, purchase.idGroup)
            informationBinding.recyclerView.adapter = adapter

            binding.apply {
                nameTextView.text = purchase.product.productEntity.name
                countTextView.text = purchase.quantity
                Picasso.get().load(purchase.product.category.url).into(userImageView)
                if (purchase.price != 0.0)
                    priceTextView.text = purchase.cost
            }
            countBasket = viewModel.getCountBasket(purchase.id)
            countBasket?.observe(viewLifecycleOwner,countBasketObserver)
            viewModel.basketSwipeLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    informationBinding.recyclerView.adapter?.notifyItemChanged(it.first)
                }
            }

        }

        private fun removeObservers() {
            baskets?.removeObserver(basketsObserver)
            countBasket?.removeObserver(countBasketObserver)
            viewModel.basketSwipeLiveData.removeObservers(viewLifecycleOwner)
            countBasket = null
            baskets = null
        }

        private fun generateCountProductChips() {
            informationBinding.chipGroup.removeAllViews()
            val count = purchase.count
            val metric = purchase.product.metric.name
            val set = viewModel.selectSetMetric(metric, count)
            set.forEach { s ->
                val chip = createChipAction(
                    informationBinding.chipGroup,
                    "${s.toStringFormat()} $metric"
                ) {
                    val count = (it as Chip).text.split(" ")[0].toDouble()
                    viewModel.addBasket(purchase.id, count, purchase.idGroup)
                    if (adapter.itemCount > 0) {
                        informationBinding.recyclerView.smoothScrollToPosition(adapter.itemCount)
                    }
                }
                informationBinding.chipGroup.addView(chip)
            }
        }

        override fun onClick(p0: View) {
            if (!press) {
                generateCountProductChips()
                binding.itemLayout.addView(informationBinding.root)
                informationBinding.apply {
                    adapter.metric = purchase.product.metric.name
                    descriptionTextView.visibility = when (purchase.description.isNotBlank()) {
                        true -> {
                            descriptionTextView.text =
                                root.context.getString(
                                    R.string.comment_purchase,
                                    purchase.description
                                )
                            View.VISIBLE
                        }

                        false -> View.GONE
                    }
                    personNameTextView.text = purchase.person.name
                    Picasso.get().load(purchase.personPic).into(imageView)
                }
                baskets = viewModel.getBaskets(purchase.id)
                baskets?.observe(viewLifecycleOwner, basketsObserver)
            } else {
                binding.itemLayout.removeView(informationBinding.root)
            }
            press = !press
        }

    }
}