package com.example.sharedcard.ui.purchase

import android.os.Bundle
import android.text.Editable
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentListBinding
import com.example.sharedcard.ui.purchase.adapters.GroupListAdapter
import com.example.sharedcard.ui.purchase.filter.FilterBottomSheet
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.ui.adapter.TextWatcher
private const val DATE_FORMAT = "yyyy-MM.dd HH:mm"

class PurchaseFragment : Fragment() {

    private lateinit var localDateFormat: String

    val viewModel by viewModels<PurchaseViewModel>({ requireActivity() }) {
        appComponent.multiViewModelFactory
    }
    private lateinit var groupListAdapter: GroupListAdapter

    private lateinit var binding: FragmentListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locale = requireContext().resources.configuration.locales[0]
        localDateFormat = DateFormat.getBestDateTimePattern(locale, DATE_FORMAT)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        groupListAdapter = GroupListAdapter(viewModel, viewLifecycleOwner, parentFragmentManager)
        binding.apply {
            recyclerView.adapter = groupListAdapter
        }


        viewModel.groupChanged.observe(viewLifecycleOwner) {
            viewModel.setQuery("")
        }

        viewModel.sendLiveData.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.filterLiveData.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.getGroupsItem().observe(viewLifecycleOwner) { list ->
                    groupListAdapter.submitList(list)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().run {
            findViewById<EditText>(R.id.search_edit_text).addTextChangedListener(
                object : TextWatcher() {
                    override fun onTextChange(text: String) {
                        viewModel.setQuery(text)
                    }
                })
            findViewById<ImageButton>(R.id.filter_image_button).setOnClickListener {
                FilterBottomSheet().show(childFragmentManager,FilterBottomSheet.TAG)
            }
        }
    }
}