package com.example.sharedcard.ui.bottom_navigation.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.gpoup_users.GroupUsers
import com.example.sharedcard.database.entity.gpoup_users.UserEntityWithStatus
import com.example.sharedcard.databinding.FragmentGroupBinding
import com.example.sharedcard.databinding.FragmentGroupStateBinding
import com.example.sharedcard.databinding.ListItemGroupBinding
import com.example.sharedcard.databinding.ListItemUserBinding
import com.example.sharedcard.ui.bottom_navigation.group.create_group.CreateGroupFragment
import com.example.sharedcard.ui.bottom_navigation.group.delete_group.DeleteGroupFragment
import com.example.sharedcard.ui.bottom_navigation.group.edit_group.EditGroupFragment
import com.example.sharedcard.ui.bottom_navigation.group.join_group.JoinGroupFragment
import com.squareup.picasso.Picasso

class GroupStateFragment : Fragment() {
    private val groupAdapter = GroupListAdapter()
    private lateinit var binding: FragmentGroupStateBinding
    private val viewModel: GroupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater, R.layout.fragment_group_state, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.groupRecyclerView.adapter = groupAdapter
        viewModel.getGroups().observe(viewLifecycleOwner) { groups ->
            groupAdapter.submitList(groups)
        }
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            groupCreateButton.setOnClickListener {
                CreateGroupFragment().show(
                    childFragmentManager,
                    CreateGroupFragment.DIALOG_CREATE_GROUP
                )
            }
            groupJoinButton.setOnClickListener {
                JoinGroupFragment().show(
                    childFragmentManager,
                    JoinGroupFragment.DIALOG_JOIN_GROUP
                )
            }
            groupLocalButton.setOnClickListener {

            }
        }
    }

    companion object {
        private val diffUtilGroup = object : DiffUtil.ItemCallback<GroupUsers>() {
            override fun areItemsTheSame(oldItem: GroupUsers, newItem: GroupUsers): Boolean {
                return oldItem.group.id == newItem.group.id
            }

            override fun areContentsTheSame(oldItem: GroupUsers, newItem: GroupUsers): Boolean {
                return oldItem == newItem
            }

        }
        private val diffUtilUser = object : DiffUtil.ItemCallback<UserEntityWithStatus>() {
            override fun areItemsTheSame(
                oldItem: UserEntityWithStatus, newItem: UserEntityWithStatus
            ): Boolean {
                return oldItem.user.id == newItem.user.id
            }

            override fun areContentsTheSame(
                oldItem: UserEntityWithStatus, newItem: UserEntityWithStatus
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    private inner class GroupViewHolder(private val binding: ListItemGroupBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        lateinit var group: GroupUsers
        private val userAdapter = UserListAdapter()

        init {
            binding.root.setOnClickListener(this)
        }

        fun onBind(group: GroupUsers) {
            this.group = group
            binding.apply {
                itemGroupName.text = group.group.name
                groupRecyclerView.adapter = userAdapter
                userAdapter.submitList(group.users)

//                val sr = "http://192.168.80.31:8080/groups/photo/${group.group.id}"
//                Picasso.get()
//                    .load(sr)
//                    .into(binding.itemGroupImage)

                val status = group.users.find { user ->
                    user.status == true
                }!!.status

                if (status) {
                    itemGroupDelete.visibility = View.VISIBLE
                    itemGroupEdit.visibility = View.VISIBLE
                }
                itemGroupEdit.setOnClickListener {
                    EditGroupFragment
                        .newInstance(group.group.id)
                        .show(childFragmentManager, EditGroupFragment.DIALOG_EDIT_GROUP)
                }
                itemGroupDelete.setOnClickListener {
                    DeleteGroupFragment
                        .newInstance(group.group.id, group.group.name)
                        .show(childFragmentManager, DeleteGroupFragment.DIALOG_DELETE)
                }
            }
        }

        override fun onClick(v: View?) {
            binding.groupRecyclerView.visibility =
                if (binding.groupRecyclerView.visibility == View.VISIBLE) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
        }
    }

    private inner class GroupListAdapter : ListAdapter<GroupUsers, GroupViewHolder>(diffUtilGroup) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GroupViewHolder(
            DataBindingUtil.inflate(
                layoutInflater, R.layout.list_item_group, parent, false
            )
        )

        override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
            holder.onBind(getItem(position))
        }
    }

    private inner class UserViewHolder(private val binding: ListItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        lateinit var user: UserEntityWithStatus
        fun onBind(user: UserEntityWithStatus) {
            this.user = user
            binding.itemUserName.text = user.user.name
            if (user.status) binding.itemUserAdministratorImageView.visibility = View.VISIBLE
        }
    }

    private inner class UserListAdapter :
        ListAdapter<UserEntityWithStatus, UserViewHolder>(diffUtilUser) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder(
            DataBindingUtil.inflate(
                layoutInflater, R.layout.list_item_user, parent, false
            )
        )

        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
            holder.onBind(getItem(position))
        }

    }
}