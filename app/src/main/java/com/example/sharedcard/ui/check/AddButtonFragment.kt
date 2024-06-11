package com.example.sharedcard.ui.check

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.sharedcard.R
import com.example.sharedcard.ui.check.add_check.AddCheckBottomSheet
import com.example.sharedcard.ui.check.add_target.AddTargetBottomSheet
import com.google.android.material.floatingactionbutton.FloatingActionButton


class AddButtonFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_add_button, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<FloatingActionButton>(R.id.add_button).setOnClickListener{
            when ( requireActivity().findViewById<ViewPager2>(R.id.check_pager).currentItem) {
                0 -> AddCheckBottomSheet().show(
                    parentFragmentManager,
                    AddCheckBottomSheet.DIALOG_ADD
                )

                1 -> AddTargetBottomSheet().show(
                    parentFragmentManager,
                    AddTargetBottomSheet.DIALOG_ADD
                )
            }
        }
    }


}