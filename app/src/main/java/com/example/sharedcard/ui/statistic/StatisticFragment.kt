package com.example.sharedcard.ui.statistic

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.graphics.drawable.toDrawable
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.product.Product
import com.example.sharedcard.databinding.FragmentStatisticBinding
import com.example.sharedcard.databinding.ListItemProductBinding
import com.example.sharedcard.ui.history.adapter.HistoryListAdapter
import com.example.sharedcard.ui.statistic.adapter.ViewPagerAdapter
import com.example.sharedcard.ui.statistic.data.ChartDate
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.util.getPrimaryColor
import com.example.sharedcard.util.isVisible
import com.example.sharedcard.util.toStringFormat
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.squareup.picasso.Picasso
import java.util.Calendar


private const val DATE_FORMAT = "MM.dd.yyyy"

class StatisticFragment : Fragment() {
    private lateinit var localDateFormat: String
    private lateinit var  adapter: HistoryListAdapter
    private lateinit var binding: FragmentStatisticBinding
    private val viewModel: StatisticViewModel by viewModels({ requireActivity() }) {
        appComponent.multiViewModelFactory
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locale = requireContext().resources.configuration.locales[0]
        localDateFormat = DateFormat.getBestDateTimePattern(locale, DATE_FORMAT)
        dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText(getString(R.string.select_range))
            .setSelection(Pair(viewModel.dateLast, viewModel.dateLast))
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticBinding.inflate(layoutInflater, container, false)
        binding.customChip.text = when (viewModel.dateFirst == 0L) {
            true -> getString(R.string.chip_custom_default)
            false -> {
                val first = DateFormat.format(localDateFormat, viewModel.dateFirst)
                val last = DateFormat.format(localDateFormat, viewModel.dateLast)
                "$first - $last"
            }
        }
        binding.appBar.toolbar.setNavigationIcon(R.drawable.ic_arrow_left)
        return binding.root
    }

    private lateinit var dateRangePicker: MaterialDatePicker<Pair<Long, Long>>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = HistoryListAdapter(viewModel,viewLifecycleOwner)
        binding.recyclerView.adapter = adapter
        binding.viewPager.isSaveEnabled = false
        viewModel.getGroups().observe(viewLifecycleOwner) { groups ->
            val personalTab = binding.tabLayout.newTab()
            personalTab.text = getString(R.string.personal)
            binding.tabLayout.addTab(personalTab)
            viewModel.mapGroup.clear()
            groups.forEach { group ->
                val tab = binding.tabLayout.newTab()
                viewModel.mapGroup[tab] = group.id
                tab.text = group.name
                binding.tabLayout.addTab(tab)
            }

        }
        viewModel.getCountMostPopularProduct().observe(viewLifecycleOwner) { count ->
            binding.mostPopularTextView.text = if (count == null || count == 0) {
                getString(R.string.most_popular)
            } else {
                getString(R.string.most_popular_value, count.toString())
            }
        }
        viewModel.getCountMostExpensiveProduct().observe(viewLifecycleOwner) { value ->
            binding.mostExpensiveTextView.text = if (value == null) {
                getString(R.string.most_expensive)
            } else {
                val c = value.count.toStringFormat()
                val m = value.metric
                val p = value.price.toStringFormat()
                val s = value.symbol
                getString(R.string.most_expensive_value, c, m, p, s)
            }
        }

        viewModel.getMostPopularProduct().observe(viewLifecycleOwner) { product ->
            binding.mostPopularLinearLayout.isVisible(product != null)
            productView(binding.mostPopularLayout, product)
        }
        viewModel.getMostExpensiveProduct().observe(viewLifecycleOwner) { product ->
            binding.mostExpensiveLinearLayout.isVisible(product != null)
            productView(binding.mostExpensiveLayout, product)
        }
        viewModel.getCountPurchase().observe(viewLifecycleOwner) { cp ->
            var count = 0
            val sb = StringBuilder()
            cp.forEach {
                count += it.count
                sb.append("${it.price.toStringFormat()} ${it.symbol} ")
            }
            binding.purchaseCountPriceCardView.isVisible(count != 0)
            binding.purchaseCountTextView.text =
                getString(R.string.count_purchase_value, count.toString())
            binding.purchasePriceTextView.text =
                getString(R.string.money_spent_value, sb.toString())
        }

