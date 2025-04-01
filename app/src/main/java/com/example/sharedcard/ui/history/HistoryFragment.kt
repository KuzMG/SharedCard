package com.example.sharedcard.ui.history

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.history.History
import com.example.sharedcard.databinding.FragmentHistoryBinding
import com.example.sharedcard.databinding.ListItemHistoryProductBinding
import com.example.sharedcard.databinding.ListItemHistoryProductInformationBinding
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.util.toStringFormat
import com.squareup.picasso.Picasso

private const val DATE_FORMAT = "MM.dd.yyyy HH:mm"
class HistoryFragment : Fragment() {
    companion object {
        private val diffUtilCheck = object : DiffUtil.ItemCallback<History>() {
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
    }
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var localDateFormat: String
    private val historyListAdapter = HistoryListAdapter()

    private val viewModel by viewModels<HistoryViewModel> {
        appComponent.multiViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locale = requireContext().resources.configuration.locales[0]
        localDateFormat = DateFormat.getBestDateTimePattern(locale, DATE_FORMAT)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            recyclerView.adapter = historyListAdapter
            appBar.toolbar.setNavigationIcon(R.drawable.ic_arrow_left)
            appBar.toolbar.setNavigationOnClickListener{
                parentFragmentManager.popBackStack()
            }
        }
        viewModel.getHistory().observe(viewLifecycleOwner) { history ->
            historyListAdapter.submitList(history)
        }
    }




    private inner class HistoryHolder(val binding: ListItemHistoryProductBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        val informationBinding: ListItemHistoryProductInformationBinding by lazy{
            ListItemHistoryProductInformationBinding.inflate(
                layoutInflater,
                binding.root as ViewGroup,
                false
            )
        }
        var press = false
        lateinit var history: History
        fun onBind(history: History) {
            this.history = history
            binding.apply {
                viewModel.getPurchase(history.idPurchase).observe(viewLifecycleOwner){purchase ->
                    nameTextView.text = purchase.product.productEntity.name
                    countTextView.text = "${history.count.toStringFormat()} ${purchase.product.metric.name}"
                    if(history.price != 0.0)
                        priceTextView.text = "${history.price.toStringFormat()} ${purchase.currency.symbol}"
                    Picasso.get().load(purchase.product.category.url).into(categoryImageView)

                    viewModel.getGroup(purchase.idGroup).observe(viewLifecycleOwner){ group ->
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
                    viewModel.getPerson(history.idPerson).observe(viewLifecycleOwner) {person ->
                        Picasso.get().load(person.url).into(personImageView)
                        personNameTextView.text = person.name
                    }
                    viewModel.getShop(history.idShop).observe(viewLifecycleOwner) {
                        shopTextView.text = getString(R.string.shop,it.name)
                    }
                    dateTextView.text = DateFormat.format(localDateFormat, history.date)
                }
            } else {
                binding.itemLayout.removeView(informationBinding.root)
            }
            press = !press
        }
    }

    private inner class HistoryListAdapter : ListAdapter<History, HistoryHolder>(
        diffUtilCheck
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HistoryHolder(
            ListItemHistoryProductBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
        override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
            holder.onBind(getItem(position))
        }
    }

}