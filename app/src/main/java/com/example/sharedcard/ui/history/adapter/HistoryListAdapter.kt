package com.example.sharedcard.ui.history.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.history.History
import com.example.sharedcard.databinding.ListItemHistoryProductBinding
import com.example.sharedcard.databinding.ListItemHistoryProductInformationBinding
import com.example.sharedcard.util.toStringFormat
import com.squareup.picasso.Picasso

class HistoryListAdapter(private val viewModel: ViewModelHistoryAdapter,private val lifecycleOwner: LifecycleOwner) : ListAdapter<History, HistoryListAdapter.HistoryHolder>(
    object : DiffUtil.ItemCallback<History>() {
        override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(
            oldItem: History,
            newItem: History
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    private val DATE_FORMAT = "MMMM.dd.yyyy HH:mm"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HistoryHolder(
        ListItemHistoryProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    inner class HistoryHolder(val binding: ListItemHistoryProductBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private val informationBinding: ListItemHistoryProductInformationBinding by lazy {
            ListItemHistoryProductInformationBinding.inflate(
                LayoutInflater.from(binding.root.context),
                binding.root as ViewGroup,
                false
            )
        }
        private var press = false
        lateinit var history: History
        fun onBind(history: History) {
            this.history = history
            binding.apply {
                viewModel.getPurchase(history.idPurchase).observe(lifecycleOwner) { purchase ->
                    nameTextView.text = purchase.product.productEntity.name
                    countTextView.text = "${history.count.toStringFormat()} ${purchase.product.metric.name}"
                    if (history.price != 0.0)
                        priceTextView.text = "${history.price.toStringFormat()} ${purchase.currency.symbol}"
                    Picasso.get().load(purchase.product.category.url).into(categoryImageView)

                    viewModel.getGroup(purchase.group.id).observe(lifecycleOwner) { group ->
                        Picasso.get().load(group.url).into(informationBinding.groupImageView)
                        informationBinding.groupNameTextView.text = group.name
                    }
                }
                binding.root.setOnClickListener(this@HistoryHolder)
            }

        }

        override fun onClick(v: View?) {
            if (!press) {
                binding.itemLayout.addView(informationBinding.root)
                informationBinding.apply {
                    viewModel.getPerson(history.idPerson).observe(lifecycleOwner) { person ->
                        Picasso.get().load(person.url).into(personImageView)
                        personNameTextView.text = person.name
                    }
                    viewModel.getShop(history.idShop).observe(lifecycleOwner) {
                        shopTextView.text = binding.root.context.getString(R.string.shop, it.name)
                    }
                    val locale = root.resources.configuration.locales[0]
                    val localDateFormat = DateFormat.getBestDateTimePattern(locale, DATE_FORMAT)
                    dateTextView.text = DateFormat.format(localDateFormat, history.date)
                }
            } else {
                binding.itemLayout.removeView(informationBinding.root)
            }
            press = !press
        }
    }

}