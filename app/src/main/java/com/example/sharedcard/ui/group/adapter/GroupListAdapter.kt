package com.example.sharedcard.ui.group.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.databinding.ListItemGroupBinding
import com.example.sharedcard.ui.group.GroupViewModel
import com.example.sharedcard.ui.group.edit_group.EditGroupBottomSheet
import com.example.sharedcard.ui.group.token_group.DialogTokenGroupFragment
import com.example.sharedcard.util.isVisible
import com.project.shared_card.database.dao.group_users.GroupPersonsEntity
import com.squareup.picasso.Picasso


class GroupListAdapter(
    private val viewModel: GroupViewModel,
    private val lifecycleOwner: LifecycleOwner,
    private val fragmentManager: FragmentManager
) :
    ListAdapter<GroupEntity, GroupListAdapter.GroupViewHolder>(object :
        DiffUtil.ItemCallback<GroupEntity>() {
        override fun areItemsTheSame(oldItem: GroupEntity, newItem: GroupEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GroupEntity, newItem: GroupEntity): Boolean {
            return oldItem == newItem
        }

    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GroupViewHolder(
        ListItemGroupBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    inner class GroupViewHolder(private val binding: ListItemGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        lateinit var group: GroupEntity
        private lateinit var userAdapter: UserListAdapter
        private var press = false

        init {
            binding.root.setOnClickListener {
                when (press) {
                    false -> openGroup()
                    true -> closeGroup()
                }
                press = !press
            }
            binding.userRecyclerView.layoutManager = GridLayoutManager(binding.root.context, 2)
        }


        fun onBind(group: GroupEntity) {
            this.group = group
            userAdapter = UserListAdapter(group.id, viewModel, fragmentManager)
            binding.apply {
                itemGroupName.text = group.name
                userRecyclerView.adapter = userAdapter

                Picasso.get()
                    .load(group.url)
                    .into(itemGroupImage)
                viewModel.getStatus(group.id).observe(lifecycleOwner) { status ->
                    status ?: return@observe
                    itemGroupEditButton.isVisible(status != GroupPersonsEntity.USER)
                    userAdapter.status = status
                    viewModel.getPersons(group.id).observe(lifecycleOwner) {
                        userAdapter.submitList(it)
                    }
                }
                itemGroupEditButton.setOnClickListener {
                    EditGroupBottomSheet
                        .newInstance(group.id)
                        .show(fragmentManager, EditGroupBottomSheet.DIALOG_EDIT_GROUP)
                }
                itemGroupTokenButton.setOnClickListener {
                    DialogTokenGroupFragment.newInstance(group.id).show(
                        fragmentManager,
                        DialogTokenGroupFragment.DIALOG_TOKEN_GROUP
                    )
                }
            }
            closeGroup()
        }

        private fun openGroup() {
            binding.userRecyclerView.visibility = View.VISIBLE
            binding.border.visibility = View.VISIBLE
        }

        fun closeGroup() {
            binding.userRecyclerView.visibility = View.GONE
            binding.border.visibility = View.GONE
        }
    }
}