package com.example.sharedcard.ui.statistic

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sharedcard.R
import com.example.sharedcard.ui.MainActivity

class StatisticFragment : Fragment() {


    private lateinit var viewModel: StatisticViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as MainActivity).setToolbar(R.string.statistic)
        return inflater.inflate(R.layout.fragment_statistic, container, false)
    }


}