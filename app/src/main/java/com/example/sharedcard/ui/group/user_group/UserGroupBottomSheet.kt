package com.example.sharedcard.ui.group.user_group

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.sharedcard.R
import com.example.sharedcard.databinding.FragmentUserOptionsBinding
import com.example.sharedcard.util.appComponent
import com.example.sharedcard.util.isInternetConnection
import com.example.sharedcard.util.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.project.shared_card.database.dao.group_users.GroupUsersEntity
import java.util.UUID

class UserGroupBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentUserOptionsBinding

    private val viewModel by viewModels<UserGroupViewModel> {
        appComponent.multiViewModelFactory
    }
    private lateinit var idGroup: UUID
    private lateinit var idUser: UUID
    private var statusMy: Int = GroupUsersEntity.ADMIN
    private var statusUser: Int = GroupUsersEntity.USER

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        idUser = UUID.fromString(requireArguments().getString(KEY_ID_USER))
        idGroup = UUID.fromString(requireArguments().getString(KEY_ID_GROUP))
        statusMy = requireArguments().getInt(KEY_STATUS_MY,GroupUsersEntity.ADMIN)
        statusUser = requireArguments().getInt(KEY_STATUS_USER,GroupUsersEntity.USER)
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_user_options,
            container,
            false
        )
        binding.run {
            makeAdminLayout.isVisible(statusMy == GroupUsersEntity.CREATOR && statusUser == GroupUsersEntity.USER)
            delAdminLayout.isVisible(statusMy == GroupUsersEntity.CREATOR && statusUser == GroupUsersEntity.ADMIN)
            deleteLayout.isVisible(statusMy != GroupUsersEntity.USER)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.sendLiveData.observe(viewLifecycleOwner){
            it?.let {
                Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
            }
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.run {
            makeAdminLayout.setOnClickListener {
                viewModel.setUserStatus(isInternetConnection(requireContext()),idUser, idGroup,GroupUsersEntity.ADMIN)
            }
            delAdminLayout.setOnClickListener {
                viewModel.setUserStatus(isInternetConnection(requireContext()),idUser, idGroup,GroupUsersEntity.USER)
            }
            deleteLayout.setOnClickListener {
                viewModel.deleteUser(isInternetConnection(requireContext()),idUser, idGroup)
            }
        }
    }

    companion object {
        const val DIALOG_JOIN_GROUP = "dialogUserGroup"
        private const val KEY_ID_GROUP = "idGroup"
        private const val KEY_ID_USER = "idUser"
        private const val KEY_STATUS_MY = "statusMy"
        private const val KEY_STATUS_USER = "statusUser"
        fun newInstance(idUser: UUID, idGroup: UUID,statusMy: Int,statusUser: Int): UserGroupBottomSheet {
            val dialog = UserGroupBottomSheet()
            val args = Bundle()
            args.putString(KEY_ID_USER, idUser.toString())
            args.putString(KEY_ID_GROUP, idGroup.toString())
            args.putInt(KEY_STATUS_MY, statusMy)
            args.putInt(KEY_STATUS_USER, statusUser)
            dialog.arguments = args
            return dialog
        }
    }
}