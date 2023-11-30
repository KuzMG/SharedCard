package com.example.sharedcard.ui.check.target

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import com.example.sharedcard.ui.check.check_list.CheckListFragment
import com.example.sharedcard.ui.check.check_list.ListAdapterGeneral
import com.example.sharedcard.ui.check.check_list.ViewHolderGeneral
import com.project.shared_card.database.dao.target.TargetEntity

class TargetFragment: CheckListFragment() {
    companion object {
        private val diffUtilTarget = object : DiffUtil.ItemCallback<TargetEntity>() {
            override fun areItemsTheSame(oldItem: TargetEntity, newItem: TargetEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: TargetEntity,
                newItem: TargetEntity
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    private val viewModel by viewModels<TargetViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            recyclerView.adapter = TargetListAdapter()
        }

    }

    private inner class TargetHolder(itemView: View) : ViewHolderGeneral<TargetEntity>(itemView) {
        private lateinit var check: TargetEntity
        override fun onBind(check: TargetEntity) {
            super.onBind(check)
            this.check = check
        }
    }

    private inner class TargetListAdapter() : ListAdapterGeneral<TargetEntity, TargetHolder>(
        diffUtilTarget
    )
}