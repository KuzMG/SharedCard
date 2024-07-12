package com.example.sharedcard.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.gpoup_users.GroupUsers
import com.example.sharedcard.database.entity.gpoup_users.UserEntityWithStatus
import com.example.sharedcard.databinding.ListItemGroupBinding
import com.example.sharedcard.databinding.ListItemUserBinding
import com.example.sharedcard.ui.group.create_group.CreateGroupFragment
import com.example.sharedcard.ui.group.delete_group.DeleteGroupFragment
import com.example.sharedcard.ui.group.edit_group.EditGroupBottomSheet
import com.example.sharedcard.ui.group.join_group.JoinGroupFragment
import com.example.sharedcard.ui.group.token_group.DialogTokenGroup
import com.example.sharedcard.ui.group.user_group.UserGroupBottomSheet
import com.example.sharedcard.ui.navigation_drawer.NavigationDrawerActivity
import com.example.sharedcard.util.appComponent
import io.github.kobakei.materialfabspeeddial.FabSpeedDial
import java.util.UUID


class GroupFragment : Fragment() {


    private lateinit var viewModel: GroupViewModel
    private lateinit var recyclerView: RecyclerView
    private val groupAdapter = GroupListAdapter()
    private lateinit var toolbar: Toolbar
    private lateinit var localButton: ImageButton
    private lateinit var fabButton: FabSpeedDial

    private val itemTouchHelper = ItemTouchHelper(object : CustomSwipeCallback() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val group = (viewHolder as GroupViewHolder).group
            when (direction) {
                ItemTouchHelper.LEFT -> {
                    DeleteGroupFragment
                        .newInstance(group.group.id, group.group.name)
                        .show(parentFragmentManager, DeleteGroupFragment.DIALOG_DELETE)
                    groupAdapter.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                    viewHolder.closeGroup()
                }
            }

        }

    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this, appComponent.multiViewModelFactory)[GroupViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_group, container, false)
        init(view)

        setToolbar()

        recyclerView.adapter = groupAdapter
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return view
    }

    private fun init(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        fabButton = view.findViewById(R.id.fab_button)
        localButton = view.findViewById(R.id.local_button)
        recyclerView = view.findViewById(R.id.group_recycler_view)
    }

    private fun setToolbar() {
        toolbar.setTitle(R.string.group)
        (requireActivity() as NavigationDrawerActivity).setSupportActionBar(toolbar)
        (requireActivity() as NavigationDrawerActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
            true
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.run {
            getGroups().observe(viewLifecycleOwner) { groups ->
                groupAdapter.submitList(groups)
            }
            currentGroupLiveData.observe(viewLifecycleOwner) {
                when (viewModel.isLocalGroup()) {
                    true -> localButton.setImageResource(R.drawable.planet_selected)
                    false -> localButton.setImageResource(R.drawable.planet_default)
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        fabButton.addOnMenuItemClickListener { _, _, itemId ->
            when (itemId) {
                R.id.join_group -> JoinGroupFragment().show(
                    parentFragmentManager,
                    JoinGroupFragment.DIALOG_JOIN_GROUP
                )

                R.id.create_group -> CreateGroupFragment().show(
                    parentFragmentManager,
                    CreateGroupFragment.DIALOG_CREATE_GROUP
                )
            }
        }
        localButton.setOnClickListener {
            viewModel.setGroupToLocal()
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
        RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {
        lateinit var group: GroupUsers
        private lateinit var userAdapter: UserListAdapter

        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
            binding.groupRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        }


        fun onBind(group: GroupUsers) {
            this.group = group
            binding.apply {
                itemGroupName.text = group.group.name

                viewModel.isAdmin(group.group.id).observe(viewLifecycleOwner) { isAdmin ->
                    if (isAdmin) {
                        itemGroupTokenButton.visibility = View.VISIBLE
                        itemGroupEditButton.visibility = View.VISIBLE
                    }
                    userAdapter = UserListAdapter(group.group.id, isAdmin)
                    groupRecyclerView.adapter = userAdapter
                }

                viewModel.currentGroupLiveData.observe(viewLifecycleOwner) { groupId ->
                     when (group.group.id) {
                        groupId -> {
                            materialCardView.strokeColor =
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.red
                            )
                            border.setBackgroundResource(R.color.red)
                        }

                        else ->{
                            materialCardView.strokeColor =ContextCompat.getColor(requireContext(), R.color.colorPrimary)
                            border.setBackgroundResource(R.color.colorPrimary)
                        }
                    }
                }
                itemGroupEditButton.setOnClickListener {
                    EditGroupBottomSheet
                        .newInstance(group.group.id)
                        .show(parentFragmentManager, EditGroupBottomSheet.DIALOG_EDIT_GROUP)
                }
                itemGroupTokenButton.setOnClickListener {
                    DialogTokenGroup.newInstance(group.group.id).show(
                        parentFragmentManager,
                        DialogTokenGroup.DIALOG_TOKEN_GROUP
                    )
                }
            }
        }

        override fun onLongClick(p0: View?): Boolean {
            viewModel.setGroup(group.group.id)
            return true
        }

        override fun onClick(v: View?) {
            when (userAdapter.itemCount) {
                0 -> openGroup()
                else -> closeGroup()
            }
        }

        private fun openGroup() {
            userAdapter.submitList(group.users)
            binding.border.visibility = View.VISIBLE
        }

        fun closeGroup() {
            userAdapter.submitList(listOf())
            binding.border.visibility = View.GONE
        }
    }

    private inner class GroupListAdapter :
        ListAdapter<GroupUsers, GroupViewHolder>(diffUtilGroup) {
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

        fun onBind(user: UserEntityWithStatus) {
            binding.run {
                itemUserName.text = user.user.name
                itemUserAdministratorImageView.visibility = when (user.status) {
                    true -> View.VISIBLE
                    else -> View.INVISIBLE
                }
            }
        }


    }

    private inner class UserListAdapter(
        private val idGroup: UUID,
        private val isAdmin: Boolean
    ) :
        ListAdapter<UserEntityWithStatus, UserViewHolder>(diffUtilUser) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder(
            DataBindingUtil.inflate(
                layoutInflater, R.layout.list_item_user, parent, false
            )
        )

        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
            holder.run {
                onBind(getItem(position))

                itemView.setOnLongClickListener {
                    if (!getItem(position).status && isAdmin) {
                        UserGroupBottomSheet.newInstance(getItem(position).user.id, idGroup).show(
                            parentFragmentManager,
                            UserGroupBottomSheet.DIALOG_JOIN_GROUP
                        )
                    }
                    true
                }
            }
        }
    }
}