        viewModel.getAddMostPurchasesPerson().observe(viewLifecycleOwner) { person ->
            binding.addMostPurchasesCardView.isVisible(person != null)
            person ?: return@observe
            Picasso.get().load(person.url).into(binding.personAddMostImageView)
            binding.personAddMostTextView.text = person.name
        }
        viewModel.getBuyMostPurchasesPerson().observe(viewLifecycleOwner) { person ->
            binding.buyMostPurchasesCardView.isVisible(person != null)
            person ?: return@observe
            Picasso.get().load(person.url).into(binding.personBuyMostImageView)
            binding.personBuyMostTextView.text = person.name
        }
        viewModel.getCountPurchaseChart().observe(viewLifecycleOwner) { list ->
            binding.selectableDateTextView.isVisible(list.isNotEmpty())
            binding.chartChipGroup.isVisible(list.isNotEmpty())

            viewModel.setDate(viewModel.dateLast)
            binding.viewPager.adapter = ViewPagerAdapter(this, list.size)
            binding.viewPager.currentItem = list.size - 1
        }
        viewModel.getPurchases().observe(viewLifecycleOwner) { purchases ->
            adapter.submitList(purchases)
        }
        viewModel.selectableDateLiveData.observe(viewLifecycleOwner){ time ->
            val calendar = Calendar.getInstance().apply {
                timeInMillis = time
            }
            val year = calendar.get(Calendar.YEAR).toString()
            val month = viewModel.months[calendar.get(Calendar.MONTH)]
            binding.selectableDateTextView.text = when(viewModel.getChartPresentation()){
                ChartDate.YEAR -> year
                else ->"$year $month"
            }
        }
    }


    override fun onStart() {
        super.onStart()
        binding.chartChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            viewModel.setChartPresentation(
                when (checkedIds.first()) {
                    R.id.week_chip -> ChartDate.WEEK
                    R.id.month_chip -> ChartDate.MONTH
                    R.id.year_chip -> ChartDate.YEAR
                    else -> throw IndexOutOfBoundsException()
                }
            )
        }
        binding.appBar.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.allTimeChip.setOnCheckedChangeListener { _, b ->
            if (b) {
                viewModel.setDateInterval(0, System.currentTimeMillis())
                viewModel.dateFirst = 0
                viewModel.dateLast = System.currentTimeMillis()
            }
        }
        binding.customChip.setOnClickListener {
            if (!dateRangePicker.isAdded)
                dateRangePicker.show(parentFragmentManager, null)
        }
        dateRangePicker.addOnPositiveButtonClickListener {
            dateRangePicker.selection?.let {
                viewModel.setDateInterval(it.first, it.second)
            }
            val first = DateFormat.format(localDateFormat, viewModel.dateFirst)
            val last = DateFormat.format(localDateFormat, viewModel.dateLast)
            binding.customChip.text = "$first - $last"

        }
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val isGroup = viewModel.mapGroup.contains(tab)
                if(!isGroup){
                    binding.addMostPurchasesCardView.isVisible(false)
                    binding.buyMostPurchasesCardView.isVisible(false)
                }
                viewModel.setCurrentGroup(viewModel.mapGroup[tab])
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })
    }

    private fun productView(productBinding: ListItemProductBinding, product: Product?) {
        if (product == null) {
            productBinding.apply {
                root.strokeColor =
                    getColor(requireContext(), R.color.color_background_view)
                nameTextView.text = ""
                picImageView.setImageDrawable(null)
                caloriesTextView.text = ""
                pointCardView.setCardBackgroundColor(
                    getColor(
                        requireContext(),
                        android.R.color.transparent
                    )
                )
                pfcTextView.text = ""
                pfcLineChart.clear()
            }

        } else {
            productBinding.apply {
                root.strokeColor = requireContext().getPrimaryColor()
                nameTextView.text = product.productEntity.name
                Picasso.get().load(product.category.url).into(picImageView)
                caloriesTextView.text = getString(
                    R.string.pie_chart_calories,
                    product.productEntity.calories!!.toInt().toString()
                )
                pointCardView.setCardBackgroundColor(
                    getColor(
                        requireContext(),
                        android.R.color.transparent
                    )
                )
                pfcTextView.text = ""
                pfcLineChart.apply {
                    setDataChart(
                        product.productEntity.protein!!.toFloat(),
                        product.productEntity.fat!!.toFloat(),
                        product.productEntity.carb!!.toFloat()
                    )
                    addOnClickListener { color, name ->
                        pointCardView.setCardBackgroundColor(getColor(context, color))
                        pfcTextView.text = name
                    }
                    startAnimation()
                }
            }

        }
    }
}