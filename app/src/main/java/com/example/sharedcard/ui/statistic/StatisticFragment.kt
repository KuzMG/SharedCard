package com.example.sharedcard.ui.statistic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.example.sharedcard.R
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerActivity


class StatisticFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view  = inflater.inflate(R.layout.fragment_statistic, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitle(R.string.statistic)
        (requireActivity() as NavigationDrawerActivity).setSupportActionBar(toolbar)
        (requireActivity() as NavigationDrawerActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return view
    }

}