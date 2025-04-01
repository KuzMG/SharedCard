package com.example.sharedcard.ui.purchase.filter

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.sharedcard.databinding.BottomSheetFilterBinding
import com.example.sharedcard.ui.purchase.PurchaseViewModel
import com.example.sharedcard.ui.purchase.filter.enums.GROUPING_BY
import com.example.sharedcard.ui.purchase.filter.enums.SORTING_BY
import com.example.sharedcard.ui.purchase.filter.enums.SORT_MODE
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.util.createChipChose
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import java.util.UUID

class FilterBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetFilterBinding
    private val viewModel: PurchaseViewModel by viewModels({ requireParentFragment() })
    private val excludeGroupsMap = mutableMapOf<Int, UUID>()
    private val excludePersonsMap = mutableMapOf<Int, UUID>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = BottomSheetFilterBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sortModeButton.isChecked = when (viewModel.sortMode) {
            SORT_MODE.ASC -> true
            SORT_MODE.DESC -> false
        }
        binding.sortingChipGroup.check(
            when (viewModel.sortingBy) {
                SORTING_BY.CATEGORY -> binding.sortingCategoryChip.id
                SORTING_BY.NAME -> binding.soringNameChip.id
                SORTING_BY.DATE -> binding.sortingDateChip.id
            }
        )

        binding.groupingChipGroup.check(
            when (viewModel.groupingBy) {
                GROUPING_BY.GROUP -> binding.groupingGroupChip.id
                GROUPING_BY.PERSON -> binding.groupingPersonChip.id
            }
        )

        binding.closeButton.setOnClickListener {
            dismiss()
        }
        binding.completeButton.setOnClickListener {
            viewModel.sortMode = when(binding.sortModeButton.isChecked){
                true ->SORT_MODE.ASC
                false -> SORT_MODE.DESC
            }
            viewModel.sortingBy = when (binding.sortingChipGroup.checkedChipId) {
                binding.soringNameChip.id -> SORTING_BY.NAME
                binding.sortingCategoryChip.id -> SORTING_BY.CATEGORY
                binding.sortingDateChip.id -> SORTING_BY.DATE
                else -> throw IndexOutOfBoundsException()
            }
            viewModel.groupingBy = when (binding.groupingChipGroup.checkedChipId) {
                binding.groupingGroupChip.id -> GROUPING_BY.GROUP
                binding.groupingPersonChip.id -> GROUPING_BY.PERSON
                else -> throw IndexOutOfBoundsException()
            }
            val gIds = binding.excludeGroupsChipGroup.checkedChipIds
            viewModel.excludeGroupsSet = excludeGroupsMap.filter {
                gIds.contains(it.key)
            }.values.toMutableSet()
            val pIds = binding.excludePersonsChipGroup.checkedChipIds
            viewModel.excludePersonsSet = excludePersonsMap.filter {
                pIds.contains(it.key)
            }.values.toMutableSet()

            viewModel.filterComplete()
            dismiss()
        }
        viewModel.getGroupsWithoutDefault().observe(viewLifecycleOwner) { list ->
            if(list.isEmpty())
                binding.excludeGroupsTextVew.visibility = View.GONE
            list.forEach { group ->
                val chip = createChipChose(binding.excludeGroupsChipGroup, group.name, {})
                binding.excludeGroupsChipGroup.addView(chip)
                excludeGroupsMap[chip.id] = group.id
                if (viewModel.excludeGroupsSet.contains(group.id))
                    binding.excludeGroupsChipGroup.check(chip.id)
                binding.excludeGroupsChipGroup.checkedChipIds
            }
        }
        viewModel.getPersons().observe(viewLifecycleOwner) { list ->
            if(list.isEmpty())
                binding.excludePersonsTextVew.visibility = View.GONE
            list.forEach { person ->
                val chip = createChipChose(binding.excludePersonsChipGroup, person.name, {})
                binding.excludePersonsChipGroup.addView(chip)
                excludePersonsMap[chip.id] = person.id
                if (viewModel.excludePersonsSet.contains(person.id))
                    binding.excludePersonsChipGroup.check(chip.id)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.sortModeButton.setOnCheckedChangeListener { _, f ->

        }
    }

    companion object {
        const val TAG = "EditTextBottomSheet"
    }
}