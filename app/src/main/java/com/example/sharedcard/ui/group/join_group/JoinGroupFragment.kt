package com.example.sharedcard.ui.group.join_group

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentJoinGroupBinding
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.viewmodel.MultiViewModelFactory
import javax.inject.Inject

class JoinGroupFragment : DialogFragment() {


    private val viewModel: JoinGroupViewModel by viewModels {
        appComponent.multiViewModelFactory
    }
    private lateinit var binding: FragmentJoinGroupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_join_group,
            container,
            false
        )
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.run {
            dialogToHistoryAddButton.setOnClickListener {
                viewModel.join()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    companion object {
        const val DIALOG_JOIN_GROUP = "dialogJoinGroup"
    }
}