package com.example.sharedcard.ui.purchase.adapters

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.basket.Basket
import com.example.sharedcard.databinding.ListItemCountPurchaseBinding
import com.example.sharedcard.ui.purchase.to_history.ToHistoryBottomSheet
import com.example.sharedcard.util.isVisible
import com.example.sharedcard.util.toStringFormat
import com.squareup.picasso.Picasso
import java.util.UUID

private const val DATE_FORMAT = "MM.dd.yyyy HH:mm"
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
    var currency = ""
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
            this.basket = basket
            binding.apply {
                purchaseButton.isVisible(basket.history == null)
                personCardView.isVisible(basket.history != null)
                personTextView.isVisible(basket.history != null)
                dateTextView.isVisible(basket.history != null)
                shopTextView.isVisible(basket.history != null)
                val sb = StringBuilder()
                sb.append("${basket.count.toStringFormat()} $metric")
                binding.root.setCardBackgroundColor(root.context.getColor(R.color.color_background_view))
                basket.history?.let { history ->
                    shopTextView.text = history.shop.name
                    personTextView.text = history.person.name
                    binding.root.setCardBackgroundColor(root.context.getColor(R.color.light_green))
                    Picasso.get().load(history.person.url).into(personImageView)
                    val locale = root.resources.configuration.locales[0]
                    val localDateFormat = DateFormat.getBestDateTimePattern(locale, DATE_FORMAT)
                    binding.dateTextView.text = DateFormat.format(localDateFormat, history.purchaseDate)
                    if (history.price != 0.0)
                        sb.append(" - ${history.price.toStringFormat()} $currency")
                }
                historyTextView.text = sb
                purchaseButton.setOnClickListener {
                    ToHistoryBottomSheet.newInstance(basket.id, groupId)
                        .show(fragmentManager, ToHistoryBottomSheet.DIALOG_TO_HISTORY)
                }
            }
        }
    }
}