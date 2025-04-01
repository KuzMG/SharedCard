package com.example.sharedcard.ui.purchase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.viewpager2.widget.ViewPager2
import com.example.sharedcard.R
import com.example.sharedcard.ui.purchase.add_purchase.AddPurchaseFragment
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
            parentFragmentManager.commit {
                replace(R.id.nav_host_fragment,AddPurchaseFragment())
                addToBackStack(null)
            }
        }
    }


}