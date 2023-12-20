package com.example.sharedcard.ui.bottom_navigation.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.gpoup_users.GroupUsers
import com.example.sharedcard.databinding.FragmentGroupBinding
import com.example.sharedcard.databinding.ListItemGroupBinding
import com.example.sharedcard.databinding.ListtItemUserBinding
import com.example.sharedcard.ui.MainActivity

private const val TAG = "GroupFragment"

class GroupFragment : Fragment() {

    private lateinit var binding: FragmentGroupBinding
    private val viewModel: GroupViewModel by viewModels()
    private val expandableListAdapter = GroupExpandableListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_group,
            container,
            false
        )

        binding.flag = when (viewModel.userId) {
            0L -> false
            else -> true
        }
        binding.groupExpandableListView.setAdapter(expandableListAdapter)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.createGroup()
//        viewModel.createUser()
//        viewModel.createGroupUsers()

        viewModel.getGroups().observe(viewLifecycleOwner) { groups ->
            expandableListAdapter.submitData(groups)
        }
    }

    private inner class GroupExpandableListAdapter :
        BaseExpandableListAdapter() {
        private var groups: List<GroupUsers> = listOf()

        fun submitData(groups: List<GroupUsers>) {
            if (this.groups.isEmpty()) {
                this.groups = groups
                notifyDataSetChanged()
            }
        }

        override fun getGroupCount() = groups.size

        override fun getChildrenCount(p0: Int) = groups[p0].users.size

        override fun getGroup(p0: Int) = groups[p0].group

        override fun getChild(p0: Int, p1: Int) = groups[p0].users[p1]

        override fun getGroupId(p0: Int) = groups[p0].group.id

        override fun getChildId(p0: Int, p1: Int) = groups[p0].users[p1].user.id

        override fun hasStableIds() = true

        override fun getGroupView(p0: Int, p1: Boolean, p2: View?, p3: ViewGroup?): View {
            val binding = DataBindingUtil.inflate<ListItemGroupBinding>(
                layoutInflater,
                R.layout.list_item_group,
                p3,
                false
            )
            val group = groups[p0].group
            val status = groups[p0].users.find { user ->
                user.user.id == viewModel.userId
            }!!.status
            binding.apply {
                itemGroupName.text = group.name
                if (!status) {
                    itemGroupEdit.visibility = View.INVISIBLE
                    itemGroupDelete.visibility = View.INVISIBLE
                }
                itemGroupId.text = "${group.id}#${group.name}"
            }
            return binding.root
        }

        override fun getChildView(p0: Int, p1: Int, p2: Boolean, p3: View?, p4: ViewGroup?): View {
            val binding = DataBindingUtil.inflate<ListtItemUserBinding>(
                layoutInflater,
                R.layout.listt_item_user,
                p4,
                false
            )
            val user = groups[p0].users[p1].user
            val status = groups[p0].users[p1].status
            binding.apply {
                itemUserName.text = user.name
                if (!status) itemUserAdministratorImageView.visibility = View.INVISIBLE

            }
            return binding.root
        }

        override fun isChildSelectable(p0: Int, p1: Int) = true


    }


}