package com.example.sharedcard.ui.bottom_navigation.history

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.history.History
import com.example.sharedcard.databinding.FragmentListBinding
import com.example.sharedcard.databinding.ListItemHistoryBinding
import com.example.sharedcard.ui.MainActivity

private const val DATE_FORMAT = "MM.dd HH:mm"

class HistoryFragment : Fragment() {

    companion object {
        val diffUtilHistory = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.dateLast == newItem.dateLast
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }

        }
    }

    private lateinit var localDateFormat: String
    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var binding: FragmentListBinding
    private val historyListAdapter = HistoryListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locale = requireContext().resources.configuration.locales[0]
        localDateFormat = DateFormat.getBestDateTimePattern(locale, DATE_FORMAT)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_list,
            container,
            false
        )
        binding.recyclerView.adapter = historyListAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.historyLiveData.observe(viewLifecycleOwner) { histories ->
//            historyListAdapter.submitList(histories)
//        }
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            searchBar.searchEditView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    viewModel.setQuery(p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {}
            })
            searchBar.buttonSort.setOnClickListener { v ->
                showPopupMenu(v)
            }
        }
    }

    private fun showPopupMenu(v: View) {
        val popupMenu = PopupMenu(requireContext(), v)
        popupMenu.inflate(R.menu.popup_menu)
        popupMenu
            .setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.sort_by_date -> {
                            historyListAdapter.sorting(1)
                        }

                        R.id.sort_by_product -> {
                            historyListAdapter.sorting(2)
                        }

                        R.id.sort_by_category -> {
                            historyListAdapter.sorting(3)
                        }

                        R.id.sort_by_user -> {
                            historyListAdapter.sorting(4)
                        }
                    }
                    return true
                }

            })
        popupMenu.show()
    }

    private inner class HistoryViewHolder(private val binding: ListItemHistoryBinding) :
        ViewHolder(binding.root) {
        lateinit var history: History
        fun onBind(history: History) {
            this.history = history

            binding.history = history
            binding.dateOfBuy.text = DateFormat.format(localDateFormat, history.dateLast)
            binding.executePendingBindings()
        }
    }

    private inner class HistoryListAdapter() :
        ListAdapter<History, HistoryViewHolder>(diffUtilHistory) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HistoryViewHolder(
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.list_item_history,
                parent,
                false
            )
        )

        override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
            holder.onBind(getItem(position))
        }

        private var flag1 = false
        private var flag2 = false
        private var flag3 = false
        private var flag4 = false
        fun sorting(fieldSorting: Int) {
            submitList(when (fieldSorting) {
                1 -> {
                    flag1 = !flag1
                    if (flag1)
                        currentList.sortedBy { it.dateLast }
                    else
                        currentList.sortedByDescending { it.dateLast }
                }

                2 -> {
                    flag2 = !flag2
                    if (flag2)
                        currentList.sortedBy { it.name }
                    else
                        currentList.sortedByDescending { it.name }
                }

                3 -> {
                    flag3 = !flag3
                    if (flag3)
                        currentList.sortedBy { it.category }
                    else
                        currentList.sortedByDescending { it.category }
                }

                4 -> {
                    flag4 = !flag4
                    if (flag4)
                        currentList.sortedBy { it.buyer }
                    else
                        currentList.sortedByDescending { it.buyer }
                }

                else -> throw IndexOutOfBoundsException()
            })
        }
    }
}