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
import com.example.sharedcard.ui.history.adapter.HistoryListAdapter
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.util.toStringFormat
import com.squareup.picasso.Picasso

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding

    private lateinit var historyListAdapter: HistoryListAdapter

    private val viewModel by viewModels<HistoryViewModel> {
        appComponent.multiViewModelFactory
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
        historyListAdapter = HistoryListAdapter(viewModel,viewLifecycleOwner)

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

}