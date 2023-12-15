package com.example.sharedcard.ui.bottom_navigation.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sharedcard.R
import com.example.sharedcard.ui.MainActivity

class StatisticFragment : Fragment() {


    private lateinit var viewModel: StatisticViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_statistic, container, false)
    }


}