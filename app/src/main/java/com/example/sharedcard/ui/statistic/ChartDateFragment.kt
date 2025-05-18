package com.example.sharedcard.ui.statistic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentChartDateBinding
import com.example.sharedcard.ui.statistic.data.ChartDate
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.util.getDaysInMonthFromTimestamp
import com.example.sharedcard.util.getPrimaryColor
import com.example.sharedcard.util.getWeekDaysForDate
import org.eazegraph.lib.models.BarModel


class ChartDateFragment : Fragment() {
    private var pos: Int = 0
    private var colorSelectedChart: Int = 0
    private var colorDefaultChart: Int = 0
    private var colorEmptyChart: Int = 0
    private lateinit var viewModel: StatisticViewModel
    private lateinit var binding: FragmentChartDateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pos = it.getInt(ARG_POSITION)
        }
        viewModel = ViewModelProvider( requireActivity(),appComponent.multiViewModelFactory)[StatisticViewModel::class.java]
        colorSelectedChart = getColor(requireContext(), R.color.fat)
        colorDefaultChart = requireContext().getPrimaryColor()
        colorEmptyChart = getColor(requireContext(), R.color.color_background_view)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChartDateBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (viewModel.getChartPresentation()) {
            ChartDate.YEAR -> visibleChartYear()
            ChartDate.MONTH -> visibleChartMonth()
            ChartDate.WEEK -> visibleChartWeek()
            else -> throw IndexOutOfBoundsException()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.barChart.setOnBarClickedListener { selectableIndex ->
            viewModel.currentChartData.forEachIndexed { index, barModel ->
                if (barModel.color != colorEmptyChart) {
                    val item = viewModel.listChartData[pos]
                    if (selectableIndex == index) {
                        if (barModel.color != colorSelectedChart) {
                            barModel.color = colorSelectedChart
                        } else {
                            barModel.color = colorDefaultChart
                        }
                        val selectableItem = if (item.size < viewModel.currentChartData.size && barModel.color == colorSelectedChart) {
                            item.find {
                                if (viewModel.getChartPresentation() != ChartDate.YEAR) {
                                    it.day.toInt() == barModel.legendLabel.toInt()
                                } else {
                                    viewModel.months.indexOf(barModel.legendLabel) != -1
                                }
                            }!!
                        } else if(barModel.color == colorSelectedChart) {
                            item[selectableIndex]
                        }else{
                            null
                        }
                        viewModel.setDatePurchases(selectableItem)
                    } else {
                        barModel.color = colorDefaultChart
                    }
                }
            }
            binding.barChart.update()
        }
    }

    private fun visibleChartYear() {
        viewModel.currentChartData = arrayListOf()
        var j = 0
        val currentChartDate = viewModel.listChartData[pos]
        val size = currentChartDate.size
        for (i in 0..11) {
            val year = viewModel.months[i]
            val value = when (j < size && currentChartDate[j].month.toInt() == i + 1) {
                true -> currentChartDate[j++].count.toFloat()
                false -> 0.01F
            }
            val color = when (value == 0.01F) {
                true -> colorEmptyChart
                false -> colorDefaultChart
            }
            val barModel = BarModel(year, value, color)
            viewModel.currentChartData.add(barModel)
        }
        binding.barChart.addBarList(viewModel.currentChartData)
    }

    private fun visibleChartMonth() {
        viewModel.currentChartData = arrayListOf()
        var j = 0
        val currentChartDate = viewModel.listChartData[pos]
        val size = currentChartDate.size
        for (i in 1..viewModel.selectableDateLiveData.value!!.getDaysInMonthFromTimestamp()) {
            val value = when (j < size && currentChartDate[j].day.toInt() == i) {
                true -> currentChartDate[j++].count.toFloat()
                false -> 0.01F
            }
            val color = when (value == 0.01F) {
                true -> colorEmptyChart
                false -> colorDefaultChart
            }
            val barModel = BarModel(i.toString(), value, color)
            viewModel.currentChartData.add(barModel)
        }
        binding.barChart.addBarList(viewModel.currentChartData)
    }

    private fun visibleChartWeek() {
        viewModel.currentChartData = arrayListOf()
        var j = 0
        val currentChartDate = viewModel.listChartData[pos]
        val size = currentChartDate.size
        val days = viewModel.selectableDateLiveData.value!!.getWeekDaysForDate()
        for (i in 1..7) {
            val value =
                when (j < size && (currentChartDate[j].week.toInt() == i || currentChartDate[j].week.toInt() == 0 && i == 7)) {
                    true -> currentChartDate[j++].count.toFloat()
                    false -> 0.01F
                }
            val color = when (value == 0.01F) {
                true -> colorEmptyChart
                false -> colorDefaultChart
            }
            val barModel = BarModel(days[i - 1].toString(), value, color)
            viewModel.currentChartData.add(barModel)
        }
        binding.barChart.addBarList(viewModel.currentChartData)
    }

    companion object {
        private const val ARG_POSITION = "position"

        @JvmStatic
        fun newInstance(position: Int) =
            ChartDateFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POSITION, position)
                }
            }
    }
}