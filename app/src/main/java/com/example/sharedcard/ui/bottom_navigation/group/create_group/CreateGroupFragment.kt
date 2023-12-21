package com.example.sharedcard.ui.bottom_navigation.group.create_group

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentCreateGroupBinding
import com.example.sharedcard.databinding.FragmentToHistoryBinding

class CreateGroupFragment : DialogFragment() {

    private val viewModel: CreateGroupViewModel by viewModels()

    private lateinit var binding: FragmentCreateGroupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_create_group,
            container,
            false
        )
        return binding.root
    }

    companion object{
        const val DIALOG_CREATE_GROUP = "dialogCreateGroup"
    }
}