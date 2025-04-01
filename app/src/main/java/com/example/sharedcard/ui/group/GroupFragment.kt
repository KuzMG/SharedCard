package com.example.sharedcard.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcard.R
import com.example.sharedcard.database.entity.group.GroupEntity
import com.example.sharedcard.ui.group.adapter.GroupListAdapter
import com.example.sharedcard.ui.group.adapter.GroupSwipeCallback
import com.example.sharedcard.ui.group.create_group.CreateGroupFragment
import com.example.sharedcard.ui.group.data.Result
import com.example.sharedcard.ui.group.delete_group.DeleteGroupFragment
import com.example.sharedcard.ui.group.join_group.JoinGroupFragment
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.util.isVisible
import com.google.android.material.progressindicator.LinearProgressIndicator
import io.github.kobakei.materialfabspeeddial.FabSpeedDial


class GroupFragment : Fragment() {


    private val viewModel: GroupViewModel by viewModels({this}) {
        appComponent.multiViewModelFactory
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var groupAdapter: GroupListAdapter
    private lateinit var toolbar: Toolbar
    private lateinit var fabButton: FabSpeedDial
    private lateinit var progressBar: LinearProgressIndicator


    private val itemTouchHelper = ItemTouchHelper(object : GroupSwipeCallback() {
        override fun swipeLeft(group: GroupEntity) {
            DeleteGroupFragment
                .newInstance(group.id, group.name)
                .show(parentFragmentManager, DeleteGroupFragment.DIALOG_DELETE)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_group, container, false)
        init(view)
        groupAdapter = GroupListAdapter(viewModel,viewLifecycleOwner,parentFragmentManager)
        recyclerView.adapter = groupAdapter
        itemTouchHelper.attachToRecyclerView(recyclerView)
        return view
    }

    private fun init(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        fabButton = view.findViewById(R.id.fab_button)
        recyclerView = view.findViewById(R.id.group_recycler_view)
        progressBar = view.findViewById(R.id.progress_bar)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            progressBar.isVisible(it.state == Result.State.LOADING)
            it.error?.let { e ->
                Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                return@observe
            }
        }
        viewModel.run {
            getGroups().observe(viewLifecycleOwner) { groups ->
                groupAdapter.submitList(groups)
            }
            sendDelGroupLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        fabButton.addOnMenuItemClickListener { _, _, itemId ->
            when (itemId) {
                R.id.join_group -> {
                    JoinGroupFragment().show(
                        parentFragmentManager,
                        JoinGroupFragment.DIALOG_JOIN_GROUP
                    )

                }

                R.id.create_group -> {
                    parentFragmentManager.commit {
                        replace(R.id.nav_host_fragment, CreateGroupFragment())
                        addToBackStack(null)
                    }
                }
            }
        }
    }
}