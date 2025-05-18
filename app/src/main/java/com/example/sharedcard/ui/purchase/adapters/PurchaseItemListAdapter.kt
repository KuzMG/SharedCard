package com.example.sharedcard.ui.purchase.adapters

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.MotionEvent
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
import com.example.sharedcard.database.entity.basket.Basket
import com.example.sharedcard.database.entity.purchase.Purchase
import com.example.sharedcard.databinding.ListItemPurchaseBinding
import com.example.sharedcard.databinding.ListItemPurchaseInformationBinding
import com.example.sharedcard.ui.purchase.PurchaseViewModel
import com.example.sharedcard.ui.purchase.add_purchase.AddPurchaseFragment
import com.example.sharedcard.ui.purchase.bottom_sheet.NumberTextBottomSheet
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
    private var isGroup: Boolean = true

    fun submitList(list: List<Purchase>, _isGroup: Boolean) {
        isGroup = _isGroup
        submitList(list)
    }

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

    @SuppressLint("ClickableViewAccessibility")
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
        private var countHistory: LiveData<Double>? = null
        private val countHistoryObserver = { c: Double? ->
            val m = purchase.product.metric.name
            val cur = purchase.currency.symbol

            when {
                c != null && c > 0 -> {
                    val count = purchase.count - c
                    val factor = count / purchase.count
                    val newPrice = factor * purchase.price
                    when {
                        count > 0 -> {
                            binding.countTextView.setTypeface(binding.countTextView.typeface, Typeface.NORMAL)
                            binding.countTextView.text = binding.root.context.getString(
                                R.string.pair_string,
                                count.toStringFormat(),
                                m
                            )
                            binding.priceTextView.text = if (purchase.price > 0) {
                                binding.root.context.getString(
                                    R.string.pair_string,
                                    newPrice.toStringFormat(),
                                    cur
                                )
                            } else {
                                ""
                            }

                        }

                        else -> {
                            binding.countTextView.setTypeface(binding.countTextView.typeface, Typeface.BOLD)
                            binding.countTextView.text =
                                binding.root.context.getString(R.string.bought)
                            binding.priceTextView.text = ""
                        }
                    }
                }

                else -> {
                    binding.countTextView.setTypeface(binding.countTextView.typeface, Typeface.NORMAL)
                    binding.countTextView.text = binding.root.context.getString(
                        R.string.pair_string,
                        purchase.count.toStringFormat(),
                        m
                    )
                    binding.priceTextView.text = if (purchase.price > 0.0) {
                        binding.root.context.getString(
                            R.string.pair_string,
                            purchase.price.toStringFormat(),
                            cur
                        )
                    } else {
                        ""
                    }
                }
            }
        }


        private var countBasket: LiveData<Double>? = null
        private val countBasketObserver = { c: Double? ->
            val m = purchase.product.metric.name
            binding.purchaseConstraintLayout.visibility = when {
                c != null && c > 0 -> {
                    binding.countPurchaseTextView.text =
                        binding.root.context.getString(R.string.pair_string, c.toStringFormat(), m)
                    if (purchase.price > 0.0) {
                        val factor = c / purchase.count
                        val newPrice = factor * purchase.price
                        val cur = purchase.currency.symbol
                        binding.pricePurchaseTextView.text = binding.root.context.getString(
                            R.string.pair_string,
                            newPrice.toStringFormat(),
                            cur
                        )
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
                if (event.action == MotionEvent.ACTION_DOWN) {
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
            groupId = purchase.group.id
            adapter = CountPurchaseItemAdapter(fragmentManager, groupId)
            informationBinding.recyclerView.adapter = adapter

            binding.apply {
                nameTextView.text = purchase.product.productEntity.name
                Picasso.get().load(purchase.product.category.url).into(categoryImageView)
            }
            countBasket = viewModel.getCountBasket(purchase.id)
            countHistory = viewModel.getCountHistory(purchase.id)
            countHistory?.observe(viewLifecycleOwner, countHistoryObserver)
            countBasket?.observe(viewLifecycleOwner, countBasketObserver)
            viewModel.basketSwipeLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    informationBinding.recyclerView.adapter?.notifyItemChanged(it.first)
                    Toast.makeText(binding.root.context, it.second.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }

        private fun removeObservers() {
            baskets?.removeObserver(basketsObserver)
            countBasket?.removeObserver(countBasketObserver)
            countHistory?.removeObserver(countHistoryObserver)
            viewModel.basketSwipeLiveData.removeObservers(viewLifecycleOwner)
            countBasket = null
            baskets = null
            countHistory = null
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
                    val countChip = (it as Chip).text.split(" ")[0].toDouble()
                    viewModel.addBasket(purchase.id, countChip, groupId)
                }
                informationBinding.chipGroup.addView(chip)
            }
            val customChip = createChipAction(
                informationBinding.chipGroup,
                binding.root.context.getString(R.string.chip_custom_default)
            ) {
                val connect = object : AddPurchaseFragment.Connect {
                    override fun ok(count: Double) {
                        viewModel.addBasket(purchase.id, count, groupId)
                        if (adapter.itemCount > 0) {
                            informationBinding.recyclerView.smoothScrollToPosition(adapter.itemCount)
                        }
                    }
                }
                val isDouble = when (purchase.product.metric.name) {
                    "шт" -> false
                    else -> true
                }
                NumberTextBottomSheet
                    .newInstance(0.0, isDouble, connect)
                    .show(fragmentManager, NumberTextBottomSheet.TAG)
            }
            informationBinding.chipGroup.addView(customChip)

        }

        override fun onClick(p0: View) {
            if (!press) {
                generateCountProductChips()
                binding.itemLayout.addView(informationBinding.root)
                informationBinding.apply {
                    adapter.metric = purchase.product.metric.name
                    adapter.currency = purchase.currency.symbol
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
                    if (isGroup) {
                        Picasso.get().load(purchase.person.url).into(imageView)
                        personNameTextView.text = purchase.person.name
                    } else {
                        Picasso.get().load(purchase.group.url).into(imageView)
                        personNameTextView.text = purchase.group.name
                    }
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